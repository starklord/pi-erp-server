package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.transformacion_det")
public class TransformacionDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Transformacion transformacion;
	public Producto producto;
	public Almacen almacen;
	public BigDecimal cantidad;
	
	@Override
	public String toString() {
		return producto.toString();
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
		TransformacionDet other = (TransformacionDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
