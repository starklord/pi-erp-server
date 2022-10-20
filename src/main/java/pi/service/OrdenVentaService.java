package pi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pi.service.model.venta.OrdenVenta;
import pi.service.model.venta.OrdenVentaDet;
import pi.service.model.venta.Proforma;

public interface OrdenVentaService {

    public OrdenVenta getOrdenVenta(String app, int ordenVentaId) throws Exception;

    public OrdenVenta getLastOrdenVenta(String app, int sucursalId) throws Exception;
    public OrdenVenta getLastOrdenVenta(String app, int documentoTipoId, String documento_serie) throws Exception;

    public List<OrdenVenta> list(String app, int sucursalId, Date inicio, Date fin, int vendedorId) throws Exception;

    public List<OrdenVenta> listOnlyEfact(String app, int sucursalId, Date inicio, Date fin, int vendedorId) throws Exception;

    public List<OrdenVenta> list(String app, int personaId) throws Exception;

    public List<OrdenVentaDet> listDetalles(String app, int ordenVentaId) throws Exception;

    public List<OrdenVenta> listCabsLight(String app) throws Exception;

    public List<OrdenVenta> listCabsLightAnulados(String app) throws Exception;

    public List<OrdenVenta> listCabsLightOnlyEfact(String app, int limit) throws Exception;

    public List<OrdenVentaDet> listDetsLight(String app, int ovId) throws Exception;

    public OrdenVenta save(String app, Proforma proforma, OrdenVenta ordenVenta, List<OrdenVentaDet> detalles, int cajaId,
            BigDecimal pagaCon, String numero_operacion) throws Exception;

    public void anular(String app, int ordenVentaId) throws Exception;

}
