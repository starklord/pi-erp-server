package pi.service.model.almacen;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.model.empresa.Empresa;


@TableDB(name="logistica.marca")
public class Marca implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public String abreviatura;
	public Empresa empresa;
	
	
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
		Marca other = (Marca) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
