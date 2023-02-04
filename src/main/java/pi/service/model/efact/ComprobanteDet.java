package pi.service.model.efact;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;
import pi.service.model.almacen.Unidad;

@TableDB(name = "efact.comprobante_det")
public class ComprobanteDet implements Serializable {

    public Integer              id;
    public String               creador;
    public Boolean              activo;
    public Comprobante        comprobante;
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComprobanteDet other = (ComprobanteDet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }



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

    public String getCreador() {
        return creador;
    }

    public Producto getProducto() {
        return producto;
    }
    public String getDescripcion() {
        return producto.nombre;
    }
    
    

}
