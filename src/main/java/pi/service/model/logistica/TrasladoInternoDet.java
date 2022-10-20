package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.almacen.Producto;
import pi.service.model.almacen.StockProducto;
import pi.service.util.db.client.TableDB;


@TableDB(name="logistica.traslados_internos_det")
public class TrasladoInternoDet implements Serializable{
	public Integer id;
	public String creador;
	public Boolean activo;
	public TrasladoInternoCab traslado_cab;
	public Producto producto;
	public BigDecimal cantidad;
	public BigDecimal cantidad_fraccion;
	public Date fecha_vencimiento;
	public String lote;
	public String observaciones;
 
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
		TrasladoInternoDet other = (TrasladoInternoDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
