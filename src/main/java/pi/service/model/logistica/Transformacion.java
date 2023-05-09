package pi.service.model.logistica;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import pi.service.model.empresa.Sucursal;
import pi.service.model.rrhh.Empleado;
import pi.service.util.Util;

public class Transformacion implements Serializable {

    public Date creado = new Date();
	public String creador = "root";
	public Boolean activo = true;
	public Sucursal sucursal;
	public Integer numero;
	public Date fecha;
	public String observaciones;
	public Empleado atendido_por;
	public String fecha_atencion;
	public Empleado aprobado_por;
	public String fecha_aprobacion;
	public Orden orden;
    public List<TransformacionDet> detalles;

	public String getEstado(){
        String estado = Util.OK;
		return activo?estado:Util.ANULADO;
	}

	public String getNumeroStr(){
		return "OT-"+Util.completeWithZeros(numero+"", 8);
	}

	public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE_HOURS);
    }

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getCreador(){
		return creador;
	}

	public String getObservaciones(){
		return observaciones;
	}

    public Integer getNumero() {
        return numero;
    }
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sucursal == null) ? 0 : sucursal.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		Transformacion other = (Transformacion) obj;
		if (sucursal == null) {
			if (other.sucursal != null)
				return false;
		} else if (!sucursal.equals(other.sucursal))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	

}
