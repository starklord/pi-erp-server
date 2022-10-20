package pi.service.model;

import java.io.Serializable;

import pi.service.model.empresa.Sucursal;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name = "public.sucursal_serie")
public class SucursalSerie implements Serializable {

	public Integer id;
	public Sucursal sucursal;
	public String serie;
	public Boolean por_defecto;
	public Boolean activo;
	public DocumentoTipo documento_tipo;
	
	@Override
	public String toString() {
		return serie+"";
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
		SucursalSerie other = (SucursalSerie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
