package pi.service.model.logistica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.FormaPago;
import pi.service.model.Moneda;
import pi.service.model.almacen.Almacen;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Persona;
import pi.service.util.Util;

@TableDB(name = "logistica.orden")
public class Orden implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Character estado;//Creado,Aprobado,Facturado,Atendido
    public Persona proveedor;
    public Persona cliente;
    public String cliente_string;
    public String direccion_proveedor;
    public String direccion_cliente;
    public Date fecha;
    public Date fecha_entrega;
    public Sucursal sucursal;
    public Character tipo;//Venta,Compra,Trasladointerno,transFormacion,Entradasimple,Salidasimple
    public Integer numero;
    public Moneda moneda;
    public BigDecimal tipo_cambio;
    public FormaPago forma_pago;
    public Integer dias_credito;
    public BigDecimal total;
    public BigDecimal total_cobrado;
    public Persona aprobado_por;
    public Date fecha_aprobacion;
    public Persona atendido_por;
    public Date fecha_atencion;
    public Almacen almacen_origen;
    public Almacen almacen_destino;
    public String observaciones;

    public String getEstado(){
        String estado = Util.OK;
        if(fecha_aprobacion==null||fecha_atencion==null){
            estado = Util.PENDIENTE;
        }
		return activo?estado:Util.ANULADO;
	}

    public String getNumeroStr(){
        return Util.completeWithZeros(numero+"", 7);
    }

    public String getAlmacenOrigen(){
        return almacen_origen.codigo;
    }

    public String getAlmacenDestino(){
        return almacen_destino.codigo;
    }

    public String getAprobadoPorStr(){
        return aprobado_por==null?"-":aprobado_por.toString();
    }

    public String getAtendidoPorStr(){
        return atendido_por==null?"-":atendido_por.toString();
    }

    public String getFechaStr(){
        return Util.formatDate(fecha, Util.SDF_DATE_HOURS);
    }

    public String getFechaEntregaStr(){
        return Util.formatDate(fecha_entrega, Util.SDF_DATE_HOURS);
    }

    public String getFechaAtencionStr(){
        return fecha_atencion==null?"-":Util.formatDate(fecha_atencion, Util.SDF_DATE_HOURS);
    }

    public String getFechaAprobacionStr(){
        return fecha_aprobacion==null?"-":Util.formatDate(fecha_aprobacion, Util.SDF_DATE_HOURS);
    }

    public BigDecimal getTotal(){
        return Numbers.getBD(total, 2);
    }

    public String getClienteStr(){
        return cliente_string;
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
        Orden other = (Orden) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
