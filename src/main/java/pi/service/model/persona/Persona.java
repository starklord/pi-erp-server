package pi.service.model.persona;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import pi.service.model.DocumentoIdentidad;
import pi.service.model.academico.Ciclo;
import pi.service.util.db.client.TableDB;


@TableDB(name = "persona.persona")
public class Persona implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public String apellidos; 
    public String nombres;
    public Character sexo;
    public DocumentoIdentidad documento_identidad;
    public String identificador;
    public String brevette;
    public String email;
    public String telefonos;
    public String nombre_comercial;
    public String codigo;
    public String clave_portal;
    public Boolean es_agente_retencion;
    public Ciclo ciclo;
    public Character area;// S B I
    public Character turno;//T M
    public Character tipo_cliente;//N P B
    public BigDecimal limite_credito;

    
    public Boolean getActivo() {
        return activo;
    }
    
    public Direccion getDireccion() {
        Direccion dir = null;
        try {
            // dir = Services.getDireccion().getDireccionDNI(identificador).get(0);
        } catch (Exception ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en obtener direccion por dni :" + ex);
        }
        return dir;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getActivoStr() {
        if(activo==null){
            return "NO";
        }
        return activo?"SI":"NO";
    }
    
    public String getIdentificador() {
        return identificador;
    }
    public String getApellidos(){
        return apellidos;
    }
    
    public String getNombres(){
        return nombres;
    } 
    
    @Override
    public String toString() {
        return apellidos + (nombres == null ? "" : (" " + nombres));
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Persona other = (Persona) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
