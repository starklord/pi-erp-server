package pi.server.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.Update;
import pi.server.db.server.CRUD;
import pi.service.OrdenService;
import pi.service.model.almacen.Articulo;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;
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
                "almacen_destino"
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
                "almacen_destino"
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrdenArt> listOrdenArtsByAlmacenProducto(String app, int almacenId, int productoId) {
        List<OrdenArt> list = new ArrayList<>();
        String[] require = {
                "orden",
                "articulo",
                "articulo.producto"
        };
        String where = " where (b.almacen_origen = " + almacenId + " or b.almacen_destino = " + almacenId;
        where += ") and c.producto = " + productoId;
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
    public Orden saveOrden(String app, Orden orden, List<OrdenDet> detalles) throws Exception {
        try {
            Update.beginTransaction(app);
            Orden ordenLast = getLastOrden(app, orden.sucursal.id, orden.tipo);
            int numero = ordenLast == null ? 1 : (ordenLast.numero + 1);
            orden.numero = numero;
            CRUD.save(app, orden);
            for (OrdenDet det : detalles) {
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
    public Orden updateOrden(String app, Orden orden, List<OrdenDet> detalles) throws Exception {
        try {
            Update.beginTransaction(app);
            CRUD.update(app, orden);
            CRUD.execute(app, "delete from logistica.orden_det where orden = " + orden.id);
            for (OrdenDet det : detalles) {
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
            if (orden.aprobado_por != null) {
                throw new Exception("La orden ya ha sido aprobada");
            }
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
    public void atenderOrden(String app, int ordenId, int personaId, String usuario) throws Exception {
        try {
            Orden orden = getOrden(app, ordenId);
            if (orden.atendido_por != null) {
                throw new Exception("La orden ya ha sido atendida");
            }
            Update.beginTransaction(app);
            List<OrdenDet> detalles = listDets(app, ordenId);
            for (OrdenDet det : detalles) {
                createOrdenArtByOrdenDet(app, orden.tipo,orden, det, usuario);
            }
            orden.atendido_por = new Persona();
            orden.atendido_por.id = personaId;
            orden.fecha_atencion = new Date();
            CRUD.update(app, orden);
            Update.commitTransaction(app);
        } catch (Exception ex) {
            Update.rollbackTransaction(app);
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    private void createOrdenArtByOrdenDet(String app, char tipo,Orden orden, OrdenDet det, String usuario) throws Exception {
        int almacenOrigenId = det.orden.almacen_origen.id;
        int almacenDestinoId = det.orden.almacen_destino.id;
        if (tipo == Util.TIPO_ORDEN_ENTRADA||tipo==Util.TIPO_ORDEN_COMPRA) {
            Articulo lastArt = getLastArticulo(app, almacenDestinoId, det.producto.id);
            if (lastArt == null) {
                Articulo last = getLastArticulo(app, almacenDestinoId);
                int lastNumber = last == null ? 0
                        : Integer.parseInt(last.serie.substring(4, last.serie.length()));
                Articulo art = new Articulo();
                art.activo = true;
                art.creador = usuario;
                art.fecha_vencimiento = null;
                art.lote = "-";
                art.serie = "PI" + Util.completeWithZeros(almacenDestinoId + "", 2)
                        + Util.completeWithZeros((lastNumber + 1) + "", 7);
                art.orden_art = null;
                art.producto = det.producto;
                CRUD.save(app, art);
                lastArt = art;
            }
            OrdenArt lastOArt = getLastOrdenArt(app, almacenDestinoId, det.producto.id);
            OrdenArt oart = new OrdenArt();
            oart.activo = true;
            oart.articulo = lastArt;
            oart.creador = usuario;
            oart.movimiento = Util.MOVIMIENTO_ENTRADA;
            oart.observaciones = ""; 
            oart.orden = det.orden;
            oart.stock_anterior = lastOArt == null ? BigDecimal.ZERO : lastOArt.stock;
            oart.cantidad = det.cantidad;
            oart.stock = oart.stock_anterior.add(oart.cantidad);
            CRUD.save(app, oart);
            lastArt.orden_art = oart;
            CRUD.update(app, lastArt);
            return;
        }
        if (tipo == Util.TIPO_ORDEN_SALIDA||tipo==Util.TIPO_ORDEN_VENTA) {
            Articulo lastArt = getLastArticulo(app, almacenOrigenId, det.producto.id);
            if (lastArt == null) {
                throw new Exception("No hay stock para el producto: " + det.producto.nombre);
            }
            OrdenArt lastOArt = getLastOrdenArt(app, almacenOrigenId, det.producto.id);
            if (lastOArt.stock.compareTo(det.cantidad) < 0) {
                throw new Exception("No hay stock para el producto: " + det.producto.nombre);
            }
            OrdenArt oart = new OrdenArt();
            oart.activo = true;
            oart.articulo = lastArt;
            oart.creador = usuario;
            oart.movimiento = Util.MOVIMIENTO_SALIDA;
            oart.observaciones = "";
            oart.orden = det.orden;
            oart.stock_anterior = lastOArt.stock;
            oart.cantidad = det.cantidad;
            oart.stock = oart.stock_anterior.subtract(oart.cantidad);
            CRUD.save(app, oart);
            lastArt.orden_art = oart;
            CRUD.update(app, lastArt);
            return;
        }
    }

    @Override
    public void anularOrden(String app, int ordenId) throws Exception {
        Orden orden = getOrden(app, ordenId);
        if (!orden.activo) {
            throw new Exception("La orden ya figura anulada");
        }
        CRUD.execute(app, "update logistica.orden set activo = false where id = " + ordenId);
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
    public List<Articulo> listArticulos(String app, int almacenId, int marcaId, int lineaId,
            String ver, String txt) {
        String[] req = {
                "producto",
                "producto.marca",
                "producto.linea",
                "producto.unidad",
                "producto.unidad_conversion",
                "producto.moneda",
                "orden_art",
                "orden_art.orden" };
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
                + " and (i.almacen_origen = " + almacenId + " or i.almacen_destino = "+almacenId+") order by b.nombre,b.codigo asc";
        try {
            list = CRUD.list(app, Articulo.class, req, filter); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Articulo> listArticulos(String app, int almacenId) {
        // TODO Auto-generated method stub
        return null;
    }

}
