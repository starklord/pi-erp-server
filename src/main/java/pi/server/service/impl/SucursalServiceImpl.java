package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;





import pi.service.model.SucursalSerie;
import pi.service.model.empresa.Sucursal;
import pi.service.SucursalService;
import pi.service.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/SucursalService")
public class SucursalServiceImpl extends HessianServlet implements SucursalService {

	@Override
	public List<Sucursal> list(String app) {
		List<Sucursal> list = new ArrayList<>();
		try {
			String[] required = { 
					"empresa",
					"empresa.direccion",
					"empresa.direccion.persona",
					"direccion"
			};
			list = CRUD.list(app,Sucursal.class, required);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Sucursal> listActive(String app) {
		List<Sucursal> list = new ArrayList<>();
		try {
			String[] required = {
					"empresa",
					"empresa.direccion",
					"empresa.direccion.persona",
					"direccion"
			};
			list = CRUD.list(app,Sucursal.class, required, " where a.activo is true");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Sucursal> list(String app, int empresaId) {
		List<Sucursal> list = new ArrayList<>();
		try {
			String[] required = {
					"empresa",
					"empresa.direccion",
					"empresa.direccion.persona",
					"direccion"
			};
			list = CRUD.list(app,Sucursal.class, required, " where a.empresa = " + empresaId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Sucursal> listActive(String app, int empresaId) {
		List<Sucursal> list = new ArrayList<>();
		try {
			String[] required = {
					"empresa",
					"empresa.direccion",
					"empresa.direccion.persona",
					"direccion"
			};
			list = CRUD.list(app,Sucursal.class, required, " where empresa = " + empresaId + " and a.activo is true");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public Sucursal save(String app, Sucursal sucursal) throws Exception {
		CRUD.save(app,sucursal);
		return sucursal;
	}

	@Override
	public void update(String app, Sucursal sucursal) throws Exception {
		CRUD.update(app,sucursal);
	}

	@Override
	public Sucursal saveOrUpdate(String app, Sucursal sucursal) throws Exception {
		if (sucursal.id == null) {
			CRUD.save(app,sucursal);
		} else {
			CRUD.update(app,sucursal);
		}
		return sucursal;
	}

	@Override
	public List<SucursalSerie> listSeries(String app, int sucursalId) {
		List<SucursalSerie> list = new ArrayList<>();
		try {
			list = CRUD.list(app,SucursalSerie.class, " where a.sucursal = " + sucursalId + " and a.activo is true"
					+ " order by a.por_defecto desc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<SucursalSerie> listSeries(String app, int sucursalId, int documentoTipoId) {
		List<SucursalSerie> list = new ArrayList<>();
		try {
			list = CRUD.list(app,SucursalSerie.class, " where a.sucursal = " + sucursalId + " and a.activo is true"
					+ " and a.documento_tipo = " + documentoTipoId
					+ " order by a.por_defecto desc");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public Sucursal get(String app, int sucursalId) {
		List<Sucursal> list = new ArrayList();
		try {
			String[] required = {
					"empresa",
					"empresa.direccion",
					"empresa.direccion.persona",
					"direccion"
			};
			list = CRUD.list(app,Sucursal.class, required, " where a.id = " + sucursalId);
			
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return list.isEmpty() ? null : list.get(0);
	}

}
