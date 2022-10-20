package pi.service;

import java.math.BigDecimal;
import java.util.List;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.StockProducto;
import pi.service.model.auxiliar.KardexProducto;
import pi.service.model.auxiliar.ProductoModel;
import pi.service.model.logistica.OrdenCompraDet;

public interface StockProductoService{
	
	public List<ProductoModel> listProductosParaReponer(String app) throws Exception;
	public ProductoModel getModelByCode(String app, List<Almacen> almacenes, String txt);
	public List<ProductoModel> listModels(String app, List<Almacen> almacenes, int marcaId, int lineaId, boolean ver_anulados, String txt);
	public List<Producto> listProductos(String app, int sucursalId, int marcaId, int lineaId, int buscarPor, String txt) throws Exception;
	public List<OrdenCompraDet> listByOrdenesCompra(String app, int sucursalId, int marcaId, int lineaId, int buscarPor, String txt) throws Exception;
	public List<OrdenCompraDet> listByCodeOrBarcode(String app, String txt) throws Exception;
	public Producto getByCode(String app, int sucursalId, String txt) throws Exception;
	public List<KardexProducto> ListKardexPorProducto(String app, int productoId) throws Exception;
	public List<KardexProducto> listKardexGeneralProductos(String app, int empresaId)  throws Exception;
	public Producto getByCodeOrName(String app, int sucursalId, String codigo, String nombre) throws Exception;
	public void setStock(String app, int stockProductoId, BigDecimal newStock) throws Exception;
	public List<StockProducto> listStocks(String app, int sucursalId, int marcaId, int lineaId, int buscarPor, String txt) throws Exception;
	public void updateStock(String app, int productoId, int almacenId, BigDecimal addStock) throws Exception;
}
