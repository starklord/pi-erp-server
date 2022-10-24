package pi.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.almacen.Almacen;
import pi.service.AlmacenService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/AlmacenService")

public class AlmacenServiceImpl extends HessianServlet implements AlmacenService {

	@Override
	public List<Almacen> list(String app, int empresaId) {
		List<Almacen> list = new ArrayList<>(); 
		try{
			String[] req={"sucursal","sucursal.empresa"};
		list = CRUD.list(app,Almacen.class,req,"where c.id = " + empresaId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<Almacen> listActives(String app, int empresaId) {
		List<Almacen> list = new ArrayList<>(); 
		try{
			String[] req={"sucursal","sucursal.empresa"};
		list = CRUD.list(app,Almacen.class,req,"where c.id = " + empresaId + "and a.activo is true");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public void delete(String app, Almacen almacen) throws Exception {
		CRUD.delete(app,almacen);
	}

	@Override
	public Almacen saveOrUpdate(String app, boolean save, Almacen almacen) throws Exception {
		if(save){
			CRUD.save(app,almacen);
		}else{
			CRUD.update(app,almacen);
		}
		return almacen;
	}

	

	

}
