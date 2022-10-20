package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.almacen.Almacen;
import pi.service.model.persona.Direccion;
import pi.service.model.persona.Persona;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.guia_remision")
public class GuiaRemision implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Character movimiento;// E S
	public Character tipo; // R C V T
	public Almacen almacen_partida;
	public Almacen almacen_llegada;
	public String serie;
	public Integer numero;
	public Direccion direccion_partida;
	public Direccion direccion_llegada;
	public Date fecha;
	public Date fecha_traslado;
	public String observaciones;
	public Persona transportista;
	public MotivoTraslado motivo_traslado;
	public String contacto;
	public Boolean recepcionado;
	
	@Override
	public String toString() {
		return numero +"";
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
		GuiaRemision other = (GuiaRemision) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
