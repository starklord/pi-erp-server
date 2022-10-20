/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.model.clinica;

import pi.service.util.db.client.TableDB;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Enrique
 */
@TableDB(name = "clinica.tipo_paciente")
public class TipoPaciente implements Serializable {

	public Integer id;
	public String creador;
	public Boolean activo;
	public String nombre;

	public TipoPaciente() {
	}

	public TipoPaciente(Integer id, String creador, Boolean activo, String nombre) {
		this.id = id;
		this.creador = creador;
		this.activo = activo;
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.id);
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
		final TipoPaciente other = (TipoPaciente) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
