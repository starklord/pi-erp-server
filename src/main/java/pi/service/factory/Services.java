package pi.service.factory;

import pi.service.AlmacenService;
import pi.service.AreaService;
import pi.service.CargoPermisoService;
import pi.service.CargoService;
import pi.service.CrudService;
import pi.service.DireccionService;
import pi.service.DocumentoIdentidadService;
import pi.service.DocumentoPagoService;
import pi.service.DocumentoTipoService;
import pi.service.EmpleadoService;
import pi.service.EmpresaService;
import pi.service.FinanzaService;
import pi.service.FormaPagoService;
import pi.service.ImpuestoService;
import pi.service.LineaService;
import pi.service.LoginService;
import pi.service.MantenimientoService;
import pi.service.MarcaService;
import pi.service.MonedaService;
import pi.service.OrdenCompraService;
import pi.service.OrdenVentaService;
import pi.service.PermisoService;
import pi.service.PersonaService;
import pi.service.ProductoService;
import pi.service.ProveedorService;
import pi.service.StockProductoService;
import pi.service.SucursalService;
import pi.service.UbigeoService;
import pi.service.UnidadService;
import pi.service.UtilidadService;
import pi.service.impl.AlmacenServiceImpl;
import pi.service.impl.AreaServiceImpl;
import pi.service.impl.CargoPermisoServiceImpl;
import pi.service.impl.CargoServiceImpl;
import pi.service.impl.CrudServiceImpl;
import pi.service.impl.DireccionServiceImpl;
import pi.service.impl.DocumentoIdentidadServiceImpl;
import pi.service.impl.DocumentoPagoServiceImpl;
import pi.service.impl.DocumentoTipoServiceImpl;
import pi.service.impl.EmpleadoServiceImpl;
import pi.service.impl.EmpresaServiceImpl;
import pi.service.impl.FinanzaServiceImpl;
import pi.service.impl.FormaPagoServiceImpl;
import pi.service.impl.ImpuestoServiceImpl;
import pi.service.impl.LineaServiceImpl;
import pi.service.impl.LoginServiceImpl;
import pi.service.impl.MantenimientoServiceImpl;
import pi.service.impl.MarcaServiceImpl;
import pi.service.impl.MonedaServiceImpl;
import pi.service.impl.OrdenCompraServiceImpl;
import pi.service.impl.OrdenVentaServiceImpl;
import pi.service.impl.PermisoServiceImpl;
import pi.service.impl.PersonaServiceImpl;
import pi.service.impl.ProductoServiceImpl;
import pi.service.impl.ProveedorServiceImpl;
import pi.service.impl.StockProductoServiceImpl;
import pi.service.impl.SucursalServiceImpl;
import pi.service.impl.UbigeoServiceImpl;
import pi.service.impl.UnidadServiceImpl;
import pi.service.impl.UtilidadServiceImpl;

public class Services {

    private static AlmacenService almacen                           = new AlmacenServiceImpl();
    private static AreaService area                                 = new AreaServiceImpl();
    private static CargoService cargo                               = new CargoServiceImpl();
    private static CargoPermisoService cargoPermiso                 = new CargoPermisoServiceImpl();
    private static CrudService crud                                 = new CrudServiceImpl();
    private static DireccionService direccion                       = new DireccionServiceImpl();
    private static DocumentoPagoService documentoPago               = new DocumentoPagoServiceImpl();
    private static DocumentoIdentidadService documentoIdentidad     = new DocumentoIdentidadServiceImpl();
    private static DocumentoTipoService documentoTipo               = new DocumentoTipoServiceImpl();
    private static EmpleadoService empleado                         = new EmpleadoServiceImpl();
    private static EmpresaService empresa                           = new EmpresaServiceImpl();
    private static FinanzaService finanza                           = new FinanzaServiceImpl();
    private static FormaPagoService formaPago                       = new FormaPagoServiceImpl();
    private static ImpuestoService impuesto                         = new ImpuestoServiceImpl();
    private static LineaService linea                               = new LineaServiceImpl();
    private static LoginService login                               = new LoginServiceImpl();
    private static MarcaService marca                               = new MarcaServiceImpl();
    private static OrdenCompraService ordenCompra                   = new OrdenCompraServiceImpl();
    private static OrdenVentaService ordenVenta                     = new OrdenVentaServiceImpl();
    private static MonedaService moneda                             = new MonedaServiceImpl();
    private static MantenimientoService mantenimiento               = new MantenimientoServiceImpl();
    private static PersonaService persona                           = new PersonaServiceImpl();
    private static PermisoService permiso                           = new PermisoServiceImpl();
    private static ProductoService producto                         = new ProductoServiceImpl();
    private static ProveedorService proveedor                       = new ProveedorServiceImpl();
    private static SucursalService sucursal                         = new SucursalServiceImpl();
    private static StockProductoService stockProducto               = new StockProductoServiceImpl();
    private static UbigeoService ubigeo                             = new UbigeoServiceImpl();
    private static UnidadService unidad                             = new UnidadServiceImpl();
    private static UtilidadService utilidad                         = new UtilidadServiceImpl();

    public static AlmacenService getAlmacen() {
        return almacen;
    }

    public static AreaService getArea() {
        return area;
    }

    public static CargoService getCargo() {
        return cargo;
    }

    public static CargoPermisoService getCargoPermiso() {
        return cargoPermiso;
    }

    public static CrudService getCrud(){
        return crud;
    }

    public static DireccionService getDireccion() {
        return direccion;
    }

    public static DocumentoPagoService getDocumentoPago() {
        return documentoPago;
    }

    public static DocumentoIdentidadService getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public static DocumentoTipoService getDocumentoTipo() {
        return documentoTipo;
    }

    public static EmpleadoService getEmpleado() {
        return empleado;
    }

    public static EmpresaService getEmpresa() {
        return empresa;
    }

    public static FinanzaService getFinanza() {
        return finanza;
    }

    public static FormaPagoService getFormaPago() {
        return formaPago;
    }

    public static ImpuestoService getImpuesto() {
        return impuesto;
    }

    public static LineaService getLinea() {
        return linea;
    }

    public static LoginService getLogin() {
        return login;
    }

    public static MarcaService getMarca() {
        return marca;
    }

    public static OrdenCompraService getOrdenCompra() {
        return ordenCompra;
    }

    public static OrdenVentaService getOrdenVenta() {
        return ordenVenta;
    }

    public static MantenimientoService getMantenimiento() {
        return mantenimiento;
    }

    public static MonedaService getMoneda() {
        return moneda;
    }

    public static PersonaService getPersona() {
        return persona;
    }

    public static PermisoService getPermiso() {
        return permiso;
    }

    public static ProductoService getProducto() {
        return producto;
    }

    public static ProveedorService getProveedor() {
        return proveedor;
    }

    public static SucursalService getSucursal() {
        return sucursal;
    }

    public static StockProductoService getStockProducto() {
        return stockProducto;
    }

    public static UbigeoService getUbigeo() {
        return ubigeo;
    }

    public static UnidadService getUnidad() {
        return unidad;
    }

    public static UtilidadService getUtilidad() {
        return utilidad;
    }

}
