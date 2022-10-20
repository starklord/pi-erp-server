package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.factory.Numbers;
import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Producto;
import pi.service.util.db.client.TableDB;

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
	
	public String getCantidadFraccion() {
		BigDecimal contenido = producto.contenido;
		if(contenido.compareTo(BigDecimal.ONE)>0) {
			double dCantidad = cantidad.doubleValue();
			long cantE = (long) (dCantidad/contenido.doubleValue());
			double cantF = dCantidad - (cantE*contenido.longValue());
			
			if(cantF<=0) {
				return cantE+"";
			}
			if(cantE==0){
				return "F"+cantF;
			}
			return cantE+"F"+cantF;
			
		}else {
			return Numbers.getBD(contenido, 0).toString();
		}
	}
	

}
