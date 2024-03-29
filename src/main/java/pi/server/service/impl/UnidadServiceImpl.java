package pi.server.service.impl;

import java.util.List;





import pi.service.model.almacen.Unidad;
import pi.service.UnidadService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/UnidadService")
public class UnidadServiceImpl extends HessianServlet implements UnidadService{
	@Override
	public List<Unidad> list(String app) throws Exception {
		return CRUD.list(app,Unidad.class,"order by nombre asc");
	}

	@Override
	public Unidad saveOrUpdate(String app, Unidad unidad) throws Exception {
		if(unidad.id==null){
			CRUD.save(app,unidad);
		}else{
			CRUD.update(app,unidad);
		}
		return unidad;
	}

	@Override
	public void delete(String app, Unidad unidad) throws Exception {
		CRUD.delete(app, unidad);
	}

	@Override
	public List<Unidad> listActives(String app) throws Exception {
		return CRUD.list(app,Unidad.class,"where activo is true order by nombre asc");
	}
}
