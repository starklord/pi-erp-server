package pi.service.model.empresa;

import java.io.Serializable;

import pi.service.db.client.TableDB;
import pi.service.model.DocumentoTipo;

@TableDB(name="empresa.empresa")
public class Empresa implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public String commercial_name;
	public String logo_enterprise;
	public Boolean allow_buy_without_stock;
	public Boolean is_fast_pos;
	public Boolean require_sales_pin;
	public DocumentoTipo documento_tipo_xdefecto;
	public String app_name;
	public Character tipo_sistema = 'C';//Comercial,Drogueria,Farmacia

	public Empresa(){
		
	}
	
	@Override
	public String toString() {
		return commercial_name;
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
