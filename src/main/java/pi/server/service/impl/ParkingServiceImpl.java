package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.service.ParkingService;
import pi.server.db.server.CRUD;
import pi.service.model.parking.Ticket;

@WebServlet("pi/ParkingService")
public class ParkingServiceImpl extends HessianServlet implements ParkingService {

    @Override
    public void save(String app, Ticket ticket) throws Exception {
        CRUD.save(app,ticket);
    }

    @Override
    public Ticket getById(String app, int ticketId) {
        try{
            List<Ticket> list = CRUD.list(app,Ticket.class,"where id = "+ ticketId + " order by id desc limit 1");
            return list.isEmpty()?null:list.get(0);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Ticket getLast(String app, String serie) {
        try{
            List<Ticket> list = CRUD.list(app,Ticket.class,"where serie = '"+ serie + "' order by id desc limit 1");
            return list.isEmpty()?null:list.get(0);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ticket> list(String app, Date inicio, Date fin, String placa, String serie, Integer numero) {
        List<Ticket> list = new ArrayList<>();
        try{
            String where = "where hora_inicio between '"+inicio.toString()+"' and '"+fin.toString()+
            "' and placa ilike '%"+placa+"%'" +
            " and serie ilike '%"+serie +"%'";
            if(numero!=null){
                where+= " and numero = " + numero;
            }
            where+=" order by id desc";
            list = CRUD.list(app,Ticket.class,where);
            return list;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    
}
