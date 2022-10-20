package pi.service.model.venta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.factory.Numbers;
import pi.service.model.FormaPago;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Direccion;
import pi.service.model.rrhh.Empleado;
import pi.service.util.Util;
import pi.service.util.db.client.TableDB;


@TableDB(name="venta.orden_venta")
public class OrdenVenta implements Serializable {
	
	public Integer 		id;
	public Integer 		numero;
	public String		creador;
	public Date 		creado;
	public Boolean 		activo;
	public Date 		fecha;
	public Date 		fecha_entrega;
	public Sucursal 	sucursal;
	public Integer 		documento_tipo;
	public String 		documento_serie;
	public Integer 		documento_numero;
	public Date 		documento_fecha;
	public String 		nombre_imprimible;
	public String 		direccion_imprimible;
	public Moneda 		moneda;
	public BigDecimal 	tipo_cambio;
	public Impuesto 	impuesto;
	public BigDecimal 	total;
	public BigDecimal 	total_cobrado;
	public Empleado 	vendedor;
	public String 		observaciones;
	public Direccion 	direccion_cliente;
	public FormaPago 	forma_pago;
	public Integer 		dias_credito;
	public String 		guia_remision;
	public String 		orden_compra;

	
	@Override
	public String toString() {
		return documento_serie + "-" +documento_numero;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getAnuladoStr() {
		return activo?"NO":"SI";
	}
	public String getEstadoStr() {
		if(!activo){
			return "Anulado";
		}
		if(getSaldo().doubleValue()>=0.10){
			return "Por Cobrar";
		}
		return Util.OK;
	}

	public String getActivoStr(){
		return activo?"SI":"NO";
	}

	public String getMonedaStr() {
		return moneda.simbolo;
	}

	public String getFechaStr() {
		return Util.SDF_DATE_HOURS.format(fecha);
	}

	public String getVendedorStr(){
		return vendedor.usuario;
	}

	public String getGuiaRemision() {
		return guia_remision==null?"-":guia_remision;
	}

	public String getOrdenCompra() {
		return orden_compra==null?"-":orden_compra;
	}

	public String getFormaPagoStr() {
		String fp = forma_pago.descripcion;
		if(forma_pago.id == Util.FP_CREDITO){
			fp +=" " + dias_credito + " dias";
		}
		return fp;
	}

	public String getDocumentoStr() {
		return documento_serie+"-"+ Util.completeWithZeros(documento_numero+"", 8);
	}

	public String getSerie() {
		return documento_serie;
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

	public BigDecimal getCobrado() {
		return Numbers.getBD(total_cobrado, 2);
	}

	public BigDecimal getSaldo() {
		return Numbers.getBD(total.subtract(total_cobrado), 2);
	}


	public Date getFecha() {
		return fecha;
	}

	public Date getDocumentoFecha() {
		return documento_fecha;
	}

	public String getDocumentoFechaStr() {
		return documento_fecha==null?"-":Util.SDF_DATE_HOURS.format(documento_fecha);
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
		OrdenVenta other = (OrdenVenta) obj;
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

    public Date getDocumento_fecha() {
        return documento_fecha;
    }

    public String getDocumento() {
        return documento_serie + "-" + documento_numero;
    }
    
    
	
}