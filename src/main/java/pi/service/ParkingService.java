package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.parking.Ticket;

public interface ParkingService {

    public void save(String app, Ticket ticket) throws Exception;

    
    public Ticket getById(String app, int ticketId);

    public Ticket getLast(String app, String serie);

    public List<Ticket> list(String app, Date inicio, Date fin, String placa, String serie, Integer numero);

    
}
