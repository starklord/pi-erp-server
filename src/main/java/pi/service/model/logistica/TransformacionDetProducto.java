package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.almacen.Producto;

public class TransformacionDetProducto implements Serializable {

    public Date creado = new Date();
	public String creador = "root";
	public Boolean activo = true;
    public TransformacionDet transformacionDet;
    public Producto producto;
    public BigDecimal cantidad;
    public BigDecimal precioUnitario;
    public BigDecimal total;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((transformacionDet == null) ? 0 : transformacionDet.hashCode());
        result = prime * result + ((producto == null) ? 0 : producto.hashCode());
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
        TransformacionDetProducto other = (TransformacionDetProducto) obj;
        if (transformacionDet == null) {
            if (other.transformacionDet != null)
                return false;
        } else if (!transformacionDet.equals(other.transformacionDet))
            return false;
        if (producto == null) {
            if (other.producto != null)
                return false;
        } else if (!producto.equals(other.producto))
            return false;
        return true;
    }
    

}
