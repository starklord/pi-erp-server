package pi.service.model;

import java.io.Serializable;

public class PiQuery implements Serializable {

    public String app_name;
    public String query;

    public PiQuery(){

    }

    @Override
    public String toString() {
        return "app_name: " + app_name + " query: " + query;
    }
    
}
