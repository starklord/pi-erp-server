package pi.service.model;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.model.empresa.Sucursal;
import pi.service.model.rrhh.Empleado;

@TableDB(name="public.serie")
public class Serie implements Serializable {

	public Integer id;
	public Empleado creador;
	public Boolean activo;
	public String numero;
	public DocumentoTipo documentoTipo;
	public Sucursal sucursal;
}
