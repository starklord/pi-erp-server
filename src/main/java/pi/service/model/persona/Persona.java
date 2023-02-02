package pi.service.model.persona;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.model.DocumentoIdentidad;
import pi.service.util.Util;


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
    public Boolean es_agente_retencion;
    public Boolean es_proveedor;
    public Character tipo_cliente;//N P L
    public BigDecimal limite_credito;

    
    public Boolean getActivo() {
        return activo;
    }

    public String getEstado(){
        return activo?Util.OK:Util.INACTIVO;
    }

    public String getTipoCliente(){
        
        String tipo = Util.NORMAL;
        if(tipo_cliente==Util.TIPO_CLIENTE_PREFERENCIAL_ID){
            tipo = Util.PREFERENCIAL;
        }
        if(tipo_cliente==Util.TIPO_CLIENTE_LISTA_NEGRA_ID){
            tipo = Util.LISTA_NEGRA;
        }
        if(es_proveedor){
            tipo+= " "+Util.PROVEEDOR;
        }
        return tipo;
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
    public String getNombreStr(){
        return toString();
    }

    public String getNombreComercial(){
        return nombre_comercial;
    }

    public String getTelefonos(){
        return telefonos;
    }

    public String getEmail(){
        return email;
    }

    public String getAgenteRetencion(){
        return es_agente_retencion?Util.SI:Util.NO;
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
