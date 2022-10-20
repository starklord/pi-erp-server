package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.almacen.Almacen;
import pi.service.model.persona.Persona;
import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.TableDB;


@TableDB(name="logistica.traslados_internos_cab")
public class TrasladoInternoCab implements Serializable{
	public Integer id;
	public String creador;
	public Boolean activo;
	public Date fecha;
	public String codigo;
	public Empleado responsable_envio;
	public Almacen almacen_origen;
	public Almacen almacen_destino;
	public Boolean cerrado;
	public Empleado responsable_recep;
	public Date fecha_recep;
	public String observaciones;
	public Persona paciente;
	public Persona medico;
	public String documento_pago;
	
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
		TrasladoInternoCab other = (TrasladoInternoCab) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
 

}
