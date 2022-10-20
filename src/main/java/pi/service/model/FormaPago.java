package pi.service.model;

import java.io.Serializable;
import java.util.Date;
import pi.service.util.db.client.TableDB;

@TableDB(name="forma_pago")
public class FormaPago implements Serializable {

	public Integer id;
	public String creador;
	public String descripcion;
	public Boolean efectivo;
	public Boolean activo;
	
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
		FormaPago other = (FormaPago) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
