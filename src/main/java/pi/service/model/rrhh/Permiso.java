package pi.service.model.rrhh;import java.io.Serializable;

import pi.service.util.db.client.TableDB;

@TableDB(name="rrhh.permiso")
public class Permiso implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String descripcion;
	public Area area;

	@Override
	public String toString() {
		return descripcion;
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
		Permiso other = (Permiso) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
