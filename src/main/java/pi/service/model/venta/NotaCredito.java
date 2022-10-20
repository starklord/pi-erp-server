package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.factory.Numbers;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.util.Util;
import pi.service.util.db.client.TableDB;

@TableDB(name="venta.nota_credito")
public class NotaCredito implements Serializable {
	
	public Integer 			id;
	public String 			creador;
	public Boolean 			activo;
	public Date 			fecha;
	public String 			serie;
	public Integer 			numero;
	public String			tipo_operacion;
	public String			motivo;
	public Impuesto 		impuesto;
	public Moneda			moneda;
	public BigDecimal		tipo_cambio;
	public BigDecimal 		total;
	public String 			observaciones;
	public Date 			sunat_fecha_generacion;
	public Date 			sunat_fecha_envio;
	public String			sunat_ind_situacion;
	public String 			sunat_des_obse;
	public String 			sunat_firma;
	public DocumentoPago	documento_pago;
	public Sucursal 		sucursal;

	@Override
	public String toString() {
		return serie + "-" +numero;
	}

	public String getAnuladoStr() {
		return activo?"NO":"SI";
	}

	public String getActivoStr(){
		return activo?"SI":"NO";
	}

	public String getNotaCreditoStr() {
		return getSerie()+"-"+ Util.completeWithZeros(numero+"", 8);
	}

	public String getDocumentoAfectoStr() {
		return documento_pago.getDocumentoStr();
	}

	public String getDocumentoEfactStr() {
		return sucursal.invoice_ruc+"-"+getSerie()+"-"+ Util.completeWithZeros(numero+"", 8);
	}

	public String getDocumentoFechaHoraStr() {
		return Util.SDF_DATE_HOURS.format(fecha);
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getSerie() {
		return serie;
	}

	public Integer getNumero() {
		return numero;
	}

	public String getNumeroStr() {
		return Util.completeWithZeros(numero+"", 8);
	}

	public BigDecimal getTotal() {
		return Numbers.getBD(total, 2);
	}

	public String getEstadoSunat() {
		return Util.MAP_EFACT_SITUACIONES.get(this.sunat_ind_situacion);
	}

	public String getObservacionSunat() {
		return sunat_des_obse;
	}

	public Date getFecha() {
		return fecha;
	}

	public Date getDocumentoFecha() {
		return fecha;
	}
	public String getFechaHoraStr() {
		return Util.SDF_DATE_HOURS.format(fecha);
	}

	public String getFechaGeneracionStr() {
		return sunat_fecha_generacion==null?"-":Util.SDF_DATE_HOURS.format(sunat_fecha_generacion);
	}

	public String getFechaEnvioStr() {
		return sunat_fecha_envio==null?"-":Util.SDF_DATE_HOURS.format(sunat_fecha_envio);
	}

	public Date getFechaGeneracion(){
		return sunat_fecha_generacion;
	}

	public Date getFechaEnvio() {
		return sunat_fecha_envio;
	}
	
	public String getIdentificador(){
		return documento_pago.direccion_cliente.persona.identificador;
	}

	public String getPersonaStr() {
		return documento_pago.direccion_cliente.persona.toString();
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
		NotaCredito other = (NotaCredito) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    public String getNombres() {
        return documento_pago.direccion_cliente.persona.toString();
    }
	
}