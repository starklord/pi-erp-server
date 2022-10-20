package pi.service.model.parking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.factory.Numbers;
import pi.service.model.venta.OrdenVenta;
import pi.service.util.Util;
import pi.service.util.db.client.TableDB;

@TableDB(name="parking.ticket")
public class Ticket implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Date hora_inicio;
    public Date hora_fin;
    public BigDecimal tarifa;
    public BigDecimal monto;
    public String serie;
    public Integer numero;
    public String placa;
    public String observaciones;

    public String getEstadoStr(){
        return activo?Util.OK:Util.ANULADO;
    }

    public String getTicketStr(){
        return serie+"-"+Util.completeWithZeros(numero+"", 8);
    }

    public String getPlaca(){
        return placa;
    }

    public String getConcepto(){
        String concepto = "Parking x Hora";
        if(tarifa.compareTo(Util.TARIFAXNOCHE)==0){
            concepto = "Parking x Noche";
        }
        if(tarifa.compareTo(Util.TARIFAXDIA)==0){
            concepto = "Parking x Dia";
        }
        return concepto;
    }

    public BigDecimal getTarifa() {
        return Numbers.getBD(tarifa, 2);
    }

    public String getHoraIngresoStr() {
        return Util.formatDate(hora_inicio, Util.SDF_DATE_HOURS);
    }

    public String getHoraSalidaStr() {
        return hora_fin==null?"":Util.formatDate(hora_inicio, Util.SDF_DATE_HOURS);
    }

    public BigDecimal getMonto() {
        return monto;
    }

    // public BigDecimal getTotal() {
    //     return Numbers.getBD(total, 2);
    // }

    // public BigDecimal getCobrado() {
    //     return Numbers.getBD(total_cobrado, 2);
    // }

    // public BigDecimal getSaldo() {
    //     return Numbers.getBD(total.subtract(total_cobrado), 2);
    // }

    public String getObservaciones() {
        return observaciones;
    }


    @Override
    public String toString() {
        return serie + Util.completeWithZeros(numero+"", 8);
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
        Ticket other = (Ticket) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
