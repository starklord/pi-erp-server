package pi.service.model.almacen;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.persona.Persona;
import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.TableDB;

@TableDB(name="almacen.orden_traslado")
public class OrdenTraslado implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Integer numero;
	public Date fecha;
	public Almacen almacen_origen;
	public Almacen almacen_destino;
	public Boolean recepcionado;
	public Persona recepcionista;
	public String observaciones;
	
	@Override
	public String toString() {
		return "OT # "+numero;
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
		OrdenTraslado other = (OrdenTraslado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
