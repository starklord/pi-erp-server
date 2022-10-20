package pi.service.model.logistica;

import java.io.Serializable;

import pi.service.model.empresa.Empresa;
import pi.service.model.persona.Direccion;
import pi.service.model.persona.Persona;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.proveedor")
public class Proveedor implements Serializable {
	
	public Persona id;
	public String creador;
	public Boolean activo;
	public Empresa empresa;
	
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
		Proveedor other = (Proveedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
