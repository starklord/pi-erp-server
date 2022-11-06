package pi.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pi.service.model.almacen.Almacen;
import pi.service.model.almacen.Linea;
import pi.service.model.almacen.Marca;
import pi.service.model.almacen.Unidad;
import pi.service.model.efact.TxxxSituacion;
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
    public List<Sucursal> sucursales;
    public List<DocumentoTipo> documentosTipo;
    public List<DocumentoIdentidad> documentosIdentidad;
    public List<FormaPago> formasPago;
    public List<SucursalSerie> sucursalSeries;
    public List<Moneda> monedas;
    public List<Almacen> almacenes;
    public List<Marca> marcas;
    public List<Linea> lineas;
    public List<Unidad> unidades;
    public List<Impuesto> impuestos;
    public List<TxxxSituacion> efactSituaciones;
    public Map<String, String> mapEfactSituaciones;
    public BigDecimal tc_compra;
    public BigDecimal tc_venta;
    //
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
