package pi.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.almacen.Linea;
import pi.service.LineaService;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/LineaService")
public class LineaServiceImpl extends HessianServlet implements LineaService {

	@Override
	public void saveOrUpdate(String app, Linea linea) throws Exception {
		if(linea.id==null){
			CRUD.save(app,linea);
		}else{
			CRUD.update(app,linea);
		}
	}

	@Override
	public void delete(String app, Linea linea) throws Exception {
		CRUD.delete(app, linea);
	}

	@Override
	public List<Linea> list(String app, int empresaId) {
		List<Linea> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Linea.class,"where empresa=" + empresaId +" order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Linea> listActives(String app, int empresaId) {
		
		List<Linea> list = new ArrayList<>();
		try{
			list = CRUD.list(app,Linea.class,"where empresa=" + empresaId +" and activo is true order by nombre asc");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

}
