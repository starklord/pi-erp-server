package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.venta.DocumentoPago;
import pi.service.model.venta.DocumentoPagoDet;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;
import pi.service.model.venta.OrdenVenta;

public interface DocumentoPagoService {
    public DocumentoPago getDocumentoPago(String app, int dpId) throws Exception;
    public DocumentoPago getDocumentoPagoByOv(String app, OrdenVenta ov) throws Exception;
    public DocumentoPago getLastDocumentoPago(String app, String serie);
    public List<DocumentoPago> list(String app, int sucursalId, Date inicio, Date fin) throws Exception;

    public List<DocumentoPago> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin,
            String serie, String numero, String identificador, String apellidos) throws Exception;

    public List<DocumentoPago> list(String app, int personaId) throws Exception;

    public List<DocumentoPagoDet> listDetalles(String app, int dpId) throws Exception;

    // to save
    public void save(String app, DocumentoPago cab, List<DocumentoPagoDet> dets) throws Exception;
    public void saveByOrdenVenta(String app, OrdenVenta ov) throws Exception;

    public void updateDP(String app, DocumentoPago dp) throws Exception;


    //para las notas de credito

    public NotaCredito getNotaCredito(String app, int ncId);
    public NotaCredito getLastNotaCredito(String app, String serie);
    
    public List<NotaCredito> listNotasCredito(String app, int sucursalId, Date inicio, Date fin,
    String serie, String numero, String identificador, String apellidos);

    public List<NotaCreditoDet> listNotasCreditoDets(String app, int ncId);

    public void saveNotaCredito(String app, NotaCredito nc, List<NotaCreditoDet> listDets) throws Exception;
    public void updateNotaCredito(String app, NotaCredito nc) throws Exception;

    public void anular(String app, DocumentoPago dp) throws Exception;
}
