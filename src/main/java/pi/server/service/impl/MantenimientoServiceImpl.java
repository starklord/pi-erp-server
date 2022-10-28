package pi.server.service.impl;

import java.util.Collections;
import java.util.List;
import pi.service.MantenimientoService;
import pi.server.db.server.CRUD;
import pi.service.model.empresa.Empresa;
import pi.service.model.empresa.Sucursal;
import pi.service.model.venta.OrdenVenta;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/MantenimientoService")
public class MantenimientoServiceImpl extends HessianServlet implements MantenimientoService {

    @Override
    public void importarDocumentosPago(String app, int limite) throws Exception {
        try {
            String update_fechas    = "update venta.orden_venta set documento_fecha = fecha::date + creado::time where documento_fecha is null or fecha::date<>documento_fecha::date";
            CRUD.execute(app, update_fechas);
            OrdenVentaServiceImpl ovService = new OrdenVentaServiceImpl();
            DocumentoPagoServiceImpl dpService = new DocumentoPagoServiceImpl();
            List<OrdenVenta> listOv = ovService.listCabsLightOnlyEfact(app, limite);
            Collections.reverse(listOv);
            for (OrdenVenta ov : listOv) {
                dpService.saveByOrdenVenta(app, ov);
            }
            ActualizarAnulados(app);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public void ActualizarAnulados(String app) throws Exception {
        try {
            OrdenVentaServiceImpl ovService = new OrdenVentaServiceImpl();
            List<OrdenVenta> listAnulados = ovService.listCabsLightAnulados(app);
            StringBuilder sbr = new StringBuilder();
            for (OrdenVenta ov : listAnulados) {
                String serie = ov.documento_serie;
                int numero = ov.documento_numero;
                String where = "where serie = '" + serie + "' and numero = " + numero;
                String query = "update venta.documento_pago set activo = false " + where;
                sbr.append(query).append(";").append("\n");
            }
            CRUD.execute(app, sbr.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void saveMantenimientoSistema(String app, Empresa empresa, Sucursal sucursal) throws Exception {
        try {
            CRUD.update(app,empresa);
            CRUD.update(app,sucursal);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

}
