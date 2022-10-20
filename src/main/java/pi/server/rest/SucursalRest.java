package pi.server.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import pi.service.factory.Services;
import pi.service.model.empresa.Sucursal;

@Path("/pi/sucursal")
public class SucursalRest {

    @GET
    @Path("/listActive")
    public List<Sucursal> listActive(@QueryParam("app") String app) {
        return Services.getSucursal().listActive(app);
    }
    
}
