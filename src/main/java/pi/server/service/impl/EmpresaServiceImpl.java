package pi.server.service.impl;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.server.db.server.CRUD;
import pi.service.EmpresaService;
import pi.service.model.empresa.Empresa;

@WebServlet("pi/EmpresaService")
public class EmpresaServiceImpl extends HessianServlet implements EmpresaService {

	public static Class table = Empresa.class;

	@Override
	public List<Empresa> list(String app) throws Exception {
		String[] required = { "documento_tipo_xdefecto" };
		List<Empresa> list = CRUD.list(app, table, required);
		return list;
	}

	@Override
	public Empresa get(String app) throws Exception {
		System.out.println("EmpresaService: "+ app);
		//para actualizar los datos iniciales de la empresa...
		updateDB(app);
		//Fin de actualizacion de datos iniciales de la empresa.
		String[] required = { "documento_tipo_xdefecto" };
		Empresa empresa = null;
		List<Empresa> list = CRUD.list(app, table, required);
		empresa = list.isEmpty() ? null : list.get(0);
		return empresa;
	}

	@Override
	public List<Empresa> listActive(String app) throws Exception {
		String[] required = { "documento_tipo_xdefecto" };
		List<Empresa> list = CRUD.list(app, table, required, "where a.activo is true");
		return list;
	}

	@Override
	public Empresa save(String app, Empresa empresa) throws Exception {
		CRUD.save(app, empresa);
		return empresa;
	}

	@Override
	public void delete(String app, Empresa empresa) throws Exception {
		CRUD.delete(app, empresa);
	}

	@Override
	public Empresa saveOrUpdate(String app, boolean save, Empresa empresa) throws Exception {
		CRUD.save(app, empresa);
		return empresa;
	}

	private void updateDB(String app) throws Exception {
		try {
			System.out.println("actualizando datos de la empresa...");
			InputStream is = App.class.getResourceAsStream("/pi/pisqldb.txt");
			String update = Util.readFile(is);
			CRUD.execute(app, update);
			System.out.println("Datos de la empresa actualizados exitosamente.");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("no se pudo cargar los datos de la empresa");
		}

	}

}
