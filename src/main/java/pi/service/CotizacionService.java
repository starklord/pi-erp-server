package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.logistica.Cotizacion;
import pi.service.model.logistica.CotizacionDet;

public interface CotizacionService {

    public Cotizacion get(String app, int cotizacionId);
    public Cotizacion getLast(String app, int sucursalId);
    public List<Cotizacion> list(String app, Date inicio, Date fin,int sucursalId);
    public List<CotizacionDet> listDets(String app,int cotizacionId);
    public Cotizacion save(String app,Cotizacion cotizacion, List<CotizacionDet> detalles) throws Exception;
    public Cotizacion update(String app,Cotizacion cotizacion, List<CotizacionDet> detalles) throws Exception;
    
}
