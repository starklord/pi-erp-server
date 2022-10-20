package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Direccion;
import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.TableDB;

@TableDB(name="venta.proforma")
public class Proforma implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Sucursal sucursal;
	public Integer numero;
	public Direccion direccion_cliente;
	public Date fecha;
	public Moneda moneda;
	public BigDecimal tipo_cambio;
	public BigDecimal total;
	public String observaciones;
	public Empleado vendedor;
	public OrdenVenta orden_venta;
	
	@Override
	public String toString() {
		return numero+"";
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
		Proforma other = (Proforma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
