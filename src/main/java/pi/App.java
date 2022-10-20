package pi;

import java.util.TimeZone;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import pi.server.Server;
import pi.service.util.db.CConexion;

@QuarkusMain
public class App {

    public static void main(String... args) {
        try{
            initDB();
        }catch(Exception ex){
            System.out.println("no se pudo cargar los datos iniciales..."); 
            System.out.println(ex.getMessage());
        }
        // Quarkus.run(MyApp.class, args);
        Quarkus.run(args);
    }
    
    // public static class MyApp implements QuarkusApplication {

    //     @Override
    //     public int run(String... args) throws Exception {
    //         System.out.println("Do startup logic here");
    //         Quarkus.waitForExit();
    //         return 0;
    //     }
    // }

    private static void initDB() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        System.out.println("inicializando datos de la base de datos...");
        Server.DB_PWD = "evadb7007";
        Server.DB_DRIVER = "org.postgresql.Driver";
        Server.DB_USR = "postgres";
        Server.IP_SERVER = "154.53.32.33";
        Server.DB_PORT = 7077;
        CConexion.strDriver = Server.DB_DRIVER;
        CConexion.strPwd = Server.DB_PWD;
        CConexion.strUsr = Server.DB_USR;
        CConexion.IP_SERVER = Server.IP_SERVER;
        CConexion.port = Server.DB_PORT;
        System.out.println("datos de la base de datos cargados exitosamente");
    }

}
