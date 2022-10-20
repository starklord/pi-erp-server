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
@TableDB(name="clinica.medico")
public class Medico implements Serializable{
    
        public Integer id;
        public String creador;
        public Boolean activo;
        public Integer persona;
        public String condicion;
        public String precio_clinica;
        public String precio_medico;

    public Medico() {
    }

    public Medico(Integer id, String creador, Boolean activo, Integer persona, String condicion, String precio_clinica, String precio_medico) {
        this.id = id;
        this.creador = creador;
        this.activo = activo;
        this.persona = persona;
        this.condicion = condicion;
        this.precio_clinica = precio_clinica;
        this.precio_medico = precio_medico;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Medico other = (Medico) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
        
        
}
