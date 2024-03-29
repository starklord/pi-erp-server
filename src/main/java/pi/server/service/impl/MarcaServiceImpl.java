package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.almacen.Marca;
import pi.service.MarcaService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/MarcaService")
public class MarcaServiceImpl extends HessianServlet implements MarcaService {

	@Override
	public List<Marca> list(String app){
		List<Marca> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Marca.class," order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public Marca saveOrUpdate(String app, Marca marca) throws Exception {
		if(marca.id==null){
			CRUD.save(app,marca);
		}else{
			CRUD.update(app,marca);
		}
		return marca;
	}

	@Override
	public void delete(String app, Marca marca) throws Exception {
		CRUD.delete(app, marca);
	}

	@Override 
	public List<Marca> listActives(String app) {
		List<Marca> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Marca.class,"where activo is true order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

}
