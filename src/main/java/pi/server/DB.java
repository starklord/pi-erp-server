package pi.server;
import java.util.ArrayList;
import java.util.List;

import one.microstream.reference.Lazy;
import pi.service.model.logistica.Transformacion;


public class DB {
    
    private Lazy<List<Transformacion>> transformaciones = Lazy.Reference(new ArrayList<>());
    private Integer transformacionNumero;

    public List<Transformacion> getTransformaciones() {
        return Lazy.get(transformaciones);
    }

    public void setTransformaciones(Lazy<List<Transformacion>> transformaciones) {
        this.transformaciones = transformaciones;
    }

    public Integer getTransformacionNumero() {
        return transformacionNumero;
    }

    public void setTransformacionNumero(Integer transformacionNumero) {
        this.transformacionNumero = transformacionNumero;
    }
}
