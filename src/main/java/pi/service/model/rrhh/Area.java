package pi.service.model.rrhh;import java.io.Serializable;

import pi.service.db.client.TableDB;

@TableDB(name="rrhh.area")
public class Area implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String descripcion;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	@Override
	public String toString() {
		return descripcion;
	}
	
}
