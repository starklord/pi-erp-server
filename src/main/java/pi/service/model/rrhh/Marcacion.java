package pi.service.model.rrhh;

import java.io.Serializable;
import java.util.Date;

import pi.service.model.academico.Ciclo;
import pi.service.model.persona.Persona;
import pi.service.util.db.client.TableDB;

@TableDB(name="rrhh.marcacion")
public class Marcacion implements Serializable {

    public Integer id;
    public String creador;
    public Boolean activo;
    public Persona persona;
    public Date marcacion;
    public String observaciones;
    public Ciclo ciclo;
    public Character turno;

    @Override
    public String toString() {
        return persona.identificador;
    }


    
}
