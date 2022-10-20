package pi.service;

import java.util.Date;
import java.util.List;
import pi.service.model.almacen.OrdenRegularizacion;
import pi.service.model.almacen.OrdenRegularizacionDet;
public interface OrdenMovimientoService{
	
	public OrdenRegularizacion getOrdenRegularizacion(String app, int ordenId) throws Exception;
	public OrdenRegularizacion saveOrdenRegularizacion(String app, boolean save, OrdenRegularizacion orden, List<OrdenRegularizacionDet> detalles) throws Exception;
	public List<OrdenRegularizacion> listOrdenesRegularizacion(String app, char movimiento, Date inicio, Date fin) throws Exception;
	public List<OrdenRegularizacionDet> listDetallesOrdenRegularizacion(String app, int ordenId) throws Exception;
	public OrdenRegularizacion saveOrdenRegularizacion(String app, OrdenRegularizacion orden, List<OrdenRegularizacionDet> detalles) throws Exception;

}
