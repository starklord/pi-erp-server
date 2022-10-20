package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;


@TableDB(name="venta.proforma_det")
public class ProformaDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Proforma proforma;
	public Producto producto;
	public Almacen almacen;
	public BigDecimal cantidad;
	public BigDecimal precio_unitario;
	public BigDecimal total;
	
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
		ProformaDet other = (ProformaDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
