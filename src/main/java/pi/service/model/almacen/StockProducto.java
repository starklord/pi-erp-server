package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.util.db.client.TableDB;


@TableDB(name="logistica.stock_producto")
public class StockProducto implements Serializable {
	
	public Integer id;
	public Boolean activo;
	public String creador;
	public Almacen almacen;
	public Producto producto;
	public BigDecimal stock;
	
	@Override
	public String toString() {
		return producto.codigo + "["+stock+"]";
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
		StockProducto other = (StockProducto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
