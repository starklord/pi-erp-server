
package pi.server.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import pi.service.util.Util;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Articulo;
import pi.service.model.almacen.Kardex;
import pi.service.model.almacen.Linea;
import pi.service.model.almacen.Marca;
import pi.service.model.almacen.OrdenRegularizacion;
import pi.service.model.almacen.OrdenRegularizacionDet;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;
import pi.service.model.auxiliar.MABCProducto;
import pi.service.model.empresa.Empresa;
import pi.service.ProductoService;
import pi.service.util.db.Query;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/ProductoService")
public class ProductoServiceImpl extends HessianServlet implements ProductoService {

	@Override
	public Producto getByCodigo(String app, String codigo) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String where = "where a.codigo = '" + codigo+ "' limit 1";
		List<Producto> list = CRUD.list(app,Producto.class, req, where);
		return list.isEmpty()?null:list.get(0);
	}
	
	private Producto getByNombre(String app, String nombre) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String where = "where a.nombre = '" + nombre+ "' limit 1";
		List<Producto> list = CRUD.list(app,Producto.class, req, where);
		return list.isEmpty()?null:list.get(0);
	}
	
	@Override
	public List<Producto> list(String app) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		List<Producto> list = CRUD.list(app,Producto.class, req, "order by a.nombre asc");
		return list;
	}
	
	@Override
	public void annul(String app, int productoId) throws Exception {
		CRUD.execute(app, "update logistica.producto set activo = false where id = " + productoId);
	}
	
	@Override
	public void delete(String app, Producto object) throws Exception {
		try{
			
			CRUD.execute(app, "delete from logistica.stock_producto where producto = " + object.id);
			CRUD.delete(app, object);
			
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
	}

	@Override
	public Producto saveOrUpdate(String app, boolean save, Producto object) throws Exception {
		try {
			
			int empresaId = object.empresa.id;
			if (save) {
				if(object.codigo.trim().isEmpty()) {
					System.out.println("entrando a crear un codigo");
					String filter = "where empresa = " + empresaId + " order by codigo_interno desc limit 1";
					List<Producto> list = CRUD.list(app,Producto.class, filter);
					if (!list.isEmpty()) {
						object.codigo_interno = list.get(0).codigo_interno+1;
					}else {
						object.codigo_interno = 1;
					}
					object.codigo = object.codigo_interno+"";
				}
				
				CRUD.save(app,object);
			}else{
				CRUD.update(app,object);
			}
			
			return object;
		} catch (Exception ex) {
			ex.printStackTrace();
			
			throw new Exception(ex.getMessage());
			
		}

	}

	@Override
	public List<Producto> list(String app, int empresaId) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String filter = "where a.empresa=" + empresaId + " order by a.nombre asc";
		return CRUD.list(app,Producto.class, req, filter);
	}

	@Override
	public List<Producto> listActives(String app, int empresaId) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String filter = "where a.empresa=" + empresaId + " and a.activo is true order by a.nombre asc";
		return CRUD.list(app,Producto.class, req, filter);
	}

	@Override
	public List<Producto> listActives(String app, int empresaId, int marcaId, int lineaId, int buscarPor, String txt)
			throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String filterMarca = marcaId == -1 ? "" : " and a.marca = " + marcaId;
		String filterLinea = lineaId == -1 ? "" : " and a.linea = " + lineaId;
		String filterBuscarPor = " and a.codigo ilike '%" + txt + "%'";
		if (buscarPor == 1) {
			filterBuscarPor = txt.isEmpty() ? " " : (" and a.codigo_interno = " + txt);
		}
		if (buscarPor == 2) {
			filterBuscarPor = " and a.nombre ilike '%" + txt + "%'";
		}
		if (buscarPor == 3) {
			filterBuscarPor = " and a.descripcion ilike '%" + txt + "%'";
		}
		String filter = "where a.empresa=" + empresaId + filterMarca + filterLinea + filterBuscarPor
				+ " and a.activo is true order by a.nombre asc";
		return CRUD.list(app,Producto.class, req, filter);
	}

	@Override
	public void importProductsFromTxt(String app) throws Exception {

		readFileProductos(app);
				// readFileKardexCorteInicial();
//		String[] require = {
//			"traslado_cab","producto"
//		};
//		try {
//			List<TrasladoInternoDet> list = CRUD.list(app,TrasladoInternoDet.class, require, "where b.activo is true order by a.id asc");
//			for(TrasladoInternoDet item : list) {
//				CRUD.execute(app, "update almacen.kardex set fecha_vencimiento = '" + item.fecha_vencimiento.toString()+"'"
//						+ ", lote ='" + item.lote+"' where tipo = 'T' and orden_id = " + item.traslado_cab.id+" and producto = " + item.producto.id);
//			}
//		}catch(Exception ex) {
//			
//		}
	}
	
	private void readFileRegularizacionesPendientes(String app) throws Exception {
		try {
			
			String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("D:/empresas/emmel/regularizaciones.txt");
			Scanner scan = new Scanner(file, iso);
			scan.useDelimiter("\n");
			String text = "";
			StockProductoServiceImpl stockService = new StockProductoServiceImpl();
			OrdenRegularizacion orden = new OrdenRegularizacion();
			orden.activo = true;
			orden.almacen = new Almacen();
			orden.almacen.id = 1;
			orden.creador =  "root";
			orden.fecha = new Date();
			orden.movimiento = 'E';
			orden.observaciones = "Regularizaciones material medico";
			List<OrdenRegularizacionDet> detalles = new ArrayList<>();
			while (scan.hasNext()) {
				String line = scan.next();
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				//lectura de datos				
				String strCodigo				= scanLine.next().trim();
				String strFechaVencimiento 	= scanLine.next().trim();
				String strLote 				= scanLine.next().trim();
				String strCantidad			= scanLine.next().trim();
				String strCantidadFraccion  = scanLine.next().trim();
				System.out.println("codigo: "			+ strCodigo);
				System.out.println("Fecha V: " 			+ strFechaVencimiento);
				System.out.println("Lote: " 			+ strLote);
				System.out.println("Cantidad: " 		+ strCantidad);
				System.out.println("Cantidad Fraccion: "+ strCantidadFraccion);
				
				String codigo 					= strCodigo;
			    Date fechaVencimiento 			= new SimpleDateFormat("dd-MM-yyyy").parse(strFechaVencimiento);
			    String lote 					= strLote;
				BigDecimal cantidad				= new BigDecimal(strCantidad);
				BigDecimal cantidadFraccion		= new BigDecimal(strCantidadFraccion);
				//fin lectura de datos
				Producto producto = getByCodigo(app,codigo);
				if(producto == null) {
					System.out.println("Producto no encontrado: " + codigo);
					continue;
				}
				OrdenRegularizacionDet det = new OrdenRegularizacionDet();
				det.activo = true;
				det.cantidad = cantidad;
				det.cantidad_fraccion = cantidadFraccion;
				det.creador="root";
				det.fecha_vencimiento = fechaVencimiento;
				det.lote = lote;
				det.producto = producto;
				detalles.add(det);
				
			}
			
			OrdenMovimientoServiceImpl ordenService = new OrdenMovimientoServiceImpl();
			
			ordenService.saveOrdenRegularizacion(app, true, orden, detalles);
			System.out.println(text);
			scan.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
			
			throw new Exception(ex.getMessage());

		}
	}

	private void readFileKardexCorteInicial(String app) throws Exception {
		try {
			
			String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("/Users/starklord/empresas/univet/corte_inventario_consunivet202206231325.txt");
			Scanner scan = new Scanner(file, iso);
			scan.useDelimiter("\n");
			String text = "";
			StockProductoServiceImpl stockService = new StockProductoServiceImpl();
			while (scan.hasNext()) {
				String line = scan.next();
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				//lectura de datos				
				String codigo= scanLine.next().trim();
				String strf = scanLine.next().trim();
				String strLote = "-";
				String strFecha = "2022-06-23";
				System.out.println("s1: " + strf);
				
				//fin lectura de datos
				BigDecimal fisico				= new BigDecimal(strf);
				Producto producto = getByCodigo(app, codigo);
				if(producto == null) {
					continue;
				}
				Almacen alm1 = new Almacen();
				alm1.id = 1;//tienda

				saveKardexFromCorteInicial(app, alm1,producto, fisico, strFecha,strLote);
			}
			System.out.println(text);
			scan.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
			
			throw new Exception(ex.getMessage());

		}
	}
	
	private void saveKardexFromCorteInicial(String app, Almacen almacen, Producto producto, BigDecimal fisico,String strFecha, String strLote) throws Exception {
		ProductoServiceImpl productoService = new ProductoServiceImpl();
		Kardex ok = productoService.getLastKardexFromProducto(app,producto.id, almacen.id);
		Kardex nk= new Kardex();
		nk.activo = true;
		nk.almacen = almacen;
		nk.creador = "root";
		nk.destino = "-";
		nk.origen = "Regularizacion";
		nk.fecha = new Date();
		nk.fecha_orden = nk.fecha;
		nk.lote = strLote;

		String[] strDate = strFecha.split("-");
		System.out.println("array size: " + strDate.length);
		int day = Integer.parseInt(strDate[2].trim());
		int month = Integer.parseInt(strDate[1].trim())-1;
		int year = Integer.parseInt(strDate[0].trim())-1900;
		Date date = new Date();
		date.setYear(year);
		date.setDate(day);
		date.setMonth(month);
		nk.fecha_vencimiento = date;
		BigDecimal sistema = BigDecimal.ZERO;
		if(ok!=null){
			sistema = ok.stock;
		}
		BigDecimal dif = sistema.subtract(fisico);
		nk.documento = "REG-20220623";
		nk.ingreso = dif.compareTo(BigDecimal.ZERO)<=0?BigDecimal.ZERO:dif;
		nk.salida = dif.compareTo(BigDecimal.ZERO)<=0?new BigDecimal(Math.abs(dif.doubleValue())):BigDecimal.ZERO;
		
		nk.orden_id = 0;
		nk.precio_costo = BigDecimal.ZERO;
		nk.precio_venta = BigDecimal.ZERO;
		nk.producto = producto;
		nk.stock_anterior = ok==null?BigDecimal.ZERO:ok.stock;
		nk.stock = nk.stock_anterior.add(nk.ingreso).subtract(nk.salida);
		nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
		nk.usado = BigDecimal.ZERO;
		nk.movimiento = nk.salida.compareTo(BigDecimal.ZERO)==0?Util.MOVIMIENTO_KARDEX_ENTRADA:Util.MOVIMIENTO_KARDEX_SALIDA;
		CRUD.save(app,nk);
	}
	
	private void readFileUpdateCostos(String app) throws Exception {
		try {
			
			String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("D:/empresas/emel/productos_costos.txt");
			Scanner scan = new Scanner(file, iso);
			scan.useDelimiter("\n");
			String text = "";
			int codigo = 1;
			ProductoServiceImpl productoService = new ProductoServiceImpl();
			while (scan.hasNext()) {
				String line = scan.next();
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				//lectura de datos Nombre	Presentacion	UNIDAD	LABORATORIO	PrecioCosto	Stockactual
				
				String str_id 		= scanLine.next().trim();
				String str_codigo	= scanLine.next().trim();
				BigDecimal costo 	= new BigDecimal(scanLine.next().trim());
				////////////////////////// fin de lectura de datos
				Producto prod = productoService.getProductoByCodigo(app,str_codigo);
				if(prod==null) {
					continue;
				}else {
					System.out.println("costo_ultima_compra: " + prod.costo_ultima_compra);
					if(prod.costo_ultima_compra.compareTo(new BigDecimal("42.2912"))==0) {
						
						CRUD.execute(app, "update logistica.producto set costo_ultima_compra = " +costo+" where codigo = '" + str_codigo+"'" );
					}else {
						
					}
					
				}
			}
			System.out.println(text);
			scan.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
			
			throw new Exception(ex.getMessage());

		}
	}

	
	private void readFileProductos(String app) throws Exception {
		try {
			
			// String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("D:/empresas/juanmacedo/productos.txt");
			// File file = new File("/Users/starklord/empresas/univet/consunivet/productos_consunivet.txt");
			Scanner scan = new Scanner(file, utf);
			
			scan.useDelimiter("\n");
			String text = "";
			ProductoServiceImpl productoService = new ProductoServiceImpl();
			LineaServiceImpl lineaService = new LineaServiceImpl();
			MarcaServiceImpl marcaService = new MarcaServiceImpl();
			UnidadServiceImpl unidadService = new UnidadServiceImpl();
			List<Linea> lineas 		= lineaService.list(app, 0);
			List<Marca> marcas		= marcaService.list(app, 0);
			List<Unidad> unidades 	= unidadService.list(app);
			// int codigo = 100001;
			Update.beginTransaction(app);
			System.out.println("entrando 1");
			while (scan.hasNext()) {
				System.out.println("entrando 2");
				String line = scan.next();
				System.out.println("linea:" + line);
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				//lectura de datos
				
				
				//
				//String 		str_contenido		= scanLine.next().trim();
				
				String 		str_marca 			= scanLine.next().trim().toUpperCase();
				String 		str_linea 			= scanLine.next().trim().toUpperCase();
				String 		str_codigo 			= scanLine.next().trim();
				String 		str_nombre			= scanLine.next().trim();
				String 		str_unidad 			= scanLine.next().trim();
				String 		str_precio1			= scanLine.next().trim();
				// String 		str_precio2			= scanLine.next().trim();
				String 		str_costo			= scanLine.next().trim().replace(" ", "");
				String 		str_stock			= scanLine.next().trim().replace(" ", "");
//				String 		lote 				= scanLine.next().trim();
//				String 		fecha_vencimiento 	= scanLine.next().trim();
				//fin lectura de datos 
				str_precio1 = str_precio1.substring(1, str_precio1.length());
				str_costo = str_costo.substring(1, str_costo.length());
				Producto prod =productoService.getByNombre(app, str_nombre);
				if(prod!=null) {
					continue;
				}
				
				Producto producto = new Producto();
				producto.activo = true;
				producto.codigo_barras1 = "-";
				producto.codigo_barras2 = "-";
				producto.codigo_barras3 = "-";
				producto.concentracion = "";
				producto.contenido = BigDecimal.ONE;
				producto.costo_ultima_compra = new BigDecimal(str_costo);
				producto.creador = "root";
				producto.descripcion = "";
				producto.empresa = new Empresa();
				producto.empresa.id = 0 ;
				producto.es_servicio = false;
				producto.garantia = false;
				producto.laboratorio = str_marca;
				producto.accion_farmacologica = "";
				producto.linea = getLinea(app, str_linea,0);
				producto.lote = 0;
				producto.marca = getMarca(app,str_marca,0);
				producto.moneda = new Moneda();
				producto.moneda.id = 1;
				producto.nombre = str_nombre;
				producto.peso = BigDecimal.ZERO;
				producto.precio = new BigDecimal(str_precio1);
				producto.precio_promocion = BigDecimal.ZERO;
				producto.precio_distribuidor = BigDecimal.ZERO;
				producto.unidad = getUnidad(app, str_unidad);
				producto.presentacion = producto.unidad.abreviatura;
				producto.stock_minimo = BigDecimal.ZERO;
				producto.codigo_interno = Integer.parseInt(str_codigo+"");
				producto.codigo = str_codigo+"";
				producto.presentacion = "-";
				producto.codigo_ubicacion="-";
				producto.impuesto = new Impuesto();
				producto.impuesto.id = Util.TIPO_IMPUESTO_IGV;
				producto.unidad_conversion = producto.unidad;
				producto.factor_conversion= BigDecimal.ONE;
				CRUD.save(app,producto);
				// codigo++;
				 Almacen alm1 = new Almacen();
				 alm1.id = 1;
				 saveKardexFromCorteInicial(app, alm1,producto, new BigDecimal(str_stock), "2022-07-01","00000");
			}
			System.out.println(text);
			scan.close();
			Update.commitTransaction(app);

		} catch (Exception ex) {
			ex.printStackTrace();
			Update.rollbackTransaction(app);
			throw new Exception(ex.getMessage());

		}
	}
	
	
	
	private Marca getMarcaByNombre(List<Marca> list, String nombre) {
		Marca obj = new Marca();
		obj.id = 82;
		for(Marca m : list) {
			if(m.nombre.equals(nombre)) {
				obj = m;
				break;
			}
		}
		return obj;
	}
	
	private Linea getLineaByNombre(List<Linea> list, String nombre) {
		Linea obj = new Linea();
		obj.id = 142;
		for(Linea l : list) {
			if(l.nombre.equals(nombre)) {
				obj = l;
				break;
			}
		}
		return obj;
	}
	
	private Linea getLineaById(List<Linea> list, int id) {
		
		for(Linea l : list) {
			if(l.id == id) {
				return l;
			}
		}
		return null;
	}
	
	private Marca getMarcaById(List<Marca> list, int id) {
		
		for(Marca l : list) {
			if(l.id == id) {
				return l;
			}
		}
		return null;
	}
	
	private Unidad getUnidadById(List<Unidad> list, int id) {
	
	for(Unidad l : list) {
		if(l.id == id) {
			return l;
		}
	}
	return null;
	}

	private Unidad getUnidadByAbreviatura(List<Unidad>unidades, String unidadStr) {
		Unidad uni = new Unidad();
		uni.id = Util.UNIDAD_UN_ID;
		for(Unidad unidad:unidades){
			if(unidad.abreviatura.equals(unidadStr)){
				uni = unidad;
				break;
			}
		}
		return uni;
	}
	
	private Unidad getUnidad(String app, String nombre) throws Exception {
		Unidad object = new Unidad();
		String filter = "where nombre ='" + nombre + "'";
		List<Unidad> list = CRUD.list(app,Unidad.class, filter);
		if (list.isEmpty()) {
			object.activo = true;
			object.creador = "root";
			object.nombre = nombre;
			object.abreviatura = nombre;
			CRUD.save(app,object);
		} else {
			object = list.get(0);
		}
		return object;
	}

	private Marca getMarca(String app, String nombre, int empresaId) throws Exception {
		Marca object = new Marca();
		String filter = "where empresa = " + empresaId + " and nombre ='" + nombre + "'";
		List<Marca> list = CRUD.list(app,Marca.class, filter);
		if (list.isEmpty()) {
			if(nombre.length()>3) {
				object.abreviatura = nombre.substring(0, 3);
			}else {
				object.abreviatura = nombre;
			}
			object.activo = true;
			object.creador = "root";
			object.empresa = new Empresa();
			object.empresa.id = empresaId;
			object.nombre = nombre;
			CRUD.save(app,object);
		} else {
			object = list.get(0);
		}
		return object;
	}

	private Linea getLinea(String app, String nombre, int empresaId) throws Exception {
		Linea object = new Linea();
		String filter = "where empresa = " + empresaId + " and nombre ='" + nombre + "'";
		List<Linea> list = CRUD.list(app,Linea.class, filter);
		if (list.isEmpty()) {
			object.activo = true;
			object.creador = "root";
			object.empresa = new Empresa();
			object.empresa.id = empresaId;
			object.nombre = nombre;
			object.abreviatura = nombre;
			CRUD.save(app,object);
		} else {
			object = list.get(0);
		}
		return object;
	}
	
	private Producto getProductoByCodigo(String app, String codigo) throws Exception {
		List<Producto> list = CRUD.list(app,Producto.class,"where codigo='" + codigo+"'");
		return list.isEmpty()?null:list.get(0);
	}
	
	private Producto getProductoByNombre(String app, String nombre) throws Exception {
		List<Producto> list = CRUD.list(app,Producto.class,"where nombre='" + nombre+"'");
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public Articulo getArticuloBySerie(String app, String serie, int productoId, int empresaId) throws Exception {
		String[] req = {"producto","almacen"};
		String filter = "where a.serie ='" + serie + "' and a.empresa = " + empresaId +
				" and a.producto = " + productoId +" limit 1";
		List<Articulo> list = CRUD.list(app,Articulo.class, req, filter);
		return list.isEmpty()?null:list.get(0);
	}
	
	@Override
	public Articulo getArticuloBySerieCoincidences(String app, String serie, int productoId, int empresaId) throws Exception {
		String[] req = {"producto","almacen"};
		String filter = "where a.serie ilike '%" + serie + "%' and a.empresa = " + empresaId +
				" and a.producto = " + productoId +" limit 1";
		List<Articulo> list = CRUD.list(app,Articulo.class, req, filter);
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public List<Articulo> listArticulosByLote(String app, int cantidad, int productoId, int almacenId) throws Exception {
		String[] req = {"producto","almacen"};
		String filter = "where a.almacen = " + almacenId + " and a.producto = "+productoId +"order by a.creado limit " + cantidad;
		List<Articulo> list = CRUD.list(app,Articulo.class, req, filter);
		return list;
	}
	
	@Override
	public Kardex getLastKardexFromProducto(String app, int productoId, int almacenId) throws Exception {
		String[] require = {
				"producto","almacen"
		};
		
		String filter = " where producto = " + productoId + " and almacen = " + almacenId 
						+ " order by a.id desc limit 1";
		
		List<Kardex> list = CRUD.list(app,Kardex.class, require, filter);
		return list.isEmpty()?null:list.get(0);
		
	}
	
	@Override
	public List<Kardex> listKardexFromProducto(String app, int productoId, int almacenId) throws Exception {
		String[] require = {
				"producto","almacen"
		};
		
		String filter = " where producto = " + productoId + " and almacen = " + almacenId 
				+ " order by a.id asc";
		
		List<Kardex> list = CRUD.list(app,Kardex.class, require, filter);
		return list;
		
	}
	
	@Override
	public List<MABCProducto> listABCProductos(String app, Date inicio, Date fin) throws Exception {
		try {

			List<MABCProducto> list = new ArrayList<>();
			Query query = new Query(app,null);
			String select = "select sum(a.cantidad*i.contenido + a.cantidad_fraccion),sum(a.total) as total,"
					+ "i.id, i.codigo, i.nombre,i.costo_ultima_compra, i.contenido "
					+ "from venta.orden_venta_det as a "
					+ "left join venta.orden_venta as b on b.id = a.orden_venta "
					+ "left join empresa.sucursal as c on c.id = b.sucursal "
					+ "left join logistica.producto as i on i.id = a.producto ";
			query.select.set(select);
			query.where = " where b.fecha between '"+inicio.toString() + "' and '"+fin.toString() + "' and b.activo is true"
					+ " and i.es_servicio is false";
			query.end = " group by i.id, i.codigo, i.nombre,i.costo_ultima_compra, i.contenido order by total desc";
			Object[][] rs = query.initResultSet();

			if (rs == null) {
				return list;
			}
			for (int i = 0; i < rs.length; i++) {
				MABCProducto abc = new MABCProducto();
				abc.cantidad	= (BigDecimal) 	rs[i][0];
				abc.total		= (BigDecimal) 	rs[i][1];
				abc.id			= (Integer)		rs[i][2];
				abc.codigo		= (String)		rs[i][3];
				abc.nombre		= (String)		rs[i][4];
				abc.costo_ultima_compra	= (BigDecimal) rs[i][5];
				abc.contenido	= (BigDecimal) rs[i][6];
				list.add(abc);
			}
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public List<Kardex> listKardexDisponibleVenta(String app, int productoId, int almacenId) throws Exception {
		String[] require = { "producto", "almacen" };

		String filter = " where producto = " + productoId + " and almacen = " + almacenId + ""
				+ " and movimiento = 'E' and tipo in('C','T','R') and origen not ilike 'anulacion%'"
				+ " and ingreso-usado >0" + " order by a.fecha_vencimiento asc";

		List<Kardex> list = CRUD.list(app,Kardex.class, require, filter);
		return list;
	}

	@Override
	public List<Kardex> listKardexUsados(String app, int productoId, int almacenId) throws Exception {
		String[] require = { "producto", "almacen" };

		String filter = " where producto = " + productoId + " and almacen = " + almacenId + ""
				+ " and movimiento = 'E' and tipo in('C','T','R') and origen not ilike 'anulacion%'" + " and usado >0"
				+ " order by a.fecha_vencimiento asc";

		List<Kardex> list = CRUD.list(app,Kardex.class, require, filter);
		return list;
	}

	@Override
	public List<Kardex> listKardexVencidos(String app, int mesesAVencer) throws Exception {
		Date now = new Date();
		now.setMonth(now.getMonth() + mesesAVencer);
		String[] require = { "producto", "almacen", "producto.marca", "producto.linea" };

		String filter = " where movimiento = 'E' and tipo in('C','T','R') and origen not ilike 'anulacion%'"
				+ " and ingreso-usado >0" + " and a.fecha_vencimiento < '" + now + "'"
				+ " order by a.fecha_vencimiento asc";

		List<Kardex> list = CRUD.list(app,Kardex.class, require, filter);
		return list;
	}

    @Override
    public List<Producto> listPagosMatricula(String app) throws Exception {
//        String[] require = {"producto","almacen"};
		
		String filter = "where nombre in ('MATRICULA', '1RA PENSION', '2DA PENSION', '3ERA PENSION', '4TA PENSION', '5TA PENSION')"
				+ " order by a.id asc";
		
		List<Producto> list = CRUD.list(app,Producto.class, filter);
        return list;
    }

}


































