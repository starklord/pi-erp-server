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
@TableDB(name="clinica.especialidad")
public class especialidad implements Serializable{
    
        public Integer id;
	public String creador;
	public Boolean activo;
        public Integer sub_esp;
        public Integer cod_ss;
        public Integer cod_cm;
        public String nombre;
        public String abreviatura;
        public String codigo;
        public String colegio_prof;

    public especialidad() {
    }

    public especialidad(Integer id, String creador, Boolean activo, 
            Integer sub_esp, Integer cod_ss, Integer cod_cm, 
            String nombre, String abreviatura, String codigo, 
            String colegio_prof) 
    {
        this.id = id;
        this.creador = creador;
        this.activo = activo;
        this.sub_esp = sub_esp;
        this.cod_ss = cod_ss;
        this.cod_cm = cod_cm;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.codigo = codigo;
        this.colegio_prof = colegio_prof;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final especialidad other = (especialidad) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
        
}
