package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.almacen.Linea;
import pi.service.LineaService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/LineaService")
public class LineaServiceImpl extends HessianServlet implements LineaService {

	@Override
	public Linea saveOrUpdate(String app, Linea linea) throws Exception {
		if(linea.id==null){
			CRUD.save(app,linea);
		}else{
			CRUD.update(app,linea);
		}
		return linea;
	}

	@Override
	public void delete(String app, Linea linea) throws Exception {
		CRUD.delete(app, linea);
	}

	@Override
	public List<Linea> list(String app) {
		List<Linea> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Linea.class," order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Linea> listActives(String app) {
		
		List<Linea> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Linea.class,"where activo is true order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

}
