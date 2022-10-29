package pi.service.model.almacen;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;

@TableDB(name="logistica.articulo")
public class Articulo implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Character estado;//K:OK P:Pendiente
	public Producto producto;
	public Boolean es_unico;
	// public Ordenartic;
	public String serie;
	public String lote;
	public Date fecha_vencimiento;
	
	@Override
	public String toString() {
		return serie;
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
		Articulo other = (Articulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
