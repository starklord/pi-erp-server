package pi.server.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.server.CRUD;
import pi.server.factory.Services;
import pi.service.EfactService;
import pi.service.factory.Numbers;
import pi.service.model.efact.ResumenDiario;
import pi.service.model.efact.ResumenDiarioDet;
import pi.service.model.efact.TxxxSituacion;
import pi.service.model.empresa.Sucursal;
import pi.service.model.venta.DocumentoPago;
import pi.service.model.venta.DocumentoPagoDet;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;
import pi.service.util.NumberToLetterConverter;
import pi.service.util.Util;

@WebServlet("pi/EfactService")
public class EfactServiceImpl extends HessianServlet implements EfactService {

    @Override
    public void generarArchivosPlanos(String app, DocumentoPago ov, List<DocumentoPagoDet> detalles) throws Exception {
        generateTxtFiles(app, ov, detalles);
    }

    @Override
    public void generarComprobante(String app, int ordenVentaId, int pdf) throws Exception {
        DocumentoPago ov = Services.getDocumentoPago().getDocumentoPago(app, ordenVentaId);
        List<DocumentoPagoDet> detalles = Services.getDocumentoPago().listDetalles(app, ov.id);
        // primero generar los archivos planos
        String[] vars = generateTxtFiles(app, ov, detalles);
        generarXML(app, ov, vars);
    }

    @Override
    public void generarComprobantes(String app, List<Integer> ordenVentaIds, int pdf) throws Exception {
        for (Integer id : ordenVentaIds) {
            generarComprobante(app, id, pdf);
        }
    }

    @Override
    public void enviarComprobantes(String app, List<Integer> ordenVentaIds, int pdf) throws Exception {
        for (Integer id : ordenVentaIds) {
            enviarComprobante(app, id, pdf);
        }
    }

    @Override
    public DocumentoPago enviarComprobante(String app, int ordenVentaId, int pdf) throws Exception {
        DocumentoPago ov = Services.getDocumentoPago().getDocumentoPago(app, ordenVentaId);
        List<DocumentoPagoDet> detalles = Services.getDocumentoPago().listDetalles(app, ov.id);
        // primero generar los archivos planos
        String[] vars = generateTxtFiles(app, ov, detalles);
        return enviarXML(app, ov, vars);
    }

    @Override
    public void generarXML(String app, DocumentoPago dp, String[] vars) throws Exception {
        String numRuc = dp.sucursal.invoice_ruc;
        String tipDoc = "0" + dp.tipo;
        String numDoc = dp.getDocumentoStr();
        String nom = numRuc + "-" + tipDoc + "-" + numDoc;
        String invoice_path = dp.sucursal.invoice_path_sunat;
        String url = dp.sucursal.invoice_url;
        String rpta = postEfact(app, nom, Util.COD_SITU_POR_GENERAR_XML, invoice_path, vars, url);
        if (rpta.startsWith("Firma: ")) {
            dp.firma = rpta.replace("Firma: ", "");
            dp.ind_situacion = Util.COD_SITU_XML_GENERADO;
            dp.des_obse = "-";
            dp.fecha_generacion = new Date();
        } else {
            dp.ind_situacion = Util.COD_SITU_CON_ERRORES;
            dp.des_obse = rpta;
        }
        Services.getDocumentoPago().updateDP(app, dp);
    }

