package pi.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import pi.service.model.FormaPago;
import pi.service.model.Moneda;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Kardex;
import pi.service.model.almacen.Producto;
import pi.service.model.empresa.Sucursal;
import pi.service.model.logistica.ImportacionInicial;
import pi.service.model.logistica.OrdenCompra;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.model.logistica.OrdenEntradaSalida;
import pi.service.model.logistica.OrdenEntradaSalidaDet;
import pi.service.model.persona.Direccion;
import pi.service.model.venta.OrdenVentaDet;
import pi.service.factory.Numbers;
import pi.service.OrdenCompraService;
import pi.service.util.Util;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/OrdenCompraService")
public class OrdenCompraServiceImpl extends HessianServlet implements OrdenCompraService {

	@Override
	public List<OrdenCompra> list(String app, int sucursalId) throws Exception {
		String[] req = { "almacen_entrega", "sucursal", "direccion_proveedor", "direccion_proveedor.persona",
				"forma_pago", "moneda" };
		String filter = "where a.sucursal = " + sucursalId + "order by numero desc";
		return CRUD.list(app,OrdenCompra.class, req, filter);
	}

	@Override
	public List<OrdenCompra> list(String app, int sucursalId, Date inicio, Date fin, int proveedorId) throws Exception {
		String[] req = { "almacen_entrega", "sucursal", "direccion_proveedor", "direccion_proveedor.persona",
				"forma_pago", "moneda" };
		String filter = "where fecha  >= '" + inicio + "' and fecha <= '" + fin
				+ "' ";
		if(sucursalId != -1) {
			filter += " and a.sucursal =" + sucursalId;
		}
		if (proveedorId != -1) {
			filter += " and e.id =" + proveedorId;
		}
		filter += " order by numero desc";
		return CRUD.list(app,OrdenCompra.class, req, filter);
	}
	
	@Override
	public List<OrdenCompraDet> listDetalles(String app, int sucursalId, Date inicio, Date fin) throws Exception {
		String[] req = { "orden_compra","orden_compra.direccion_proveedor","orden_compra.direccion_proveedor.persona"
				, "orden_compra.impuesto", "producto", "producto.marca", "producto.linea",
				"producto.unidad" };
		String filter = "where b.activo is true and b.sucursal = " + sucursalId
				+ " and b.fecha between '"+inicio+"' and '"+ fin+"' order by b.fecha desc";
		return CRUD.list(app,OrdenCompraDet.class, req, filter);

	}

	@Override
	public List<OrdenCompra> getLastOrdenCompra(String app) throws Exception {
		
		return null;
	}

	@Override
	public List<OrdenEntradaSalida> getLastOrdenEntrada(String app, int sucursal) throws Exception {
		
		return null;
	}
	
	@Override
	public BigDecimal getCostoPromedioByProductoId(String app, int productoId, Date fechaUltima) throws Exception {
		String[] require = {"orden_compra","orden_compra.impuesto","producto"};
		String where = " where b.activo is true and producto = " + productoId;
		if(fechaUltima!=null) {
			where+=" and b.fecha <='" + fechaUltima.toString()+"' ";
		}
		where+= " order by b.fecha desc";
		List<OrdenCompraDet> detalles = CRUD.list(app,OrdenCompraDet.class,require,where);
		BigDecimal costoPromedio= BigDecimal.ZERO;
		BigDecimal totalCantidad= BigDecimal.ZERO;
		BigDecimal totalCosto	= BigDecimal.ZERO;
		if(detalles.isEmpty()) {
			List<Producto> productos = CRUD.list(app,Producto.class,"where id = " + productoId);
			return productos.get(0).costo_ultima_compra;
		}
		for (OrdenCompraDet det : detalles) {
			
			OrdenCompra ordenCompra = det.orden_compra;
			BigDecimal total = det.total;
			if (!ordenCompra.impuesto_incluido) {
				total = total.multiply(new BigDecimal("1.18"));
			}
			if(ordenCompra.moneda.id== Util.MONEDA_DOLARES_ID) {
				total = total.multiply(ordenCompra.tipo_cambio);
			}
			totalCantidad = det.cantidad;
			
			totalCosto = totalCosto.add(total);
			System.out.println("cantidad: " + totalCantidad + " total: " + total+"" + totalCosto);
		}
		if(totalCantidad.compareTo(BigDecimal.ZERO)==0) {
			return BigDecimal.ZERO;
		}else {
			costoPromedio = Numbers.divide(totalCosto,totalCantidad, 4);
			return costoPromedio;
		}
	}
	
