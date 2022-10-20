package pi.service.model.logistica;

import java.io.Serializable;

import pi.service.model.almacen.Articulo;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.orden_entrada_salida_det")
public class OrdenEntradaSalidaDet implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	@FieldDB("orden_entrada_salida")
	public OrdenEntradaSalida ordenEntradaSalida;
	public Articulo articulo;
	
	
	
}
