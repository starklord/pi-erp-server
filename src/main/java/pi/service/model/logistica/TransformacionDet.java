package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pi.service.model.almacen.Articulo;

public class TransformacionDet implements Serializable {

    public Date creado = new Date();
	public String creador = "root";
	public Boolean activo = true;
    public Transformacion transformacion;
    public Articulo articulo;
    public String plantilla = "-";
    public BigDecimal costo = BigDecimal.ZERO;
    public List<TransformacionDetProducto> detalle = new ArrayList<>();

    public String getProductoStr(){
        return articulo.producto.nombre;
    }

    public String getSerie(){
        return articulo.serie;
    }

    public String getPlantilla(){
        return plantilla;
    }

    public BigDecimal getCosto(){
        return costo;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transformacion == null) ? 0 : transformacion.hashCode());
        result = prime * result + ((articulo == null) ? 0 : articulo.hashCode());
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
        TransformacionDet other = (TransformacionDet) obj;
        if (transformacion == null) {
            if (other.transformacion != null)
                return false;
        } else if (!transformacion.equals(other.transformacion))
            return false;
        if (articulo == null) {
            if (other.articulo != null)
                return false;
        } else if (!articulo.equals(other.articulo))
            return false;
        return true;
    }
    

    

    
}
