
package pi.service.impl;

import java.util.ArrayList;
import java.util.List;
import pi.service.model.Moneda;
import pi.service.MonedaService;
import pi.service.util.db.server.CRUD;
import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/MonedaService")
public class MonedaServiceImpl extends HessianServlet implements MonedaService {
	
	public static Class table = Moneda.class;

	@Override
	public List<Moneda> list(String app){
		List<Moneda> list = new ArrayList<>();
		try{
			list = CRUD.list(app,table, "order by nacional desc ");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
}
