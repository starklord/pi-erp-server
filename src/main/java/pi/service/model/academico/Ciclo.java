package pi.service.model.academico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import pi.service.util.db.client.TableDB;

@TableDB(name="academico.ciclo")
public class Ciclo implements Serializable {

    public Integer      id;
    public String       creador;
    public Boolean      activo;
    
    public String       codigo;
    public Date         fecha_Inicio;
    public Date         fecha_Fin;
    public Integer     dia_Pago;
    public Integer      asistente;
    public String       indicadores;
    public Integer      carrera;    
    public String       semestre;
    public String       seccion;
    public Integer      anio;
    public String       etapa;
    public String transmision_vivo;
    public BigDecimal monto_matricula;
    public BigDecimal monto_pension1;
    public BigDecimal monto_pension2;
    public BigDecimal monto_pension3;
    public BigDecimal monto_pension4;
    public BigDecimal monto_pension5;
    public BigDecimal monto_pension6;
    public BigDecimal monto_pension7;
    public Boolean exo_matricula;
    public Boolean exo_pension1;
    public Boolean exo_pension2;
    public Boolean exo_pension3;
    public Boolean exo_pension4;
    public Boolean exo_pension5;
    public Boolean exo_pension6;
    public Boolean exo_pension7;
    
    
    
	@Override
	public String toString() {
//		if (semestre == null) {
//            return "ELEGIR CICLO";
//        } else {
//            return semestre + "-" + seccion + "-" + anio + "-" + etapa;
//        }
		return codigo;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + Objects.hashCode(this.codigo);
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
		final Ciclo other = (Ciclo) obj;
		if (!Objects.equals(this.codigo, other.codigo)) {
			return false;
		}
		return true;
	}
	
	
	

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 67 * hash + Objects.hashCode(this.id);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Ciclo other = (Ciclo) obj;
//        if (!Objects.equals(this.id, other.id)) {
//            return false;
//        }
//        return true;
//    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	public Boolean getActivo() {
		return activo;
	}
        public String getActivoStr() {
		return activo?"SI":"NO";
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getFecha_Inicio() {
		return fecha_Inicio;
	}

	public void setFecha_Inicio(Date fecha_Inicio) {
		this.fecha_Inicio = fecha_Inicio;
	}

	public Date getFecha_Fin() {
		return fecha_Fin;
	}

	public void setFecha_Fin(Date fecha_Fin) {
		this.fecha_Fin = fecha_Fin;
	}
        
	public Integer getAsistente() {
		return asistente;
	}

	public void setAsistente(Integer asistente) {
		this.asistente = asistente;
	}

	public String getIndicadores() {
		return indicadores;
	}

	public void setIndicadores(String indicadores) {
		this.indicadores = indicadores;
	}

	public Integer getCarrera() {
		return carrera;
	}

	public void setCarrera(Integer carrera) {
		this.carrera = carrera;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getSeccion() {
		return seccion;
	}

	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
    
    
    
}
