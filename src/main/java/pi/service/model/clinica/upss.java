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
@TableDB(name="clinica.upss")
public class upss implements Serializable{

        public Integer id;
	public String creador;
	public Boolean activo;
        public Integer codigo;
        public String upss;
        public String codigo_alfabetico;
        public Integer centro_costos;

    public upss() {
    }

    public upss(Integer id, String creador, Boolean activo, Integer codigo, String upss, String codigo_alfabetico, Integer centro_costos) {
        this.id = id;
        this.creador = creador;
        this.activo = activo;
        this.codigo = codigo;
        this.upss = upss;
        this.codigo_alfabetico = codigo_alfabetico;
        this.centro_costos = centro_costos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final upss other = (upss) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
        
        
}
