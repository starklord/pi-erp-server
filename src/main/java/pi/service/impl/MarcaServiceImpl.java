package pi.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.almacen.Marca;
import pi.service.MarcaService;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/MarcaService")
public class MarcaServiceImpl extends HessianServlet implements MarcaService {

	@Override
	public List<Marca> list(String app, int empresaId){
		List<Marca> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Marca.class,"where empresa= " + empresaId+" order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public void saveOrUpdate(String app, Marca marca) throws Exception {
		if(marca.id==null){
			CRUD.save(app,marca);
		}else{
			CRUD.update(app,marca);
		}
	}

	@Override
	public void delete(String app, Marca marca) throws Exception {
		CRUD.delete(app, marca);
	}

	@Override
	public List<Marca> listActives(String app, int empresaId) {
		List<Marca> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Marca.class,"where empresa= " + empresaId+" and activo is true order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

}
