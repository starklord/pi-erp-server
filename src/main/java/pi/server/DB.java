package pi.server;
import java.util.List;

import pi.service.model.logistica.Transformacion;


public class DB {
    
    private List<Transformacion> transformaciones;
    private Integer transformacionNumero;

    public List<Transformacion> getTransformaciones() {
        return transformaciones;
    }

    public void setTransformaciones(List<Transformacion> transformaciones) {
        this.transformaciones = transformaciones;
    }

    public Integer getTransformacionNumero() {
        return transformacionNumero;
    }

    public void setTransformacionNumero(Integer transformacionNumero) {
        this.transformacionNumero = transformacionNumero;
    }
}
