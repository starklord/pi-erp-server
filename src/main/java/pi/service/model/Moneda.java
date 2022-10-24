package pi.service.model;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.model.rrhh.Empleado;


@TableDB(name="public.moneda")
public class Moneda implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public String abreviatura;
	public String simbolo;
	public Character inicial;
	public Boolean nacional;
	
	@Override
	public String toString() {
		return nombre;
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
		Moneda other = (Moneda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
