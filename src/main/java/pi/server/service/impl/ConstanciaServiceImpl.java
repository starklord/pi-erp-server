package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.server.CRUD;
import pi.service.ConstanciaService;
import pi.service.model.parroquia.ConstanciaBautismo;
import pi.service.model.parroquia.ConstanciaConfirmacion;
import pi.service.model.parroquia.ConstanciaMatrimonio;

import pi.service.util.Util;

@WebServlet("pi/ConstanciaService")
public class ConstanciaServiceImpl extends HessianServlet implements ConstanciaService {

    @Override
    public ConstanciaBautismo getConstanciaBautismo(String app, int id) {
        String where = "where id = " + id;
        List<ConstanciaBautismo> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaBautismo.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ConstanciaBautismo saveOrUpdateConstanciaBautismo(String app, boolean save, ConstanciaBautismo constancia)
            throws Exception {
        try {
            if (save) {
                CRUD.save(app, constancia);
            } else {
                CRUD.update(app, constancia);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return constancia;
    }

    @Override
    public List<ConstanciaBautismo> listConstanciasBautismo(String app, String filtroFecha, Date inicio, Date fin, 
    String serie, Integer numero, String bautizado, String padre, String madre) {
        String fechaStr = filtroFecha.equals(Util.PARROQUIA_FECHA_BAUTISMO)?"fecha":"fecha_bautismo";
        String where = "where " +fechaStr+" between '" + inicio.toString() + "' and '" + fin + "' ";
        if(!serie.isEmpty()||numero!=null||!bautizado.isEmpty()||!padre.isEmpty()||!madre.isEmpty()){
            where = "where id is not null";
        }
        
        if (!serie.trim().isEmpty()) {
            where += " and serie ilike '%" + serie + "%' ";
        }
        if (numero != null) {
            where += " and numero = " + numero;
        }
        where += " and nombres_bautizado ilike '%" + bautizado+"%' ";
        where += " and nombres_padre ilike '%" + padre+"%' ";
        where += " and nombres_madre ilike '%" + madre+"%' ";
        where += " order by id desc";
        List<ConstanciaBautismo> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaBautismo.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ConstanciaConfirmacion getConstanciaConfirmacion(String app, int id) {
        String where = "where id = " + id;
        List<ConstanciaConfirmacion> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaConfirmacion.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ConstanciaConfirmacion saveOrUpdateConstanciaConfirmacion(String app, boolean save,
            ConstanciaConfirmacion constancia) throws Exception {
        try {
            if (save) {
                CRUD.save(app, constancia);
            } else {
                CRUD.update(app, constancia);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return constancia;
    }

    @Override
    public List<ConstanciaConfirmacion> listConstanciasConfirmacion(String app, String filtroFecha, Date inicio, Date fin, 
    String serie, Integer numero, String confirmante, String padre, String madre) {
        String fechaStr = filtroFecha.equals(Util.PARROQUIA_FECHA_CONFIRMACION)?"fecha":"fecha_bautismo";

        String where = "where " +fechaStr+" between '" + inicio.toString() + "' and '" + fin + "' ";
        if(!serie.isEmpty()||numero!=null||!confirmante.isEmpty()||!padre.isEmpty()||!madre.isEmpty()){
            where = "where id is not null";
        }
        if (!serie.trim().isEmpty()) {
            where += " and serie ilike '%" + serie + "%' ";
        }
        if (numero != null) {
            where += " and numero = " + numero;
        }
        where += " and nombres_confirmado ilike '%" + confirmante+"%' ";
        where += " and nombres_padre_confirmado ilike '%" + padre+"%' ";
        where += " and nombres_madre_confirmado ilike '%" + madre+"%' ";
        where += " order by id desc";
        List<ConstanciaConfirmacion> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaConfirmacion.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ConstanciaMatrimonio getConstanciaMatrimonio(String app, int id) {
        String where = "where id = " + id;
        List<ConstanciaMatrimonio> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaMatrimonio.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ConstanciaMatrimonio saveOrUpdateConstanciaMatrimonio(String app, boolean save,
            ConstanciaMatrimonio constancia) throws Exception {
        try {
            if (save) {
                CRUD.save(app, constancia);
            } else {
                CRUD.update(app, constancia);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return constancia;
    }

    @Override
    public List<ConstanciaMatrimonio> listConstanciasMatrimonio(String app, String filtroFecha, Date inicio, Date fin, 
    String serie, Integer numero, String esposo, String esposa) {
        String fechaStr = "fecha";

        String where = "where " +fechaStr+" between '" + inicio.toString() + "' and '" + fin + "' ";
        if(!serie.isEmpty()||numero!=null||!esposo.isEmpty()||!esposa.isEmpty()){
            where = "where id is not null";
        }
        if (!serie.trim().isEmpty()) {
            where += " and serie ilike '%" + serie + "%' ";
        }
        if (numero != null) {
            where += " and numero = " + numero;
        }
        where += " and nombres_esposo ilike '%" + esposo+"%' ";
        where += " and nombres_esposa ilike '%" + esposa+"%' ";
        where += " order by id desc";
        List<ConstanciaMatrimonio> list = new ArrayList<>();
        try {
            list = CRUD.list(app, ConstanciaMatrimonio.class, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
