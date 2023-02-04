package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pi.server.db.Update;
import pi.server.factory.Services;
import pi.service.ComprobanteService;
import pi.server.db.server.CRUD;
import pi.service.model.efact.Comprobante;
import pi.service.model.efact.ComprobanteDet;
import pi.service.model.efact.ResumenDiario;
import pi.service.model.efact.ResumenDiarioDet;
import pi.service.model.logistica.Orden;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;
import pi.service.model.venta.OrdenVenta;
import pi.service.util.Util;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/ComprobanteService")
public class ComprobanteServiceImpl extends HessianServlet implements ComprobanteService {

    @Override
    public Comprobante getDocumentoPago(String app, int dpId) throws Exception {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "direccion_cliente",
                "direccion_cliente.persona",
                "forma_pago",
                "impuesto"
        };
        String where = "where a.id = " + dpId + " order by a.id desc limit 1";
        List<Comprobante> list = CRUD.list(app, Comprobante.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Comprobante getDocumentoPagoByOv(String app, int ordenId) throws Exception {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "cliente",
                "forma_pago",
                "impuesto"
        };
        String where = "where a.orden = " + ordenId
                + " order by a.id desc limit 1";
        List<Comprobante> list = CRUD.list(app, Comprobante.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Comprobante> list(String app, int sucursalId, Date inicio, Date fin) throws Exception {
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago",
                "vendedor", "vendedor.persona" };
        String filterCab = "where a.sucursal = " + sucursalId + " and a.fecha  between '" + inicio + "' and '" + fin
                + "'";
        filterCab += " order by a.id desc";
        List<Comprobante> listCabs = CRUD.list(app, Comprobante.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<Comprobante> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin,
            String serie, String numero, String identificador, String apellidos) throws Exception {
        String[] reqCab = { "sucursal",
                "forma_pago",
                "cliente",
                "impuesto"
        };
        String filterCab = "where a.fecha  between '" + inicio + "' and '" + fin
                + "'"
                + " and a.tipo in(1,3)";
        if (sucursalId != -1) {
            filterCab += " and a.sucursal =" + sucursalId;
        }
        filterCab += " and a.serie ilike '%" + serie + "%'";
        if (!numero.trim().isEmpty()) {
            filterCab += " and a.numero =" + numero;
        }
        filterCab += " and d.identificador ilike '%" + identificador + "%'";
        filterCab += " and d.apellidos ilike '%" + apellidos + "%'";
        filterCab += " order by a.id desc";
        List<Comprobante> listCabs = CRUD.list(app, Comprobante.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<Comprobante> list(String app, int personaId) throws Exception {
        // primero iniciar las cabeceras
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago", "moneda" };
        String filterCab = "where d.id = " + personaId + " order by a.id desc";
        List<Comprobante> listCabs = CRUD.list(app, Comprobante.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<ComprobanteDet> listDetalles(String app, int dpId) {
        List<ComprobanteDet> list = new ArrayList<>();
        String[] req = { "comprobante",
                "producto",
                "producto.unidad",
                "producto.marca",
                "producto.linea",
                "unidad",
                "comprobante.impuesto" };
        String filter = "where comprobante = " + dpId;
        try {
            list = CRUD.list(app, ComprobanteDet.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void saveByOrden(String app, Orden ov) throws Exception {
        // String whereDp = "where a.serie = '" + ov.documento_serie +
        //         "' and a.numero = " + ov.documento_numero;
        // List<DocumentoPago> listDp = CRUD.list(app, DocumentoPago.class, whereDp);
        // if (!listDp.isEmpty()) {
        //     return;
        // }
        // DocumentoPago dp = new DocumentoPago();
        // dp.activo = ov.activo;
        // dp.creador = ov.creador;
        // dp.des_obse = "-";
        // dp.dias_credito = ov.dias_credito;
        // dp.direccion_cliente = ov.direccion_cliente;
        // dp.fecha = ov.documento_fecha;
        // dp.forma_pago = ov.forma_pago;
        // dp.moneda = ov.moneda;
        // dp.nombre_imprimible = ov.nombre_imprimible;
        // dp.direccion_imprimible = ov.direccion_imprimible;
        // dp.numero = ov.documento_numero;
        // dp.observaciones = ov.observaciones;
        // dp.serie = ov.documento_serie;
        // dp.sucursal = ov.sucursal;
        // dp.tipo = ov.documento_tipo;
        // dp.tipo_cambio = ov.tipo_cambio;
        // dp.total = ov.total;
        // dp.guia_remision = ov.guia_remision;
        // dp.orden_compra = ov.orden_compra;
        // dp.orden = ov;
        // dp.impuesto = ov.impuesto;
        // dp.ind_situacion = Server.COD_SITU_POR_GENERAR_XML;

        // OrdenVentaServiceImpl ordenVentaService = new OrdenVentaServiceImpl();
        // List<OrdenVentaDet> listOvdets = ordenVentaService.listDetsLight(app, ov.id);
        // List<DocumentoPagoDet> listDPdets = new ArrayList<>();
        // for (OrdenVentaDet ovd : listOvdets) {
        //     DocumentoPagoDet dpd = new DocumentoPagoDet();
        //     dpd.activo = true;
        //     dpd.cantidad = ovd.cantidad;
        //     dpd.creador = ovd.creador;
        //     dpd.descuento = ovd.descuento;
        //     dpd.documento_pago = dp;
        //     dpd.observaciones = ovd.observaciones;
        //     dpd.precio_unitario = ovd.precio_unitario;
        //     dpd.producto = ovd.producto;
        //     dpd.total = ovd.total;
        //     dpd.unidad = ovd.unidad;
        //     if (dpd.cantidad.compareTo(BigDecimal.ZERO) == 0) {
        //         dpd.cantidad = Numbers.divide(dpd.total, dpd.precio_unitario, 2);
        //     }
        //     listDPdets.add(dpd);
        // }
        // save(app, dp, listDPdets);
    }

    @Override
    public void save(String app, Comprobante cab, List<ComprobanteDet> dets) throws Exception {
        try {
            Update.beginTransaction(app);
            CRUD.save(app, cab);
            for (ComprobanteDet det : dets) {
                det.comprobante = cab;
                CRUD.save(app, det);
            }
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void updateDP(String app, Comprobante dp) throws Exception {
        CRUD.update(app, dp);
    }

    @Override
    public NotaCredito getLastNotaCredito(String app, String serie) {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "documento_pago",
                "documento_pago.direccion_cliente",
                "documento_pago.direccion_cliente.persona",
        };
        String where = "where a.serie = '" + serie + "' order by a.numero desc limit 1";
        List<NotaCredito> list = new ArrayList<>();
        try {
            list = CRUD.list(app, NotaCredito.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Comprobante getLastDocumentoPago(String app, String serie) {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "cliente"
        };
        String where = "where a.serie = '" + serie + "' order by a.numero desc limit 1";
        List<Comprobante> list = new ArrayList<>();
        try {
            list = CRUD.list(app, Comprobante.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public NotaCredito getNotaCredito(String app, int ncId) {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "documento_pago",
                "documento_pago.cliente",
                "impuesto"
        };
        String where = "where a.id = " + ncId + " order by a.id desc limit 1";
        List<NotaCredito> list = new ArrayList<>();
        try {
            list = CRUD.list(app, NotaCredito.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<NotaCredito> listNotasCredito(String app, int sucursalId, Date inicio, Date fin, String serie,
            String numero,
            String identificador, String apellidos) {
        String[] reqCab = { "sucursal", "documento_pago",
                "documento_pago.direccion_cliente", "documento_pago.direccion_cliente.persona" };
        String filterCab = "where a.fecha  between '" + inicio + "' and '" + fin
                + "'";
        if (sucursalId != -1) {
            filterCab += " and a.sucursal =" + sucursalId;
        }
        filterCab += " and a.serie ilike '%" + serie + "%'";
        if (!numero.trim().isEmpty()) {
            filterCab += " and a.numero =" + numero;
        }
        filterCab += " and e.identificador ilike '%" + identificador + "%'";
        filterCab += " and e.apellidos ilike '%" + apellidos + "%'";
        filterCab += " order by a.id desc";
        List<NotaCredito> listCabs = new ArrayList<>();
        try {
            listCabs = CRUD.list(app, NotaCredito.class, reqCab, filterCab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCabs;
    }

    @Override
    public List<NotaCreditoDet> listNotasCreditoDets(String app, int ncId) {
        String[] req = { "nota_credito",
                "producto",
                "producto.unidad",
                "producto.marca",
                "producto.linea",
                "unidad" };
        String filter = "where a.nota_credito = " + ncId;
        List<NotaCreditoDet> list = new ArrayList<>();
        try {
            list = CRUD.list(app, NotaCreditoDet.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void saveNotaCredito(String app, NotaCredito nc, List<NotaCreditoDet> listDets) throws Exception {
        try {
            Update.beginTransaction(app);
            CRUD.execute(app, "update venta.documento_pago set observaciones = observaciones||'NC" +
                    nc.getNotaCreditoStr() + "' where id = " + nc.comprobante.id);
            CRUD.save(app, nc);
            for (NotaCreditoDet ncd : listDets) {
                ncd.id = null;
                ncd.nota_credito = nc;
                CRUD.save(app, ncd);
            }
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void updateNotaCredito(String app, NotaCredito nc) throws Exception {
        CRUD.update(app, nc);
    }

    @Override
    public void anular(String app, Comprobante dp) throws Exception {
        try {
            Update.beginTransaction(app);
            Services.getOrdenVenta().anular(app, dp.orden.id);
            CRUD.execute(app, "update venta.documento_pago set activo = false where id = " + dp.id);
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public ResumenDiario getLastResumenDiarioByDate(String app, Date fecha) {
        List<ResumenDiario> list = new ArrayList<>();
        try {
            String where = "where fecha::date = '" + fecha.toString() + "'::date order by numero desc limit 1";
            list = CRUD.list(app, ResumenDiario.class, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<ResumenDiario> listResumenes(String app, Date inicio, Date fin) {
        String where = "where fecha between '" + inicio + "' and '" + fin + "' order by fecha desc";
        List<ResumenDiario> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ResumenDiario.class, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ResumenDiarioDet> listDetallesResumen(String app, int rdId) throws Exception {
        String[] req = { "resumen_diario",
                "comprobante",
                "comprobante.sucursal",
                "comprobante.direccion_cliente",
                "comprobante.direccion_cliente.persona",
            "comprobante.impuesto" };
        String filter = "where resumen_diario = " + rdId;
        return CRUD.list(app, ResumenDiarioDet.class, req, filter);
    }

    @Override
    public void saveResumenByDocsPago(String app, List<Comprobante> docspago, String usuario) throws Exception {
        try {
            ResumenDiario rd = new ResumenDiario();
            rd.activo = true;
            rd.creador = usuario;
            rd.des_obse = "-";
            rd.fecha = new Date();
            rd.ind_situacion = Util.COD_SITU_POR_GENERAR_XML;
            ResumenDiario rdlast = getLastResumenDiarioByDate(app, rd.fecha);
            rd.numero = rdlast == null ? 1 : (rdlast.numero + 1);
            CRUD.save(app, rd);
            for (Comprobante dp : docspago) {
                ResumenDiarioDet rdd = new ResumenDiarioDet();
                rdd.activo = true;
                rdd.creador = rd.creador;
                rdd.comprobante = dp;
                rdd.resumen_diario = rd;
                dp.observaciones = "[RD-" + Util.formatDateDMY(rd.fecha) + "-"
                        + Util.completeWithZeros(rd.numero + "", 3) + "] " + dp.observaciones;
                CRUD.update(app, dp);
                CRUD.save(app, rdd);
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

}
