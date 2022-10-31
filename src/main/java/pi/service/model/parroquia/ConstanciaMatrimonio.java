package pi.service.model.parroquia;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.util.Util;

@TableDB(name= "parroquia.constancia_matrimonio")
public class ConstanciaMatrimonio implements Serializable{

    public Integer id;
    public String creador;
    public Boolean activo;
    public String serie;
    public Integer numero; 
    public Date fecha;
    public String nombres_esposo;
    public String nombres_padre_esposo;
    public String nombres_madre_esposo;
    public String nombres_esposa;
    public String nombres_padre_esposa;
    public String nombres_madre_esposa;
    public String nombres_testigos;
    public String nombres_padrinos;
    public String nombres_celebrante;
    public String notas_marginales;
    public String getEstado(){
        return activo?Util.OK:Util.ANULADO;
    }

    public String getSerie(){
        return serie;
    }

    public String getNumeroStr() {
        return Util.completeWithZeros(numero+"", 7);
    }

    public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE);
    }

    public String getEsposo(){
        return nombres_esposo;
    }

    public String getEsposa(){
        return nombres_esposa;
    }

    public String getTestigos(){
        return nombres_testigos;
    }
    public String getPadrinos(){
        return nombres_padrinos;
    }

    public String getCelebrante(){
        return nombres_celebrante;
    }

    public String getNotasMarginales(){
        return notas_marginales;
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
        ConstanciaMatrimonio other = (ConstanciaMatrimonio) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

   
    
}
