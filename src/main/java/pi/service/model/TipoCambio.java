package pi.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.model.rrhh.Empleado;

@TableDB(name="public.tipo_cambio")
public class TipoCambio implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Date fecha;
	public Moneda moneda;
	public BigDecimal compra;
	public BigDecimal venta;
}
