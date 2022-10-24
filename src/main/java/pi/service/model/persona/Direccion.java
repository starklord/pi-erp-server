package pi.service.model.persona;

import java.io.Serializable;

import pi.service.db.client.TableDB;

@TableDB(name="persona.direccion")
public class Direccion implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Persona persona;
	public String ubigeo;
	public String descripcion;
	public String telefono;
	public String email;
	public String referencia;
	
	
	@Override
	public String toString() { 
		return descripcion;
	}

	public String getNombreStr() {
		return this.persona.toString();
	}

	public String getNombreComercial(){
		return this.persona.nombre_comercial;
	}
	
	public String getIdentificador() {
		return this. persona.identificador;
	}

	public String getDireccionStr(){
		return this.descripcion;
	}

	public String getTelefonosStr(){
		return this.telefono;
	}

	public String getEmailStr(){
		return this.email;
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
		Direccion other = (Direccion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
