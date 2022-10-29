package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import pi.service.util.Util;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Kardex;
import pi.service.model.logistica.TrasladoInternoCab;
import pi.service.model.logistica.TrasladoInternoDet;
import pi.server.db.Update;
import pi.service.TrasladoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/TrasladoService")
public class TrasladoServiceImpl extends HessianServlet implements TrasladoService{

	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(String app, TrasladoInternoCab trasCab, List<TrasladoInternoDet> trasDet,Boolean save, boolean isRequerimientoPaciente) throws Exception {
		try {
			
			if(save) {				
				CRUD.save(app,trasCab);
				for(TrasladoInternoDet det: trasDet) {
					det.id = null;
					det.creador = trasCab.creador;
					det.traslado_cab = trasCab;
					CRUD.save(app,det);
				}
			}else {
				CRUD.update(app,trasCab);
				CRUD.execute(app, "delete from logistica.traslados_internos_det where traslado_cab = " + trasCab.id);
				for(TrasladoInternoDet det: trasDet) {
					det.id = null;
					det.creador = trasCab.creador;
					det.traslado_cab = trasCab;
					CRUD.save(app,det);
				}
			}
			if (trasCab.cerrado&&!isRequerimientoPaciente) {
				for (TrasladoInternoDet det : trasDet) {
					saveKardexFromTrasladoEntrada(app, det);
					saveKardexFromTrasladoSalida(app, det);
				}
			}
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}
	
	private void saveKardexFromTrasladoEntrada(String app, TrasladoInternoDet ocd) throws Exception {
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app,ocd.producto.id, ocd.traslado_cab.almacen_destino.id);
		Kardex nk= new Kardex();
		nk.activo = true;
		nk.almacen 	= ocd.traslado_cab.almacen_destino;
		nk.creador 	= ocd.creador;
		nk.destino 	= ocd.traslado_cab.almacen_destino.codigo;
		nk.origen 	= ocd.traslado_cab.almacen_origen.codigo;
		nk.fecha = new Date();
		nk.fecha_orden = ocd.traslado_cab.fecha;
		nk.documento = ocd.traslado_cab.id+"";
		nk.ingreso = ocd.cantidad;
		nk.salida = BigDecimal.ZERO;
		nk.movimiento = Util.MOVIMIENTO_ENTRADA;
		nk.orden_id = ocd.traslado_cab.id;
		nk.precio_costo = BigDecimal.ZERO;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = ocd.producto;
		nk.stock_anterior = ok==null?BigDecimal.ZERO:ok.stock;
		nk.stock = nk.stock_anterior.add(nk.ingreso);
		nk.tipo = Util.TIPO_ORDEN_TRASLADO_INTERNO;
		nk.fecha_vencimiento = ocd.fecha_vencimiento;
		nk.lote = ocd.lote;
		CRUD.save(app,nk);
	}
	
