package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.almacen.Articulo;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;

public interface OrdenService {

    public Orden getOrden(String app, int ordenId);
    public Orden getLastOrden(String app, int sucursalId, char tipo);
    public List<Orden> list(String app, Date inicio, Date fin,int sucursalId, char tipo);
    public List<OrdenDet> listDets(String app,int ordenId);
    public List<OrdenArt> listOrdenArts(String app,int ordenId);
    public List<OrdenArt> listOrdenArtsByArticulo(String app, int articuloId);
    public List<OrdenArt> listOrdenArtsByAlmacenProducto(String app,int almacenId, int productoId);
    public Orden saveOrden(String app,Orden orden, List<OrdenDet> detalles) throws Exception;
    public Orden updateOrden(String app,Orden orden, List<OrdenDet> detalles) throws Exception;
    public void aprobarOrden(String app, int ordenId, int personaId) throws Exception;
    public void atenderOrden(String app, int ordenId, int personaId, String usuario) throws Exception;
    public void anularOrden(String app, int ordenId) throws Exception;
    public String getClienteStringByCoincidence(String app, String txt);

    public Articulo getLastArticulo(String app, int almacenId);
    public Articulo getLastArticulo(String app, int almacenId, int productoId);
    public OrdenArt getLastOrdenArt(String app, int almacenId, int productoId);
    public List<Articulo> listArticulos(String app, int almacenId);
    public List<Articulo> listArticulos(String app, int almacenId, int marcaId, int lineaId,
            String ver, String txt);
    
}
