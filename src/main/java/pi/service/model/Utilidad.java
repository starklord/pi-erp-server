package pi.service.model;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.model.empresa.Empresa;
import pi.service.util.db.client.TableDB;

@TableDB(name="public.utilidad")
public class Utilidad implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public BigDecimal porcentaje;
	public Empresa empresa;
	public Boolean porDefecto;
	
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
		Utilidad other = (Utilidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
