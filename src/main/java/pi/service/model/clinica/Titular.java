/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.model.clinica;

import pi.service.model.persona.Persona;
import pi.service.util.db.client.TableDB;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Enrique
 */

@TableDB(name = "clinica.titular")
public class Titular implements Serializable {

	public Integer id;
	public Date creado;
	public String creador;
	public Boolean activo;
	public TipoPaciente tipo_paciente;
	public Pagador pagador;
	public String tipo_seguro;
	public String numero_seguro;
	public String compania_seguro;
	public String empresa_contratante;
	public String ruc_contratante;
	public String persona_dni;
	public Persona persona;

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Titular other = (Titular) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
