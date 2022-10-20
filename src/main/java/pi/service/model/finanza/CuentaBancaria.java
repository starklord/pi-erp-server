package pi.service.model.finanza;

import java.io.Serializable;

import pi.service.util.db.client.TableDB;

@TableDB(name="finanza.cuenta_bancaria")
public class CuentaBancaria implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;
	public Banco banco;
	public String numero;
	
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
		CuentaBancaria other = (CuentaBancaria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
