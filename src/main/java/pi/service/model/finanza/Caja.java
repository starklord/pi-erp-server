package pi.service.model.finanza;

import java.io.Serializable;

import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="finanza.caja")
public class Caja implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Sucursal sucursal;
	public String nombre;
	
	@FieldDB("cuenta_bancaria")
	public CuentaBancaria cuentaBancaria;
	
	@FieldDB("es_detraccion")
	public Boolean esDetraccion;
	
	@FieldDB("es_caja_fisica")
	public Boolean esCajaFisica;
	
	
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
		Caja other = (Caja) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
