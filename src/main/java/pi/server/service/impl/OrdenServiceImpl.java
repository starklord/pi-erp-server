package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.Update;
import pi.server.db.server.CRUD;
import pi.service.OrdenService;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;

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
    public List<Orden> list(String app, Date inicio, Date fin, int sucursalId) {
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
        String where = "where a.fecha between '" + inicio.toString() + "' and '" + fin.toString()+"'";
        if (sucursalId != -1) {
            where += " and a.sucursal = " + sucursalId;
        }
        where+= " order by a.id desc";
        try {
            list = CRUD.list(app, Orden.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<OrdenDet> listDetalles(String app, int ordenId) {
        List<OrdenDet> list = new ArrayList<>();
        String[] require ={
            "producto",
            "orden",
            "unidad"
        };
        String where = " where a.orden= " + ordenId;
        try {
            list = CRUD.list(app, OrdenDet.class,require,where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrdenArt> listArticulos(String app, int ordenId) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void aprobarOrden(String app, Orden orden, int personaId) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void atenderOrden(String app, Orden orden, int personaId, List<OrdenArt> ordenArticulos) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void anularOrden(String app, int ordenId) throws Exception {
        Orden orden = getOrden(app, ordenId);
        if(!orden.activo){
            throw new Exception("La orden ya figura anulada");
        }
        CRUD.execute(app, "update logistica.orden set activo = false where id = " + ordenId);
    }

    @Override
    public String getClienteStringByCoincidence(String app, String txt) {
        String cliente_string = null;
        String where = " where cliente_string ilike '%"+txt+"%' order by id desc limit 1";
        try {
            List<Orden> list = CRUD.list(app,Orden.class,where);
            if(!list.isEmpty()){
                cliente_string = list.get(0).cliente_string;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente_string;
    }

}
