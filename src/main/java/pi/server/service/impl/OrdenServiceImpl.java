package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.server.CRUD;
import pi.service.OrdenService;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenArt;
import pi.service.model.logistica.OrdenDet;

@WebServlet("pi/OrdenService")
public class OrdenServiceImpl extends HessianServlet implements OrdenService {

    @Override
    public List<Orden> list(String app,int sucursalId) {
        List<Orden> list = new ArrayList<>();
        try{
            list = CRUD.list(getServletName(), getClass())
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return list;
    }

    @Override
    public List<OrdenDet> listDetalles(String app,int ordenId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrdenArt> listArticulos(String app,int ordenId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Orden saveOrden(String app,Orden orden, List<OrdenDet> detalles) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Orden updateOrden(String app,Orden orden, List<OrdenDet> detalles) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void aprobarOrden(String app,Orden orden, int personaId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void atenderOrden(String app,Orden orden, int personaId, List<OrdenArt> ordenArticulos) {
        // TODO Auto-generated method stub
        
    }
    
}
