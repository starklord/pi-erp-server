package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;

@TableDB(name="logistica.plantilla_transformacion_det")
public class PlantillaTransformacionDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public PlantillaTransformacion plantilla_transformacion;
	public Producto producto;
	public BigDecimal cantidad;

	public String getCodigo(){
		return producto.codigo;
	}

	public String getNombre(){
		return producto.nombre;
	}

	public BigDecimal getCantidad(){
		return Numbers.getBD(cantidad, 2);
	}

	public BigDecimal getCosto(){
		return Numbers.getBD(producto.costo_ultima_compra, 2);
	}

	public BigDecimal getTotal(){
		return Numbers.getBD(producto.costo_ultima_compra.multiply(cantidad), 2);
	}
	
	@Override
	public String toString() {
		return producto.toString();
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
		PlantillaTransformacionDet other = (PlantillaTransformacionDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
