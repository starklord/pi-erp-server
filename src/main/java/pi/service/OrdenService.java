package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;

public interface OrdenService {

    public Orden getLastOrden(String app, int sucursalId, char tipo);
    public List<Orden> list(String app, Date inicio, Date fin,int sucursalId);
    public List<OrdenDet> listDetalles(String app,int ordenId);
    public List<OrdenArt> listArticulos(String app,int ordenId);
    public Orden saveOrden(String app,Orden orden, List<OrdenDet> detalles) throws Exception;
    public Orden updateOrden(String app,Orden orden, List<OrdenDet> detalles) throws Exception;
    public void aprobarOrden(String app,Orden orden, int personaId) throws Exception;
    public void atenderOrden(String app,Orden orden, int personaId, List<OrdenArt> ordenArticulos) throws Exception;
    
}
