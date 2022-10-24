package pi.service.model.printer;

import java.io.Serializable;

import pi.service.db.client.FieldDB;
import pi.service.db.client.TableDB;
import pi.service.model.rrhh.Empleado;

@TableDB(name="printer.print")
public class Print implements Serializable {
	
	public Integer id;
	public String creador;
	public Integer empleado;
	
	@FieldDB("documento_id")
	public Integer documentoId;
	public String reporte;
	public Boolean impreso;
	public String descripcion;
	

}
