package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.model.almacen.Articulo;

@TableDB(name = "logistica.orden_arg")
public class OrdenArt implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Orden orden;
    public Articulo articulo;
    public Character movimiento;
    public BigDecimal stock;
    public String observaciones;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrdenArt other = (OrdenArt) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    

    
    
}
