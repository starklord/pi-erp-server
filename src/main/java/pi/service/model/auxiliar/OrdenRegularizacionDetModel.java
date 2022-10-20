package pi.service.model.auxiliar;

import java.math.BigDecimal;

import pi.service.factory.Numbers;
import pi.service.model.almacen.OrdenRegularizacionDet;

public class OrdenRegularizacionDetModel {
	
	public OrdenRegularizacionDet detalle;
	public BigDecimal stock;
	public BigDecimal diferencia;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((detalle == null) ? 0 : detalle.hashCode());
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
		OrdenRegularizacionDetModel other = (OrdenRegularizacionDetModel) obj;
		if (detalle == null) {
			if (other.detalle != null)
				return false;
		} else if (!detalle.equals(other.detalle))
			return false;
		return true;
	}
	
	public String getCantidadFraccion() {
		BigDecimal contenido = detalle.producto.contenido;
		if(contenido.compareTo(BigDecimal.ONE)>0) {
			double dCantidad = stock.doubleValue();
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
			
			return Numbers.getBD(stock, 0).toString();
		}
	}
	
	public String getDiferencia() {
		BigDecimal contenido = detalle.producto.contenido;
		if(contenido.compareTo(BigDecimal.ONE)>0) {
			double dCantidad = diferencia.doubleValue();
			long cantE = (long) (dCantidad/contenido.doubleValue());
			double cantF = dCantidad - (cantE*contenido.longValue());
			
			if(cantF==0) {
				return cantE+"";
			}
			if(cantE==0){
				return "F"+cantF;
			}
			return cantE+"F"+cantF;
			
		}else {
			
			return Numbers.getBD(diferencia, 0).toString();
		}
	}
	
	

}
