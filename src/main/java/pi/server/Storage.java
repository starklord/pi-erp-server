package pi.server;

import java.nio.file.Paths;

import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

public class Storage {

    public static EmbeddedStorageManager STORAGE;
    public static DB DB;
    
    public static final String PATH = "D:/pi/spring";



    public static void initStorage() {
        System.out.println("Creating storage...");
        Storage.STORAGE = EmbeddedStorage.start(DB,Paths.get(PATH));
        System.out.println("storage: " + STORAGE.root());
        STORAGE.storeRoot();
       
    }

    public static void stopStorage(){
        STORAGE.close();
    }

    

    
    
}