    @Override
    public DocumentoPago enviarXML(String app, DocumentoPago dp, String[] vars) throws Exception {
        String numRuc = dp.sucursal.invoice_ruc;
        String tipDoc = "0" + dp.tipo;
        String numDoc = dp.getDocumentoStr();
        String nom = numRuc + "-" + tipDoc + "-" + numDoc;
        String invoice_path = dp.sucursal.invoice_path_sunat;
        String url = dp.sucursal.invoice_url;
        String rpta = postEfact(app, nom, Util.COD_SITU_XML_GENERADO, invoice_path, vars, url);
        rpta = rpta.replace("\'", "");
        if (rpta.contains("situacion")) {
            dp.ind_situacion = rpta.substring(11, 13);
            if (dp.ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO) ||
                    dp.ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR)) {
                dp.fecha_envio = new Date();
                dp.des_obse = "-";
            }
            if (dp.ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO_OBSERVADO) ||
                    dp.ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR_OBS)) {
                dp.fecha_envio = new Date();
                dp.des_obse = rpta;
            }
        } else {
            dp.des_obse = rpta;
        }
        Services.getDocumentoPago().updateDP(app, dp);
        return dp;

    }

    @Override
    public ResumenDiario enviarXMLRD(String app, ResumenDiario rd, String[] vars, Sucursal sucursal) throws Exception {
        String empruc = sucursal.invoice_ruc;
        Calendar cal = Calendar.getInstance();
        cal.setTime(rd.fecha);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // 20602803288-RC-20220525-002.RDI
        String serie = year + "" + Util.completeWithZeros(month+"", 2) + "" + Util.completeWithZeros(day+"", 2);
        String numero = Util.completeWithZeros(rd.numero + "", 3);
        String nom = empruc + "-RC-" + serie + "-" + numero;
        String invoice_path = sucursal.invoice_path_sunat;
        String url = sucursal.invoice_url;
        String rpta = postEfact(app, nom, Util.COD_SITU_XML_GENERADO, invoice_path, vars, url);
        rpta = rpta.replace("\'", "");
        if (rpta.contains("situacion")) {
            rd.ind_situacion = rpta.substring(11, 13);
            if (rd.ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO) ||
                    rd.ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR)) {
                rd.fecha_envio = new Date();
                rd.des_obse = "-";
            }
            if (rd.ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO_OBSERVADO) ||
                    rd.ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR_OBS)) {
                rd.fecha_envio = new Date();
                rd.des_obse = rpta;
            }
        } else {
            rd.des_obse = rpta;
        }
        CRUD.update(app, rd);
        return rd;

    }

    public String[] generateTxtFiles(String app, DocumentoPago doc, List<DocumentoPagoDet> detalles) throws Exception {
        String[] vars = new String[10];
        try {
            BigDecimal IGV = Numbers.getBD(doc.impuesto.valor,2);
            BigDecimal IGVPercent = Numbers.getBD(doc.impuesto.valor.multiply(new BigDecimal("100")),2);
            BigDecimal impuesto = IGV.add(BigDecimal.ONE);
            Sucursal sucursal = doc.sucursal;
            String empruc = sucursal.invoice_ruc;
            String tipodoc = "0" + doc.tipo;
            String serie = doc.serie;
            String numero = Util.completeWithZeros(doc.numero + "", 8);
            String nom = String.valueOf(empruc) + "-" + tipodoc + "-" + serie + "-" + numero;
            String observaciones = String.valueOf(doc.sucursal.direccion.descripcion) + "|"
                    + doc.sucursal.direccion.telefono + "|" + doc.direccion_cliente.persona.nombre_comercial + "|"
                    + doc.forma_pago.descripcion + "|" + doc.dias_credito + "|"
                    + ((doc.observaciones == null) ? " " : doc.observaciones.concat(" "));
            String dir = doc.direccion_cliente.descripcion;
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = formatter.format(doc.fecha);
            Format formatterH = new SimpleDateFormat("HH:mm:ss");
            String hora = formatterH.format(doc.fecha);
            int documento_identidad = doc.direccion_cliente.persona.documento_identidad.id;
            if (documento_identidad == 4) {
                documento_identidad = 6;
            }
            
            if (documento_identidad == 2) {
                documento_identidad = 1;
            }
            String tipoDocIdentidad = documento_identidad + "";
            String razon = doc.nombre_imprimible.isEmpty() ? doc.direccion_cliente.persona.toString()
                    : doc.nombre_imprimible;
            // String razon = doc.direccion_cliente.persona.toString();
            String moneda = (doc.moneda.id == 1) ? "PEN" : "USD";
            BigDecimal importeNum = Numbers.divide(doc.total, impuesto, 2);
            BigDecimal igvNum = doc.total.subtract(importeNum);
            String importe = importeNum.toString();
            String igv = Numbers.getBD(igvNum, 2).toString();
            String total = Numbers.getBD(doc.total, 2).toString();
            StringBuilder scab = new StringBuilder();
            scab.append(nom).append(".CAB").append(";;;");
            scab.append("0101")
                    .append("|")
                    .append(fecha).append("|")
                    .append(hora).append("|")
                    .append("-").append("|")
                    .append("0000").append("|")
                    .append(tipoDocIdentidad).append("|")
                    .append(doc.direccion_cliente.persona.identificador).append("|")
                    .append(razon).append("|")
                    .append(moneda).append("|");
            if (doc.impuesto.tipo == 1) {
                scab.append(igv).append("|")
                        .append(importe).append("|")
                        .append(total).append("|");
            } else {
                scab.append(Numbers.getBD("0", 2)).append("|")
                        .append(total).append("|")
                        .append(total).append("|");
            }
            scab.append("0.00").append("|")
                    .append("0.00").append("|")
                    .append("0.00").append("|")
                    .append(total).append("|")
                    .append("2.1").append("|")
                    .append("2.0").append("|");
            String cab = scab.toString();
            
            StringBuilder dets = new StringBuilder("");
            dets.append(nom).append(".DET").append(";;;");
            for (DocumentoPagoDet det : detalles) {
                String codUnidadMedida = "NIU";
                if (det.producto.unidad.abreviatura.equals("MT"))
                    codUnidadMedida = "MTR";
                String ctdUnidadItem = Numbers.getBD(det.cantidad, 2).toString();
                String codProducto = det.producto.codigo;
                String marca = det.producto.marca.nombre;
                if (marca.length() > 10)
                    marca = marca.substring(0, 10);
                String descripcion = det.producto.nombre;
                StringBuilder sbr = new StringBuilder();
                BigDecimal cant = new BigDecimal(ctdUnidadItem);
                BigDecimal mtoValorVentaItem_ = cant.multiply(det.precio_unitario);
                BigDecimal divisor = Numbers.divide(mtoValorVentaItem_, cant, 8);
                BigDecimal mtoValorUnitario_ = Numbers.divide(divisor, impuesto, 8);
                BigDecimal mtoIgvItem_ = Numbers.divide(mtoValorVentaItem_, impuesto, 8).multiply(IGV);
                if (mtoIgvItem_.compareTo(new BigDecimal("0.01")) < 0)
                    mtoIgvItem_ = new BigDecimal("0.01");
                BigDecimal mtoValorUnitario = Numbers.getBD(mtoValorUnitario_, 8);
                BigDecimal mtoIgvItem = Numbers.getBD(mtoIgvItem_, 2);
                BigDecimal mtoPrecioVentaItem = Numbers.getBD(det.precio_unitario, 2);
                BigDecimal impr = mtoValorUnitario;
                BigDecimal imprItem = Numbers.getBD(cant.multiply(impr), 2);
                BigDecimal subtotal = Numbers.getBD(cant.multiply(mtoPrecioVentaItem), 2);
                BigDecimal igvUnitario = Numbers.divide(mtoIgvItem, cant, 8);
                BigDecimal igvTotalItem = Numbers.getBD(igvUnitario.multiply(cant), 2);
                BigDecimal baseImponibleTotalItem = Numbers.getBD(mtoValorUnitario.multiply(cant), 2);
                String cantEntera = Numbers.getBD(det.cantidad, 0).toString();
                String cantidad_str = String.valueOf(cantEntera) + "/" + det.observaciones + "/"
                        + det.producto.marca.nombre;
                if (det.cantidad.compareTo(BigDecimal.ZERO) != 0) 
                    cantidad_str = cantEntera;
                cantidad_str = String.valueOf(cantidad_str) + "/" + det.observaciones + "/" + det.producto.marca.nombre;
                sbr.append(codUnidadMedida).append("|");
                sbr.append(ctdUnidadItem).append("|");
                sbr.append(codProducto).append("|");
                sbr.append("-").append("|");
                sbr.append(descripcion).append("|");
                if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_IGV) {
                    sbr.append(mtoValorUnitario).append("|");
                    sbr.append(igvTotalItem).append("|");
                    sbr.append("1000").append("|");
                    sbr.append(igvTotalItem).append("|");
                    sbr.append(baseImponibleTotalItem).append("|");
                    sbr.append("IGV").append("|");
                    sbr.append("VAT").append("|");
                    sbr.append("10").append("|");
                } 
                if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_INA) {
                    sbr.append(mtoPrecioVentaItem).append("|")
                            .append("0.00").append("|")
                            .append("9998").append("|")
                            .append("0.00").append("|")
                            .append(subtotal).append("|")
                            .append("INA").append("|")
                            .append("FRE").append("|")
                            .append("30").append("|");
                }
                if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_EXP) {
                    sbr.append(mtoPrecioVentaItem).append("|")
                            .append("0.00").append("|")
                            .append("9995").append("|")
                            .append("0.00").append("|")
                            .append(subtotal).append("|")
                            .append("EXP").append("|")
                            .append("FRE").append("|")
                            .append("40").append("|");
                }
                // sbr.append("18.00").append("|"); 
                sbr.append(IGVPercent).append("|");
                sbr.append("-").append("|");
                sbr.append("0.00").append("|");
                sbr.append("0.00").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("15.00").append("|");
                sbr.append("-").append("|");
                sbr.append("ICBPER|OTH|0.00|-|0.00|0.00|");
                sbr.append("0.00").append("|");
                sbr.append("0.00").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("15.00").append("|");
                sbr.append(mtoPrecioVentaItem).append("|");
                if (doc.impuesto.tipo == 1) {
                    sbr.append(imprItem.setScale(2).toString()).append("|");
                } else {
                    sbr.append(subtotal.setScale(2).toString()).append("|");
                }

                sbr.append("0.00").append("|");
                String[] strArray = sbr.toString().split("\\|");
                for (int i = 0; i < strArray.length; i++)
                    System.out.println("value of strArray [" + i + "] " + strArray[i]);
                dets.append(sbr.toString()).append("\n");
            }
            System.out.println(dets);
            System.out.println("nom: " + nom);
            System.out.println("dir: " + dir);
            System.out.println("cab: " + cab);
            System.out.println("dets: " + dets);
            System.out.println("obs: " + observaciones);
            // CAB
            vars[0] = scab.toString();
            // DET
            vars[1] = dets.toString();
            // LEY
            StringBuilder sbrLey = new StringBuilder();
            sbrLey.append(nom).append(".LEY").append(";;;");
            sbrLey.append("1000|").append(NumberToLetterConverter.convertNumberToLetter(total, moneda)).append("|");
            vars[2] = sbrLey.toString();
            // TRI
            StringBuilder sbrTri = new StringBuilder();
            sbrTri.append(nom).append(".TRI").append(";;;");
            String strIgvBase = "0.00";
            String strIgvTrib = "0.00";
            String strInaBase = "0.00";
            String strInaTrib = "0.00";
            String strExoBase = "0.00";
            String strExoTrib = "0.00";
            String strExpBase = "0.00";
            String strExpTrib = "0.00";

            if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_IGV) {
                strIgvBase = importe;
                strIgvTrib = igv;
            }
            if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_INA) {
                strInaBase = total; 
            }
            if (doc.impuesto.tipo == Util.TIPO_IMPUESTO_EXP) {
                strExpBase = total; 
            }
            sbrTri.append("1000|IGV|VAT|").append(strIgvBase).append("|").append(strIgvTrib).append("|").append("\n");
            sbrTri.append("9998|INA|FRE|").append(strInaBase).append("|").append(strInaTrib).append("|").append("\n");
            sbrTri.append("9997|EXO|VAT|").append(strExoBase).append("|").append(strExoTrib).append("|").append("\n");
            sbrTri.append("9995|EXP|FRE|").append(strExpBase).append("|").append(strExpTrib).append("|");
            vars[3] = sbrTri.toString();

            if (doc.tipo == Util.DOCUMENTO_TIPO_FACTURA) {
                BigDecimal cuota1 = doc.total;
                if (doc.direccion_cliente.persona.es_agente_retencion) {
                    System.out.println("es_agente_retencion true");
                    BigDecimal retencion = doc.total.multiply(new BigDecimal("0.03"));
                    cuota1 = doc.total.subtract(retencion);
                }
                String cuota1Str = Numbers.getBD(cuota1, 2).toString();
                // PAG
                StringBuilder sbrPag = new StringBuilder();
                sbrPag.append(nom).append(".PAG").append(";;;");
                String fp = doc.forma_pago.id.intValue() == Util.FP_EFECTIVO ? Util.CONTADO : Util.CREDITO;
                if (fp.equals(Util.CREDITO) && doc.dias_credito == 0) {
                    fp = Util.CONTADO;
                }
                if (fp.equals(Util.CONTADO)) {
                    sbrPag.append(Util.CONTADO).append("|").append("0.00").append("|").append(moneda).append("|");
                } else {
                    sbrPag.append(Util.CREDITO).append("|").append(cuota1Str).append("|").append(moneda).append("|");
                }
                vars[4] = sbrPag.toString();
                // DPA
                if (fp.equals(Util.CREDITO)) {
                    long timeDif = doc.dias_credito * (long) (1000 * 60 * 60 * 24);
                    Date fechaVencimiento = new Date(doc.fecha.getTime() + timeDif);
                    String fechaVencimientoStr = formatter.format(fechaVencimiento);
                    StringBuilder sbrDpa = new StringBuilder();
                    sbrDpa.append(nom).append(".DPA").append(";;;");
                    sbrDpa.append(cuota1Str).append("|").append(fechaVencimientoStr).append("|").append(moneda)
                            .append("|");
                    vars[5] = sbrDpa.toString();
                }
            }
            if (doc.guia_remision != null || doc.orden_compra != null) {
                StringBuilder sbrRel = new StringBuilder();
                sbrRel.append(nom).append(".REL").append(";;;");
                if (doc.orden_compra.length() > 1) {
                    sbrRel.append("3").append("|")
                            .append("-").append("|")
                            .append("99").append("|")
                            .append(doc.orden_compra).append("|")
                            .append(tipoDocIdentidad).append("|")
                            .append(doc.getDocumentoStr()).append("|")
                            .append(total).append("|").append("\n");
                }
                if (doc.guia_remision.length() > 1) {
                    sbrRel.append("1").append("|")
                            .append("-").append("|")
                            .append("09").append("|")
                            .append(doc.guia_remision).append("|")
                            .append(tipoDocIdentidad).append("|")
                            .append(doc.getDocumentoStr()).append("|")
                            .append(total).append("|");
                }
                if (doc.orden_compra.length() > 1 || doc.guia_remision.length() > 1) {
                    vars[6] = sbrRel.toString();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return vars;
    }

    public String[] generateTxtFilesRD(String app, ResumenDiario rd, List<ResumenDiarioDet> detalles) throws Exception {
        String[] vars = new String[10];
        try {
            Sucursal sucursal = detalles.get(0).documento_pago.sucursal;
            String empruc = sucursal.invoice_ruc;
            Calendar cal = Calendar.getInstance();
            cal.setTime(rd.fecha);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // 20602803288-RC-20220525-002.RDI
            String serie = year + "" + Util.completeWithZeros(month+"", 2) + "" + Util.completeWithZeros(day+"", 2);
            String numero = Util.completeWithZeros(rd.numero + "", 3);
            String nom = empruc + "-RC-" + serie + "-" + numero;
            StringBuilder rdi = new StringBuilder();
            StringBuilder trd = new StringBuilder();
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            rdi.append(nom).append(".RDI").append(";;;");
            trd.append(nom).append(".TRD").append(";;;");
            int index = 1;
            for (ResumenDiarioDet det : detalles) {
                DocumentoPago dp = det.documento_pago;
                StringBuilder sbr = new StringBuilder();
                String fecha_comprobante = formatter.format(dp.fecha);
                String fecha_resumen = formatter.format(det.resumen_diario.fecha);
                String tipo = "0" + dp.tipo;
                String comprobanteStr = dp.getDocumentoStr();
                int tipo_docidentidad = dp.direccion_cliente.persona.documento_identidad.id;
                if (tipo_docidentidad == 4) {
                    tipo_docidentidad = 6;
                }
                if (tipo_docidentidad == 2) {
                    tipo_docidentidad = 1;
                }
                String dni = dp.direccion_cliente.persona.identificador;
                String moneda = dp.moneda.id == 1 ? "PEN" : "USD";
                BigDecimal op_gravadas = Numbers.getBD("0", 2);
                BigDecimal op_exoneradas = Numbers.getBD("0", 2);
                BigDecimal op_inafectas = Numbers.getBD("0", 2);
                BigDecimal op_exportacion = Numbers.getBD("0", 2);
                BigDecimal op_gratuitas = Numbers.getBD("0", 2);
                BigDecimal total_otros_Cargos = Numbers.getBD("0", 2);
                BigDecimal total_venta = Numbers.getBD(dp.total,2);
                String tipo_doc_modifica = "";
                String serie_doc_modifica = "";
                String numero_doc_modifica = "";
                String regimen_perc = "";
                String porcentaje_perc = "0.00";
                String base_imponible_perc = "0.00";
                String monto_perc = "0.00";
                BigDecimal total_venta_inc_perc = total_venta;
                String estado = "1";
                // otro dato adicional
                BigDecimal igv = Numbers.getBD("0", 2);
                if (dp.impuesto.tipo == Util.TIPO_IMPUESTO_IGV) {
                    op_gravadas = Util.getOpGravadas(dp.total, 2,dp.impuesto.valor);
                    igv = Util.getIgv(dp.total, 2,dp.impuesto.valor);
                }
                if (dp.impuesto.tipo == Util.TIPO_IMPUESTO_EXO) {
                    op_exoneradas = Numbers.getBD(dp.total, 2);
                }
                if (dp.impuesto.tipo == Util.TIPO_IMPUESTO_INA) {
                    op_inafectas = Numbers.getBD(dp.total, 2);
                }
                sbr.append(fecha_comprobante).append("|");
                sbr.append(fecha_resumen).append("|");
                sbr.append(tipo).append("|");
                sbr.append(comprobanteStr).append("|");
                sbr.append(tipo_docidentidad).append("|");
                sbr.append(dni).append("|");
                sbr.append(moneda).append("|");
                sbr.append(op_gravadas).append("|");
                sbr.append(op_exoneradas).append("|");
                sbr.append(op_inafectas).append("|");
                sbr.append(op_exportacion).append("|");
                sbr.append(op_gratuitas).append("|");
                sbr.append(total_otros_Cargos).append("|");
                sbr.append(total_venta).append("|");
                sbr.append(tipo_doc_modifica).append("|");
                sbr.append(serie_doc_modifica).append("|");
                sbr.append(numero_doc_modifica).append("|");
                sbr.append(regimen_perc).append("|");
                sbr.append(porcentaje_perc).append("|");
                sbr.append(base_imponible_perc).append("|");
                sbr.append(monto_perc).append("|");
                sbr.append(total_venta_inc_perc).append("|");
                sbr.append(estado).append("|");
                rdi.append(sbr.toString()).append("\n");
                //// ahora el trd
                StringBuilder sbrTrd = new StringBuilder();
                sbrTrd.append(index).append("|")
                        .append("1000").append("|")
                        .append("IGV").append("|")
                        .append("VAT").append("|")
                        .append(op_gravadas).append("|")
                        .append(igv).append("|").append("\n");
                sbrTrd.append(index).append("|")
                        .append("9998").append("|")
                        .append("INA").append("|")
                        .append("FRE").append("|")
                        .append(op_inafectas).append("|")
                        .append("0.00").append("|").append("\n");
                sbrTrd.append(index).append("|")
                        .append("9997").append("|")
                        .append("EXO").append("|")
                        .append("VAT").append("|")
                        .append(op_exoneradas).append("|")
                        .append("0.00").append("|").append("\n");
                sbrTrd.append(index).append("|")
                        .append("9995").append("|")
                        .append("EXP").append("|")
                        .append("FRE").append("|")
                        .append(op_exportacion).append("|")
                        .append("0.00").append("|");
                trd.append(sbrTrd.toString()).append("\n");
                index++;
            }
            System.out.println(rdi);
            System.out.println("nom: " + nom);
            // RDI
            vars[0] = rdi.toString();
            // TRD
            vars[1] = trd.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return vars;
    }

    @Override
    public void generarArchivosPlanosNC(String app, NotaCredito nc, List<NotaCreditoDet> detalles) throws Exception {
        generateTxtFilesNC(app,nc, detalles);
    }

    @Override
    public void generarNotaCredito(String app, int ncId, int pdf) throws Exception {
        NotaCredito nc = Services.getDocumentoPago().getNotaCredito(app,ncId);
        List<NotaCreditoDet> detalles = Services.getDocumentoPago().listNotasCreditoDets(app,nc.id);
        // primero generar los archivos planos
        String[] vars = generateTxtFilesNC(app,nc, detalles);
        generarXMLNC(app,nc, vars);
    }

    @Override
    public void generarNotasCredito(String app, List<Integer> ncIds, int pdf) throws Exception {
        for (Integer id : ncIds) {
            generarNotaCredito(app,id, pdf);
        }
    }

    @Override
    public void enviarNotasCredito(String app, List<Integer> ncIds, int pdf) throws Exception {
        for (Integer id : ncIds) {
            enviarNotaCredito(app,id, pdf);
        }
    }

    @Override
    public void enviarNotaCredito(String app, int ncId, int pdf) throws Exception {
        NotaCredito nc = Services.getDocumentoPago().getNotaCredito(app,ncId);
        List<NotaCreditoDet> detalles = Services.getDocumentoPago().listNotasCreditoDets(app,nc.id);
        // primero generar los archivos planos
        String[] vars = generateTxtFilesNC(app,nc, detalles);
        enviarXMLNC(app,nc, vars);
    }

    @Override
    public void generarXMLNC(String app, NotaCredito nc, String[] vars) throws Exception {
        String numRuc = nc.sucursal.invoice_ruc;
        String tipDoc = "07";
        String numDoc = nc.getNotaCreditoStr();
        String nom = numRuc + "-" + tipDoc + "-" + numDoc;
        String invoice_path = nc.sucursal.invoice_path_sunat;
        String url = nc.sucursal.invoice_url;
        String rpta = postEfact(app,nom, Util.COD_SITU_POR_GENERAR_XML, invoice_path, vars, url);
        if (rpta.startsWith("Firma: ")) {
            nc.sunat_firma = rpta.replace("Firma: ", "");
            nc.sunat_ind_situacion = Util.COD_SITU_XML_GENERADO;
            nc.sunat_des_obse = "-";
            nc.sunat_fecha_generacion = new Date();
        } else {
            nc.sunat_ind_situacion = Util.COD_SITU_CON_ERRORES;
            nc.sunat_des_obse = rpta;
        }
        Services.getDocumentoPago().updateNotaCredito(app,nc);
    }

    @Override
    public void enviarXMLNC(String app, NotaCredito nc, String[] vars) throws Exception {
        String numRuc = nc.sucursal.invoice_ruc;
        String tipDoc = "07";
        String numDoc = nc.getNotaCreditoStr();
        String nom = numRuc + "-" + tipDoc + "-" + numDoc;
        String invoice_path = nc.sucursal.invoice_path_sunat;
        String url = nc.sucursal.invoice_url;
        String rpta = postEfact(app,nom, Util.COD_SITU_XML_GENERADO, invoice_path, vars, url);
        rpta = rpta.replace("\'", "");
        if (rpta.contains("situacion")) {
            nc.sunat_ind_situacion = rpta.substring(11, 13);
            if (nc.sunat_ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO) ||
                    nc.sunat_ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR)) {
                nc.sunat_fecha_envio = new Date();
                nc.sunat_des_obse = "-";
            }
            if (nc.sunat_ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO_OBSERVADO) ||
                    nc.sunat_ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR_OBS)) {
                nc.sunat_fecha_envio = new Date();
                nc.sunat_des_obse = rpta;
            }
        } else {
            nc.sunat_des_obse = rpta;
        }
        Services.getDocumentoPago().updateNotaCredito(app,nc);

    }

    public String[] generateTxtFilesNC(String app, NotaCredito nc, List<NotaCreditoDet> detalles) throws Exception {
        String[] vars = new String[10];
        try {
            BigDecimal impuesto = new BigDecimal("1.18");
            Sucursal sucursal = nc.sucursal;
            String empruc = sucursal.invoice_ruc;
            String tipodocAfecto = "0" + nc.documento_pago.tipo;
            String nom = String.valueOf(empruc) + "-" + "07" + "-" + nc.getNotaCreditoStr();
            String observaciones = nc.observaciones;
            String identificador = nc.documento_pago.direccion_cliente.persona.identificador;
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = formatter.format(nc.fecha);
            Format formatterH = new SimpleDateFormat("HH:mm:ss");
            String hora = formatterH.format(nc.fecha);
            int documento_identidad = nc.documento_pago.direccion_cliente.persona.documento_identidad.id;
            if (documento_identidad == 4) {
                documento_identidad = 6;
            }
            if (documento_identidad == 2) {
                documento_identidad = 1;
            }
            String tipoDocIdentidad = documento_identidad + "";
            String razon = nc.documento_pago.nombre_imprimible.length() < 2 ? nc.getPersonaStr()
                    : nc.documento_pago.nombre_imprimible;
            // String razon = doc.direccion_cliente.persona.toString();
            String moneda = (nc.moneda.id == 1) ? "PEN" : "USD";
            BigDecimal importeNum = Numbers.divide(nc.total, impuesto, 2);
            BigDecimal igvNum = nc.total.subtract(importeNum);
            String importe = importeNum.toString();
            String igv = Numbers.getBD(igvNum, 2).toString();
            String total = Numbers.getBD(nc.total, 2).toString();
            String tipoOperacion = nc.tipo_operacion;
            String motivo = nc.motivo;
            String serieNumeroDocAfecto = nc.documento_pago.getDocumentoStr();
            StringBuilder scab = new StringBuilder();
            scab.append(nom).append(".NOT").append(";;;");
            scab.append("0101").append("|")// 1
                    .append(fecha).append("|")// 2
                    .append(hora).append("|")// 3
                    .append("0000").append("|")// 4
                    .append(tipoDocIdentidad).append("|")// 5
                    .append(identificador).append("|")// 6
                    .append(razon).append("|")// 7
                    .append(moneda).append("|")// 8
                    .append(tipoOperacion).append("|")// 9
                    .append(motivo).append("|")// 10
                    .append(tipodocAfecto).append("|")// 11
                    .append(serieNumeroDocAfecto).append("|");// 12
            if (nc.impuesto.tipo == 1) {
                scab.append(igv).append("|")
                        .append(importe).append("|")
                        .append(total).append("|");
            } else {
                scab.append(Numbers.getBD("0", 2)).append("|")
                        .append(total).append("|")
                        .append(total).append("|");
            }
            scab.append("0.00").append("|")
                    .append("0.00").append("|")
                    .append("0.00").append("|")
                    .append(total).append("|")
                    .append("2.1").append("|")
                    .append("2.0").append("|");
            String cab = scab.toString();
            BigDecimal IGV_MAS_UNO = new BigDecimal("1.18");
            BigDecimal IGV = new BigDecimal("0.18");
            StringBuilder dets = new StringBuilder("");
            dets.append(nom).append(".DET").append(";;;");
            for (NotaCreditoDet det : detalles) {
                String codUnidadMedida = "NIU";
                if (det.producto.unidad.abreviatura.equals("MT"))
                    codUnidadMedida = "MTR";
                String ctdUnidadItem = Numbers.getBD(det.cantidad, 2).toString();
                String codProducto = det.producto.codigo;
                String marca = det.producto.marca.nombre;
                if (marca.length() > 10)
                    marca = marca.substring(0, 10);
                String descripcion = det.producto.nombre;
                StringBuilder sbr = new StringBuilder();
                BigDecimal cant = new BigDecimal(ctdUnidadItem);
                BigDecimal mtoValorVentaItem_ = cant.multiply(det.precio_unitario);
                BigDecimal divisor = Numbers.divide(mtoValorVentaItem_, cant, 8);
                BigDecimal mtoValorUnitario_ = Numbers.divide(divisor, IGV_MAS_UNO, 8);
                BigDecimal mtoIgvItem_ = Numbers.divide(mtoValorVentaItem_, IGV_MAS_UNO, 8).multiply(IGV);
                if (mtoIgvItem_.compareTo(new BigDecimal("0.01")) < 0)
                    mtoIgvItem_ = new BigDecimal("0.01");
                BigDecimal mtoValorUnitario = Numbers.getBD(mtoValorUnitario_, 8);
                BigDecimal mtoIgvItem = Numbers.getBD(mtoIgvItem_, 2);
                BigDecimal mtoPrecioVentaItem = Numbers.getBD(det.precio_unitario, 2);
                BigDecimal impr = mtoValorUnitario;
                BigDecimal imprItem = Numbers.getBD(cant.multiply(impr), 2);
                BigDecimal subtotal = Numbers.getBD(cant.multiply(mtoPrecioVentaItem), 2);
                BigDecimal igvUnitario = Numbers.divide(mtoIgvItem, cant, 8);
                BigDecimal igvTotalItem = Numbers.getBD(igvUnitario.multiply(cant), 2);
                BigDecimal baseImponibleTotalItem = Numbers.getBD(mtoValorUnitario.multiply(cant), 2);
                String cantEntera = Numbers.getBD(det.cantidad, 0).toString();
                String cantidad_str = String.valueOf(cantEntera) + "/" + det.observaciones + "/"
                        + det.producto.marca.nombre;
                if (det.cantidad.compareTo(BigDecimal.ZERO) != 0)
                    cantidad_str = cantEntera;
                cantidad_str = String.valueOf(cantidad_str) + "/" + det.observaciones + "/" + det.producto.marca.nombre;
                sbr.append(codUnidadMedida).append("|");
                sbr.append(ctdUnidadItem).append("|");
                sbr.append(codProducto).append("|");
                sbr.append("-").append("|");
                sbr.append(descripcion).append("|");
                if (nc.impuesto.tipo.intValue() == 1) {
                    sbr.append(mtoValorUnitario).append("|");
                    sbr.append(igvTotalItem).append("|");
                    sbr.append("1000").append("|");
                    sbr.append(igvTotalItem).append("|");
                    sbr.append(baseImponibleTotalItem).append("|");
                    sbr.append("IGV").append("|");
                    sbr.append("VAT").append("|");
                    sbr.append("10").append("|");
                } else {
                    sbr.append(mtoPrecioVentaItem).append("|")
                            .append("0.00").append("|")
                            .append("9998").append("|")
                            .append("0.00").append("|")
                            .append(subtotal).append("|")
                            .append("INA").append("|")
                            .append("FRE").append("|")
                            .append("30").append("|");
                }
                sbr.append("18.00").append("|");
                sbr.append("-").append("|");
                sbr.append("0.00").append("|");
                sbr.append("0.00").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("15.00").append("|");
                sbr.append("-").append("|");
                sbr.append("ICBPER|OTH|0.00|-|0.00|0.00|");
                sbr.append("0.00").append("|");
                sbr.append("0.00").append("|");
                sbr.append("").append("|");
                sbr.append("").append("|");
                sbr.append("15.00").append("|");
                sbr.append(mtoPrecioVentaItem).append("|");
                if (nc.impuesto.tipo == 1) {
                    sbr.append(imprItem.setScale(2).toString()).append("|");
                } else {
                    sbr.append(subtotal.setScale(2).toString()).append("|");
                }

                sbr.append("0.00").append("|");
                String[] strArray = sbr.toString().split("\\|");
                for (int i = 0; i < strArray.length; i++)
                    System.out.println("value of strArray [" + i + "] " + strArray[i]);
                dets.append(sbr.toString()).append("\n");
            }
            System.out.println(dets);
            System.out.println("nom: " + nom);
            System.out.println("cab: " + cab);
            System.out.println("dets: " + dets);
            System.out.println("obs: " + observaciones);
            // CAB
            vars[0] = scab.toString();
            // DET
            vars[1] = dets.toString();
            // LEY
            StringBuilder sbrLey = new StringBuilder();
            sbrLey.append(nom).append(".LEY").append(";;;");
            sbrLey.append("1000|").append(NumberToLetterConverter.convertNumberToLetter(total, moneda)).append("|");
            vars[2] = sbrLey.toString();
            // TRI
            StringBuilder sbrTri = new StringBuilder();
            sbrTri.append(nom).append(".TRI").append(";;;");
            String strIgvBase = "0.00";
            String strIgvTrib = "0.00";
            String strInaBase = "0.00";
            String strInaTrib = "0.00";
            String strExoBase = "0.00";
            String strExoTrib = "0.00";
            String strExpBase = "0.00";
            String strExpTrib = "0.00";

            if (nc.impuesto.tipo == Util.TIPO_IMPUESTO_IGV) {
                strIgvBase = importe;
                strIgvTrib = igv;
            }
            if (nc.impuesto.tipo == Util.TIPO_IMPUESTO_INA) {
                strInaTrib = total;
            }
            if (nc.impuesto.tipo == Util.TIPO_IMPUESTO_EXP) {
                strExpTrib = total;
            }
            sbrTri.append("1000|IGV|VAT|").append(strIgvBase).append("|").append(strIgvTrib).append("|").append("\n");
            sbrTri.append("9998|INA|FRE|").append(strInaBase).append("|").append(strInaTrib).append("|").append("\n");
            sbrTri.append("9997|EXO|VAT|").append(strExoBase).append("|").append(strExoTrib).append("|").append("\n");
            sbrTri.append("9995|EXP|FRE|").append(strExpBase).append("|").append(strExpTrib).append("|");
            vars[3] = sbrTri.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return vars;
    }

    public static String postEfact(String app, String nom, String opt, String invoice_path, String[] vars, String urlStr)
            throws Exception {
        URL url = new URL(urlStr);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("nom", nom);
        params.put("opt", opt);
        params.put("app", invoice_path);
        params.put("var0", vars[0]);
        params.put("var1", vars[1]);
        params.put("var2", vars[2]);
        params.put("var3", vars[3]);
        params.put("var4", vars[4]);
        params.put("var5", vars[5]);
        params.put("var6", vars[6]);
        params.put("var7", vars[7]);
        params.put("var8", vars[8]);
        params.put("var9", vars[9]);

        String encode = "ISO-8859-1";
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), encode));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), encode));
        }
        byte[] postDataBytes = postData.toString().getBytes(encode);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
        StringBuilder sbr = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            sbr.append((char) c);
        }
        System.out.println(sbr.toString());
        return sbr.toString();
    }

    @Override
    public List<TxxxSituacion> listEfactSituaciones(String app) {
        List<TxxxSituacion> list = new ArrayList<>();
        try {
            list = CRUD.list(app, TxxxSituacion.class, "where id is not null");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void generarArchivosPlanosRD(String app, ResumenDiario rd, List<ResumenDiarioDet> detalles) throws Exception {
        generateTxtFilesRD(app,rd, detalles);
    }

    @Override
    public ResumenDiario enviarComprobanteRD(String app, ResumenDiario rd, int pdf) throws Exception {
        List<ResumenDiarioDet> detalles = Services.getDocumentoPago().listDetallesResumen(app,rd.id);
        // primero generar los archivos planos
        String[] vars = generateTxtFilesRD(app,rd, detalles);
        Sucursal sucursal = detalles.get(0).documento_pago.sucursal;
        return enviarXMLRD(app,rd, vars, sucursal);
    }

    @Override
    public void generarXMLRD(String app, ResumenDiario rd, String[] vars) throws Exception {
        // TODO Auto-generated method stub

    }

}