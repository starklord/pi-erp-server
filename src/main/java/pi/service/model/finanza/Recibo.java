package pi.service.model.finanza;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.FieldDB;
import pi.service.db.client.TableDB;
import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.model.logistica.OrdenCompra;
import pi.service.model.rrhh.Area;
import pi.service.model.venta.OrdenVenta;

@TableDB(name="finanza.recibo")
public class Recibo implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Integer numero;
	public Sucursal sucursal;
	public Caja caja;
	public Date fecha;
	public Moneda moneda;
	public Character movimiento;
	public ReciboTipo recibo_tipo;
	public OrdenVenta orden_venta;
	public OrdenCompra orden_compra;
	public Letra letra;
	public String numero_operacion;
	public String observaciones;
	public BigDecimal total;
	public BigDecimal paga_con;
	public BigDecimal tipo_cambio;
	public Boolean cerrado;
	public Area area;
	public Integer turno;
	
	@Override
	public String toString() {
		return numero+"";
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
		Recibo other = (Recibo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
