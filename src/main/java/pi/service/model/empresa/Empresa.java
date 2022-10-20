package pi.service.model.empresa;

import java.io.Serializable;

import pi.service.model.DocumentoTipo;
import pi.service.model.persona.Direccion;
import pi.service.util.db.client.TableDB;

@TableDB(name="empresa.empresa")
public class Empresa implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public Direccion direccion;
	public String commercial_name;
	public String logo_enterprise;
	public String logo_width;
	public Boolean allow_buy_without_stock;
	public Boolean show_medical_perspective;
	public Boolean buy_fractionable; 
	public Boolean is_fast_pos;
	public Boolean require_sales_pin;
	public DocumentoTipo documento_tipo_xdefecto;
	public String app_name;
	public String path_webapps;

	public Empresa(){
		
	}
	
	@Override
	public String toString() {
		return direccion.persona.toString();
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
