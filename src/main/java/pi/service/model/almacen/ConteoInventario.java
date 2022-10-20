package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.factory.Numbers;
import pi.service.util.Util;
import pi.service.util.db.client.TableDB;

@TableDB(name="almacen.conteo_inventario")
public class ConteoInventario implements Serializable{

    public Integer id;
    public String creador;
    public Boolean activo;
    public Producto producto;
    public String lote;
    public Date fecha_vencimiento;
    public BigDecimal cantidad;
    public String observaciones;

    @Override
    public String toString() {
        return producto.codigo+"-"+lote+"-";
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
        ConteoInventario other = (ConteoInventario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getUsuario(){
        return creador;
    }

    public String getFechaVencimiento(){
        return Util.formatDate(fecha_vencimiento, Util.SDF_DATE);
    }

    public String getLote(){
        return lote;
    }

    public String getCantidadStr(){
        return Numbers.getBD(cantidad, 2).toString();
    }

    public String getLaboratorio(){
        return producto.marca.nombre;
    }

    public String getProductoStr(){
        return producto.codigo+" - "+ producto.nombre;
    }

    public String getObservaciones(){
        return observaciones;
    }
    

}
