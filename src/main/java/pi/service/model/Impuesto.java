package pi.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;

@TableDB(name="public.impuesto")
public class Impuesto implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public BigDecimal valor;
	public Integer tipo;
	
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
		Impuesto other = (Impuesto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
