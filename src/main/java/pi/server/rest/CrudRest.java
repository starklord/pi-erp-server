package pi.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pi.service.factory.Services;
import pi.service.model.PiQuery;

@Path("/pi/crud") 
public class CrudRest {

    @GET
    public String hello() { 
        return "hello :D"; 
    }

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON) 
    public String get(PiQuery query) {
        System.out.println("PiQuery: " + query.toString());
        return Services.getCrud().get(query.app_name, query.query); 
    } 

    @POST
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON) 
    public String listJson(PiQuery query) {
        System.out.println("PiQuery: " + query.toString());
        return Services.getCrud().list(query.app_name, query.query); 
    }
 
    @POST
    @Path("/getjson")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(PiQuery query) {
        System.out.println("PiQuery: " + query.toString());
        return Services.getCrud().getJson(query.app_name, query.query); 
    } 

    @POST 
    @Path("/listjson") 
    @Produces(MediaType.APPLICATION_JSON)
    public String list(PiQuery query) {
        System.out.println("PiQuery list: " + query.toString());  
        return Services.getCrud().listJson(query.app_name, query.query);  
    }
}
