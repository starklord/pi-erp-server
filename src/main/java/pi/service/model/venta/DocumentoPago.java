package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.FormaPago;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Direccion;
import pi.service.util.Util;


@TableDB(name="venta.documento_pago")
public class DocumentoPago implements Serializable {
	
	public Integer 			id;
	public String 			creador;
	public Boolean 			activo;
	public Sucursal 		sucursal;
	public Direccion 		direccion_cliente;
	public FormaPago 		forma_pago;
	public Integer 			dias_credito;
	public Moneda 			moneda;
	public BigDecimal 		tipo_cambio;
	public BigDecimal 		total;
	public String 			observaciones;
	public Integer 			tipo;
	public String 			serie;
	public Integer 			numero;
	public String 			nombre_imprimible;
	public String 			direccion_imprimible;
	public Impuesto			impuesto;
	public Date 			fecha;
	public Date 			fecha_generacion;
	public Date 			fecha_envio;
	public String			ind_situacion;
	public String 			des_obse;
	public String 			guia_remision;
	public String 			orden_compra;
	public String 			firma;
	public OrdenVenta 		orden_venta;
	

	@Override
	public String toString() {
		return serie + "-" +numero;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getEstadoStr() {
		if(!activo){
			return "Anulado";
		}
		return Util.OK;
	}

	public String getAnuladoStr() {
		return activo?"NO":"SI";
	}

	public String getActivoStr(){
		return activo?"SI":"NO";
	}

	public String getMonedaStr() {
		return moneda.simbolo;
	}

	public String getFormaPagoStr() {
		String fp = forma_pago.descripcion;
		if(forma_pago.id == Util.FP_CREDITO){
			fp +=" " + dias_credito + " dias";
		}
		return fp;
	}

	public String getDocumentoStr() {
		return getSerie()+"-"+ Util.completeWithZeros(numero+"", 8);
	}
	public String getDocumentoEfactStr() {
		return sucursal.invoice_ruc+"-"+getSerie()+"-"+ Util.completeWithZeros(numero+"", 8);
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
		String estado =  Util.MAP_EFACT_SITUACIONES.get(this.ind_situacion);
		if(observaciones.contains("RD-")&&!(
			ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO) ||
			ind_situacion.equals(Util.COD_SITU_ENVIADO_ACEPTADO_OBSERVADO) ||
			ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR) ||
			ind_situacion.equals(Util.COD_SITU_DESCARGAR_CDR_OBS)
		)){
			estado = "Enviado Por Resumen";
		}
		return estado;
	}

	public String getIndSituacion() {
		return this.ind_situacion;
	}

	public String getObservacionSunat() {
		return des_obse;
	}

	public Date getFecha() {
		return fecha;
	}

	public Date getDocumentoFecha() {
		return fecha;
	}
	public String getDocumentoFechaHoraStr() {
		return Util.SDF_DATE_HOURS.format(fecha);
	}

	public String getFechaGeneracionStr() {
		return fecha_generacion==null?"-":Util.SDF_DATE_HOURS.format(fecha_generacion);
	}

	public String getFechaEnvioStr() {
		return fecha_envio==null?"-":Util.SDF_DATE_HOURS.format(fecha_envio);
	}

	public Date getFechaGeneracion(){
		return fecha_generacion;
	}

	public Date getFechaEnvio() {
		return fecha_envio;
	}

	public String getGuiaRemision() {
		return guia_remision==null?"-":guia_remision;
	}

	public String getOrdenCompra() {
		return orden_compra==null?"-":orden_compra;
	}
	
	public String getIdentificador(){
		return direccion_cliente.persona.identificador;
	}

	public String getPersonaStr() {
		return direccion_cliente.persona.toString();
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
		DocumentoPago other = (DocumentoPago) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    public String getNombres() {
        return direccion_cliente.persona.toString();
    }
	
}