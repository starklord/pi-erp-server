package pi.service.model;

import java.io.Serializable;
import java.util.List;

import pi.service.model.almacen.Almacen;
import pi.service.model.empresa.Empresa;
import pi.service.model.empresa.Sucursal;
import pi.service.model.persona.Direccion;
import pi.service.model.rrhh.Empleado;
import pi.service.model.rrhh.Permiso;

public class MetaServer implements Serializable {

    public Empresa empresa;
    public Sucursal sucursal;
    public Empleado empleado;
    public List<Permiso> permisos;
    // para los datos iniciales de las ventas
    public Direccion sinDireccion;
    public List<DocumentoTipo> documentosTipo;
    public List<FormaPago> formasPago;
    public List<SucursalSerie> sucursalSeries;
    public List<Moneda> monedas;
    public List<Almacen> almacenes;
    public String key;
    public String app;
    public String token;
    public Integer eva_server;
    public String ip_server;
    public String path_images;
    public String path_pdfs;
    public String path_temps;

    public MetaServer() {
    }

}
