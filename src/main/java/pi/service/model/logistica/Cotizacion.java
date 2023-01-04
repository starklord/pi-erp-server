package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.FormaPago;
import pi.service.model.Moneda;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Persona;
import pi.service.util.Util;

@TableDB(name = "logistica.cotizacion")
public class Cotizacion implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Persona cliente;
    public String cliente_string;
    public String direccion_cliente;
    public Date fecha;
    public Date fecha_entrega;
    public Sucursal sucursal;
    public Integer numero;
    public Moneda moneda;
    public BigDecimal tipo_cambio;
    public FormaPago forma_pago;
    public Integer dias_credito;
    public BigDecimal total;
    public String observaciones;
    public Persona encargado;
    public Orden orden;//si tiene orden de venta

    public String getEstado(){
        String estado = Util.OK;
		return activo?estado:Util.ANULADO;
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

    public String getIdentificadorCliente(){
		return cliente.identificador;
	}
    
    public String getEncargadoStr(){
        return encargado.toString();
    }

    public String getNumeroStr(){
        return Util.completeWithZeros(numero+"", 7);
    }

    public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE_HOURS);
    }

    public String getFechaEntregaStr(){
        return Util.formatDate(fecha_entrega, Util.SDF_DATE_HOURS);
    }

    public BigDecimal getTotal(){
        return Numbers.getBD(total, 2);
    }

    public String getClienteStr(){
        return cliente_string;
    }

    public String getOrdenStr() {
        return orden==null?"":"V-"+orden.getNumeroStr();
    }

    public String getObservaciones() {
        return observaciones;
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
        Cotizacion other = (Cotizacion) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
    
}