	@Override
	public void updateCostoPromedioToVentas(String app, List<OrdenVentaDet> list) throws Exception {
		try {
			for(OrdenVentaDet ovd : list) {
				ovd.costo_unitario = getCostoPromedioByProductoId(app, ovd.producto.id, ovd.orden_venta.fecha);
				CRUD.update(app,ovd);
			}
			
		}catch(Exception ex) {
			throw new Exception(ex.getMessage());
			
		}
	}

	@Override
	public OrdenCompra save(String app, OrdenCompra ordenCompra, List<OrdenCompraDet> detalles) throws Exception {
		try {
			Update.beginTransaction(app);
			String where = "where sucursal = " + ordenCompra.sucursal.id + " order by numero desc limit 1";
			List<OrdenCompra> list = CRUD.list(app,OrdenCompra.class, where);
			int numero = 0;
			if (list.isEmpty()) {
				numero++;
			} else {
				numero = list.get(0).numero + 1;
			}
			ordenCompra.numero = numero;
			CRUD.save(app,ordenCompra);
			for (OrdenCompraDet det : detalles) {
				det.orden_compra = ordenCompra;
				det.id = null;
				det.almacen = ordenCompra.almacen_entrega;
				CRUD.save(app,det);
				BigDecimal precioUnitario = det.precio_unitario;
				if (!ordenCompra.impuesto_incluido) { 
					precioUnitario = precioUnitario.multiply(new BigDecimal("1.18"));
				}
				if(ordenCompra.moneda.id== Util.MONEDA_DOLARES_ID) {
					precioUnitario = precioUnitario.multiply(ordenCompra.tipo_cambio);
				}
				//BigDecimal costo_promedio = getCostoPromedioByProductoId(det.producto.id,null);
				BigDecimal costo_promedio = precioUnitario;
				CRUD.execute(app, "update logistica.producto set costo_ultima_compra =  " + costo_promedio
						+ "where id = " + det.producto.id);
				det.precio_unitario = precioUnitario;
				saveKardexFromOrdenCompraDet(app, det);
			}
			Update.commitTransaction(app);
			return ordenCompra;
		} catch (Exception ex) {
			Update.rollbackTransaction(app);
			ex.printStackTrace();
			throw new Exception("Problemas al grabar");
		}
	}

