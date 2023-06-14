package pi;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TimeZone;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import pi.server.Server;
import pi.server.DB;
import pi.server.db.CConexion;
import pi.service.util.Util;

@QuarkusMain
public class App {
    private static EmbeddedStorageManager STORAGE;
    // private static StorageRestService STORAGE_REST_SERVICE;
    private static DB DB;

    public static void main(String... args) {
        try{ 
            initDBSql();
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

    private static void initDBSql() throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        System.out.println("inicializando datos de la base de datos...");
        Server.DB_USR = "postgres";
        Server.DB_PWD = "evadb7007";
        Server.DB_DRIVER = "org.postgresql.Driver";
        // Server.IP_SERVER = Util.EVAS[5];
        Server.IP_SERVER = "localhost";
        Server.DB_PORT = 7077;
        CConexion.strDriver = Server.DB_DRIVER;
        CConexion.strPwd = Server.DB_PWD;
        CConexion.strUsr = Server.DB_USR;
        CConexion.IP_SERVER = Server.IP_SERVER;
        CConexion.port = Server.DB_PORT;
        System.out.println("datos de la base de datos cargados exitosamente");
        
    }

    public static void initStorage() { 
        System.out.println("initializing storage...");
        App.STORAGE = EmbeddedStorage.Foundation(Paths.get(Server.PATH_STORAGE))
                .onConnectionFoundation(cf -> cf.setClassLoaderProvider(ClassLoaderProvider.New(
                        Thread.currentThread().getContextClassLoader())))
                .start(); 
        if (STORAGE.root() == null) {
            STORAGE.setRoot(new DB());   
        } 
        App.DB = (DB)STORAGE.root();
        App.initDB();
        
        System.out.println("storage started successfuly...");
    }

    public static void initStorageRestService(){
        // App.STORAGE_REST_SERVICE = StorageRestServiceResolver.resolve(App.STORAGE);
        // App.STORAGE_REST_SERVICE.start();;
    }

    public static DB getDB() {
        return DB;
    }

    public static void store(Object object){
        App.STORAGE.store(object);
    }

    public static void storeAll(Object... objects){
        App.STORAGE.storeAll(objects);
    }

    public static void storeRoot(){
        App.STORAGE.storeRoot();
    }

    public static void shutDownStorage(){
        // App.STORAGE_REST_SERVICE.stop();
        App.STORAGE.shutdown();
    }

    private static void initDB(){
        if(App.DB.getTransformacionNumero()==null){
            App.DB.setTransformacionNumero(0);
            App.DB.setTransformaciones(new ArrayList<>());
            
        }
        App.storeRoot();
    }

}
