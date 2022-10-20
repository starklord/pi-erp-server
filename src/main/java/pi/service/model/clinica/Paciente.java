/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.model.clinica;

import pi.service.model.persona.Persona;
import pi.service.util.db.client.TableDB;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Enrique
 */
@TableDB(name = "clinica.paciente")
public class Paciente implements Serializable {


	
	public Integer 	id;
	public Boolean 	activo;
	public String 	creador;

	public Integer 	tipo_paciente;
	public Integer 	filiacion_titular;
	public Integer 	tipo_acompanante;
	public Date 	fecha_nacimiento;
	public String 	distrito_nacimiento;
	public String 	provincia_nacimiento;
	public String 	dpto_nacimiento;
	public Boolean 	discapacidad;
	public Boolean 	alergia;
	public String 	grupo_sanguineo;
	public String 	regimen_pensionario;
	public String 	cuspp;
	public String 	condicion_laboral;
	public String 	area_laboral;
	public String 	caso_emergencia;
	public String 	parentesco;
	public String 	fijo_domicilio;
	public String 	celular;
	public String 	grado_instruccion;
	public String 	ocupacion;
	public String 	domicilio_actual;
	public String 	lugar_procedencia;
	public String 	religion;
	public String 	afinidad;
	
	public String 	persona_identificador;
	public String 	titular_identificador;
	public Persona 	persona;
	public Titular 	titular;
	public String 	apoderado_identificador;
	public String 	acompanante_identificador;
	public Integer	numero_historia;
	public String	serie_historia;
	//PRIMERA VISITA
	public String 	pv_especialidad;
	public String 	pv_servicio;
	public String 	pv_medico_tratante;
	public String 	pv_codigo_medico_tratante;
	
	//FUNCIONES VITALES
	public String 	pv_temperatura;
	public String	pv_frecuencia_respiratoria;
	public String 	pv_frecuencia_cardiaca;
	public String 	pv_presion_arterial;
	public String 	pv_peso;
	public String 	pv_talla;

	public Paciente() {
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
		Paciente other = (Paciente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
