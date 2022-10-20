package pi.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pi.service.factory.Services;
import pi.service.model.Meta;
import pi.service.model.empresa.Empresa;

@Path("/pi/login")
public class LoginRest {

    @GET
    public String hello() {
        return "Service login online..."; 
    } 

    @GET
    @Path("/login") 
    @Produces(MediaType.APPLICATION_JSON)
    public Meta get(@QueryParam("app") String app,
    @QueryParam("user") String user,
    @QueryParam("pass") String pass,
    @QueryParam("empresaId") int empresaId,
    @QueryParam("sucursalId") int sucursalId
    ) throws Exception{
        return Services.getLogin().login(app, user, pass, empresaId,sucursalId);
    }   

    @PUT
    @Path("/save")
    public Empresa save(Empresa empresa, @QueryParam("app") String app){
        System.out.println("entrando al servicio");
        empresa = new Empresa();
        empresa.id = 1000;
        empresa.activo = true;
        empresa.allow_buy_without_stock =false;
        empresa.app_name = "hola";
        empresa.commercial_name = empresa.commercial_name+"modificadito denuevo :D";
        empresa.app_name = app+"empresa 2 app name :D :D :D";
        return empresa;
    }
}
