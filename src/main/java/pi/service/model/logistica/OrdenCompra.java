package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.model.DocumentoTipo;
import pi.service.model.FormaPago;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.almacen.Almacen;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Direccion;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.orden_compra")
public class OrdenCompra implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Almacen almacen_entrega;
	public Sucursal sucursal;
	public Integer numero;
	public Direccion direccion_proveedor;
	public Date fecha;
	public Date fecha_entrega;
	public FormaPago forma_pago;
	public Moneda moneda;
	public Impuesto impuesto;
	public Boolean impuesto_incluido;
	public BigDecimal total;
	public String observaciones;
	public BigDecimal total_cobrado;
	public BigDecimal tipo_cambio;
	public Integer dias_credito;
	public String documento_pago;
	
	
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
		OrdenCompra other = (OrdenCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
