/** *****************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************************** */
package pi.server.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pi.server.factory.Services;
import pi.service.LoginService;
import pi.server.db.server.CRUD;
import pi.service.model.MetaServer;
import pi.service.model.TipoCambio;
import pi.service.model.efact.TxxxSituacion;
import pi.service.model.rrhh.CargoPermiso;
import pi.service.model.rrhh.Empleado;
import pi.service.model.rrhh.Permiso;
import pi.service.util.Util;

import com.caucho.hessian.server.HessianServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet("pi/LoginService")
public class LoginServiceImpl extends HessianServlet implements LoginService {

    @Override
	public MetaServer login(String app, String user, String pass, int empresaId, int sucursalId) throws Exception {
		Class table = Empleado.class;
		String[] required = {
			"persona","sucursal","cargo","empresa","caja" 
		};
		String filter = "where usuario = '" + user + "' and clave = '" + Util.encrypt(pass) +"' and a.empresa = " +empresaId ;
		List<Empleado> listEmpleados =  CRUD.list(app,table,required,filter);
		if(listEmpleados.isEmpty()){
			throw new Exception("No se pudo validar el usuario o clave");
		}
		Empleado empleado = listEmpleados.get(0);
		if(!empleado.activo){
			throw new Exception("El usuario ingresado se encuentra inhabilitado");
		}
		
		//para los permisos
		String[] requiredCP = {"cargo","permiso"};
		List<CargoPermiso> cargosPermisos = CRUD.list(app,CargoPermiso.class,requiredCP,"where b.id = " + empleado.cargo.id);
		List<Permiso> permisos = new ArrayList<>();
		boolean tienePermisoTodasSucursales = false;
		for(CargoPermiso cp:cargosPermisos){
			permisos.add(cp.permiso);
			if(cp.permiso.id == Util.PER_JEFE_ADMINISTRACION||cp.permiso.id == Util.PER_JEFE_VENTAS||cp.permiso.id == Util.PER_JEFE_LOGISTICA) {
				tienePermisoTodasSucursales = true;
			}
		}
		if(!tienePermisoTodasSucursales) {
			if(empleado.sucursal.id !=sucursalId){
				throw new Exception("No cuenta con permisos para la sucursal seleccionada");
			}
		}
		List<TxxxSituacion> efactSituaciones = Services.getEfact().listEfactSituaciones(app);
        if (efactSituaciones.isEmpty()) {
            throw new Exception("No se pudieron cargar efact_situaciones");
        }
		Map<String,String> mapEfactSituaciones = new HashMap<>();
        for (TxxxSituacion item : efactSituaciones) {
            String nomSitu = item.nom_situacion;
            if (item.cod_situacion.equals(Util.COD_SITU_DESCARGAR_CDR)) {
                nomSitu = "ENVIADO ACEPTADO";
            }
            if (item.cod_situacion.equals(Util.COD_SITU_DESCARGAR_CDR_OBS)) {
                nomSitu = "ENVIADO ACEPTADO OBSERVADO";
            }
            mapEfactSituaciones.put(item.cod_situacion, nomSitu);
        }
		TipoCambio tc = Services.getTipoCambio().getLastTipoCambio(app);
		MetaServer meta = new MetaServer();
		meta.sucursal = empleado.sucursal; 
		meta.empresa = empleado.empresa;
		meta.empleado = empleado;
		meta.permisos = permisos;
		meta.sinDireccion = Services.getDireccion().getDireccionSinCliente(app);
		meta.sucursales = Services.getSucursal().listActive(app);
		meta.documentosTipo = Services.getDocumentoTipo().listActives(app);
		meta.formasPago = Services.getFormaPago().list(app);
		meta.sucursalSeries = Services.getSucursal().listSeries(app, meta.sucursal.id);
		meta.monedas = Services.getMoneda().list(app);
		meta.almacenes = Services.getAlmacen().listActives(app, meta.empresa.id);
		meta.marcas = Services.getMarca().list(app, meta.empresa.id);
		meta.lineas = Services.getLinea().list(app, meta.empresa.id);
		meta.unidades = Services.getUnidad().list(app);
		meta.impuestos = Services.getImpuesto().listActives(app);
		meta.efactSituaciones = efactSituaciones;
		meta.mapEfactSituaciones = mapEfactSituaciones;
		meta.tc_compra = tc.compra;
		meta.tc_venta = tc.venta;
		meta.app = app;
        return meta;
    }

}
