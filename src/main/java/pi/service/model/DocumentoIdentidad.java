package pi.service.model;

import java.io.Serializable;

import pi.service.db.client.TableDB;


@TableDB(name="public.documento_identidad")
public class DocumentoIdentidad implements Serializable {

	public Integer id;
	public Boolean activo;
	public String abreviatura;
	public String nombre;
	
	@Override
	public String toString() {
		return abreviatura;
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
		DocumentoIdentidad other = (DocumentoIdentidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
