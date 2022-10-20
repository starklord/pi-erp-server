package pi.service.model.venta;

import java.io.Serializable;

import pi.service.util.db.client.TableDB;

@TableDB(name="venta.orden_venta_documento_pago")
public class OrdenVentaDocumentoPago implements Serializable {
    
    public Integer id;
    public OrdenVenta orden_venta;
    public String creador;
    public Boolean activo;
    public String documento_serie;
    public Integer documento_numero;

    
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
        OrdenVentaDocumentoPago other = (OrdenVentaDocumentoPago) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
