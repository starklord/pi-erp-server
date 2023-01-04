package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.Update;
import pi.server.db.server.CRUD;
import pi.service.CotizacionService;
import pi.service.model.logistica.Cotizacion;
import pi.service.model.logistica.CotizacionDet;

@WebServlet("pi/CotizacionService")
public class CotizacionServiceImpl extends HessianServlet implements CotizacionService {

    @Override
    public Cotizacion get(String app, int cotizacionId) {
        List<Cotizacion> list = new ArrayList<>();
        String[] require = {
                "cliente",
                "moneda",
                "orden"
        };
        String where = "where a.id = " + cotizacionId + " order by numero desc limit 1";
        try {
            list = CRUD.list(app, Cotizacion.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Cotizacion getLast(String app, int sucursalId) {
        List<Cotizacion> list = new ArrayList<>();
        String[] require = {
                "cliente",
                "moneda",
                "orden"
        };
        String where = "where a.sucursal = " + sucursalId + " order by numero desc limit 1";
        try {
            list = CRUD.list(app, Cotizacion.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Cotizacion> list(String app, Date inicio, Date fin, int sucursalId) {
        List<Cotizacion> list = new ArrayList<>();
        String[] require = {
                "cliente",
                "moneda",
                "orden",
                "sucursal"
        };
        String where = "where a.fecha between '" + inicio.toString() + "' and '" + fin.toString() + "'";
        if (sucursalId != -1) {
            where += " and a.sucursal = " + sucursalId;
        }
        where += " order by a.id desc";
        try {
            list = CRUD.list(app, Cotizacion.class, require, where);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<CotizacionDet> listDets(String app, int cotizacionId) {
        List<CotizacionDet> list = new ArrayList<>();
        String[] require = {
                "producto",
                "cotizacion",
                "unidad"
        };
        String where = " where a.cotizacion = " + cotizacionId;
        try {
            list = CRUD.list(app, CotizacionDet.class, require, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    

    @Override
    public Cotizacion save(String app, Cotizacion cotizacion,
            List<CotizacionDet> detalles)
            throws Exception {
        try {
            Update.beginTransaction(app);
            Cotizacion ordenLast = getLast(app, cotizacion.sucursal.id);
            int numero = ordenLast == null ? 1 : (ordenLast.numero + 1);
            cotizacion.numero = numero;
            CRUD.save(app, cotizacion);
            for (CotizacionDet det : detalles) {
                det.id = null;
                det.cotizacion = cotizacion;
                CRUD.save(app, det);
            }
            Update.commitTransaction(app);
            return cotizacion;
        } catch (Exception ex) {
            ex.printStackTrace();
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public Cotizacion update(String app, Cotizacion cotizacion,
            List<CotizacionDet> detalles)
            throws Exception {
        try {
            Update.beginTransaction(app);
            CRUD.update(app, cotizacion);
            CRUD.execute(app, "delete from logistica.cotizacion_det where cotizacion = " + cotizacion.id);
            for (CotizacionDet det : detalles) {
                det.id = null;
                det.cotizacion = cotizacion;
                CRUD.save(app, det);
            }
            Update.commitTransaction(app);
            return cotizacion;
        } catch (Exception ex) {
            ex.printStackTrace();
            Update.rollbackTransaction(app);
            throw new Exception(ex.getMessage());
        }
    }

}
