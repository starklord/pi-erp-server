package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;

@TableDB(name="logistica.orden_compra_det")
public class OrdenCompraDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	
	public OrdenCompra orden_compra;
	public Almacen almacen;
	public Producto producto;
	public BigDecimal cantidad;
	public BigDecimal cantidad_fraccion;
	public BigDecimal cantidad_tg;
	public BigDecimal cantidad_usada;
	public BigDecimal precio_unitario;
	public BigDecimal total;
	public BigDecimal descuento;
	public String lote;
	public Date fecha_vencimiento;
	
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
		OrdenCompraDet other = (OrdenCompraDet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
