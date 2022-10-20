/*******************************************************************************
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
 *******************************************************************************/
package pi.service.impl;

import java.util.ArrayList;
import java.util.List;

import pi.service.model.rrhh.CargoPermiso;
import pi.service.model.rrhh.Empleado;
import pi.service.model.rrhh.Permiso;
import pi.service.EmpleadoService;
import pi.service.util.Util;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/EmpleadoService")
public class EmpleadoServiceImpl extends HessianServlet implements EmpleadoService {
	
	public static String table = Empleado.class.getName();

	@Override
	public void cambiarClave(String app, int empleadoId, String claveActual, String claveNueva, String claveNuevaRep) throws Exception {
		
		String claveEncriptada = Util.encrypt(claveNueva);
        
		List<Empleado> list = CRUD.list(app,Empleado.class,"where id = " + empleadoId);
		if(list.isEmpty()){
			throw new Exception("No se han encontrado datos para el usuario seleccionado");
		}
		Empleado emp = list.get(0);
        if(!emp.clave.equals(Util.encrypt(claveActual))){
        	throw new Exception("La Clave Actual ingresada no coincide");
        }

        if (claveActual.equals(claveNueva)) {
            throw new Exception("La Clave Actual es igual a la Clave Nueva");
        }

        if (!claveNueva.equals(claveNuevaRep)) {
            throw new Exception("No ha repetido la Clave Nueva correctamente");
        }

        if (claveNueva.length() < 4) {
            throw new Exception("La Clave Nueva debe tener 4 o mas caracteres");
        }
        try {
        	emp.clave = claveEncriptada;
        	CRUD.update(app,emp);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
	}

	@Override
	public List<Empleado> list(String app, int empresaId) throws Exception {
		String[] req = {
				"persona","sucursal","cargo","cargo.area","empresa","empresa.direccion","empresa.direccion.persona"
		};
		return CRUD.list(app,Empleado.class,req,"where a.empresa = " + empresaId + " order by b.apellidos asc");
		
	}
	
	@Override
	public List<Empleado> listUsuariosSistema(String app, int empresaId) throws Exception {
		String[] req = {
				"persona","sucursal","cargo","cargo.area","empresa","empresa.direccion","empresa.direccion.persona"
		};
		return CRUD.list(app,Empleado.class,req,"where a.empresa = " + empresaId + " and a.es_usuario_sistema is true order by b.apellidos asc");
	}
	
	@Override
	public Empleado save(String app, Empleado object) throws Exception {
		object.clave = Util.encrypt("123456");
		CRUD.save(app,object);
		return object;
		
	}

	@Override
	public Empleado saveOrUpdate(String app, boolean save, Empleado object) throws Exception {
		if(save){
			object.clave = Util.encrypt("123456");
			CRUD.save(app,object);
		}else{
			CRUD.update(app,object);
		}
		return object;
	}

	@Override
	public List<Permiso> listPermisosSession(String app) throws Exception {
//		HttpSession httpSession = getThreadLocalRequest().getSession();
//		List<Permiso> permisos= (List<Permiso>) httpSession.getAttribute("permisos");
//		return permisos;
            return null;
	}

	@Override
	public List<Empleado> listVendedores(String app, int empresaId) throws Exception {
		List<CargoPermiso> cargosPermisos = CRUD.list(app,CargoPermiso.class,"where permiso = " + Util.PER_VENDEDOR);
		StringBuilder sb = new StringBuilder();
		for(CargoPermiso cp: cargosPermisos){
			sb.append(cp.cargo.id).append(",");
		}
		List<Empleado> listVendedores = new ArrayList<>();
		if(!cargosPermisos.isEmpty()){
			sb.deleteCharAt(sb.length()-1);
			String[] req = {
					"persona","sucursal","cargo","cargo.area","empresa","empresa.direccion","empresa.direccion.persona"
			};
			String where = " where a.empresa = " + empresaId + " and a.cargo in("+sb.toString() +  ") and a.activo is true order  by b.apellidos asc";
			listVendedores = CRUD.list(app,Empleado.class,req,where);
		}
		return listVendedores;
	}
	
	@Override
	public Empleado getByPin(String app, int pin) throws Exception {
		String[] req = {
				"persona","sucursal","cargo","cargo.area","empresa","empresa.direccion","empresa.direccion.persona"
		};
		String where = " where a.pin = " + pin + " and a.activo is true order  by b.apellidos asc limit 1";
		List<Empleado> list = CRUD.list(app,Empleado.class,req,where);
		return list.isEmpty()?null:list.get(0);
	}
	
}





























