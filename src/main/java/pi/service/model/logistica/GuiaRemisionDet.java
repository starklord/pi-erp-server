package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.model.almacen.Producto;

@TableDB(name="logistica.guia_remision_det")
public class GuiaRemisionDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public GuiaRemision guia_remision;
	public Producto producto;
	public BigDecimal cantidad;
	public String documento_pago;
	public String descripcion;
	
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
		GuiaRemisionDet other = (GuiaRemisionDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
