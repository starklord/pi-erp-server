package pi.service.model.almacen;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.model.empresa.Sucursal;
import pi.service.util.Util;


@TableDB(name="logistica.almacen")
public class Almacen implements Serializable {
	
	public Integer id;
	public Sucursal sucursal;
	public String codigo;
	public String descripcion;
	public Boolean activo;
	public String creador;
	public Boolean es_para_venta;
	public Boolean es_principal;
	
	
	public String getEstado() {
		return activo?Util.OK:Util.ANULADO;
	}

	public String getSucursal(){
		return sucursal.toString();
	}

	public String getCodigo(){
		return codigo;
	}

	public String getEsParaVenta(){
		return es_para_venta?Util.SI:Util.NO;
	}

	public String getEsPrincipal(){
		return es_principal?Util.SI:Util.NO;
	}

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
		Almacen other = (Almacen) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
