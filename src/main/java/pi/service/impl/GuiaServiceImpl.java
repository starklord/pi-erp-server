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
import java.util.Date;
import java.util.List;
import pi.service.model.logistica.GuiaRemision;
import pi.service.model.logistica.GuiaRemisionDet;
import pi.service.model.logistica.MotivoTraslado;
import pi.service.GuiaService;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/GuiaService")
public class GuiaServiceImpl extends HessianServlet implements GuiaService {

	@Override
	public void annul(String app, int guiaId) throws Exception {
		
		try {
			/*List<GuiaRemisionDet> list = listDetallesVenta(guiaId);
			for(GuiaRemisionDet gd: list) {
				CRUD.execute(app, "update venta.orden_venta_det set cantidad_atendida = cantidad_atendida-" + gd.cantidad+
						" where producto = " + gd.producto.id+" and orden_venta = " + gd.orden_venta_det.orden_venta.id);
				CRUD.execute(app, "delete from logistica.guia_venta_det where id = " + gd.id);
			}*/
			GuiaRemision guia = getVenta(app, guiaId);
			guia.activo = false;
			CRUD.update(app,guia);
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());	
		}
	}
	@Override
	public GuiaRemision getLastGuiaVenta(String app, String serie) throws Exception {
		String[] require = {
				"direccion_llegada", "direccion_llegada.persona","almacen_partida","almacen_partida.sucursal",
				"almacen_partida.sucursal.direccion"
		};
		String filter = "where a.serie = '" + serie + "' order by a.numero desc limit 1";
		
		List<GuiaRemision> list = CRUD.list(app,GuiaRemision.class, require, filter);
		return list.isEmpty()?null:list.get(0);
	}
	@Override
	public GuiaRemision getVenta(String app, int guiaVentaId) throws Exception {
		String[] require = {
				"direccion_llegada", "direccion_llegada.persona","transportista","direccion_partida",
				"almacen_partida","almacen_partida.sucursal","almacen_partida.sucursal.direccion"
		};
		String filter = "where a.id = " + guiaVentaId;
		
		List<GuiaRemision> list = CRUD.list(app,GuiaRemision.class, require, filter);
		return list.isEmpty()?null:list.get(0);
	}
	
	@Override
	public List<GuiaRemision> listVentaByOrdenVenta(String app, int ordenVentaId) throws Exception {
		String[] require = {
				"guia_remision", "producto", "producto.unidad","orden_venta_det","orden_venta_det.orden_venta","documento_pago"
		};
		String filter = "where f.id = " + ordenVentaId + "order by b.numero desc";
		List<GuiaRemisionDet> listDetalles = CRUD.list(app,GuiaRemisionDet.class, require, filter);
		List<GuiaRemision> list = new ArrayList<>();
		return list;
	}
	
	@Override
	public GuiaRemision getVentaByOrdenVenta(String app, int ordenVentaId) throws Exception {
		String[] require = {
				"direccion", "direccion.persona","orden_venta"
		};
		String filter = "where orden_venta = " + ordenVentaId;
		
		List<GuiaRemision> list = CRUD.list(app,GuiaRemision.class, require, filter);
		return list.isEmpty()?null:list.get(0);
	}
	
	@Override
	public List<GuiaRemision> listVenta(String app, int sucursalId, Date fechaInicio, Date fechaFin) throws Exception {
		String[] require = {
				"direccion_llegada", "direccion_llegada.persona"
		};
		String filter = "where a.fecha between '" + fechaInicio + "' and '" + fechaFin+"'";
		
		return CRUD.list(app,GuiaRemision.class, require, filter);
	}

	@Override
	public List<GuiaRemisionDet> listDetallesVenta(String app, int guiaVentaId) throws Exception {
		String[] require = {
				"guia_remision", "producto", "producto.unidad"
		};
		String filter = "where guia_remision = " + guiaVentaId;
		return CRUD.list(app,GuiaRemisionDet.class, require, filter);
	}

	@Override
	public GuiaRemision saveOrUpdateVenta(String app, boolean save, GuiaRemision guia_venta, List<GuiaRemisionDet> detalles) throws Exception {
		try {
			if(save) {
				CRUD.save(app,guia_venta);
				
			}else {
				CRUD.update(app,guia_venta);	
			}
			CRUD.execute(app, "delete from logistica.guia_remision_det where guia_remision = " + guia_venta.id);
			for(GuiaRemisionDet det: detalles) {
				det.id = null;
				det.guia_remision= guia_venta;
				CRUD.save(app,det);
			}
			return guia_venta;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		
	}

	@Override
	public void sendToPrint(String app, int guiaVentaId, String user) throws Exception {
//		try {
//			
//			System.out.println("serverprint: getting guia_venta_detalles");
//			UbigeoServiceImpl ubigeoService = new UbigeoServiceImpl();
////			Util.UBIGEOS = ubigeoService.list();
//			List<GuiaRemisionDet> detalles = listDetallesVenta(guiaVentaId);
//			GuiaRemision guia = getVenta(detalles.get(0).guia_remision.id);
//			StringBuilder sbr = new StringBuilder();
//			//para la orden de venta
//			OrdenVentaServiceImpl ordenVentaService = new OrdenVentaServiceImpl();
//			String documento_pago= " ";
//			//user|tipo|destinatario|ruc_destinatario|transportista|ruc_transportista|motivo_traslado|serie|numero|dia|mes|anho|partida|llegada
//			//para los detalles
//			//codigo|cantidad|unidad|descripcion		
//			String tipo = "9";
//			String destinatario 		= guia.direccion_llegada.persona.toString();
//			String ruc_destinatario 	= guia.direccion_llegada.persona.identificador;
//			String transportista 		= guia.transportista.toString();
//			String ruc_transportista	= guia.transportista.identificador;
//			String motivo_traslado		= guia.motivo_traslado.id+"";
//			String serie				= guia.serie + "";
//			String numero				= guia.numero + "";
//			String dia					= guia.fecha.getDate()+"";
//			String mes					= (guia.fecha.getMonth()+1)+"";
//			String anho					= (guia.fecha.getYear()+1900) + "";
//			String direccion_partida	= guia.almacen_partida.sucursal.direccion.toString();
//			String direccion_llegada	= guia.direccion_llegada.toString();
//			destinatario				= destinatario.length()>30?destinatario.substring(0, 30):destinatario;
//			transportista				= transportista.length()>30?transportista.substring(0, 30):transportista;
//			direccion_partida			= direccion_partida.length()>40?direccion_partida.substring(0, 40):direccion_partida;
//			direccion_llegada			= direccion_llegada.length()>40?direccion_llegada.substring(0, 40):direccion_llegada;
//			sbr.append(user).append("|");
//			sbr.append(tipo).append("|");
//			sbr.append(destinatario).append("|");
//			sbr.append(ruc_destinatario).append("|");
//			sbr.append(transportista).append("|");
//			sbr.append(ruc_transportista).append("|");
//			sbr.append(motivo_traslado).append("|");
//			sbr.append(serie).append("|");
//			sbr.append(numero).append("|");
//			sbr.append(dia).append("|");
//			sbr.append(mes).append("|");
//			sbr.append(anho).append("|");
//			sbr.append(direccion_partida).append("|");
//			sbr.append(direccion_llegada).append("|");
//			sbr.append(documento_pago).append("||");
//			for (GuiaRemisionDet det : detalles) {
//				sbr.append(det.producto.codigo).append("|");
//				sbr.append(Numbers.getBigDecimal(det.cantidad, 0)).append("|");
//				sbr.append(det.producto.unidad.abreviatura).append("|");
//				sbr.append(det.producto.nombre).append("|");
//				sbr.append(det.descripcion==null?"":det.descripcion).append("||");
//				
//			}
//			System.out.println("serverprint: sendingPrintSocket");
//			                 //Server.getPrintSocketClient().sendMessage(sbr.toString());
//			System.out.println("serverprint: success");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw new Exception(ex.getMessage());
//		}
	}

	@Override
	public List<MotivoTraslado> listMotivosTraslado(String app) throws Exception {
		return CRUD.list(app,MotivoTraslado.class,"order by nombre asc");
	}
	

	

	


}
