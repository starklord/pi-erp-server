package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.util.Util;
@TableDB(name="almacen.kardex")
public class Kardex implements Serializable {
	
	public Integer id		;
	public String creador	;
	public Boolean activo	;
	public Integer orden_id;
	public String documento;
	public Date fecha		;
	public Date fecha_orden;
	public Character movimiento; // E S
	public Character tipo	; // V C T R
	public Almacen almacen	;
	public Producto producto;
	public String origen	;
	public String destino	;
	public BigDecimal precio_costo;
	public BigDecimal precio_venta;
	public BigDecimal stock_anterior;
	public BigDecimal ingreso;
	public BigDecimal salida;
	public BigDecimal stock;
	public BigDecimal usado;
	public String identificador;
	public Date fecha_vencimiento;
	public String lote;
	
	@Override
	public String toString() {
		return movimiento + "-" + id;
	}

	public String getLote(){
		return lote;
	}

	public String getFechaVencimiento(){
		return Util.formatDate(fecha_vencimiento, Util.SDF_DATE);
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
		Kardex other = (Kardex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
}
