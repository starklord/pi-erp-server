package pi.service.model.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import pi.service.factory.Numbers;
import pi.service.model.almacen.Producto;

public class ProductoModel implements Serializable {
	
	public Producto producto;
	public List<ProductoStockModel> stocks;
	public BigDecimal total_stock;
	
	
	
	@Override
	public String toString() {
		return producto.nombre + " Stock "+total_stock+"";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((producto == null) ? 0 : producto.hashCode());
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
		ProductoModel other = (ProductoModel) obj;
		if (producto == null) {
			if (other.producto != null)
				return false;
		} else if (!producto.equals(other.producto))
			return false;
		return true;
	}
	
	public String getCantidadFraccion() {
		BigDecimal contenido = producto.contenido;
		if(contenido.compareTo(BigDecimal.ONE)>0) {
			double dCantidad = stocks.get(0).stock.doubleValue();
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
			
			return stocks.isEmpty()?"0":Numbers.getBD(stocks.get(0).stock, 0).toString();
		}
	}
	
	
	

}
