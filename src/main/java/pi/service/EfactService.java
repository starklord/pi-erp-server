package pi.service;

import java.util.List;

import pi.service.model.efact.Comprobante;
import pi.service.model.efact.ComprobanteDet;
import pi.service.model.efact.ResumenDiario;
import pi.service.model.efact.ResumenDiarioDet;
import pi.service.model.efact.TxxxSituacion;
import pi.service.model.empresa.Sucursal;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;

public interface EfactService {
    public void generarArchivosPlanos(String app, Comprobante cp, List<ComprobanteDet> detalles)  throws Exception;
    public void generarXML(String app, Comprobante dp, String[] vars) throws Exception;
    public Comprobante enviarXML(String app, Comprobante ov, String[] vars) throws Exception;
    
    public void generarComprobante(String app, int dpId, int pdf) throws Exception;
    public Comprobante enviarComprobante(String app, int dpId, int pdf) throws Exception;
    
    public void generarComprobantes(String app, List<Integer> dpIds, int pdf) throws Exception;
    public void enviarComprobantes(String app, List<Integer> dpIds, int pdf) throws Exception;

    //para las notas de credito

    public void generarArchivosPlanosNC(String app, NotaCredito ov, List<NotaCreditoDet> detalles)  throws Exception;
    public void generarXMLNC(String app, NotaCredito dp, String[] vars) throws Exception;
    public void enviarXMLNC(String app, NotaCredito ov, String[] vars) throws Exception;

    public void generarNotaCredito(String app, int dpId, int pdf) throws Exception;
    public void enviarNotaCredito(String app, int dpId, int pdf) throws Exception;
    
    public void generarNotasCredito(String app, List<Integer> dpIds, int pdf) throws Exception;
    public void enviarNotasCredito(String app, List<Integer> dpIds, int pdf) throws Exception;

    // para los resumenes diarios
    public void generarArchivosPlanosRD(String app, ResumenDiario rd, List<ResumenDiarioDet> detalles)  throws Exception;
    public void generarXMLRD(String app, ResumenDiario rd, String[] vars) throws Exception;
    public ResumenDiario enviarXMLRD(String app, ResumenDiario rd, String[] vars, Sucursal sucursal) throws Exception;
    public ResumenDiario enviarComprobanteRD(String app, ResumenDiario rd, int pdf) throws Exception;

    //
    public List<TxxxSituacion> listEfactSituaciones(String app);

}
