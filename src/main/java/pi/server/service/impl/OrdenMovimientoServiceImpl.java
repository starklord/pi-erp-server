package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pi.service.util.Util;
import pi.service.model.almacen.Kardex;
import pi.service.model.almacen.OrdenRegularizacion;
import pi.service.model.almacen.OrdenRegularizacionDet;
import pi.service.OrdenMovimientoService;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/OrdenMovimientoService")
public class OrdenMovimientoServiceImpl extends HessianServlet implements OrdenMovimientoService {

	@Override
	public OrdenRegularizacion getOrdenRegularizacion(String app, int ordenId) throws Exception {

		String filter = " where id = " + ordenId;

		List<OrdenRegularizacion> list = CRUD.list(app,OrdenRegularizacion.class, filter);
		return list.isEmpty() ? null : list.get(0);
	}
	
	@Override
	public OrdenRegularizacion saveOrdenRegularizacion(String app, OrdenRegularizacion orden, List<OrdenRegularizacionDet> detalles)
			throws Exception {
		try {
			List<OrdenRegularizacion> last = CRUD.list(app,OrdenRegularizacion.class,
							" where a.movimiento = '"+orden.movimiento+"' order by numero desc limit 1");
			int numero = last.isEmpty()?1:(last.get(0).numero+1);
			orden.numero = numero;
			
			
			CRUD.save(app,orden);
			for(OrdenRegularizacionDet ord : detalles) {
				ord.id = null;
				ord.orden_regularizacion = orden;
				CRUD.save(app,ord);
				saveKardexFromOrdenRegularizacion(app, ord);
			}
			
			return orden;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public OrdenRegularizacion saveOrdenRegularizacion(String app, boolean save, OrdenRegularizacion orden,
			List<OrdenRegularizacionDet> detalles) throws Exception {
		try {
			if (save) {
				List<OrdenRegularizacion> last = CRUD.list(app,OrdenRegularizacion.class,
						" where a.movimiento = '" + orden.movimiento + "' order by numero desc limit 1");
				int numero = last.isEmpty() ? 1 : (last.get(0).numero + 1);
				orden.numero = numero;
				CRUD.save(app,orden);
			}else {
				CRUD.execute(app, "delete from almacen.orden_regularizacion_det where orden_regularizacion = " + orden.id);
				CRUD.execute(app, "delete from almacen.kardex where orden_id = " + orden.id + " and tipo = 'R'");
				CRUD.update(app,orden);
			}

			for (OrdenRegularizacionDet ord : detalles) {
				ord.id = null;
				ord.orden_regularizacion = orden;
				CRUD.save(app,ord);
				saveKardexFromOrdenRegularizacion(app, ord);
			}

			return orden;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	private void saveKardexFromOrdenRegularizacion(String app, OrdenRegularizacionDet det) throws Exception {

		char movimiento = det.orden_regularizacion.movimiento;
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app, det.producto.id, det.orden_regularizacion.almacen.id);
		Kardex nk = new Kardex();
		nk.activo = true;
		nk.almacen = det.orden_regularizacion.almacen;
		nk.creador = det.creador;
		nk.destino = "-";
		nk.origen = "-";
		nk.fecha = new Date();
		nk.fecha_orden = det.orden_regularizacion.fecha;
		nk.documento = det.orden_regularizacion.movimiento + "-" + det.orden_regularizacion.numero;
		if (movimiento == Util.MOVIMIENTO_KARDEX_ENTRADA) {
			nk.ingreso = det.cantidad.multiply(det.producto.contenido).add(det.cantidad_fraccion);
			nk.salida = BigDecimal.ZERO;
		} else {
			nk.ingreso = BigDecimal.ZERO;
			nk.salida = det.cantidad.multiply(det.producto.contenido).add(det.cantidad_fraccion);
		}
		nk.movimiento = movimiento;
		nk.orden_id = det.orden_regularizacion.id;
		nk.precio_costo = BigDecimal.ZERO;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = det.producto;
		nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
		nk.stock = movimiento == Util.MOVIMIENTO_KARDEX_ENTRADA ? nk.stock_anterior.add(nk.ingreso)
				: nk.stock_anterior.subtract(nk.salida);
		nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
		nk.fecha_vencimiento = det.fecha_vencimiento;
		nk.lote = det.lote;
		CRUD.save(app,nk);
	}

	@Override
	public List<OrdenRegularizacion> listOrdenesRegularizacion(String app, char movimiento, Date inicio, Date fin)
			throws Exception {
		String[] require = { "almacen" };
		String filter = " where a.movimiento = '" + movimiento + "' and a.fecha between '" + inicio + "' and '" + fin
				+ "'";

		return CRUD.list(app,OrdenRegularizacion.class, require, filter);
	}

	@Override
	public List<OrdenRegularizacionDet> listDetallesOrdenRegularizacion(String app, int ordenId) throws Exception {
		String[] require = { "producto", "orden_regularizacion", "orden_regularizacion.almacen" };
		String filter = " where orden_regularizacion = " + ordenId;

		return CRUD.list(app,OrdenRegularizacionDet.class, require, filter);
	}

}
