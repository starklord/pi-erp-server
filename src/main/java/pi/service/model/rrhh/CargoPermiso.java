package pi.service.model.rrhh;

import java.io.Serializable;

import pi.service.util.db.client.TableDB;

@TableDB(name="rrhh.cargo_permiso")
public class CargoPermiso implements Serializable {

	public Integer id;
	public String creador;
	public Cargo cargo;
	public Permiso permiso;
	
}