	private void saveKardexFromOrdenCompraDet(String app, OrdenCompraDet ocd) throws Exception {
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app, ocd.producto.id, ocd.almacen.id);
		Kardex nk = new Kardex();
		nk.activo = true;
		nk.almacen = ocd.almacen;
		nk.creador = ocd.creador;
		nk.destino = "-";
		nk.origen = ocd.orden_compra.direccion_proveedor.persona.toString();
		nk.fecha = new Date();
		nk.fecha_orden = ocd.orden_compra.fecha;
		nk.documento = ocd.orden_compra.documento_pago;
		nk.ingreso = ocd.cantidad.multiply(ocd.producto.contenido);
		nk.salida = BigDecimal.ZERO;
		nk.movimiento = Util.MOVIMIENTO_KARDEX_ENTRADA;
		nk.orden_id = ocd.orden_compra.id;
		nk.precio_costo = ocd.precio_unitario;
		if (!ocd.orden_compra.impuesto_incluido) {
			nk.precio_costo = ocd.precio_unitario.multiply(new BigDecimal("1.18"));
		}
		nk.fecha_vencimiento = ocd.fecha_vencimiento;
		nk.lote = ocd.lote;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = ocd.producto;
		nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
		nk.stock = nk.stock_anterior.add(nk.ingreso);
		nk.tipo = Util.TIPO_ORDEN_COMPRA;
		nk.usado = BigDecimal.ZERO;
		CRUD.save(app,nk);
	}

	@Override
	public void update(String app, OrdenCompra ordenCompra, List<OrdenCompraDet> detalles) throws Exception {
		//TODO: falta
		// try {
		// 	OrdenCompraServiceImpl ordenService = new OrdenCompraServiceImpl();
		// 	List<OrdenCompraDet> detsOc = ordenService.listDetalles(ordenCompra.id);
		// 	List<Object>dets = new ArrayList<>(detsOc);
		// 	CRUD.execute(app, "delete from logistica.orden_compra_det where orden_compra = " + ordenCompra.id);
			
		// 	for (OrdenCompraDet det : detalles) {
		// 		det.orden_compra = ordenCompra;
		// 		det.id = null;
		// 		det.almacen = ordenCompra.almacen_entrega;
		// 		BigDecimal precioUnitario = det.precio_unitario;
		// 		if (!ordenCompra.impuesto_incluido) {
		// 			precioUnitario = precioUnitario.multiply(ordenCompra.impuesto.valor.add(BigDecimal.ONE));
		// 		}
		// 		if(ordenCompra.moneda.id== Util.MONEDA_DOLARES_ID) {
		// 			precioUnitario = precioUnitario.multiply(ordenCompra.tipo_cambio);
		// 		}
		// 		BigDecimal costo_promedio = getCostoPromedioByProductoId(det.producto.id,null);
		// 		CRUD.execute(app, "update logistica.producto set costo_ultima_compra =  " + costo_promedio
		// 				+ "where id = " + det.producto.id);
		// 	}

		// } catch (Exception ex) {
		// 	ex.printStackTrace();
		// 	throw new Exception("Problemas al grabar");
		// }
	}

	@Override
	public List<OrdenCompraDet> listDetalles(String app, int ordenCompraId) throws Exception {
		String[] req = { "orden_compra", "producto", "producto.marca", "producto.linea",
				"producto.unidad" };
		String filter = "where orden_compra = " + ordenCompraId;
		return CRUD.list(app,OrdenCompraDet.class, req, filter);

	}

	@Override
	public List<OrdenCompraDet> listDetallesByProducto(String app, int productoId) throws Exception {
		String[] req = { "orden_compra", "orden_compra.impuesto" };
		String filter = "where producto = " + productoId + " and b.activo is true order by b.fecha asc";
		return CRUD.list(app,OrdenCompraDet.class, req, filter);
	}

	@Override
	public List<OrdenEntradaSalida> listOrdenesEntradaSalida(String app, Date fechaInicio, Date fechaFin, boolean esEntrada,
			int sucursalId) throws Exception {
		char tipo = esEntrada ? Util.TIPO_ORDEN_ENTRADA : Util.TIPO_ORDEN_SALIDA;
		String[] req = { "orden_venta", "orden_compra", "traslado" };
		String filter = "where tipo = '" + tipo + "' and a.sucursal = " + sucursalId + " and a.fecha >='" + fechaInicio
				+ "' and a.fecha <='" + fechaFin + "'" + " and a.activo is true order by a.numero desc";
		return CRUD.list(app,OrdenEntradaSalida.class, req, filter);
	}

	@Override
	public List<OrdenEntradaSalidaDet> listOrdenesEntradaSalidaDets(String app, boolean entrada, int ordenEntradaSalidaId)
			throws Exception {
		char tipo = entrada ? Util.TIPO_ORDEN_ENTRADA : Util.TIPO_ORDEN_SALIDA;
		String[] req = { "ordenEntradaSalida", "articulo", "articulo.producto" };
		String filter = "where b.id = " + ordenEntradaSalidaId + " and tipo = '" + tipo + "'";
		return CRUD.list(app,OrdenEntradaSalidaDet.class, req, filter);
	}

	private OrdenEntradaSalida getNewOrdenEntrada(String app, OrdenCompra ordenCompra) throws Exception {

		String filter = "where tipo = '" + Util.TIPO_ORDEN_ENTRADA + "'" + " and sucursal = "
				+ ordenCompra.sucursal.id + " order by a.numero desc limit 1";
		List<OrdenEntradaSalida> list = CRUD.list(app,OrdenEntradaSalida.class, filter);
		int numero = list.isEmpty() ? 1 : (list.get(0).numero + 1);
		OrdenEntradaSalida orden = new OrdenEntradaSalida();
		orden.activo = true;
		orden.creador = ordenCompra.creador;
		orden.fecha = new Date();
		orden.numero = numero;
		orden.ordenCompra = ordenCompra;
		orden.sucursal = ordenCompra.sucursal;
		orden.tipo = Util.TIPO_ORDEN_ENTRADA;
		return orden;
	}

	@Override
	public void saveOrdenEntradaSalidaDets(String app, List<OrdenEntradaSalidaDet> detalles, int cantidadLote) throws Exception {
		try {
			if (cantidadLote > -1) {
				if (!detalles.isEmpty()) {
					Producto prod = detalles.get(0).articulo.producto;
					prod.lote = prod.lote + cantidadLote;
					CRUD.update(app,prod);
				}
			}
			for (OrdenEntradaSalidaDet odet : detalles) {
				odet.id = null;
				CRUD.save(app,odet.articulo);
				CRUD.save(app,odet);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Problemas al grabar");
		}

	}

	@Override
	public void importOrdenCompraInicial(String app) throws Exception {
		readFile(app);
	}

	private void readFile(String app) throws Exception {
		try {
			String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("D:/empresas/emel/ordenes_compra_inicial.txt");
			Scanner scan = new Scanner(file, iso);
			scan.useDelimiter("\n");
			String text = "";
			List<ImportacionInicial> list = new ArrayList<>();
			ProductoServiceImpl productoService = new ProductoServiceImpl();

			while (scan.hasNext()) {
				String line = scan.next();
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				// CODIGO NOMBRE DEL PRODUCTO PRINCIPIO M D CONTENIDO ACCION FARMACOLOGICA
				// LABORATORIO CODIGO DE BARRAS
				// 1 AB-BRONCOL 1200 NF/ INY IM. AMPICILINA 1200 VIAL 1 ANTIBIOTICO MEDIFARMA
				// 7759307004374

				String codigo = scanLine.next().trim();
				String strFecha = scanLine.next().trim();
				String lote = scanLine.next().trim();
				String strCantidad = scanLine.next().trim();
				String strCantidadContenido = scanLine.next().trim();

				// fin lectura de datos

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fecha = sdf.parse(strFecha);
				BigDecimal cantidad = new BigDecimal(strCantidad);
				BigDecimal cantidadContenido = new BigDecimal(strCantidadContenido);
				ImportacionInicial ii = new ImportacionInicial();
				ii.creador = "root";
				ii.producto = productoService.getByCodigo(app, codigo);
				ii.cantidad = cantidad;
				ii.fecha_vencimiento = fecha;
				ii.lote = lote;
				ii.verificado = false;

				CRUD.save(app,ii);
				scanLine.close();
			}
			System.out.println(text);
			scan.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}
	}

	@Override
	public List<ImportacionInicial> listImportacionesIniciales(String app, String codigo, String codigoBarras, String lote,
			Date fechaVencimiento) throws Exception {
		String[] require = { "producto", "producto.marca", "producto.linea" };
		String filter = "where a.activo is true";
		if (codigo != null) {
			filter += " and b.codigo ilike '" + codigo + "'";
		}
		if (codigoBarras != null) {
			filter += " and ( (b.codigo_barras1 ilike '" + codigoBarras + "') " + " or (b.codigo_barras2 ilike 	'"
					+ codigoBarras + "') " + " or (b.codigo_barras3 ilike 	'" + codigoBarras + "') ) ";
		}
		if (lote != null) {
			filter += " and a.lote ilike '" + lote + "'";
		}
		if (fechaVencimiento != null) {
			filter += " and a.fecha_vencimiento ='" + fechaVencimiento.toString() + "'";
		}
		List<ImportacionInicial> list = CRUD.list(app,ImportacionInicial.class, require, filter);
		return list;

	}

	@Override
	public ImportacionInicial saveOrUpdateImportacionInicial(String app, boolean save, ImportacionInicial entity) throws Exception {
		if (save) {
			CRUD.save(app,entity);
		} else {
			CRUD.update(app,entity);
		}
		return entity;
	}

	@Override
	public void deleteImportacionInicial(String app, ImportacionInicial entity) throws Exception {
		CRUD.delete(app, entity);
	}

	@Override
	public void convertImportacionesInicialesToOrdenescompra(String app) throws Exception {
		try {
			List<ImportacionInicial> listImportacionesIniciales = CRUD.list(app,ImportacionInicial.class,
					"order by lote,producto asc");
			OrdenCompra oc = new OrdenCompra();
			oc.activo = true;
			oc.almacen_entrega = new Almacen();
			oc.almacen_entrega.id = 1;
			oc.creador = "root";
			oc.dias_credito = 0;
			oc.direccion_proveedor = new Direccion();
			oc.direccion_proveedor.id = 0;
			oc.documento_pago = "R001-00000001";
			oc.fecha = new Date();
			oc.fecha_entrega = oc.fecha;
			oc.forma_pago = new FormaPago();
			oc.forma_pago.id = Util.FP_CREDITO;
			oc.impuesto.id = 1;
			oc.impuesto_incluido = true;
			oc.moneda = new Moneda();
			oc.moneda.id = 1;
			oc.numero = 0;
			oc.observaciones = "REGULARIZACION INVENTARIO INICIAL";
			oc.sucursal = new Sucursal();
			oc.sucursal.id = 0;
			System.out.println("before tipocambio: " + BigDecimal.ONE);
			oc.tipo_cambio = BigDecimal.ONE;
			System.out.println("after tipocambio: " + oc.tipo_cambio);
			oc.total = BigDecimal.ZERO;
			oc.total_cobrado = BigDecimal.ZERO;
			CRUD.save(app,oc);
			//
			for (ImportacionInicial ii : listImportacionesIniciales) {
				OrdenCompraDet ocd = new OrdenCompraDet();
				ocd.activo = true;
				ocd.almacen = oc.almacen_entrega;
				ocd.cantidad = ii.cantidad;
				ocd.cantidad_tg = BigDecimal.ZERO;
				ocd.creador = "root";
				ocd.descuento = BigDecimal.ZERO;
				ocd.fecha_vencimiento = ii.fecha_vencimiento;
				ocd.lote = ii.lote;
				ocd.orden_compra = oc;
				ocd.precio_unitario = new BigDecimal("0.01");
				ocd.producto = ii.producto;
				ocd.total = ocd.precio_unitario.multiply(ocd.cantidad);
				CRUD.save(app,ocd);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getCause());
		}

	}

	@Override
	public OrdenCompra getOrdenCompra(String app, int ordenCompraId) throws Exception {
		String[] req = { "almacen_entrega", "sucursal", "direccion_proveedor", "direccion_proveedor.persona",
				"forma_pago", "moneda","impuesto" };
		String filter = "where a.id = " + ordenCompraId ;
		List<OrdenCompra> list = CRUD.list(app,OrdenCompra.class, req, filter);
		return list.isEmpty()?null:list.get(0);
	}


@Override
	public void annulOrdenCompra(String app, int ordenCompraId) throws Exception {
		try {
			OrdenCompra oc = getOrdenCompra(app, ordenCompraId);
			if(oc.activo== false) {
				throw new Exception("La orden de compra ya figura anulada");
			}
			oc.activo = false;
			CRUD.update(app,oc);
			List<OrdenCompraDet> list = listDetalles(app, oc.id);
			for (OrdenCompraDet ocd : list) {
				ocd.orden_compra = oc;
				saveKardexFromAnnulOrdenCompraDet(app, ocd);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		}

	}
	
	private void saveKardexFromAnnulOrdenCompraDet(String app, OrdenCompraDet ocd) throws Exception {
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app, ocd.producto.id, ocd.almacen.id);
		Kardex nk= new Kardex();
		nk.activo = true;
		nk.almacen = ocd.almacen;
		nk.creador = ocd.creador;
		nk.destino = "ANULACION DE COMPRA";
		nk.origen = ocd.orden_compra.direccion_proveedor.persona.toString();
		nk.fecha = new Date();
		nk.fecha_orden = ocd.orden_compra.fecha;
		nk.documento = ocd.orden_compra.documento_pago;
		nk.ingreso 	= BigDecimal.ZERO;
		nk.salida 	= ocd.cantidad.multiply(ocd.producto.contenido);
		nk.movimiento = Util.MOVIMIENTO_KARDEX_SALIDA;
		nk.orden_id = ocd.orden_compra.id;
		nk.precio_costo = ocd.precio_unitario;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = ocd.producto;
		nk.stock_anterior = ok==null?BigDecimal.ZERO:ok.stock;
		nk.stock = nk.stock_anterior.subtract(nk.salida);
		nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
		nk.usado = BigDecimal.ZERO;
		CRUD.save(app,nk);
	}
	
	@Override
	public void asignarDocumentoPago(String app, String documentoPago, int ordenCompraId) throws Exception {
		try {
			OrdenCompra oc = getOrdenCompra(app, ordenCompraId);
			oc.documento_pago = documentoPago;
			CRUD.update(app,oc);
			List<OrdenCompraDet> list = listDetalles(app, oc.id);
			
			for(OrdenCompraDet item : list) {
				item.orden_compra = oc;
				saveKardexFromOrdenCompraDet(app, item);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		
		
		
	}

}
