package pi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import pi.service.model.logistica.ImportacionInicial;
import pi.service.model.logistica.OrdenCompra;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.model.logistica.OrdenEntradaSalida;
import pi.service.model.logistica.OrdenEntradaSalidaDet;
import pi.service.model.venta.OrdenVentaDet;


public interface OrdenCompraService{
	
	public List<OrdenCompra> list(String app, int sucursalId) throws Exception;
	public List<OrdenCompra> list(String app, int sucursalId, Date inicio, Date fin, int proveedorId) throws Exception;
	public List<OrdenCompra> getLastOrdenCompra(String app) throws Exception;
	public List<OrdenEntradaSalida> getLastOrdenEntrada(String app, int sucursal) throws Exception;
	public List<OrdenCompraDet> listDetalles(String app, int ordenCompraId) throws Exception;
	public List<OrdenCompraDet> listDetallesByProducto(String app, int productoId) throws Exception;
	public OrdenCompra save(String app, OrdenCompra orden, List<OrdenCompraDet> detalles) throws Exception;
	public void update(String app, OrdenCompra orden, List<OrdenCompraDet> detalles) throws Exception;
	public void importOrdenCompraInicial(String app) throws Exception;
	public OrdenCompra getOrdenCompra(String app, int ordenCompraId) throws Exception;
	public void annulOrdenCompra(String app, int ordenCompraId) throws Exception;
	
	//para los movimientos de entrada salida
	public List<OrdenEntradaSalida> listOrdenesEntradaSalida(String app, Date fechaInicio, Date fechaFin,boolean esEntrada, int sucursalId) throws Exception;
	public List<OrdenEntradaSalidaDet> listOrdenesEntradaSalidaDets(String app, boolean entrada, int ordenEntradaSalidaId) throws Exception;
	public void saveOrdenEntradaSalidaDets(String app, List<OrdenEntradaSalidaDet> detalles, int porLote) throws Exception;
	
	
	
	//para los costos promedios
	public BigDecimal getCostoPromedioByProductoId(String app, int productoId, Date fechaUltima) throws Exception;
	public void updateCostoPromedioToVentas(String app, List<OrdenVentaDet> list) throws Exception;
	
	
	//para las importaciones iniciales
	public List<ImportacionInicial> listImportacionesIniciales(String app, String codigo, String codigoBarras, String lote, Date fechaVencimiento) throws Exception;
	public ImportacionInicial saveOrUpdateImportacionInicial(String app, boolean save, ImportacionInicial entity) throws Exception;
	public void deleteImportacionInicial(String app, ImportacionInicial entity) throws Exception;
	public void convertImportacionesInicialesToOrdenescompra(String app) throws Exception;
	
	public void asignarDocumentoPago(String app, String documentoPago, int ordenCompraId) throws Exception;
	public List<OrdenCompraDet> listDetalles(String app, int sucursalId, Date inicio, Date fin) throws Exception;
	
}
