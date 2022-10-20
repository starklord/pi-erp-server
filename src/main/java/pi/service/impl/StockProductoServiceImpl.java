package pi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.StockProducto;
import pi.service.model.auxiliar.KardexProducto;
import pi.service.model.auxiliar.ProductoModel;
import pi.service.model.auxiliar.ProductoStockModel;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.factory.Numbers;
import pi.service.StockProductoService;
import pi.service.util.db.Query;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/StockProductoService")
public class StockProductoServiceImpl extends HessianServlet implements StockProductoService {

	@Override
	public ProductoModel getModelByCode(String app, List<Almacen> almacenes, String txt) {
		try {
			List<ProductoModel> list = new ArrayList<>();
			String[] require = { "marca", "linea", "unidad","unidad_conversion", "moneda" };
			String filterBuscarPor = " and ( a.codigo = '" + txt + "'";
			filterBuscarPor += " or ( (a.codigo_barras1 ilike '" + txt + "') " + " or (a.codigo_barras2 ilike 	'" + txt
					+ "') " + " or (a.codigo_barras3 ilike 	'" + txt + "') ) )";

			String filter = "where a.activo is true " + filterBuscarPor
					+ " order by a.nombre,a.codigo asc";
			List<Producto> productos = CRUD.list(app,Producto.class, require, filter);

			if (productos.isEmpty()) {
				return null;
			}
			StringBuilder sbrProductIds = new StringBuilder();
			for (Producto item : productos) {
				sbrProductIds.append(item.id).append(",");
			}
			sbrProductIds.deleteCharAt(sbrProductIds.length() - 1);
			Map<Integer, Map<Integer, BigDecimal>> maps = new HashMap<>();
			for (Almacen almacen : almacenes) {
				Query query = new Query(app,null);
				String select = " select distinct on (producto) producto, stock from almacen.kardex";
				query.select.set(select);
				query.where = " where almacen = " + almacen.id
						+ " and producto in (" + sbrProductIds + ")";
				query.end = " order by producto, id desc ";
				Map<Integer, BigDecimal> stocks = new HashMap<>();
				Object[][] rs = query.initResultSet();
				if (rs != null) {
					for (int i = 0; i < rs.length; i++) {
						int productoId = (Integer) rs[i][0];
						BigDecimal stock = (BigDecimal) rs[i][1];
						stocks.put(productoId, stock);
					}
				}
				maps.put(almacen.id, stocks);
			}
			for (Producto producto : productos) {
				ProductoModel pm = new ProductoModel();
				pm.total_stock = BigDecimal.ZERO;
				pm.producto = producto;
				pm.stocks = new ArrayList<>();
				for (Almacen almacen : almacenes) {
					ProductoStockModel psm = new ProductoStockModel();
					psm.almacen = almacen;
					Map<Integer, BigDecimal> map = maps.get(almacen.id);
					psm.stock = BigDecimal.ZERO;
					if (map != null) {
						psm.stock = map.get(producto.id) == null ? BigDecimal.ZERO : map.get(producto.id);
					}
					pm.total_stock = pm.total_stock.add(psm.stock);
					pm.stocks.add(psm);
				}
				list.add(pm);
			}
			return list.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ProductoModel> listProductosParaReponer(String app) throws Exception {

		List<Almacen> listAlmacenes = CRUD.list(app,Almacen.class, "where es_principal is true");

		List<ProductoModel> list = this.listModels(app,listAlmacenes, -1, -1, false, "");
		List<ProductoModel> listFinal = new ArrayList<>();
		for (ProductoModel pm : list) {
			BigDecimal stock = pm.stocks.get(0).stock;
			if (stock.compareTo(BigDecimal.ZERO) > 0 && stock.compareTo(pm.producto.stock_minimo) <= 0) {
				listFinal.add(pm);
			}
		}
		return listFinal;

	}

	@Override
	public List<ProductoModel> listModels(String app, List<Almacen> almacenes, int marcaId, int lineaId,boolean ver_anulados, String txt) {
		List<ProductoModel> list = new ArrayList<>();
		try {

			String[] require = { "marca", "linea", "unidad","unidad_conversion", "moneda" };
			String filterMarca = marcaId == -1 ? "" : " and a.marca = " + marcaId;
			String filterLinea = lineaId == -1 ? "" : " and a.linea = " + lineaId;
			String filterBuscarPor = " and ( a.codigo ilike '%" + txt + "%'";
			filterBuscarPor += " or  a.nombre ilike '%" + txt + "%'";
			filterBuscarPor += " or ( (a.codigo_barras1 ilike '" + txt + "') " + " or (a.codigo_barras2 ilike 	'" + txt
					+ "') " + " or (a.codigo_barras3 ilike 	'" + txt + "') ) )";

			String queryPreffix = ver_anulados ? "where a.id is not null " : "where a.activo is true ";
			String querySuffix = filterMarca + filterLinea + filterBuscarPor + " order by a.nombre,a.codigo asc";
			String filter = queryPreffix  + querySuffix;

			List<Producto> productos = CRUD.list(app,Producto.class, require, filter);

			if (productos.isEmpty()) {
				return list;
			}
			StringBuilder sbrProductIds = new StringBuilder();
			for (Producto item : productos) {
				sbrProductIds.append(item.id).append(",");
			}
			sbrProductIds.deleteCharAt(sbrProductIds.length() - 1);
			Map<Integer, Map<Integer, BigDecimal>> maps = new HashMap<>();
			for (Almacen almacen : almacenes) {
				Query query = new Query(app,null);
				String select = " select distinct on (producto) producto, stock from almacen.kardex";
				query.select.set(select);
				query.where = " where almacen = " + almacen.id
						+ " and producto in (" + sbrProductIds + ")";
				query.end = " order by producto, id desc ";
				Map<Integer, BigDecimal> stocks = new HashMap<>();
				Object[][] rs = query.initResultSet();
				if (rs != null) {
					for (int i = 0; i < rs.length; i++) {
						int productoId = (Integer) rs[i][0];
						BigDecimal stock = (BigDecimal) rs[i][1];
						stocks.put(productoId, stock);
					}
				}
				maps.put(almacen.id, stocks);
			}
			for (Producto producto : productos) {
				ProductoModel pm = new ProductoModel();
				pm.total_stock = BigDecimal.ZERO;
				pm.producto = producto;
				pm.stocks = new ArrayList<>();
				for (Almacen almacen : almacenes) {
					ProductoStockModel psm = new ProductoStockModel();
					psm.almacen = almacen;
					Map<Integer, BigDecimal> map = maps.get(almacen.id);
					psm.stock = BigDecimal.ZERO;
					if (map != null) {
						psm.stock = map.get(producto.id) == null ? BigDecimal.ZERO : map.get(producto.id);
					}
					pm.total_stock = pm.total_stock.add(psm.stock);
					pm.stocks.add(psm);
				}
				list.add(pm);
			}
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Producto> listProductos(String app, int sucursalId, int marcaId, int lineaId, int buscarPor, String txt)
			throws Exception {

		String[] require = { "marca", "linea", "unidad", "moneda" };
		String filterMarca = marcaId == -1 ? "" : " and a.marca = " + marcaId;
		String filterLinea = lineaId == -1 ? "" : " and a.linea = " + lineaId;
		String filterBuscarPor = " and ( a.codigo ilike '%" + txt + "%'";
		filterBuscarPor += " or  a.nombre ilike '%" + txt + "%'";
		filterBuscarPor += " or ( (a.codigo_barras1 ilike '" + txt + "') " + " or (a.codigo_barras2 ilike 	'" + txt
				+ "') " + " or (a.codigo_barras3 ilike 	'" + txt + "') ) )";
		String filter = "where a.activo is true " + filterMarca + filterLinea + filterBuscarPor
				+ " order by a.nombre,a.codigo asc";
		return CRUD.list(app,Producto.class, require, filter);
	}

	@Override
	public List<StockProducto> listStocks(String app, int almacenId, int marcaId, int lineaId, int buscarPor, String txt)
			throws Exception {

		String[] require = { "almacen", "producto", "producto.marca", "producto.linea", "producto.unidad",
				"producto.moneda" };
		String filterMarca = marcaId == -1 ? "" : " and c.marca = " + marcaId;
		String filterLinea = lineaId == -1 ? "" : " and c.linea = " + lineaId;
		String filterBuscarPor = " and (  c.codigo ilike '%" + txt + "%'";
		filterBuscarPor += " or  c.nombre ilike '%" + txt + "%'";
		filterBuscarPor += " or ( (c.codigo_barras1 ilike '" + txt + "') " + " or (c.codigo_barras2 ilike 	'" + txt
				+ "') " + " or (c.codigo_barras3 ilike 	'" + txt + "') ) )";
		String filter = "where a.almacen =" + almacenId + filterMarca + filterLinea + filterBuscarPor
				+ " and c.activo is true order by c.nombre,c.codigo_interno asc";
		return CRUD.list(app,StockProducto.class, require, filter);
	}

	@Override
	public List<OrdenCompraDet> listByOrdenesCompra(String app, int sucursalId, int marcaId, int lineaId, int buscarPor, String txt)
			throws Exception {

		String[] require = { "almacen", "producto", "producto.marca", "producto.linea", "producto.unidad",
				"producto.moneda", "orden_compra" };
		String filterMarca = marcaId == -1 ? "" : " and c.marca = " + marcaId;
		String filterLinea = lineaId == -1 ? "" : " and c.linea = " + lineaId;
		String filterBuscarPor = " and c.codigo ilike '" + txt + "'";
		filterBuscarPor += " or  c.nombre ilike '%" + txt + "%'";
		filterBuscarPor += " or ( (c.codigo_barras1 ilike '" + txt + "') " + " or (c.codigo_barras2 ilike 	'" + txt
				+ "') " + " or (c.codigo_barras3 ilike 	'" + txt + "') ) ";
		String filter = "where b.sucursal=" + sucursalId + filterMarca + filterLinea + filterBuscarPor
				+ " and h.fecha <= '2019-02-08'" + " and a.activo is true order by c.nombre,c.codigo_interno asc";
		return CRUD.list(app,OrdenCompraDet.class, require, filter);
	}

	@Override
	public List<OrdenCompraDet> listByCodeOrBarcode(String app, String txt) throws Exception {
		String[] require = { "producto", "producto.marca", "producto.linea" };
		String filter = "where a.activo is true";
		filter += " and b.codigo ilike '" + txt + "'";
		filter += " and ( (b.codigo_barras1 ilike '" + txt + "') " + " or (b.codigo_barras2 ilike 	'" + txt + "') "
				+ " or (b.codigo_barras3 ilike 	'" + txt + "') ) ";
		List<OrdenCompraDet> list = CRUD.list(app,OrdenCompraDet.class, require, filter);
		return list;
	}

	@Override
	public Producto getByCode(String app, int sucursalId, String txt) throws Exception {
		String[] require = { "marca", "linea", "unidad", "moneda" };
		String filterBuscarPor = " and a.codigo ilike '%" + txt + "%'";
		String filter = "where a.activo is true " + filterBuscarPor + " order by a.nombre,a.codigo asc limit 1";
		List<Producto> list = CRUD.list(app,Producto.class, require, filter);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Producto getByCodeOrName(String app, int sucursalId, String codigo, String nombre) throws Exception {
		String[] require = { "marca", "linea", "unidad", "moneda" };
		String filterBuscarPor = " and a.codigo ilike '%" + codigo + "%' or a.nombre ilike '%" + nombre + "%'";
		String filter = "where a.activo is true " + filterBuscarPor
				+ " order by a.nombre, a.codigo_interno asc limit 1";
		List<Producto> list = CRUD.list(app,Producto.class, require, filter);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<KardexProducto> listKardexGeneralProductos(String app, int empresaId) throws Exception {
		try {
			List<KardexProducto> list = new ArrayList<>();
			Query query = new Query(app,null);
			String select = "select oc.numero,oc.fecha, 'INGRESO' as concepto,ocd.cantidad,ocd.precio_unitario,p.id,p.codigo,p.nombre,oc.impuesto_incluido,imp.valor from logistica.orden_compra_det as ocd"
					+ " left join logistica.orden_compra as oc on oc.id = ocd.orden_compra"
					+ " left join public.impuesto as imp on imp.id = oc.impuesto"
					+ " left join logistica.producto as p on p.id = ocd.producto"
					+ " where oc.activo is true and p.empresa = " + empresaId + " union all"
					+ " select ov.numero,ov.fecha, 'SALIDA' as concepto,ovd.cantidad,ovd.precio_unitario,p.id,p.codigo,p.nombre,true,0 from venta.orden_venta_det as ovd"
					+ " left join venta.orden_venta as ov on ov.id = ovd.orden_venta"
					+ " left join logistica.producto as p on p.id = ovd.producto";
			query.select.set(select);
			query.where = " where ov.activo is true and p.empresa = " + empresaId;
			query.end = " order by id, fecha asc";
			Object[][] rs = query.initResultSet();

			if (rs.length == 0) {
				return list;
			}
			BigDecimal saldoUnidad = BigDecimal.ZERO;
			BigDecimal saldoSoles = BigDecimal.ZERO;
			BigDecimal precioUnitario = BigDecimal.ZERO;
			boolean isFirst = true;
			int productoId = (Integer) rs[0][5];

			for (int i = 0; i < rs.length; i++) {
				KardexProducto kardex = new KardexProducto();
				kardex.id = i;
				kardex.numero = (Integer) rs[i][0];
				kardex.fecha = (Date) rs[i][1];
				kardex.concepto = (String) rs[i][2];
				kardex.cantidad = (BigDecimal) rs[i][3];
				kardex.precioUnitario = (BigDecimal) rs[i][4];
				kardex.producto = new Producto();
				kardex.producto.id = (Integer) rs[i][5];
				kardex.producto.codigo = (String) rs[i][6];
				kardex.producto.nombre = (String) rs[i][7];
				kardex.impuestoIncluido = (Boolean) rs[i][8];
				kardex.valorImpuesto = (BigDecimal) rs[i][9];
				// if accumulate or not
				if (kardex.producto.id != productoId) {
					isFirst = true;
					saldoUnidad = BigDecimal.ZERO;
					saldoSoles = BigDecimal.ZERO;
					precioUnitario = BigDecimal.ZERO;
					isFirst = true;
					productoId = kardex.producto.id;
				}

				if (kardex.concepto.equals("SALIDA") && !isFirst) {
					kardex.precioUnitario = precioUnitario;
				}

				if (kardex.concepto.equals("SALIDA")) {
					kardex.costoTotal = kardex.precioUnitario.multiply(kardex.cantidad);
					saldoUnidad = saldoUnidad.subtract(kardex.cantidad);
					saldoSoles = saldoSoles.subtract(kardex.costoTotal);
				} else {
					if (!kardex.impuestoIncluido) {
						kardex.precioUnitario = kardex.precioUnitario
								.multiply(kardex.valorImpuesto.add(BigDecimal.ONE));

					}
					CRUD.execute(app, "update logistica.producto set costo_ultima_compra = " + kardex.precioUnitario
							+ " where id = " + kardex.producto.id);

					kardex.costoTotal = kardex.precioUnitario.multiply(kardex.cantidad);
					saldoUnidad = saldoUnidad.add(kardex.cantidad);
					saldoSoles = saldoSoles.add(kardex.costoTotal);
				}
				kardex.saldoUnidad = saldoUnidad;
				kardex.saldoSoles = saldoSoles;

				if (kardex.saldoUnidad.compareTo(BigDecimal.ZERO) == 0) {
					kardex.actualPromedio = BigDecimal.ZERO;
				} else {
					kardex.actualPromedio = Numbers.divide(kardex.saldoSoles, kardex.saldoUnidad, 2);
				}
				precioUnitario = kardex.actualPromedio;

				/// add data
				isFirst = false;
				if (i == rs.length - 1) {
					list.add(kardex);
				} else {
					if (productoId != (Integer) rs[i + 1][5]) {
						list.add(kardex);
					}
				}
			}
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}

	}

	@Override
	public List<KardexProducto> ListKardexPorProducto(String app, int productoId) throws Exception {

		try {

			List<KardexProducto> list = new ArrayList<>();

			Query query = new Query(app,null);
			String select = "select oc.numero,oc.fecha, 'INGRESO' as concepto,ocd.cantidad,ocd.precio_unitario,ocd.producto,oc.impuesto_incluido,"
					+ "imp.valor, pc.apellidos as cliente, ocd.lote as lote, ocd.fecha_vencimiento as fecha_vencimiento from logistica.orden_compra_det as ocd"
					+ " left join logistica.orden_compra as oc on oc.id = ocd.orden_compra"
					+ " left join persona.direccion as dc on dc.id = oc.direccion_proveedor"
					+ " left join persona.persona as pc on pc.id = dc.persona"
					+ " left join public.impuesto as imp on imp.id = oc.impuesto" + " where ocd.producto =" + productoId
					+ " and oc.activo is true" + " union all"
					+ " select ov.numero,ov.fecha, 'SALIDA' as concepto,ovd.cantidad,ovd.precio_unitario,ovd.producto, true,"
					+ "0 , pv.apellidos as cliente, '-' as lote, 'now()' as fecha_vencimiento from venta.orden_venta_det  as ovd"
					+ " left join venta.orden_venta as ov on ov.id = ovd.orden_venta"
					+ " left join persona.direccion as dv on dv.id = ov.direccion_cliente"
					+ " left join persona.persona as pv on pv.id = dv.persona";
			query.select.set(select);
			query.where = " where ovd.producto =" + productoId + " and ov.activo is true";
			query.end = " order by producto, fecha asc";
			System.out.println(select + query.where + query.end);
			Object[][] rs = query.initResultSet();

			if (rs.length == 0) {
				return list;
			}
			BigDecimal saldoUnidad = BigDecimal.ZERO;
			BigDecimal saldoSoles = BigDecimal.ZERO;
			BigDecimal precioUnitario = BigDecimal.ZERO;

			for (int i = 0; i < rs.length; i++) {
				KardexProducto kardex = new KardexProducto();
				kardex.id = i;
				kardex.numero = (Integer) rs[i][0];
				kardex.fecha = (Date) rs[i][1];
				kardex.concepto = (String) rs[i][2];
				kardex.cantidad = (BigDecimal) rs[i][3];
				kardex.precioUnitario = (BigDecimal) rs[i][4];
				kardex.producto = new Producto();
				kardex.producto.id = (Integer) rs[i][5];
				kardex.impuestoIncluido = (Boolean) rs[i][6];
				kardex.valorImpuesto = (BigDecimal) rs[i][7];
				kardex.cliente = (String) rs[i][8];
				kardex.lote = (String) rs[i][9];
				kardex.fecha_vencimiento = (Date) rs[i][10];
				if (kardex.concepto.equals("SALIDA") && i > 0) {
					kardex.precioUnitario = precioUnitario;
					kardex.costoTotal = kardex.precioUnitario.multiply(kardex.cantidad);
					saldoUnidad = saldoUnidad.subtract(kardex.cantidad);
					saldoSoles = saldoSoles.subtract(kardex.costoTotal);

				} else {
					if (!kardex.impuestoIncluido) {
						kardex.precioUnitario = kardex.precioUnitario
								.multiply(kardex.valorImpuesto.add(BigDecimal.ONE));
					}
					kardex.costoTotal = kardex.precioUnitario.multiply(kardex.cantidad);
					saldoUnidad = saldoUnidad.add(kardex.cantidad);
					saldoSoles = saldoSoles.add(kardex.costoTotal);
				}
				kardex.saldoUnidad = saldoUnidad;
				kardex.saldoSoles = saldoSoles;

				if (kardex.saldoUnidad.compareTo(BigDecimal.ZERO) == 0) {
					kardex.actualPromedio = BigDecimal.ZERO;
				} else {
					kardex.actualPromedio = Numbers.divide(kardex.saldoSoles, kardex.saldoUnidad, 2);

				}
				precioUnitario = kardex.actualPromedio;
				list.add(kardex);
			}
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public void setStock(String app, int stockProductoId, BigDecimal newStock) throws Exception {
		CRUD.execute(app, "update logistica.stock_producto set stock = " + newStock + " where id = " + stockProductoId);
	}

	@Override
	public void updateStock(String app, int productoId, int almacenId, BigDecimal addStock) throws Exception {
		String filter = "where producto = " + productoId + " and almacen = " + almacenId + " limit 1";
		List<StockProducto> list = CRUD.list(app,StockProducto.class, filter);
		if (list.isEmpty()) {
			StockProducto sp = new StockProducto();
			sp.activo = true;
			sp.almacen = new Almacen();
			sp.almacen.id = almacenId;
			sp.producto = new Producto();
			sp.producto.id = productoId;
			sp.stock = addStock;
			sp.creador = "root";
			CRUD.save(app,sp);
		} else {
			StockProducto sp = list.get(0);
			sp.stock = sp.stock.add(addStock);
			CRUD.update(app,sp);
		}
	}

}
