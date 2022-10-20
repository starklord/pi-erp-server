package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.almacen.TrasladoAlmacen;
import pi.service.model.empresa.Sucursal;
import pi.service.model.venta.OrdenVenta;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.orden_entrada_salida")
public class OrdenEntradaSalida implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Character tipo;
	public Integer numero;
	public Date fecha;
	@FieldDB("orden_venta")
	public OrdenVenta ordenVenta;
	@FieldDB("orden_compra")
	public OrdenCompra ordenCompra;
	public TrasladoAlmacen traslado;
	public Sucursal sucursal;
	
	
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
		OrdenEntradaSalida other = (OrdenEntradaSalida) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
