package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Persona;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.requerimiento_compra")
public class RequerimientoCompra implements Serializable {
	
	public Integer id;
	public Boolean activo;
	public Integer serie;
	public Integer numero;
	public Date fecha;
	@FieldDB("fecha_entrega")
	public Date fechaEntrega;
	@FieldDB("orden_compra")
	public Integer ordenCompra;
	public String factura;
	public Persona persona_requerimiento;
	public Sucursal sucursal;
	public String creador;
	public String observaciones;
	
	public RequerimientoCompra() {
		
	}
	
	
	@Override
	public String toString() {
		return serie+"-"+numero;
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
		RequerimientoCompra other = (RequerimientoCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
