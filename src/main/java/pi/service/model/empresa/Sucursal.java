package pi.service.model.empresa;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.model.persona.Direccion;

@TableDB(name="empresa.sucursal")
public class Sucursal implements Serializable {
	
	public Integer 	id;
	public String 	creador;
	public Boolean 	activo;
	public String 	codigo;
	public String 	descripcion;
	public Direccion direccion;
	public Empresa 	empresa;
	public Boolean atencion_automatica;
	public String	invoice_ruc;
	public String	invoice_url;
	public String	invoice_logo_url;
	public String	invoice_commercial_name;
	public String	invoice_path_sunat;

	
	@Override
	public String toString() { 
		return codigo;
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
		Sucursal other = (Sucursal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
