package pi.service.model.parroquia;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.util.Util;

@TableDB(name= "parroquia.constancia_bautismo")
public class ConstanciaBautismo implements Serializable{

    public Integer id;
    public String creador;
    public Boolean activo;
    public String serie;
    public Integer numero; 
    public Date fecha;
    public String lugar_nacimiento;
    public Date fecha_nacimiento;
    public String nombres_padre;
    public String nombres_madre;
    public String nombres_bautizado; 
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

    public String getNombresStr(){
        return nombres_bautizado;
    }

    public String getLugarNacimiento(){
        return lugar_nacimiento;
    }

    public String getFechaNacimientoStr(){
        return Util.formatDate(fecha_nacimiento, Util.SDF_DATE);
    }
    public String getPadre(){
        return nombres_padre;
    }

    public String getMadre(){
        return nombres_madre;
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
        ConstanciaBautismo other = (ConstanciaBautismo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
