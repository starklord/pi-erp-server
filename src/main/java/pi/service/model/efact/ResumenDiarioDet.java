package pi.service.model.efact;

import java.io.Serializable;

import pi.service.db.client.TableDB;

@TableDB(name="efact.resumen_diario_det")
public class ResumenDiarioDet implements Serializable {
    
    public Integer id;
    public String creador;
    public Boolean activo;
    public ResumenDiario resumen_diario;
    public Comprobante comprobante;

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
        ResumenDiarioDet other = (ResumenDiarioDet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
