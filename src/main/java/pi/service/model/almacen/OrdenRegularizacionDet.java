package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.util.db.client.TableDB;

@TableDB(name="almacen.orden_regularizacion_det")
public class OrdenRegularizacionDet implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Producto producto;
	public BigDecimal cantidad;
	public OrdenRegularizacion orden_regularizacion;
	public BigDecimal cantidad_fraccion;
	public Date fecha_vencimiento;
	public String lote;
	
	
	@Override
	public String toString() {
		return id+"";
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
		OrdenRegularizacionDet other = (OrdenRegularizacionDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
