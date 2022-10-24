package pi.service.model.rrhh;

import java.io.Serializable;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.model.persona.Persona;

@TableDB(name="rrhh.marcacion")
public class Marcacion implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Persona persona;
    public Date marcacion;
    public String observaciones;
    public Character turno;

    @Override
    public String toString() {
        return persona.identificador;
    }


    
}