	private void saveKardexFromTrasladoSalida(String app, TrasladoInternoDet ovd) throws Exception {
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app,ovd.producto.id, ovd.traslado_cab.almacen_origen.id);
		Kardex nk= new Kardex();
		nk.activo 	= true;
		nk.almacen 	= ovd.traslado_cab.almacen_origen;
		nk.creador 	= ovd.creador;
		nk.destino 	= ovd.traslado_cab.almacen_destino.codigo;
		nk.origen	= ovd.traslado_cab.almacen_origen.codigo;
		nk.fecha = new Date();
		nk.fecha_orden = ovd.traslado_cab.fecha;
		nk.documento = ovd.traslado_cab.id+"";
		nk.ingreso = BigDecimal.ZERO;
		nk.salida = ovd.cantidad;
		nk.movimiento = Util.MOVIMIENTO_SALIDA;
		nk.orden_id = ovd.traslado_cab.id;
		nk.precio_costo = BigDecimal.ZERO;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = ovd.producto;
		nk.stock_anterior = ok==null?BigDecimal.ZERO:ok.stock;
		nk.stock = nk.stock_anterior.subtract(nk.salida);
		nk.tipo = Util.TIPO_ORDEN_TRANSFORMACION;
		nk.fecha_vencimiento = ovd.fecha_vencimiento;
		nk.lote = ovd.lote;
		CRUD.save(app,nk);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TrasladoInternoCab> listRequerimientosPaciente(String app, Date fechaInicio, Date fechaFin, Almacen alOrigen, Almacen alDestino)
			throws Exception {
		String[] required = { 
				"responsable_envio", 
				"responsable_envio.persona",
				"almacen_origen", "almacen_destino",
				"responsable_recep",
				"responsable_recep.persona",
				"paciente",
				"medico"
		};
		String sql = " where fecha between '" + fechaInicio + "' and '" + fechaFin + "' and paciente is not null";	
		if (alOrigen!=null) {
			sql += !alOrigen.codigo.equals("Todos los Almacenes") ?" and almacen_origen =" + alOrigen.id + " ":"";
		}
		if (alDestino!=null) {
			sql += !alDestino.codigo.equals("Todos los Almacenes") ?" and almacen_destino=" + alDestino.id + " ":"";
		}
		return CRUD.list(app,TrasladoInternoCab.class, required, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrasladoInternoCab> list(String app, Date fechaInicio, Date fechaFin, Almacen alOrigen, Almacen alDestino)
			throws Exception {
		String[] required = { 
				"responsable_envio", 
				"responsable_envio.persona",
				"almacen_origen", "almacen_destino",
				"responsable_recep",
				"responsable_recep.persona"
		};
		String sql = " where fecha between '" + fechaInicio + "' and '" + fechaFin + "' ";	
		if (alOrigen!=null) {
			sql += !alOrigen.codigo.equals("Todos los Almacenes") ?" and almacen_origen =" + alOrigen.id + " ":"";
		}
		if (alDestino!=null) {
			sql += !alDestino.codigo.equals("Todos los Almacenes") ?" and almacen_destino=" + alDestino.id + " ":"";
		}
		return CRUD.list(app,TrasladoInternoCab.class, required, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrasladoInternoDet> list(String app, TrasladoInternoCab trasCab) throws Exception {
		String[] required = { 
				"producto", 
				"traslado_cab",
				"traslado_cab.almacen_origen",
				"traslado_cab.almacen_destino",
				"traslado_cab.paciente",
				"traslado_cab.medico",
		};
		String sql = " where traslado_cab ="+trasCab.id;	
		return CRUD.list(app,TrasladoInternoDet.class, required, sql);
	}

	@Override
	public void delete(String app, TrasladoInternoDet trasDet) throws Exception {
		
		try {
			
			CRUD.execute(app, "delete from logistica.traslados_internos_det where id = " + trasDet.id);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
			
		}
	}

	@Override
	public void updateAnul(String app, TrasladoInternoCab trasCab) throws Exception {

		try {
			
			TrasladoInternoCab trasl = (TrasladoInternoCab) CRUD.list(app,TrasladoInternoCab.class, " where id = " + trasCab.id).get(0);
			if(trasl.activo== false) {
				throw new Exception("El traslado ya figura anulado");
			}
			trasCab.activo =false;
			CRUD.update(app,trasCab);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
	}

	@Override
	public String codigo(String app, String codigo, Integer almacenId) throws Exception {
		System.out.println("Codigo: "+codigo+ "Integer: "+almacenId);
		String[] required = { "almacen_origen", "almacen_destino" };
		String sql = " where a.almacen_origen =" + almacenId + " order by b.codigo desc limit 1";
		String codigoLocal = codigo + "-0000000000";
		Integer numero = 0;
		List<TrasladoInternoCab> list = CRUD.list(app,TrasladoInternoCab.class, required, sql);
		if (!list.isEmpty()) {
			codigoLocal = list.get(0).codigo;
		}
		String[] nombre = codigoLocal.split("\\-");
		numero = Integer.valueOf(nombre[1]) + 1;
		return nombre[0] + "-" + Util.completeWithZeros(numero.toString(), 10);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getTrasladoPDF(String app, Integer ventaId, String usuarioId) throws Exception {
//		ReporteServiceImpl rep = new ReporteServiceImpl();
//		try {
//			String folderReports = "reports";
//			String jasperName = "trasladoArrecife.jasper";
//		
//			Map parameters = new HashMap();
//			parameters.put("ID",(long) ventaId);
//			return rep.getPDFJasper(jasperName, parameters, usuarioId, folderReports);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw new Exception(ex.getMessage());
//		}
            return null;
	}
	

}
