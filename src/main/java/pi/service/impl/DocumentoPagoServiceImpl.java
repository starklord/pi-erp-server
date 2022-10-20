package pi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pi.server.Server;
import pi.service.DocumentoPagoService;
import pi.service.factory.Numbers;
import pi.service.factory.Services;
import pi.service.model.venta.DocumentoPago;
import pi.service.model.venta.DocumentoPagoDet;
import pi.service.model.venta.NotaCredito;
import pi.service.model.venta.NotaCreditoDet;
import pi.service.model.venta.OrdenVenta;
import pi.service.model.venta.OrdenVentaDet;
import pi.service.util.Util;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/DocumentoPagoService")
public class DocumentoPagoServiceImpl extends HessianServlet implements DocumentoPagoService {

    @Override
    public DocumentoPago getDocumentoPago(String app, int dpId) throws Exception {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "direccion_cliente",
                "direccion_cliente.persona",
                "forma_pago"
        };
        String where = "where a.id = " + dpId + " order by a.id desc limit 1";
        List<DocumentoPago> list = CRUD.list(app,DocumentoPago.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public DocumentoPago getDocumentoPagoByOv(String app, OrdenVenta ov) throws Exception {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "direccion_cliente",
                "direccion_cliente.persona",
                "forma_pago"
        };
        String where = "where a.serie = '" + ov.documento_serie+ "' and a.numero = "+ov.documento_numero+" order by a.id desc limit 1";
        List<DocumentoPago> list = CRUD.list(app,DocumentoPago.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<DocumentoPago> list(String app, int sucursalId, Date inicio, Date fin) throws Exception {
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago",
                "vendedor", "vendedor.persona" };
        String filterCab = "where a.sucursal = " + sucursalId + " and a.fecha  between '" + inicio + "' and '" + fin
                + "'";
        filterCab += " order by a.id desc";
        List<DocumentoPago> listCabs = CRUD.list(app,DocumentoPago.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<DocumentoPago> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin,
            String serie, String numero, String identificador, String apellidos) throws Exception {
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago" };
        String filterCab = "where a.fecha  between '" + inicio + "' and '" + fin
                + "'"
                + " and a.tipo in(1,3,0)";
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
        List<DocumentoPago> listCabs = CRUD.list(app,DocumentoPago.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<DocumentoPago> list(String app, int personaId) throws Exception {
        // primero iniciar las cabeceras
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago", "moneda" };
        String filterCab = "where d.id = " + personaId + " order by a.id desc";
        List<DocumentoPago> listCabs = CRUD.list(app,DocumentoPago.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<DocumentoPagoDet> listDetalles(String app, int dpId) throws Exception {
        String[] req = { "documento_pago",
                "producto",
                "producto.unidad",
                "producto.marca",
                "producto.linea",
                "unidad" };
        String filter = "where documento_pago = " + dpId;
        return CRUD.list(app,DocumentoPagoDet.class, req, filter);
    }

    @Override
    public void saveByOrdenVenta(String app, OrdenVenta ov) throws Exception {
        String whereDp = "where a.serie = '" + ov.documento_serie +
                "' and a.numero = " + ov.documento_numero;
        List<DocumentoPago> listDp = CRUD.list(app,DocumentoPago.class, whereDp);
        if (!listDp.isEmpty()) {
            return;
        }
        DocumentoPago dp = new DocumentoPago();
        dp.activo = ov.activo;
        dp.creador = ov.creador;
        dp.des_obse = "-";
        dp.dias_credito = ov.dias_credito;
        dp.direccion_cliente = ov.direccion_cliente;
        dp.fecha = ov.documento_fecha;
        dp.forma_pago = ov.forma_pago;
        dp.moneda = ov.moneda;
        dp.nombre_imprimible = ov.nombre_imprimible;
        dp.direccion_imprimible = ov.direccion_imprimible;
        dp.numero = ov.documento_numero;
        dp.observaciones = ov.observaciones;
        dp.serie = ov.documento_serie;
        dp.sucursal = ov.sucursal;
        dp.tipo = ov.documento_tipo;
        dp.tipo_cambio = ov.tipo_cambio;
        dp.total = ov.total;
        dp.guia_remision = ov.guia_remision;
        dp.orden_compra = ov.orden_compra;
        dp.orden_venta = ov;
        dp.ind_situacion = Server.COD_SITU_POR_GENERAR_XML;

        OrdenVentaServiceImpl ordenVentaService = new OrdenVentaServiceImpl();
        List<OrdenVentaDet> listOvdets = ordenVentaService.listDetsLight(app, ov.id);
        List<DocumentoPagoDet> listDPdets = new ArrayList<>();
        for (OrdenVentaDet ovd : listOvdets) {
            DocumentoPagoDet dpd = new DocumentoPagoDet();
            dpd.activo = true;
            dpd.cantidad = ovd.cantidad;
            dpd.creador = ovd.creador;
            dpd.descuento = ovd.descuento;
            dpd.documento_pago = dp;
            dpd.observaciones = ovd.observaciones;
            dpd.precio_unitario = ovd.precio_unitario;
            dpd.producto = ovd.producto;
            dpd.total = ovd.total;
            dpd.unidad = ovd.unidad;
            if (dpd.cantidad.compareTo(BigDecimal.ZERO) == 0) {
                dpd.cantidad = Numbers.divide(dpd.total, dpd.precio_unitario, 2);
            }
            listDPdets.add(dpd);
        }
        save(app, dp, listDPdets);
    }

    @Override
    public void save(String app, DocumentoPago cab, List<DocumentoPagoDet> dets) throws Exception {
        try{
            Update.beginTransaction(app);
            CRUD.save(app,cab);
            for(DocumentoPagoDet det : dets){
                det.documento_pago = cab;
                CRUD.save(app,det);
            }
            Update.commitTransaction(app);
        }catch(Exception ex){
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void updateDP(String app, DocumentoPago dp) throws Exception {
        CRUD.update(app,dp);
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
            list = CRUD.list(app,NotaCredito.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public DocumentoPago getLastDocumentoPago(String app, String serie) {
        String[] req = { "sucursal",
                "sucursal.direccion",
                "direccion_cliente",
                "direccion_cliente.persona",
        };
        String where = "where a.serie = '" + serie + "' order by a.numero desc limit 1";
        List<DocumentoPago> list = new ArrayList<>();
        try {
            list = CRUD.list(app,DocumentoPago.class, req, where);
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
                "documento_pago.direccion_cliente",
                "documento_pago.direccion_cliente.persona",
        };
        String where = "where a.id = " + ncId + " order by a.id desc limit 1";
        List<NotaCredito> list = new ArrayList<>();
        try {
            list = CRUD.list(app,NotaCredito.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<NotaCredito> listNotasCredito(String app, int sucursalId, Date inicio, Date fin, String serie, String numero,
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
            listCabs = CRUD.list(app,NotaCredito.class, reqCab, filterCab);
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
            list = CRUD.list(app,NotaCreditoDet.class, req, filter);
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
                    nc.getNotaCreditoStr() + "' where id = " + nc.documento_pago.id);
            CRUD.save(app,nc);
            for(NotaCreditoDet ncd : listDets){
                ncd.nota_credito = nc;
                CRUD.save(app,ncd);
            }
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void updateNotaCredito(String app, NotaCredito nc) throws Exception {
        CRUD.update(app,nc);
    }

    @Override
    public void anular(String app, DocumentoPago dp) throws Exception {
        try{
            Update.beginTransaction(app);
            Services.getOrdenVenta().anular(app,dp.orden_venta.id);
            CRUD.execute(app, "update venta.documento_pago set activo = false where id = " + dp.id);
            Update.commitTransaction(app);
        }catch(Exception ex){
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        
    }

}
