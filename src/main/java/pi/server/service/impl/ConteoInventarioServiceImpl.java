package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.server.CRUD;
import pi.service.ConteoInventarioService;
import pi.service.model.almacen.ConteoInventario;

@WebServlet("pi/ConteoInventarioService")
public class ConteoInventarioServiceImpl extends HessianServlet implements ConteoInventarioService{

    @Override
    public ConteoInventario save(String app, ConteoInventario entity) throws Exception {
        CRUD.save(app, entity);
        return entity;
    }

    @Override
    public void delete(String app, ConteoInventario entity) throws Exception {
        CRUD.delete(app, entity);
    }

    @Override
    public ConteoInventario update(String app, ConteoInventario entity) throws Exception {
        CRUD.update(app, entity);
        return entity;
    }

    @Override
    public List<ConteoInventario> list(String app, String usuario, int laboratorioId, String lote) {
        List<ConteoInventario> list = new ArrayList<>();
        try{
            String[] require = {
                "producto","producto.marca","producto.linea","producto.unidad",
            };
            String where = "where a.id is not null";
            if(usuario!=null){
                where+= " and a.creador = '"+usuario+"' ";
            }
            if(laboratorioId!=-1){
                where+= " and b.linea = " + laboratorioId;
            }
            where+= " and a.lote ilike '%"+lote+"%'";
            list = CRUD.list(app, ConteoInventario.class,require,where);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    
}
