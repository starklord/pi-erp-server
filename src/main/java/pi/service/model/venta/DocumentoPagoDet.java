package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;
import pi.service.util.db.client.TableDB;

@TableDB(name = "venta.documento_pago_det")
public class DocumentoPagoDet implements Serializable {

    public Integer              id;
    public String               creador;
    public Boolean              activo;
    public DocumentoPago        documento_pago;
    public Producto             producto;
    public Unidad               unidad;
    public BigDecimal           cantidad;
    public BigDecimal           precio_unitario;
    public BigDecimal           total;
    public BigDecimal           descuento;
    public String               observaciones;
	
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

    public Producto getProducto() {
        return producto;
    }
    public String getDescripcion() {
        return producto.nombre;
    }

    public BigDecimal getTotal() {
        return Numbers.getBigDecimal(total, 2);
    }
    
    

}
