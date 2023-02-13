package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.model.almacen.Producto;
import pi.service.util.Util;

@TableDB(name="logistica.plantilla_transformacion")
public class PlantillaTransformacion implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Integer numero;
	public Date fecha;
	public Producto producto;
	public String nombre;
	public String observaciones;

	public String getEstado(){
        String estado = Util.OK;
		return activo?estado:Util.ANULADO;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNumeroStr(){
		return Util.completeWithZeros(numero+"", 7);
	}

	public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE_HOURS);
    }

	public String getProductoStr(){
		return "["+producto.codigo+"] "+ producto.nombre;
	}
	
	public String getCreador(){
		return creador;
	}

	public String getObservaciones(){
		return observaciones;
	}

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
		PlantillaTransformacion other = (PlantillaTransformacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
