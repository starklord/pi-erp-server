package pi.service.model.almacen;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.util.Util;


@TableDB(name="logistica.unidad")
public class Unidad implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public String abreviatura;

	public Unidad(){

	}

	public Unidad(int id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return nombre;
	}

	public String getActivoStr() {
		return activo?Util.ACTIVO:Util.INACTIVO;
	}

	public String getNombre() {
		return nombre;
	}

	public String getAbreviatura() {
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
		Unidad other = (Unidad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
