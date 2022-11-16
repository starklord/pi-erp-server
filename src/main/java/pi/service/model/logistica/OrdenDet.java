package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;
import pi.service.util.Util;

@TableDB(name = "logistica.orden_det")
public class OrdenDet implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Orden orden;
    public Producto producto;
    public BigDecimal cantidad;
    public BigDecimal precio_unitario;
    public BigDecimal total;
    public Unidad unidad;
    public String observaciones;

    public String getCodigo(){
        return producto.codigo;
    }
    
    public String getNombre(){
        return producto.nombre;
    }

    public String getControl(){
        return producto.getControl();
    }

    public BigDecimal getCantidad(){
        return producto.tipo_control==Util.TIPO_CONTROL_PRODUCTO?Numbers.getBD(cantidad,2):Numbers.getBD(cantidad, 0);
    }

    public BigDecimal getPrecioUnitario(){
        return Numbers.getBD(precio_unitario,2);
    }

    public BigDecimal getTotal(){
        return Numbers.getBD(total,2);
    }

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
        OrdenDet other = (OrdenDet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
