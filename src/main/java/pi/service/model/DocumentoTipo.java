package pi.service.model;

import java.io.Serializable;

import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.TableDB;

@TableDB(name="public.documento_tipo")
public class DocumentoTipo implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public String nombreCorto;
	public Boolean ingreso;
	public Boolean salida;
	public Boolean afectoImpuesto;
	
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
		DocumentoTipo other = (DocumentoTipo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
