package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.efact.Comprobante;
import pi.service.model.efact.ComprobanteDet;
import pi.service.model.efact.ResumenDiario;
import pi.service.model.efact.ResumenDiarioDet;
import pi.service.model.logistica.Orden;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;

public interface ComprobanteService {
    public Comprobante getDocumentoPago(String app, int dpId) throws Exception;
    public Comprobante getDocumentoPagoByOv(String app, int ordenId) throws Exception;
    public Comprobante getLastDocumentoPago(String app, String serie);
    public List<Comprobante> list(String app, int sucursalId, Date inicio, Date fin) throws Exception;

    public List<Comprobante> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin,
            String serie, String numero, String identificador, String apellidos) throws Exception;

    public List<Comprobante> list(String app, int personaId) throws Exception;

    public List<ComprobanteDet> listDetalles(String app, int dpId);

    // to save
    public void save(String app, Comprobante cab, List<ComprobanteDet> dets) throws Exception;
    public void saveByOrden(String app, Orden ov) throws Exception;

    public void updateDP(String app, Comprobante dp) throws Exception;


    //para las notas de credito

    public NotaCredito getNotaCredito(String app, int ncId);
    public NotaCredito getLastNotaCredito(String app, String serie);
    
    public List<NotaCredito> listNotasCredito(String app, int sucursalId, Date inicio, Date fin,
    String serie, String numero, String identificador, String apellidos);

    public List<NotaCreditoDet> listNotasCreditoDets(String app, int ncId);

    public void saveNotaCredito(String app, NotaCredito nc, List<NotaCreditoDet> listDets) throws Exception;
    public void updateNotaCredito(String app, NotaCredito nc) throws Exception;
    // para los resumenes diarios
    public List<ResumenDiario> listResumenes(String app, Date inicio, Date fin);
    public List<ResumenDiarioDet> listDetallesResumen(String app, int rdId) throws Exception;
    public ResumenDiario getLastResumenDiarioByDate(String app, Date fecha);
    public void saveResumenByDocsPago(String app, List<Comprobante> docspago, String usuario) throws Exception;

    public void anular(String app, Comprobante dp) throws Exception;
}
