
package pi.server.service.impl;

import java.util.Date;
import java.util.List;
import pi.service.model.venta.Proforma;
import pi.service.model.venta.ProformaDet;
import pi.server.db.Update;
import pi.service.ProformaService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/ProformaService")
public class ProformaServiceImpl extends HessianServlet implements ProformaService {

	@Override
	public List<Proforma> list(String app, int sucursalId, Date fechaInicio, Date fechaFin, int vendedorId) throws Exception {
		String[] require = {
				"direccion_cliente", "direccion_cliente.persona", "vendedor","vendedor.persona","orden_venta","moneda"
		};
		String filter = "where a.fecha between '" + fechaInicio + "' and '" + fechaFin+"'";
		if(vendedorId!=-1) {
			filter+=" and a.vendedor = " + vendedorId;
		}
		return CRUD.list(app,Proforma.class, require, filter);
	}
	
	@Override
	public List<Proforma> listPendents(String app, int sucursalId, Date fechaInicio, Date fechaFin, int vendedorId) throws Exception {
		String[] require = {
				"direccion_cliente", "direccion_cliente.persona", "vendedor","vendedor.persona","orden_venta","moneda"
		};
		String filter = "where a.fecha between '" + fechaInicio + "' and '" + fechaFin+"' and orden_venta is null";
		if(vendedorId!=-1) {
			filter+=" and a.vendedor = " + vendedorId;
		}
		return CRUD.list(app,Proforma.class, require, filter);
	}

	@Override
	public List<ProformaDet> listDetalles(String app, int proformaId) throws Exception {
		String[] require = {
				"proforma", "producto", "producto.unidad"
		};
		String filter = "where proforma = " + proformaId;
		return CRUD.list(app,ProformaDet.class, require, filter);
	}

	@Override
	public Proforma saveOrUpdate(String app, boolean save, Proforma proforma, List<ProformaDet> detalles) throws Exception {
		try {
			
			String where = "where sucursal = " + proforma.sucursal.id + " order by numero desc limit 1";
			List<Proforma> list = CRUD.list(app,Proforma.class, where);
			int numero = 1;
			if (!list.isEmpty()) {
				numero = list.get(0).numero + 1;
			}
			proforma.numero = numero;
			if(save) {
				CRUD.save(app,proforma);
				
			}else {
				CRUD.update(app,proforma);	
			}
			CRUD.execute(app, "delete from venta.proforma_det where proforma = " + proforma.id);
			for(ProformaDet det: detalles) {
				det.id = null;
				det.proforma = proforma;
				CRUD.save(app,det);
			}
			
			return proforma;
		}catch(Exception ex) {
			
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		
	}


}
