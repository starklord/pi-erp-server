package pi.service.model.printer;

import java.io.Serializable;

import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

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
