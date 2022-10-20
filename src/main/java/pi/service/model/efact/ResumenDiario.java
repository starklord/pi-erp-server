package pi.service.model.efact;

import java.io.Serializable;
import java.util.Date;

import pi.service.util.Util;
import pi.service.util.db.client.TableDB;

@TableDB(name="efact.resumen_diario")
public class ResumenDiario implements Serializable {

    public Integer  id;
    public String   creador;
    public Boolean  activo;
    public Date     fecha;
    public Integer  numero;
    public Date     fecha_envio;
	public String   ind_situacion;
	public String   des_obse;


    public String getEstadoStr(){
        return activo ?Util.OK:Util.ANULADO;
    }

    public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE_HOURS);
    }
    public String getNumeroStr(){
        return Util.completeWithZeros(numero+"", 3);
    }

	public String getIndSituacion() {
		return this.ind_situacion;
	}
    public String getObservacionSunat() {
		return des_obse;
	}

    public String getFechaEnvioStr() {
		return fecha_envio==null?"-":Util.SDF_DATE_HOURS.format(fecha_envio);
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
        ResumenDiario other = (ResumenDiario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

    
}
