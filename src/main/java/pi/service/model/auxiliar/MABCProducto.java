package pi.service.model.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;

public class MABCProducto implements Serializable {
	
	public Integer id;
	public String codigo;
	public String nombre;
	public BigDecimal cantidad;
	public BigDecimal precio_unitario;
	public BigDecimal total;
	public BigDecimal porcentaje;
	public BigDecimal porcentaje_acumulado;
	public BigDecimal costo_ultima_compra;
	public BigDecimal contenido;
	
	public MABCProducto() {
		
	}
	
	

}
