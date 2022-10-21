package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pi.App;
import pi.server.factory.Services;
import pi.service.factory.Numbers;
import pi.service.OrdenVentaService;
import pi.service.model.Moneda;
import pi.service.model.almacen.Kardex;
import pi.service.model.finanza.Caja;
import pi.service.model.finanza.Recibo;
import pi.service.model.finanza.ReciboTipo;
import pi.service.model.venta.OrdenVenta;
import pi.service.model.venta.OrdenVentaDet;
import pi.service.model.venta.Proforma;
import pi.service.util.Util;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/OrdenVentaService")
public class OrdenVentaServiceImpl extends HessianServlet implements OrdenVentaService {

    @Override
    public OrdenVenta getOrdenVenta(String app, int ordenVentaId) throws Exception {
        String[] req = { "sucursal", "sucursal.direccion", "direccion_cliente", "direccion_cliente.persona",
                "direccion_cliente.persona.documento_identidad", "vendedor", "vendedor.persona", "forma_pago" };
        String where = "where a.id = " + ordenVentaId + " order by a.numero desc limit 1";
        List<OrdenVenta> list = CRUD.list(app,OrdenVenta.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OrdenVenta> list(String app, int sucursalId, Date inicio, Date fin, int vendedorId) throws Exception {
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago",
                "vendedor", "vendedor.persona" };
        String filterCab = " where a.fecha  between '" + inicio + "' and '" + fin + "'";
        if (sucursalId != -1) {
            filterCab += " and a.sucursal = " + sucursalId;
        }
        if (vendedorId != -1) {
            filterCab += " and a.vendedor = " + vendedorId;
        }
        filterCab += " order by a.numero desc";
        List<OrdenVenta> listCabs = CRUD.list(app,OrdenVenta.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<OrdenVenta> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin, int vendedorId) throws Exception {
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago",
                "vendedor", "vendedor.persona" };
        String filterCab = "where a.sucursal = " + sucursalId + " and a.documento_fecha  between '" + inicio + "' and '"
                + fin
                + "'"
                + " and a.documento_tipo in(1,3)";
        if (vendedorId != -1) {
            filterCab += " and a.vendedor = " + vendedorId;
        }
        filterCab += " order by a.numero desc";
        List<OrdenVenta> listCabs = CRUD.list(app,OrdenVenta.class, reqCab, filterCab);
        return listCabs;
    }

    @Override
    public List<OrdenVenta> list(String app, int personaId) throws Exception {
        // primero iniciar las cabeceras
        String[] reqCab = { "sucursal", "direccion_cliente", "direccion_cliente.persona", "forma_pago", "moneda",
                "vendedor", "vendedor.persona", "ciclo" };
        String filterCab = "where d.id = " + personaId + " order by a.numero desc";
        List<OrdenVenta> listCabs = CRUD.list(app,OrdenVenta.class, reqCab, filterCab);
        return listCabs;

    }

    @Override
    public List<OrdenVentaDet> listDetalles(String app, int ordenVentaId) throws Exception {
        String[] req = { "orden_venta",
        "orden_venta.direccion_cliente","orden_venta.direccion_cliente.persona",
                "producto",
                "producto.unidad",
                "producto.marca",
                "producto.linea",
                "unidad" };
        String filter = "where orden_venta = " + ordenVentaId;
        return CRUD.list(app,OrdenVentaDet.class, req, filter);
    }

    @Override
    public List<OrdenVenta> listCabsLight(String app) throws Exception {
        return CRUD.list(app,OrdenVenta.class, "order by id asc");
    }

    @Override
    public List<OrdenVenta> listCabsLightAnulados(String app) throws Exception {
        return CRUD.list(app,OrdenVenta.class, "where a.activo is false order by id desc limit 100");
    }

    @Override
    public List<OrdenVenta> listCabsLightOnlyEfact(String app, int limit) throws Exception {
        return CRUD.list(app,OrdenVenta.class, "where documento_tipo in(1,3) "
                + "order by documento_fecha desc limit " + limit);
    }

    @Override
    public List<OrdenVentaDet> listDetsLight(String app, int ovId) throws Exception {
        String where = "where orden_venta = " + ovId + " order by a.id asc";
        return CRUD.list(app,OrdenVentaDet.class, where);

    }

    @Override
    public OrdenVenta getLastOrdenVenta(String app, int sucursalId) throws Exception {
        String[] req = { "sucursal", "direccion_cliente", "direccion_cliente.persona",
                "direccion_cliente.persona.documento_identidad", "forma_pago", "vendedor",
                "vendedor.persona" };
        String where = "where a.sucursal = " + sucursalId + " order by numero desc limit 1";
        List<OrdenVenta> list = CRUD.list(app,OrdenVenta.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public OrdenVenta getLastOrdenVenta(String app, int documentoTipoId, String documento_serie) throws Exception {
        String[] req = { "sucursal", "direccion_cliente", "direccion_cliente.persona",
                "direccion_cliente.persona.documento_identidad", "forma_pago", "vendedor",
                "vendedor.persona" };
        String where = "where a.documento_serie = '" + documento_serie + "' and a.documento_tipo = " + documentoTipoId
                + " order by a.documento_numero desc limit 1";
        List<OrdenVenta> list = CRUD.list(app,OrdenVenta.class, req, where);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public OrdenVenta save(String app, Proforma proforma, OrdenVenta ordenVenta, List<OrdenVentaDet> detalles, int cajaId,
            BigDecimal pagaCon, String numero_operacion) throws Exception {
        try {
            Update.beginTransaction(app);
            // Thread.sleep(1000);
            String where = "where sucursal = " + ordenVenta.sucursal.id + " order by numero desc limit 1";
            List<OrdenVenta> list = CRUD.list(app,OrdenVenta.class, where);
            int numero = 1;
            if (!list.isEmpty()) {
                numero = list.get(0).numero + 1;
            }
            ordenVenta.numero = numero;
            if (ordenVenta.documento_tipo.intValue() == Util.DOCUMENTO_TIPO_ORDEN_VENTA) {
                ordenVenta.documento_numero = null;
            } else {
                OrdenVenta ovLast = getLastOrdenVenta(app, ordenVenta.documento_tipo, ordenVenta.documento_serie);
                ordenVenta.documento_numero = ovLast == null ? 1 : (ovLast.documento_numero + 1);
            }
            if(cajaId!=-1) {
            ordenVenta.total_cobrado = ordenVenta.total;
            }
            CRUD.save(app,ordenVenta);
            

            for (OrdenVentaDet det : detalles) {
                String wh = "where almacen = " + det.almacen.id + " and producto = " + det.producto.id
                        + " order by id desc limit 1";
                List<Kardex> listKardex = CRUD.list(app,Kardex.class, wh);
                String errorMessage = "Otro vendedor acaba de vender un producto elegido y se ha quedado sin stock: "
                        + det.producto.nombre;
                if (!ordenVenta.sucursal.empresa.allow_buy_without_stock) {
                    if (listKardex.isEmpty()) {
                        throw new Exception(errorMessage);
                    }
                    BigDecimal stockKardex = listKardex.get(0).stock;
                    if (stockKardex.compareTo(det.cantidad) < 0) {
                        throw new Exception(errorMessage);
                    }

                }

                det.orden_venta = ordenVenta;
                det.id = null;
                CRUD.save(app,det);
                saveKardexFromOrdenVentaDet(app, det);
            }
            // para generar el recibo
            if (cajaId != -1) {
                FinanzaServiceImpl finanzaService = new FinanzaServiceImpl();
                Recibo lastRecibo = finanzaService.getLastRecibo(app, ordenVenta.sucursal.id,
                        Util.MOVIMIENTO_INGRESO,cajaId);
                int numero_recibo = lastRecibo == null ? 1 : lastRecibo.numero + 1;
                Recibo r = new Recibo();
                r.activo = true;
                r.area = null;
                r.caja = new Caja();
                r.caja.id = cajaId;
                r.cerrado = false;
                r.creador = ordenVenta.creador;
                r.fecha = ordenVenta.fecha;
                r.letra = null;
                r.moneda = new Moneda();
                r.moneda.id = ordenVenta.moneda.id;
                r.movimiento = Util.MOVIMIENTO_INGRESO;
                r.numero = numero_recibo;
                r.numero_operacion = numero_operacion;
                r.observaciones = "";
                r.orden_compra = null;
                r.orden_venta = ordenVenta;
                r.paga_con = pagaCon;
                r.recibo_tipo = new ReciboTipo();
                r.recibo_tipo.id = Util.RT_COBRO_EFECTIVO;
                r.sucursal = ordenVenta.sucursal;
                r.tipo_cambio = BigDecimal.ONE;
                r.total = ordenVenta.total;
                r.turno =0 ;
                CRUD.save(app,r);
            }
            if (ordenVenta.documento_tipo != Util.DOCUMENTO_TIPO_ORDEN_VENTA) {
                Services.getDocumentoPago().saveByOrdenVenta(app, ordenVenta);
            }
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception("Problemas al grabar: " + ex.getMessage());
        }
        return ordenVenta;
    }

    public void saveKardexFromOrdenVentaDet(String app, OrdenVentaDet ovd) throws Exception {
        ProductoServiceImpl productoService = new ProductoServiceImpl();
        Kardex ok = productoService.getLastKardexFromProducto(app, ovd.producto.id, ovd.almacen.id);
        Kardex nk = new Kardex();
        nk.activo = true;
        nk.almacen = ovd.almacen;
        nk.creador = ovd.creador;
        nk.destino = ovd.orden_venta.direccion_cliente.persona.toString();
        nk.origen = "-";
        nk.fecha = new Date();
        nk.fecha_orden = ovd.orden_venta.fecha;
        Integer numero = ovd.orden_venta.documento_numero;
        if (numero == null) {
            numero = ovd.orden_venta.numero;
        }
        nk.documento = ovd.orden_venta.documento_serie + "-" + numero;
        nk.ingreso = BigDecimal.ZERO;

        if (ovd.producto.unidad.id.intValue() == ovd.unidad.id) {
            nk.salida = ovd.cantidad;
        } else {
            nk.salida = Numbers.divide(ovd.cantidad, ovd.producto.factor_conversion, 4).multiply(ovd.producto.contenido);
        }

        nk.movimiento = Util.MOVIMIENTO_KARDEX_SALIDA;
        nk.orden_id = ovd.orden_venta.id;
        nk.precio_costo = BigDecimal.ZERO;
        nk.precio_venta = ovd.precio_unitario;
        nk.producto = ovd.producto;
        nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
        nk.stock = nk.stock_anterior.subtract(nk.salida);
        nk.tipo = Util.TIPO_ORDEN_VENTA;
        nk.usado = BigDecimal.ZERO;
        nk.identificador = ovd.orden_venta.direccion_cliente.persona.identificador;
        CRUD.save(app,nk);
    }

    private void saveKardexFromAnnulOrdenVentaDet(String app, OrdenVentaDet ovd) throws Exception {
        ProductoServiceImpl productoService = new ProductoServiceImpl();
        Kardex ok = productoService.getLastKardexFromProducto(app, ovd.producto.id, ovd.almacen.id);
        Kardex nk = new Kardex();
        Integer numero = ovd.orden_venta.documento_numero;
        if (numero == null) {
            numero = ovd.orden_venta.numero;
        }
        nk.activo = true;
        nk.almacen = ovd.almacen;
        nk.creador = ovd.creador;
        nk.destino = ovd.orden_venta.direccion_cliente.persona.toString();
        nk.origen = "ANULACION DE VENTA";
        nk.fecha = new Date();
        nk.fecha_orden = ovd.orden_venta.fecha;
        nk.documento = ovd.orden_venta.documento_serie + "-" + numero;
        if (ovd.producto.unidad.id == ovd.unidad.id.intValue()) {
            nk.ingreso = ovd.cantidad;
        } else {
            nk.ingreso = Numbers.divide(ovd.cantidad, ovd.producto.factor_conversion, 4);
        }

        nk.salida = BigDecimal.ZERO;
        nk.movimiento = Util.MOVIMIENTO_KARDEX_ENTRADA;
        nk.orden_id = ovd.orden_venta.id;
        nk.precio_costo = BigDecimal.ZERO;
        nk.precio_venta = ovd.precio_unitario;
        nk.producto = ovd.producto;
        nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
        nk.stock = nk.stock_anterior.add(nk.ingreso);
        nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
        nk.usado = BigDecimal.ZERO;
        nk.identificador = ovd.orden_venta.direccion_cliente.persona.identificador;
        CRUD.save(app,nk);
    }

    private void saveKardexFromDesannulOrdenVentaDet(String app, OrdenVentaDet ovd) throws Exception {
        ProductoServiceImpl productoService = new ProductoServiceImpl();
        Kardex ok = productoService.getLastKardexFromProducto(app, ovd.producto.id, ovd.almacen.id);
        Kardex nk = new Kardex();
        Integer numero = ovd.orden_venta.documento_numero;
        if (numero == null) {
            numero = ovd.orden_venta.numero;
        }
        nk.activo = true;
        nk.almacen = ovd.almacen;
        nk.creador = ovd.creador;
        nk.destino = ovd.orden_venta.direccion_cliente.persona.toString();
        nk.origen = "DESANULACION DE VENTA";
        nk.fecha = new Date();
        nk.fecha_orden = ovd.orden_venta.fecha;
        nk.documento = ovd.orden_venta.documento_serie + "-" + numero;
        nk.salida = ovd.cantidad;
        nk.ingreso = BigDecimal.ZERO;
        nk.movimiento = Util.MOVIMIENTO_KARDEX_SALIDA;
        nk.orden_id = ovd.orden_venta.id;
        nk.precio_costo = BigDecimal.ZERO;
        nk.precio_venta = ovd.precio_unitario;
        nk.producto = ovd.producto;
        nk.stock_anterior = ok == null ? BigDecimal.ZERO : ok.stock;
        nk.stock = nk.stock_anterior.subtract(nk.salida);
        nk.tipo = Util.TIPO_ORDEN_REGULARIZACION;
        nk.usado = BigDecimal.ZERO;
        nk.identificador = ovd.orden_venta.direccion_cliente.persona.identificador;
        CRUD.save(app,nk);
    }

    @Override
    public void anular(String app, int ordenVentaId) throws Exception {
        OrdenVenta ov = getOrdenVenta(app, ordenVentaId);
        try{
            Update.beginTransaction(app);
            if(ov.getSaldo().compareTo(BigDecimal.ZERO)>0){
                throw new Exception("La orden de venta tiene cobranzas, anule primero la cobranza en el modulo de cuadre de caja");
            }
            CRUD.execute(app, "update venta.orden_venta set activo = false where id = " + ordenVentaId);
            List<OrdenVentaDet> list = listDetalles(app, ordenVentaId);
            for(OrdenVentaDet item : list){
                this.saveKardexFromAnnulOrdenVentaDet(app, item);
            }
            Update.commitTransaction(app);
        }catch(Exception ex){
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

}
