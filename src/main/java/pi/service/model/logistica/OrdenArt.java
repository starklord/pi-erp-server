package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Articulo;
import pi.service.util.Util;

@TableDB(name = "logistica.orden_art")
public class OrdenArt implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Orden orden;
    public Articulo articulo;
    public Character movimiento;
    public String observaciones;
    public BigDecimal cantidad;

    public String getEstado(){
        return activo?Util.OK:Util.ANULADO;
    }

    public String getCodigo(){
        return articulo.producto.codigo;
    }

    public String getNombre(){
        return articulo.producto.nombre;
    }

    public String getFechaStr(){
        return orden.getFechaStr();
    }

    public String getOrdenStr(){
        return orden.getNumeroStr();
    }

    public String getSerie(){
        return articulo.serie;
    }

    public char getMovimiento(){
        return movimiento;
    }

    public BigDecimal getCantidad(){
        return Numbers.getBD(cantidad, 2);
    }

    public String getCantidadStr(){
        char prefix = movimiento==Util.MOVIMIENTO_ENTRADA?'+':'-';
        return prefix + Numbers.getBD(cantidad, 2).toString();
    }

    public String getLote(){
        return articulo.lote==null?"":articulo.lote;
    }

    public String getFechaVencimiento(){
        return articulo.fecha_vencimiento==null?"":Util.formatDate(articulo.fecha_vencimiento, Util.SDF_DATE);
    }

    public String getObservaciones(){
        return observaciones;
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
        OrdenArt other = (OrdenArt) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }    
}
