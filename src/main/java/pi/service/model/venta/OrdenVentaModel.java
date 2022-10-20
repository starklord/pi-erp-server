package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import pi.service.factory.Numbers;


public class OrdenVentaModel implements Serializable{
	
	public Integer id ;
	
	public OrdenVenta ordenVenta;
	public List<OrdenVentaDet> detalles;
	public BigDecimal precio_venta;
	public BigDecimal precio_compra;
	public BigDecimal utilidad_monto;
	public BigDecimal utilidad;
	
	
	
	public boolean atendido() {
//		for(OrdenVentaDet det : detalles) {
//			if(det.cantidad_atendida.compareTo(det.cantidad)<0) {
//				return false;
//			}
//		}
		return true;
	}
	
	public boolean facturado() {
//		for(OrdenVentaDet det : detalles) {
//			if(det.cantidad_facturada.compareTo(det.cantidad)<0) {
//				return false;
//			}
//		}
		return true;
	}
	
	public void calcularUtilidades() {
		precio_compra	= BigDecimal.ZERO;
		precio_venta	= BigDecimal.ZERO;
		
		for(OrdenVentaDet det : detalles) {
			precio_compra 	= precio_compra.add(det.costo_unitario.multiply(det.cantidad));
			precio_venta 	= precio_venta.add(det.total);
		}
		
		if(precio_compra.compareTo(BigDecimal.ZERO)==0){
			precio_compra = new BigDecimal("0.01");
		}
		
		if(precio_venta.compareTo(BigDecimal.ZERO)==0){
			precio_venta = new BigDecimal("0.01");
		}
		utilidad_monto = precio_venta.subtract(precio_compra);
		utilidad = Numbers.divide(utilidad_monto,precio_venta,4);
		utilidad = utilidad.multiply(new BigDecimal("100"));
		
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
		OrdenVentaModel other = (OrdenVentaModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
