package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.empresa.Empresa;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.transformacion")
public class Transformacion implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Integer numero;
	public Date fecha;
	public Producto producto;
	public Empresa empresa;
	public String observaciones;
	public Almacen almacen;
	public BigDecimal cantidad;
	public Ensamblador ensamblador;
	public BigDecimal costo_unitario;
	public BigDecimal costo_total;
	
	@Override
	public String toString() {
		
		return numero+"";
		
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
		Transformacion other = (Transformacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
