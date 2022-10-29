package pi.service;

import java.util.List;

import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;

public interface OrdenService {

    public List<Orden> list(String app,int sucursalId);
    public List<OrdenDet> listDetalles(String app,int ordenId);
    public List<OrdenArt> listArticulos(String app,int ordenId);
    public Orden saveOrden(String app,Orden orden, List<OrdenDet> detalles);
    public Orden updateOrden(String app,Orden orden, List<OrdenDet> detalles);
    public void aprobarOrden(String app,Orden orden, int personaId);
    public void atenderOrden(String app,Orden orden, int personaId, List<OrdenArt> ordenArticulos);
    
}
