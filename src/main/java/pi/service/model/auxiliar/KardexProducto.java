package pi.service.model.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.almacen.Producto;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.model.venta.OrdenVentaDet;

public class KardexProducto implements Serializable {
	
	public Integer id;
	public Date fecha;
	public String concepto;
	public BigDecimal cantidad;
	public BigDecimal precioUnitario;
	public BigDecimal costoTotal;
	public BigDecimal saldoUnidad;
	public BigDecimal saldoSoles;
	public BigDecimal actualPromedio;
	public Integer numero;
	public Boolean impuestoIncluido;
	public BigDecimal valorImpuesto;
	public Producto producto;
	public String cliente;
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
		KardexProducto other = (KardexProducto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
	
}
