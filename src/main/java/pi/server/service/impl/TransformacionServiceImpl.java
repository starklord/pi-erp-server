package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import pi.service.util.Util;
import pi.service.model.almacen.Ensamblador;
import pi.service.model.almacen.Kardex;
import pi.service.model.almacen.OrdenRegularizacion;
import pi.service.model.almacen.OrdenRegularizacionDet;
import pi.service.model.almacen.PlantillaTransformacion;
import pi.service.model.almacen.PlantillaTransformacionDet;
import pi.service.model.almacen.Transformacion;
import pi.service.model.almacen.TransformacionDet;
import pi.service.model.auxiliar.PlantillaTransformacionDetModel;
import pi.service.model.auxiliar.TransformacionDetModel;
import pi.service.factory.Numbers;
import pi.service.TransformacionService;
import pi.service.util.db.Query;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/TransformacionService")
public class TransformacionServiceImpl extends HessianServlet implements TransformacionService {

	@Override
	public Transformacion get(String app, int transformacionId) throws Exception {
		String[] require = { "producto", "producto.unidad", "almacen", "ensamblador", "ensamblador.id" };
		List<Transformacion> list = CRUD.list(app,Transformacion.class, require,
				"where a.id =" + transformacionId + " order by numero desc limit 1");
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Transformacion getLast(String app, int empresaId) throws Exception {
		List<Transformacion> list = CRUD.list(app,Transformacion.class, " order by numero desc limit 1");
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<Transformacion> list(String app, int empresaId) throws Exception {

		return list(app,empresaId, -1, null, null);
	}

	@Override
	public List<Transformacion> list(String app, int empresaId, int ensambladorId, Date inicio, Date fin) throws Exception {

		String[] require = { "producto", "ensamblador", "ensamblador.id" };
		String filter = "where a.empresa = " + empresaId;

		if (inicio != null) {
			filter += " and fecha between '" + inicio + "' and '" + fin + "'";
		}
		if (ensambladorId != -1) {
			filter += " and a.ensamblador = " + ensambladorId;
		}
		filter += " order by numero desc";
		List<Transformacion> list = CRUD.list(app,Transformacion.class, require, filter);
		return list;
	}
	
	@Override
	public List<TransformacionDet> listDetalles(String app, int transformacionId) throws Exception {
		String[] require = { "transformacion", "producto", "producto.unidad", "almacen" };
		String filter = "where transformacion = " + transformacionId;

		List<TransformacionDet> list = CRUD.list(app,TransformacionDet.class, require, filter);
		return list;
	}

	@Override
	public List<TransformacionDetModel> listDetallesByModel(String app, int transformacionId) throws Exception {
		String[] require = { "transformacion", "producto", "producto.unidad", "almacen" };
		String filter = "where transformacion = " + transformacionId;

		List<TransformacionDetModel> listModel = new ArrayList<>();
		List<TransformacionDet> list = CRUD.list(app,TransformacionDet.class, require, filter);
		if (list.isEmpty()) {
			return listModel;
		}
		StringBuilder sbrProductIds = new StringBuilder();
		for (TransformacionDet item : list) {
			sbrProductIds.append(item.producto.id).append(",");
		}
		sbrProductIds.deleteCharAt(sbrProductIds.length() - 1);
		Query query = new Query(app,null);
		String select = " select distinct on (producto) producto, stock from almacen.kardex";
		query.select.set(select);
		query.where = " where almacen = " + list.get(0).almacen.id + " and producto in (" + sbrProductIds + ")";
		query.end = " order by producto, id desc ";
		Map<Integer, BigDecimal> stocks = new HashMap<>();
		Object[][] rs = query.initResultSet();
		if (rs != null) {
			for (int i = 0; i < rs.length; i++) {
				int pId = (Integer) rs[i][0];
				BigDecimal stock = (BigDecimal) rs[i][1];
				stocks.put(pId, stock);
			}
		}
		
		for (TransformacionDet item : list) {
			TransformacionDetModel model = new TransformacionDetModel();
			model.transformacionDet = item;
			model.stock = stocks.get(item.producto.id);
			listModel.add(model);
		}
		return listModel;
	}

	@Override
	public Transformacion saveOrUpdate(String app, boolean save, Transformacion trans, List<TransformacionDet> detalles)
			throws Exception {
		try {
			
			if (save) {
				Transformacion transLast = getLast(app,trans.empresa.id);
				int numero = transLast == null ? 1 : transLast.numero + 1;
				trans.numero = numero;
				CRUD.save(app,trans);
				OrdenMovimientoServiceImpl omService = new OrdenMovimientoServiceImpl();
				OrdenRegularizacion ors = new OrdenRegularizacion();
				ors.id = trans.id;
				ors.activo = true;
				ors.almacen = trans.almacen;
				ors.creador = trans.creador;
				ors.fecha = trans.fecha;
				ors.observaciones = "ORDEN DE TRANSFORMACION # " + trans.numero;
				ors.movimiento = Util.MOVIMIENTO_KARDEX_SALIDA;
				ors.numero = trans.numero;
				OrdenRegularizacion ore = new OrdenRegularizacion();
				ore.id = trans.id;
				ore.activo = true;
				ore.almacen = trans.almacen;
				ore.creador = trans.creador;
				ore.fecha = trans.fecha;
				ore.observaciones = "ORDEN DE TRANSFORMACION # " + trans.numero;
				ore.movimiento = Util.MOVIMIENTO_KARDEX_ENTRADA;
				ore.numero = trans.numero;
				OrdenRegularizacionDet orde = new OrdenRegularizacionDet();
				orde.creador = ore.creador;
				orde.activo = true;
				orde.producto = trans.producto;
				orde.cantidad = trans.cantidad;
				orde.orden_regularizacion = ore;
				saveKardexFromOrdenRegularizacion(app, orde, ore.observaciones);
				for (TransformacionDet det : detalles) {
					det.id = null;
					det.transformacion = trans;
					CRUD.save(app,det);
					OrdenRegularizacionDet ords = new OrdenRegularizacionDet();
					ords.creador = det.creador;
					ords.activo = true;
					ords.producto = det.producto;
					ords.cantidad = det.cantidad;
					ords.orden_regularizacion = ors;
					saveKardexFromOrdenRegularizacion(app, ords, ors.observaciones);
				}

			} else {

			}
			
			return trans;
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
	}

	@Override
	public void annul(String app, Transformacion trans) throws Exception {
		try {
			
			Transformacion transf = get(app,trans.id);
			if(transf.activo== false) {
				throw new Exception("La transformacion ya figura anulada");
			}
			trans.activo = false;
			CRUD.update(app,trans);
			OrdenRegularizacion ors = new OrdenRegularizacion();
			ors.id = trans.id;
			ors.activo = true;
			ors.almacen = trans.almacen;
			ors.creador = trans.creador;
			ors.fecha = trans.fecha;
			ors.observaciones = "ORDEN DE TRANSFORMACION # " + trans.numero;
			ors.movimiento = Util.MOVIMIENTO_KARDEX_SALIDA;
			ors.numero = trans.numero;
			OrdenRegularizacion ore = new OrdenRegularizacion();
			ore.id = trans.id;
			ore.activo = true;
			ore.almacen = trans.almacen;
			ore.creador = trans.creador;
			ore.fecha = trans.fecha;
			ore.observaciones = "ORDEN DE TRANSFORMACION # " + trans.numero;
			ore.movimiento = Util.MOVIMIENTO_KARDEX_ENTRADA;
			ore.numero = trans.numero;
			OrdenRegularizacionDet ords = new OrdenRegularizacionDet();
			ords.creador = ors.creador;
			ords.activo = true;
			ords.producto = trans.producto;
			ords.cantidad = trans.cantidad;
			ords.orden_regularizacion = ors;
			saveKardexFromOrdenRegularizacion(app, ords, ors.observaciones);
			List<TransformacionDet> detalles = listDetalles(app,trans.id);
			for (TransformacionDet det : detalles) {
				OrdenRegularizacionDet orde = new OrdenRegularizacionDet();
				orde.creador = det.creador;
				orde.activo = true;
				orde.producto = det.producto;
				orde.cantidad = det.cantidad;
				orde.orden_regularizacion = ore;
				saveKardexFromOrdenRegularizacion(app, orde, ore.observaciones);
			}
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	public void saveKardexFromOrdenRegularizacion(String app, OrdenRegularizacionDet det, String origen) throws Exception {

		char movimiento = det.orden_regularizacion.movimiento;
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app,det.producto.id, det.orden_regularizacion.almacen.id);
		Kardex nk = new Kardex();
		nk.activo = true;
		nk.almacen = det.orden_regularizacion.almacen;
		nk.creador = det.creador;
		nk.destino = "-";
		nk.origen = origen;
		nk.fecha = new Date();
		nk.fecha_orden = det.orden_regularizacion.fecha;
		nk.documento = det.orden_regularizacion.movimiento + "-" + det.orden_regularizacion.numero;
		nk.ingreso = movimiento == Util.MOVIMIENTO_KARDEX_ENTRADA ? det.cantidad : BigDecimal.ZERO;
		nk.salida = movimiento == Util.MOVIMIENTO_KARDEX_SALIDA ? det.cantidad : BigDecimal.ZERO;
		nk.movimiento = movimiento;
		nk.orden_id = det.orden_regularizacion.id;
		nk.precio_costo = BigDecimal.ZERO;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = det.producto;
		nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
		nk.stock = movimiento == Util.MOVIMIENTO_KARDEX_ENTRADA ? nk.stock_anterior.add(det.cantidad)
				: nk.stock_anterior.subtract(det.cantidad);
		nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
		CRUD.save(app,nk);
	}

	@Override
	public PlantillaTransformacion getLastPlantilla(String app, int empresaId) throws Exception {
		List<PlantillaTransformacion> list = CRUD.list(app,PlantillaTransformacion.class, " order by numero desc limit 1");
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public PlantillaTransformacion getLastPlantillaByProducto(String app, int productoId) throws Exception {
		List<PlantillaTransformacion> list = CRUD.list(app,PlantillaTransformacion.class,
				" where producto = " + productoId + " and a.activo is true limit 1");
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<PlantillaTransformacion> listPlantilla(String app, int empresaId) throws Exception {

		return listPlantilla(app, empresaId, null, null);
	}

	@Override
	public List<PlantillaTransformacion> listPlantilla(String app, int empresaId, Date inicio, Date fin) throws Exception {

		String[] require = { "producto" };
		String filter = "where a.empresa = " + empresaId;
		if (inicio != null) {
			filter += " and fecha between '" + inicio + "' and '" + fin + "'";
		}
		filter += " order by numero desc";
		List<PlantillaTransformacion> list = CRUD.list(app,PlantillaTransformacion.class, require, filter);
		return list;
	}

	@Override
	public List<PlantillaTransformacionDet> listPlantillaDetalles(String app, int plantillaId) throws Exception {
		String[] require = { "plantilla_transformacion", "producto" };
		String filter = "where plantilla_transformacion = " + plantillaId;

		List<PlantillaTransformacionDet> list = CRUD.list(app,PlantillaTransformacionDet.class, require, filter);
		return list;
	}

	@Override
	public List<PlantillaTransformacionDetModel> listPlantillaDetallesByProducto(String app, int productoId, int almacenId)
			throws Exception {
		String[] require = { "plantilla_transformacion", "producto" };
		String filter = "where b.producto = " + productoId + " and b.activo is true";
		List<PlantillaTransformacionDetModel> listModel = new ArrayList<>();
		List<PlantillaTransformacionDet> list = CRUD.list(app,PlantillaTransformacionDet.class, require, filter);

		if (list.isEmpty()) {
			return listModel;
		}
		StringBuilder sbrProductIds = new StringBuilder();
		for (PlantillaTransformacionDet item : list) {
			sbrProductIds.append(item.producto.id).append(",");
		}
		sbrProductIds.deleteCharAt(sbrProductIds.length() - 1);
		Query query = new Query(app,null);
		String select = " select distinct on (producto) producto, stock from almacen.kardex";
		query.select.set(select);
		query.where = " where almacen = " + almacenId + " and producto in (" + sbrProductIds + ")";
		query.end = " order by producto, id desc ";
		Map<Integer, BigDecimal> stocks = new HashMap<>();
		Object[][] rs = query.initResultSet();
		if (rs != null) {
			for (int i = 0; i < rs.length; i++) {
				int pId = (Integer) rs[i][0];
				BigDecimal stock = (BigDecimal) rs[i][1];
				stocks.put(pId, stock);
			}
		}
		
		for (PlantillaTransformacionDet item : list) {
			PlantillaTransformacionDetModel model = new PlantillaTransformacionDetModel();
			model.plantillaDet = item;
			model.stock = stocks.get(item.producto.id);
			listModel.add(model);
		}
		return listModel;
	}

	@Override
	public PlantillaTransformacion saveOrUpdatePlantilla(String app, boolean save, PlantillaTransformacion trans,
			List<PlantillaTransformacionDet> detalles) throws Exception {
		try {
			
			if (save) {
				PlantillaTransformacion transLast = getLastPlantilla(app,trans.empresa.id);
				int numero = transLast == null ? 1 : transLast.numero + 1;
				trans.numero = numero;
				CRUD.save(app,trans);
				for (PlantillaTransformacionDet det : detalles) {
					det.id = null;
					det.plantilla_transformacion = trans;
					CRUD.save(app,det);
				}
			} else {
				CRUD.update(app,trans);
				CRUD.execute(app, "delete from logistica.plantilla_transformacion_det where plantilla_transformacion = "
						+ trans.id);
				for (PlantillaTransformacionDet det : detalles) {
					det.id = null;
					det.plantilla_transformacion = trans;
					CRUD.save(app,det);
				}

			}
			
			return trans;
		} catch (Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
	}

	@Override
	public List<Ensamblador> listEnsambladores(String app, int empresaId) throws Exception {
		String[] require = { "id" };
		return CRUD.list(app,Ensamblador.class, require,
				"where a.activo is true and empresa = " + empresaId + " order by b.apellidos asc");
	}

	@Override
	public Ensamblador saveOrUpdateEnsamblador(String app, boolean save, Ensamblador ensamblador) throws Exception {
		if (save) {
			CRUD.save(app,ensamblador);
		} else {
			CRUD.update(app,ensamblador);
		}
		return ensamblador;
	}

	@Override
	public void sendToPrint(String app, int transformacionId, String user) throws Exception {
		try {

			System.out.println("serverprint: getting transformacion_detalles");
			List<TransformacionDet> detalles = listDetalles(app,transformacionId);
			Transformacion trans = get(app,transformacionId);
			StringBuilder sbr = new StringBuilder();

			// user|tipo|numero|day|month|year|codigo|producto|
			// para los detalles
			// codigo|descripcion|unidad|cantidad
			String tipo = "55";
			String numero = trans.numero + "";
			String dia = trans.fecha.getDate() + "";
			String mes = (trans.fecha.getMonth() + 1) + "";
			String anho = (trans.fecha.getYear() + 1900) + "";
			String codigo = trans.producto.codigo;
			String producto = trans.producto.nombre;
			String ensamblador = trans.ensamblador.id.toString();
			String observaciones = trans.observaciones.isEmpty() ? " " : trans.observaciones;
			sbr.append(user).append("|");
			sbr.append(tipo).append("|");
			sbr.append(numero).append("|");
			sbr.append(dia).append("|");
			sbr.append(mes).append("|");
			sbr.append(anho).append("|");
			sbr.append(codigo).append("|");
			sbr.append(producto).append("|");
			sbr.append(ensamblador).append("|");
			sbr.append(observaciones).append("||");
			for (TransformacionDet det : detalles) {
				sbr.append(det.producto.codigo).append("|");
				sbr.append(det.producto.nombre).append("|");
				sbr.append(det.producto.unidad.abreviatura).append("|");
				sbr.append(Numbers.getBigDecimal(det.cantidad, 0)).append("||");
			}
			System.out.println("serverprint: sendingPrintSocket");
			System.out.println("serverprint: success");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

    @Override
    public List<PlantillaTransformacionDet> listPlantillaDetallesByProducto(String app, int productoId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
