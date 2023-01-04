package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;

@TableDB(name = "logistica.cotizacion_det")
public class CotizacionDet implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Cotizacion cotizacion;
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
        return Numbers.getBD(cantidad, 2);
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
        CotizacionDet other = (CotizacionDet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
