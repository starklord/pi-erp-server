package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;

@TableDB(name = "venta.orden_venta_det")
public class OrdenVentaDet implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public OrdenVenta orden_venta;
    public Producto producto;
    public BigDecimal cantidad;
    public BigDecimal precio_unitario;
    public BigDecimal total;
    public Almacen almacen;
    public BigDecimal descuento;
    public BigDecimal costo_unitario;
    public Unidad unidad;
    public String observaciones;
	
	@Override
	public String toString() {
		return id.toString();
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrdenVentaDet other = (OrdenVentaDet) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public String getCreador() {
        return creador;
    }

    public OrdenVenta getOrden_venta() {
        return orden_venta;
    }
    public Date getFecha() {
        return orden_venta.documento_fecha;
    }
    public String getDocumento() {
        return orden_venta.documento_serie + "-" + orden_venta.documento_numero;
    }
    public String getNombres() {
        return orden_venta.direccion_cliente.persona.toString();
    }

    public Producto getProducto() {
        return producto;
    }
    public String getDescripcion() {
        return producto.nombre;
    }
    public BigDecimal getTotal() {
        return Numbers.getBigDecimal(total, 2);
    }

    public String getCodigo() {
        return producto.codigo;
    }

    public String getObservaciones() {
        return observaciones==null?"-":observaciones;
    }

    public BigDecimal getCantidad(){
        return Numbers.getBigDecimal(cantidad, 2);
    }

    public BigDecimal getPrecioUnitario(){
        return Numbers.getBigDecimal(precio_unitario, 2);
    }
    
    

}
