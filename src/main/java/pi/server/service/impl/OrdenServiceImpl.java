package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.Update;
import pi.server.db.server.CRUD;
import pi.server.factory.Services;
import pi.service.OrdenService;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.almacen.Articulo;
import pi.service.model.almacen.Linea;
import pi.service.model.almacen.Marca;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;
import pi.service.model.efact.Comprobante;
import pi.service.model.efact.ComprobanteDet;
import pi.service.model.finanza.Recibo;
import pi.service.model.logistica.Cotizacion;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;
import pi.service.model.logistica.TransformacionDet;
import pi.service.model.persona.Persona;
import pi.service.util.Util;

@WebServlet("pi/OrdenService")
public class OrdenServiceImpl extends HessianServlet implements OrdenService {
    @Override
    public Orden getOrden(String app, int ordenId) {
        List<Orden> list = new ArrayList<>();
        String[] require = {
                "proveedor",
                "cliente",
                "moneda",
                "aprobado_por",
                "atendido_por",
                "almacen_origen",
                "almacen_destino",
                "sucursal",
                "sucursal.empresa",
        };
        String where = "where a.id = " + ordenId + " order by numero desc limit 1";
        try {
            list = CRUD.list(app, Orden.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Orden getLastOrden(String app, int sucursalId, char tipo) {
        List<Orden> list = new ArrayList<>();
        String[] require = {
                "proveedor",
                "cliente",
                "moneda",
                "aprobado_por",
                "atendido_por",
                "almacen_origen",
                "almacen_destino",
        };
        String where = "where a.sucursal = " + sucursalId + " and tipo= '" + tipo + "' order by numero desc limit 1";
        try {
            list = CRUD.list(app, Orden.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Orden> list(String app, Date inicio, Date fin, int sucursalId, char tipo) {
        List<Orden> list = new ArrayList<>();
        String[] require = {
                "proveedor",
                "cliente",
                "moneda",
                "aprobado_por",
                "atendido_por",
                "almacen_origen",
                "almacen_destino",
                "sucursal",
        };
        String where = "where a.fecha between '" + inicio.toString() + "' and '" + fin.toString() + "'";
        where += " and a.tipo = '" + tipo + "'";
        if (sucursalId != -1) {
            where += " and a.sucursal = " + sucursalId;
        }
        where += " order by a.id desc";
        try {
            list = CRUD.list(app, Orden.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<OrdenDet> listDets(String app, Date inicio, Date fin, int sucursalId, char tipo) {
        List<OrdenDet> list = new ArrayList<>();
        String[] require = {
                "orden",
                "orden.proveedor",
                "orden.cliente",
                "orden.moneda",
                "orden.aprobado_por",
                "orden.atendido_por",
                "orden.almacen_origen",
                "orden.almacen_destino",
                "orden.sucursal",
                "producto",
                "orden",
                "unidad"
        };
        String where = "where b.fecha between '" + inicio.toString() + "' and '" + fin.toString() + "'";
        where += " and b.tipo = '" + tipo + "'";
        if (sucursalId != -1) {
            where += " and b.sucursal = " + sucursalId;
        }
        where += " order by b.id desc";
        try {
            list = CRUD.list(app, OrdenDet.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<OrdenDet> listDets(String app, int ordenId) {
        List<OrdenDet> list = new ArrayList<>();
        String[] require = {
                "producto",
                "orden",
                "unidad"
        };
        String where = " where a.orden= " + ordenId;
        try {
            list = CRUD.list(app, OrdenDet.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenArt> listOrdenArts(String app, int ordenId) {
        List<OrdenArt> list = new ArrayList<>();
        String[] require = {
                "orden",
                "articulo",
                "articulo.producto"
        };
        String where = " where b.id = " + ordenId;
        where += " order by a.creado asc";
        try {
            list = CRUD.list(app, OrdenArt.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenArt> listOrdenArtsByProducto(String app, int ordenId, int productoId) {
        List<OrdenArt> list = new ArrayList<>();
        String[] require = {
                "orden",
                "articulo",
                "articulo.producto"
        };
        String where = " where b.id = " + ordenId + " and d.id = " + productoId;
        where += " order by a.creado asc";
        try {
            list = CRUD.list(app, OrdenArt.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenArt> listOrdenArtsByAlmacenProducto(String app, int almacenId, int productoId, Date inicio,
            Date fin) {
        List<OrdenArt> list = new ArrayList<>();
        String[] require = {
                "orden",
                "articulo",
                "articulo.producto"
        };
        String where = " where (b.almacen_origen = " + almacenId + " or b.almacen_destino = " + almacenId;
        where += ") and c.producto = " + productoId;
        if (inicio != null) {
            where += " and b.fecha between '" + inicio.toString() + "' and '" + fin.toString() + "'";
        }
        where += " order by a.creado asc";
        try {
            list = CRUD.list(app, OrdenArt.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenArt> listOrdenArtsByArticulo(String app, int articuloId) {
        List<OrdenArt> list = new ArrayList<>();
        String[] require = {
                "orden",
                "articulo",
                "articulo.producto"
        };
        String where = " where a.id = " + articuloId;
        where += " order by creado asc";
        try {
            list = CRUD.list(app, OrdenArt.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Orden saveOrden(String app, Orden orden, List<OrdenDet> detalles, Integer cotizacionId,
            Comprobante comprobante, Recibo recibo) throws Exception {
        try {
            Update.beginTransaction(app);
            Orden ordenLast = getLastOrden(app, orden.sucursal.id, orden.tipo);
            int numero = ordenLast == null ? 1 : (ordenLast.numero + 1);
            orden.numero = numero;

            CRUD.save(app, orden);
            if (comprobante != null) {
                Comprobante lastCp = Services.getComprobante().getLastDocumentoPago(app, comprobante.serie);
                if (lastCp != null) {
                    comprobante.serie = lastCp.serie;
                    comprobante.numero = (lastCp.numero + 1);
                }
                comprobante.orden = orden;
                CRUD.save(app, comprobante);
                for (OrdenDet det : detalles) {
                    ComprobanteDet cdet = new ComprobanteDet();
                    cdet.activo = true;
                    cdet.cantidad = det.cantidad;
                    cdet.creador = det.creador;
                    cdet.descuento = BigDecimal.ZERO;
                    cdet.comprobante = comprobante;
                    cdet.observaciones = det.observaciones;
                    cdet.precio_unitario = det.precio_unitario;
                    cdet.producto = det.producto;
                    cdet.total = det.total;
                    cdet.unidad = det.unidad;
                    CRUD.save(app, cdet);
                }
            }
            if (recibo != null) {
                recibo.numero = 1;
                Recibo lastRec = Services.getFinanza().getLastRecibo(app, orden.sucursal.id, recibo.movimiento,
                        recibo.caja.id);
                if (lastRec != null) {
                    recibo.numero = (lastRec.numero + 1);
                }
                recibo.orden = orden;
                CRUD.save(app, recibo);
                orden.total_cobrado = orden.total_cobrado.add(recibo.total);
                CRUD.update(app, orden);
            }
            if (cotizacionId != null) {
                Cotizacion coti = Services.getCotizacion().get(app, cotizacionId);
                coti.orden = orden;
                CRUD.update(app, coti);
            }
            for (OrdenDet det : detalles) {
                det.id = null;
                det.orden = orden;
                CRUD.save(app, det);
                if (orden.sucursal.atencion_automatica) {
                    aprobarOrden(app, orden.id, orden.encargado.id);
                    char movimiento = (orden.tipo == Util.TIPO_ORDEN_VENTA || orden.tipo == Util.TIPO_ORDEN_SALIDA)
                            ? Util.MOVIMIENTO_SALIDA
                            : Util.MOVIMIENTO_ENTRADA;
                    atenderOrdenRapida(app, det, null, null, null, orden.encargado.id, movimiento);
                }
            }
            Update.commitTransaction(app);
            return orden;
        } catch (Exception ex) {
            ex.printStackTrace();
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    private void atenderOrdenRapida(String app, OrdenDet ordenDet, String serie, String lote,
            Date fecha_vencimiento, int personaId, char movimiento) throws Exception {
        System.out.println("atendiendo orden");
        Articulo articulo = new Articulo();
        articulo.activo = true;
        articulo.almacen = ordenDet.orden.almacen_destino;
        articulo.creador = "root";
        articulo.fecha_vencimiento = fecha_vencimiento;
        articulo.lote = lote;
        articulo.serie = serie;
        articulo.producto = ordenDet.producto;
        OrdenArt oart = new OrdenArt();
        oart.activo = true;
        oart.articulo = articulo;
        oart.cantidad = ordenDet.cantidad;
        oart.creador = "root";
        oart.movimiento = movimiento;
        oart.observaciones = "";
        oart.orden = ordenDet.orden;
        atenderOrden(app, ordenDet.orden.id, personaId, oart);
    }

    @Override
    public void atenderArticuloOrdenTransformacion(String app, Orden orden) throws Exception {
        System.out.println("atendiendo orden");
        OrdenArt oart = new OrdenArt();
        oart.activo = true;
        // oart.articulo = articulo;
        oart.cantidad = BigDecimal.ONE;
        oart.creador = "root";
        oart.movimiento = Util.MOVIMIENTO_ENTRADA;
        oart.observaciones = "";
        oart.orden = orden;
        CRUD.save(app, oart);
        List<OrdenDet> dets = listDets(app, orden.id);
        for (OrdenDet det : dets) {
            char movimiento = Util.MOVIMIENTO_SALIDA;
            atenderOrdenRapida(app, det, null, null, null, orden.encargado.id, movimiento);
        }
    }

    @Override
    public Orden updateOrden(String app, Orden orden, List<OrdenDet> detalles, Integer cotizacionId) throws Exception {
        try {
            Update.beginTransaction(app);
            CRUD.update(app, orden);
            if (cotizacionId != null) {
                Cotizacion coti = Services.getCotizacion().get(app, cotizacionId);
                coti.orden = orden;
                CRUD.update(app, coti);
            }
            CRUD.execute(app, "delete from logistica.orden_det where orden = " + orden.id);
            for (OrdenDet det : detalles) {
                det.id = null;
                det.orden = orden;
                CRUD.save(app, det);
            }
            Update.commitTransaction(app);
            return orden;
        } catch (Exception ex) {
            ex.printStackTrace();
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void aprobarOrden(String app, int ordenId, int personaId) throws Exception {
        try {
            Orden orden = getOrden(app, ordenId);
            // if (orden.aprobado_por != null) {
            // throw new Exception("La orden ya ha sido aprobada");
            // }
            orden.aprobado_por = new Persona();
            orden.aprobado_por.id = personaId;
            orden.fecha_aprobacion = new Date();
            CRUD.update(app, orden);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public void atenderOrden(String app, int ordenId, int personaId, OrdenArt oart) throws Exception {
        try {
            Orden orden = getOrden(app, ordenId);
            int almacenOrigenId = orden.almacen_origen.id;
            int almacenDestinoId = orden.almacen_destino.id;
            char tipo = orden.tipo;
            Update.beginTransaction(app);
            orden.atendido_por = new Persona();
            orden.atendido_por.id = personaId;
            orden.fecha_atencion = new Date();
            CRUD.update(app, orden);
            Articulo articulo = null;
            if (oart.movimiento == Util.MOVIMIENTO_ENTRADA) {
                articulo = getArticulo(app, almacenDestinoId, oart.articulo.serie, oart.articulo.lote,
                        oart.articulo.fecha_vencimiento, oart.articulo.producto.id);
                if (articulo == null) {
                    oart.observaciones = oart.observaciones;
                    articulo = new Articulo();
                    articulo.activo = true;
                    articulo.creador = oart.creador;
                    articulo.fecha_vencimiento = oart.articulo.fecha_vencimiento;
                    articulo.lote = oart.articulo.lote;
                    articulo.serie = oart.articulo.serie;
                    articulo.producto = oart.articulo.producto;
                    articulo.almacen = orden.almacen_destino;
                    articulo.stock = oart.cantidad;
                    CRUD.save(app, articulo);
                } else {
                    articulo.stock = articulo.stock.add(oart.cantidad);
                    CRUD.update(app, articulo);
                }
                OrdenArt oart2 = new OrdenArt();
                oart2.activo = true;
                oart2.articulo = articulo;
                oart2.creador = oart.creador;
                oart2.movimiento = Util.MOVIMIENTO_ENTRADA;
                oart2.observaciones = oart.observaciones;
                oart2.orden = oart.orden;
                oart2.cantidad = oart.cantidad;
                CRUD.save(app, oart2);
            } else {
                articulo = getArticulo(app, almacenOrigenId, oart.articulo.serie, oart.articulo.lote,
                        oart.articulo.fecha_vencimiento, oart.articulo.producto.id);
                if (articulo == null) {
                    oart.observaciones = oart.observaciones;
                    articulo = new Articulo();
                    articulo.activo = true;
                    articulo.creador = oart.creador;
                    articulo.fecha_vencimiento = oart.articulo.fecha_vencimiento;
                    articulo.lote = oart.articulo.lote;
                    articulo.serie = oart.articulo.serie;
                    articulo.producto = oart.articulo.producto;
                    articulo.almacen = orden.almacen_origen;
                    articulo.stock = BigDecimal.ZERO;
                    CRUD.save(app, articulo);
                }
                if (!orden.sucursal.empresa.allow_buy_without_stock) {
                    if (articulo.stock.compareTo(oart.cantidad) < 0) {
                        throw new Exception("No hay stock para el producto: " + articulo.producto.nombre);
                    }
                }

                OrdenArt oart2 = new OrdenArt();
                oart2.activo = true;
                oart2.articulo = articulo;
                oart2.creador = oart.creador;
                oart2.movimiento = Util.MOVIMIENTO_SALIDA;
                oart2.observaciones = oart.observaciones;
                oart2.orden = orden;
                oart2.cantidad = oart.cantidad;
                CRUD.save(app, oart2);
                articulo.stock = articulo.stock.subtract(oart2.cantidad);
                CRUD.update(app, articulo);
                return;
            }

            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void anularAtencionOrden(String app, int ordenArtId) throws Exception {
        try {
            Update.beginTransaction(app);
            OrdenArt oart = getOrdenArt(app, ordenArtId);
            if (!oart.activo) {
                throw new Exception("La atencion ya figura anulada");
            }
            oart.activo = false;
            if (oart.movimiento == Util.MOVIMIENTO_ENTRADA) {
                oart.articulo.stock = oart.articulo.stock.subtract(oart.cantidad);
            } else {
                oart.articulo.stock = oart.articulo.stock.add(oart.cantidad);
            }
            CRUD.update(app, oart);
            CRUD.update(app, oart.articulo);
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    // @Override
    // public void atenderOrden(String app, int ordenId, int personaId, String
    // usuario) throws Exception {
    // try {
    // Orden orden = getOrden(app, ordenId);
    // if (orden.atendido_por != null) {
    // throw new Exception("La orden ya ha sido atendida");
    // }
    // Update.beginTransaction(app);
    // List<OrdenDet> detalles = listDets(app, ordenId);
    // for (OrdenDet det : detalles) {
    // createOrdenArtByOrdenDet(app, orden.tipo, orden, det, usuario);
    // }
    // orden.atendido_por = new Persona();
    // orden.atendido_por.id = personaId;
    // orden.fecha_atencion = new Date();
    // CRUD.update(app, orden);
    // Update.commitTransaction(app);
    // } catch (Exception ex) {
    // Update.rollbackTransaction(app);
    // ex.printStackTrace();
    // throw new Exception(ex.getMessage());
    // }

    // }

    // private void createOrdenArtByOrdenDet(String app, char tipo, Orden orden,
    // OrdenDet det, String usuario)
    // throws Exception {
    // int almacenOrigenId = det.orden.almacen_origen.id;
    // int almacenDestinoId = det.orden.almacen_destino.id;
    // if (tipo == Util.TIPO_ORDEN_ENTRADA || tipo == Util.TIPO_ORDEN_COMPRA) {
    // Articulo lastArt = getLastArticulo(app, almacenDestinoId, det.producto.id);
    // if (lastArt == null) {
    // Articulo last = getLastArticulo(app, almacenDestinoId);
    // int lastNumber = last == null ? 0
    // : Integer.parseInt(last.serie.substring(4, last.serie.length()));
    // Articulo art = new Articulo();
    // art.activo = true;
    // art.creador = usuario;
    // art.fecha_vencimiento = null;
    // art.lote = "-";
    // art.serie = "PI" + Util.completeWithZeros(almacenDestinoId + "", 2)
    // + Util.completeWithZeros((lastNumber + 1) + "", 7);
    // art.producto = det.producto;
    // CRUD.save(app, art);
    // lastArt = art;
    // }
    // OrdenArt lastOArt = getLastOrdenArt(app, almacenDestinoId, det.producto.id);
    // OrdenArt oart = new OrdenArt();
    // oart.activo = true;
    // oart.articulo = lastArt;
    // oart.creador = usuario;
    // oart.movimiento = Util.MOVIMIENTO_ENTRADA;
    // oart.observaciones = "";
    // oart.orden = det.orden;
    // oart.cantidad = det.cantidad;
    // oart.stock = oart.stock_anterior.add(oart.cantidad);
    // CRUD.save(app, oart);
    // lastArt.orden_art = oart;
    // CRUD.update(app, lastArt);
    // return;
    // }
    // if (tipo == Util.TIPO_ORDEN_SALIDA || tipo == Util.TIPO_ORDEN_VENTA) {
    // Articulo lastArt = getLastArticulo(app, almacenOrigenId, det.producto.id);
    // if (lastArt == null) {
    // throw new Exception("No hay stock para el producto: " + det.producto.nombre);
    // }
    // OrdenArt lastOArt = getLastOrdenArt(app, almacenOrigenId, det.producto.id);
    // if (lastOArt.stock.compareTo(det.cantidad) < 0) {
    // throw new Exception("No hay stock para el producto: " + det.producto.nombre);
    // }
    // OrdenArt oart = new OrdenArt();
    // oart.activo = true;
    // oart.articulo = lastArt;
    // oart.creador = usuario;
    // oart.movimiento = Util.MOVIMIENTO_SALIDA;
    // oart.observaciones = "";
    // oart.orden = det.orden;
    // oart.stock_anterior = lastOArt.stock;
    // oart.cantidad = det.cantidad;
    // oart.stock = oart.stock_anterior.subtract(oart.cantidad);
    // CRUD.save(app, oart);
    // lastArt.orden_art = oart;
    // CRUD.update(app, lastArt);
    // return;
    // }
    // }

    @Override
    public void anularOrden(String app, int ordenId) throws Exception {
        try {
            Update.beginTransaction(app);
            Orden orden = getOrden(app, ordenId);
            if (!orden.activo) {
                throw new Exception("La orden ya figura anulada");
            }
            if (!orden.sucursal.atencion_automatica) {
                if (orden.atendido_por != null) {
                    throw new Exception("La orden ya figura con atencion(es)");
                }
            }
            CRUD.execute(app, "update logistica.orden set activo = false where id = " + ordenId);
            if (orden.sucursal.atencion_automatica) {
                List<OrdenArt> ordenArts = listOrdenArts(app, ordenId);
                for (OrdenArt oart : ordenArts) {
                    anularAtencionOrden(app, oart.id);
                }
            }
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public String getClienteStringByCoincidence(String app, String txt) {
        String cliente_string = null;
        String where = " where cliente_string ilike '%" + txt + "%' order by id desc limit 1";
        try {
            List<Orden> list = CRUD.list(app, Orden.class, where);
            if (!list.isEmpty()) {
                cliente_string = list.get(0).cliente_string;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente_string;
    }

    @Override
    public Articulo getLastArticulo(String app, int almacenId) {
        String[] req = { "producto", "orden_art" };
        List<Articulo> list = new ArrayList<>();
        String where = " where serie ilike 'PI" + Util.completeWithZeros(almacenId + "", 2) + "%'" +
                " order by a.id desc limit 1";
        try {
            list = CRUD.list(app, Articulo.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Articulo getArticulo(String app, int almacenId, String serie, String lote, Date fechaVencimiento,
            int productoId) {
        String[] req = { "producto" };
        List<Articulo> list = new ArrayList<>();
        String where = "where a.producto =" + productoId;
        if (serie != null) {
            where += " and a.serie = '" + serie + "'";
        } else {
            if (lote != null) {
                where += " and a.lote = '" + lote + "'";
            }
            if (fechaVencimiento != null) {
                where += " and a.fecha_vencimiento = '" + fechaVencimiento.toString() + "'";
            }
            where += " and a.almacen = " + almacenId;
        }
        where += " order by a.creado desc limit 1";
        try {
            list = CRUD.list(app, Articulo.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    public Articulo saveArticulo(String app, Articulo articulo) throws Exception {
        CRUD.save(app, articulo);
        return articulo;
    }

    @Override
    public Articulo getLastArticulo(String app, int almacenId, int productoId) {
        String[] req = { "producto", "orden_art" };
        List<Articulo> list = new ArrayList<>();
        String where = " where serie ilike 'PI" + Util.completeWithZeros(almacenId + "", 2) + "%'" +
                " and a.producto = " + productoId +
                " order by a.id desc limit 1";
        try {
            list = CRUD.list(app, Articulo.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public OrdenArt getLastOrdenArt(String app, int almacenId, int productoId) {
        String[] req = { "orden", "articulo", "articulo.producto" };
        List<OrdenArt> list = new ArrayList<>();
        String where = " where b.almacen_destino = " + almacenId + " and c.producto = " + productoId +
                " order by a.creado desc limit 1";
        try {
            list = CRUD.list(app, OrdenArt.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public OrdenArt getOrdenArt(String app, int ordenArtId) {
        String[] req = { "orden", "articulo", "articulo.producto" };
        List<OrdenArt> list = new ArrayList<>();
        String where = " where a.id = " + ordenArtId + " order by a.creado desc limit 1";
        try {
            list = CRUD.list(app, OrdenArt.class, req, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Articulo> listArticulos(String app, int almacenId, int marcaId, int lineaId,
            String ver, String txt) {
        String[] req = {
                "producto",
                "producto.marca",
                "producto.linea",
                "producto.unidad",
                "producto.unidad_conversion",
                "producto.moneda",
                "almacen"
        };
        List<Articulo> list = new ArrayList<>();
        String filterBuscarPor = " where ( b.codigo ilike '%" + txt + "%'";
        filterBuscarPor += " or  b.nombre ilike '%" + txt + "%'";
        filterBuscarPor += " or ( (b.codigo_barras1 ilike '" + txt + "') " + " or (b.codigo_barras2 ilike 	'" + txt
                + "') " + " ) )";
        String filterMarca = marcaId == -1 ? "" : " and b.marca = " + marcaId;
        String filterLinea = lineaId == -1 ? "" : " and b.linea = " + lineaId;
        String filterVer = ""; 
        if (ver.equals(Util.OCULTAR_ANULADOS)) {
            filterVer = " and b.activo is true ";
        }
        String filter = filterBuscarPor
                + filterMarca
                + filterLinea
                + filterVer
                + " and a.almacen = " + almacenId
                + " order by b.nombre,b.codigo asc";
        try {
            list = CRUD.list(app, Articulo.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Articulo> listArticulosLight(String app, int almacenId) {
        String[] req = {
                "almacen"
        };
        List<Articulo> list = new ArrayList<>();
        String filter = " where a.almacen = " + almacenId
                + " order by a.id asc";
        try {
            list = CRUD.list(app, Articulo.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenDet> listDetallesServiciosByTransformacion(String app, int sucursalId, int numero) throws Exception {
        List<OrdenDet> list = new ArrayList<>();
        List<TransformacionDet> transformacionesDet = Services.getTransformacion().listDetalles(sucursalId, numero);
        for (TransformacionDet det : transformacionesDet) {
            String plantilla = det.plantilla;
            Producto prod = Services.getProducto().getByCodigo(app, det.articulo.serie);
            if (prod == null) {
                prod = new Producto();
                prod.creador = "root";
                prod.activo = true;
                prod.codigo = det.articulo.serie;
                prod.codigo_interno = 0;
                prod.codigo_ubicacion = "-"; 
                prod.nombre = det.getPlantilla();
                prod.descripcion = det.getPlantilla();
                prod.marca = new Marca(0, "OTROS");
                prod.linea = new Linea(0, "OTROS");
                prod.unidad = new Unidad(0, "OTROS");
                prod.unidad_conversion = prod.unidad;
                prod.factor_conversion = BigDecimal.ONE;
                prod.peso = BigDecimal.ZERO;
                prod.impuesto = new Impuesto();
                prod.impuesto.id = 1;
                prod.tipo_control = Util.TIPO_CONTROL_SERVICIO;
                prod.stock_minimo = BigDecimal.ZERO;
                prod.garantia = true;
                prod.moneda = new Moneda();
                prod.moneda.id = Util.MONEDA_SOLES_ID;
                prod.precio = BigDecimal.ZERO;
                prod.costo_ultima_compra = BigDecimal.ZERO;
                prod.codigo_barras1 = "-";
                prod.codigo_barras2 = "-";
                prod.procedencia = 'N';
                prod.cod_dig = "-";
                prod.registro_sanitario = "-";
                CRUD.save(app, prod);
            } else {
                prod.nombre = det.getPlantilla();
                prod.descripcion = det.getPlantilla();
            }
            OrdenDet odet = new OrdenDet();
            odet.activo = true;
            odet.cantidad = BigDecimal.ONE;
            odet.creador = "root";
            odet.id = prod.id;
            odet.observaciones = plantilla + " - Nro llanta:"+det.articulo.serie;
            odet.orden = null;
            odet.precio_unitario = BigDecimal.ZERO;
            odet.producto = prod;
            odet.total = BigDecimal.ZERO;
            odet.unidad = prod.unidad;
            list.add(odet);
        }
        return list;
    }

}
