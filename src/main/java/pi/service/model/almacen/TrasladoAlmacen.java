package pi.service.model.almacen;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.empresa.Sucursal;
import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.traslado_almacen")
public class TrasladoAlmacen implements Serializable {
	
	public Integer id;
	public String creador;
	public boolean activo;
	public Integer numero;
	public Date fecha;
	public Date fecha_recepcion;
	
	@FieldDB("almacen_origen")
	public Almacen almacenOrigen;
	@FieldDB("almacen_destino")
	public Almacen almacenDestino;
	public Empleado recepcionista;
	public String observaciones;
	public Sucursal sucursal;
	
	
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
		TrasladoAlmacen other = (TrasladoAlmacen) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
