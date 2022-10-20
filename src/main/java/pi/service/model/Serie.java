package pi.service.model;

import java.io.Serializable;

import pi.service.model.empresa.Sucursal;
import pi.service.model.rrhh.Empleado;
import pi.service.util.db.client.TableDB;

@TableDB(name="public.serie")
public class Serie implements Serializable {

	public Integer id;
	public Empleado creador;
	public Boolean activo;
	public String numero;
	public DocumentoTipo documentoTipo;
	public Sucursal sucursal;
}
