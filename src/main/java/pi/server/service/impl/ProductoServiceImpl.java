
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
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;
import pi.service.model.auxiliar.MABCProducto;
import pi.service.model.empresa.Empresa;
import pi.server.db.Query;
import pi.server.db.Update;
import pi.service.ProductoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/ProductoService")
public class ProductoServiceImpl extends HessianServlet implements ProductoService {

	@Override
	public Producto getByCodigo(String app, String codigo) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String where = "where a.codigo = '" + codigo + "' limit 1";
		List<Producto> list = CRUD.list(app, Producto.class, req, where);
		return list.isEmpty() ? null : list.get(0);
	}

	private Producto getByNombre(String app, String nombre) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String where = "where a.nombre = '" + nombre + "' limit 1";
		List<Producto> list = CRUD.list(app, Producto.class, req, where);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<Producto> list(String app) {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		List<Producto> list = new ArrayList<>();
		try {
			list = CRUD.list(app, Producto.class, req, "order by a.nombre asc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void annul(String app, int productoId) throws Exception {
		CRUD.execute(app, "update logistica.producto set activo = false where id = " + productoId);
	}

	@Override
	public void delete(String app, Producto object) throws Exception {
		try {

			CRUD.execute(app, "delete from logistica.stock_producto where producto = " + object.id);
			CRUD.delete(app, object);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@Override
	public Producto saveOrUpdate(String app, boolean save, Producto object) throws Exception {
		try {

			if (save) {
				if (object.codigo==null||object.codigo.trim().isEmpty()) {
					System.out.println("entrando a crear un codigo"); 
					String filter = " order by codigo_interno desc limit 1";
					List<Producto> list = CRUD.list(app, Producto.class, filter);
					if (!list.isEmpty()) {
						object.codigo_interno = list.get(0).codigo_interno + 1;
					} else {
						object.codigo_interno = 1;
					}
					object.codigo = object.codigo_interno + "";
				}

				CRUD.save(app, object);
			} else {
				CRUD.update(app, object);
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
		return CRUD.list(app, Producto.class, req, filter);
	}

	@Override
	public List<Producto> listActives(String app, int empresaId) throws Exception {
		String[] req = { "marca", "linea", "unidad", "moneda" };
		String filter = "where a.empresa=" + empresaId + " and a.activo is true order by a.nombre asc";
		return CRUD.list(app, Producto.class, req, filter);
	}

	@Override
	public List<Producto> list(String app, int marcaId, int lineaId, String ver, String txt) {
		List<Producto> list = new ArrayList<>();
		String[] require = { "marca", "linea", "unidad", "unidad_conversion", "moneda" };
		String filterBuscarPor = " where ( a.codigo ilike '%" + txt + "%'";
			filterBuscarPor += " or  a.nombre ilike '%" + txt + "%'";
		filterBuscarPor += " or ( (a.codigo_barras1 ilike '" + txt + "') " + " or (a.codigo_barras2 ilike 	'" + txt
				+ "') " + " ) )";
		String filterMarca = marcaId == -1 ? "" : " and a.marca = " + marcaId;
		String filterLinea = lineaId == -1 ? "" : " and a.linea = " + lineaId;
		String filterVer = "";
		if(ver.equals(Util.OCULTAR_ANULADOS)){
			filterVer = " and a.activo is true ";
		}
		String filter = filterBuscarPor
				+ filterMarca
				+ filterLinea
				+ filterVer
				+ " order by a.nombre,a.codigo asc";
		try {
			list =  CRUD.list(app, Producto.class, require, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void importProductsFromTxt(String app) throws Exception {

		readFileProductos(app);
	}

	private void readFileProductos(String app) throws Exception {
		try {

			// String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("D:/empresas/juanmacedo/productos.txt");
			// File file = new
			// File("/Users/starklord/empresas/univet/consunivet/productos_consunivet.txt");
			Scanner scan = new Scanner(file, utf);

			scan.useDelimiter("\n");
			String text = "";
			ProductoServiceImpl productoService = new ProductoServiceImpl();
			LineaServiceImpl lineaService = new LineaServiceImpl();
			MarcaServiceImpl marcaService = new MarcaServiceImpl();
			UnidadServiceImpl unidadService = new UnidadServiceImpl();
			List<Linea> lineas = lineaService.list(app, 0);
			List<Marca> marcas = marcaService.list(app, 0);
			List<Unidad> unidades = unidadService.list(app);
			// int codigo = 100001;
			Update.beginTransaction(app);
			System.out.println("entrando 1");
			while (scan.hasNext()) {
				System.out.println("entrando 2");
				String line = scan.next();
				System.out.println("linea:" + line);
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				// lectura de datos

				//
				// String str_contenido = scanLine.next().trim();

				String str_marca = scanLine.next().trim().toUpperCase();
				String str_linea = scanLine.next().trim().toUpperCase();
				String str_codigo = scanLine.next().trim();
				String str_nombre = scanLine.next().trim();
				String str_unidad = scanLine.next().trim();
				String str_precio1 = scanLine.next().trim();
				// String str_precio2 = scanLine.next().trim();
				String str_costo = scanLine.next().trim().replace(" ", "");
				String str_stock = scanLine.next().trim().replace(" ", "");
				// String lote = scanLine.next().trim();
				// String fecha_vencimiento = scanLine.next().trim();
				// fin lectura de datos
				str_precio1 = str_precio1.substring(1, str_precio1.length());
				str_costo = str_costo.substring(1, str_costo.length());
				Producto prod = productoService.getByNombre(app, str_nombre);
				if (prod != null) {
					continue;
				}

				Producto producto = new Producto();
				producto.activo = true;
				producto.codigo_barras1 = "-";
				producto.codigo_barras2 = "-";
				producto.costo_ultima_compra = new BigDecimal(str_costo);
				producto.creador = "root";
				producto.descripcion = "";
				producto.garantia = false;
				producto.linea = getLinea(app, str_linea, 0);
				producto.marca = getMarca(app, str_marca, 0);
				producto.moneda = new Moneda();
				producto.moneda.id = 1;
				producto.nombre = str_nombre;
				producto.peso = BigDecimal.ZERO;
				producto.precio = new BigDecimal(str_precio1);
				producto.unidad = getUnidad(app, str_unidad);
				producto.stock_minimo = BigDecimal.ZERO;
				producto.codigo = str_codigo + "";
				producto.codigo_ubicacion = "-";
				producto.impuesto = new Impuesto();
				producto.impuesto.id = Util.TIPO_IMPUESTO_IGV;
				producto.unidad_conversion = producto.unidad;
				producto.factor_conversion = BigDecimal.ONE;
				CRUD.save(app, producto);
				// codigo++;
				Almacen alm1 = new Almacen();
				alm1.id = 1;
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
		for (Marca m : list) {
			if (m.nombre.equals(nombre)) {
				obj = m;
				break;
			}
		}
		return obj;
	}

	private Linea getLineaByNombre(List<Linea> list, String nombre) {
		Linea obj = new Linea();
		obj.id = 142;
		for (Linea l : list) {
			if (l.nombre.equals(nombre)) {
				obj = l;
				break;
			}
		}
		return obj;
	}

	private Linea getLineaById(List<Linea> list, int id) {

		for (Linea l : list) {
			if (l.id == id) {
				return l;
			}
		}
		return null;
	}

	private Marca getMarcaById(List<Marca> list, int id) {

		for (Marca l : list) {
			if (l.id == id) {
				return l;
			}
		}
		return null;
	}

	private Unidad getUnidadById(List<Unidad> list, int id) {

		for (Unidad l : list) {
			if (l.id == id) {
				return l;
			}
		}
		return null;
	}

	private Unidad getUnidadByAbreviatura(List<Unidad> unidades, String unidadStr) {
		Unidad uni = new Unidad();
		uni.id = Util.UNIDAD_UN_ID;
		for (Unidad unidad : unidades) {
			if (unidad.abreviatura.equals(unidadStr)) {
				uni = unidad;
				break;
			}
		}
		return uni;
	}

	private Unidad getUnidad(String app, String nombre) throws Exception {
		Unidad object = new Unidad();
		String filter = "where nombre ='" + nombre + "'";
		List<Unidad> list = CRUD.list(app, Unidad.class, filter);
		if (list.isEmpty()) {
			object.activo = true;
			object.creador = "root";
			object.nombre = nombre;
			object.abreviatura = nombre;
			CRUD.save(app, object);
		} else {
			object = list.get(0);
		}
		return object;
	}

	private Marca getMarca(String app, String nombre, int empresaId) throws Exception {
		Marca object = new Marca();
		String filter = "where empresa = " + empresaId + " and nombre ='" + nombre + "'";
		List<Marca> list = CRUD.list(app, Marca.class, filter);
		if (list.isEmpty()) {
			if (nombre.length() > 3) {
				object.abreviatura = nombre.substring(0, 3);
			} else {
				object.abreviatura = nombre;
			}
			object.activo = true;
			object.creador = "root";
			object.empresa = new Empresa();
			object.empresa.id = empresaId;
			object.nombre = nombre;
			CRUD.save(app, object);
		} else {
			object = list.get(0);
		}
		return object;
	}

	private Linea getLinea(String app, String nombre, int empresaId) throws Exception {
		Linea object = new Linea();
		String filter = "where empresa = " + empresaId + " and nombre ='" + nombre + "'";
		List<Linea> list = CRUD.list(app, Linea.class, filter);
		if (list.isEmpty()) {
			object.activo = true;
			object.creador = "root";
			object.empresa = new Empresa();
			object.empresa.id = empresaId;
			object.nombre = nombre;
			object.abreviatura = nombre;
			CRUD.save(app, object);
		} else {
			object = list.get(0);
		}
		return object;
	}

	private Producto getProductoByCodigo(String app, String codigo) throws Exception {
		List<Producto> list = CRUD.list(app, Producto.class, "where codigo='" + codigo + "'");
		return list.isEmpty() ? null : list.get(0);
	}

	private Producto getProductoByNombre(String app, String nombre) throws Exception {
		List<Producto> list = CRUD.list(app, Producto.class, "where nombre='" + nombre + "'");
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Articulo getArticuloBySerie(String app, String serie, int productoId, int empresaId) throws Exception {
		String[] req = { "producto", "almacen" };
		String filter = "where a.serie ='" + serie + "' and a.empresa = " + empresaId +
				" and a.producto = " + productoId + " limit 1";
		List<Articulo> list = CRUD.list(app, Articulo.class, req, filter);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Articulo getArticuloBySerieCoincidences(String app, String serie, int productoId, int empresaId)
			throws Exception {
		String[] req = { "producto", "almacen" };
		String filter = "where a.serie ilike '%" + serie + "%' and a.empresa = " + empresaId +
				" and a.producto = " + productoId + " limit 1";
		List<Articulo> list = CRUD.list(app, Articulo.class, req, filter);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<Articulo> listArticulosByLote(String app, int cantidad, int productoId, int almacenId)
			throws Exception {
		String[] req = { "producto", "almacen" };
		String filter = "where a.almacen = " + almacenId + " and a.producto = " + productoId
				+ "order by a.creado limit " + cantidad;
		List<Articulo> list = CRUD.list(app, Articulo.class, req, filter);
		return list;
	}

	@Override
	public Kardex getLastKardexFromProducto(String app, int productoId, int almacenId) throws Exception {
		String[] require = {
				"producto", "almacen"
		};

		String filter = " where producto = " + productoId + " and almacen = " + almacenId
				+ " order by a.id desc limit 1";

		List<Kardex> list = CRUD.list(app, Kardex.class, require, filter);
		return list.isEmpty() ? null : list.get(0);

	}

	@Override
	public List<Kardex> listKardexFromProducto(String app, int productoId, int almacenId) throws Exception {
		String[] require = {
				"producto", "almacen"
		};

		String filter = " where producto = " + productoId + " and almacen = " + almacenId
				+ " order by a.id asc";

		List<Kardex> list = CRUD.list(app, Kardex.class, require, filter);
		return list;

	}

	@Override
	public List<MABCProducto> listABCProductos(String app, Date inicio, Date fin) throws Exception {
		try {

			List<MABCProducto> list = new ArrayList<>();
			Query query = new Query(app, null);
			String select = "select sum(a.cantidad*i.contenido + a.cantidad_fraccion),sum(a.total) as total,"
					+ "i.id, i.codigo, i.nombre,i.costo_ultima_compra, i.contenido "
					+ "from venta.orden_venta_det as a "
					+ "left join venta.orden_venta as b on b.id = a.orden_venta "
					+ "left join empresa.sucursal as c on c.id = b.sucursal "
					+ "left join logistica.producto as i on i.id = a.producto ";
			query.select.set(select);
			query.where = " where b.fecha between '" + inicio.toString() + "' and '" + fin.toString()
					+ "' and b.activo is true"
					+ " and i.es_servicio is false";
			query.end = " group by i.id, i.codigo, i.nombre,i.costo_ultima_compra, i.contenido order by total desc";
			Object[][] rs = query.initResultSet();

			if (rs == null) {
				return list;
			}
			for (int i = 0; i < rs.length; i++) {
				MABCProducto abc = new MABCProducto();
				abc.cantidad = (BigDecimal) rs[i][0];
				abc.total = (BigDecimal) rs[i][1];
				abc.id = (Integer) rs[i][2];
				abc.codigo = (String) rs[i][3];
				abc.nombre = (String) rs[i][4];
				abc.costo_ultima_compra = (BigDecimal) rs[i][5];
				abc.contenido = (BigDecimal) rs[i][6];
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

		List<Kardex> list = CRUD.list(app, Kardex.class, require, filter);
		return list;
	}

	@Override
	public List<Kardex> listKardexUsados(String app, int productoId, int almacenId) throws Exception {
		String[] require = { "producto", "almacen" };

		String filter = " where producto = " + productoId + " and almacen = " + almacenId + ""
				+ " and movimiento = 'E' and tipo in('C','T','R') and origen not ilike 'anulacion%'" + " and usado >0"
				+ " order by a.fecha_vencimiento asc";

		List<Kardex> list = CRUD.list(app, Kardex.class, require, filter);
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

		List<Kardex> list = CRUD.list(app, Kardex.class, require, filter);
		return list;
	}

	@Override
	public List<Producto> listPagosMatricula(String app) throws Exception {
		// String[] require = {"producto","almacen"};

		String filter = "where nombre in ('MATRICULA', '1RA PENSION', '2DA PENSION', '3ERA PENSION', '4TA PENSION', '5TA PENSION')"
				+ " order by a.id asc";

		List<Producto> list = CRUD.list(app, Producto.class, filter);
		return list;
	}

}
