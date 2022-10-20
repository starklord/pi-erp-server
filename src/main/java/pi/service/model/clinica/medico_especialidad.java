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
@TableDB(name="clinica.medico_especialidad")
public class medico_especialidad implements Serializable{
  
        public Integer id;
	public String creador;
	public Boolean activo;
        public Integer medico;
        public Integer especialidad;

    public medico_especialidad() {
    }

    public medico_especialidad(Integer id, String creador, Boolean activo, Integer medico, Integer especialidad) {
        this.id = id;
        this.creador = creador;
        this.activo = activo;
        this.medico = medico;
        this.especialidad = especialidad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final medico_especialidad other = (medico_especialidad) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
        
        
    
}
