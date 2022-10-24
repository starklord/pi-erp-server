package pi.service.model.logistica;

import java.io.Serializable;

import pi.service.db.client.FieldDB;
import pi.service.db.client.TableDB;
import pi.service.model.almacen.Articulo;

@TableDB(name="logistica.orden_entrada_salida_det")
public class OrdenEntradaSalidaDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	@FieldDB("orden_entrada_salida")
	public OrdenEntradaSalida ordenEntradaSalida;
	public Articulo articulo;
	
	
	
}
