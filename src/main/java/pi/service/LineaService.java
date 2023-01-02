package pi.service;

import java.util.List;
import pi.service.model.almacen.Linea;

public interface LineaService{
	
	public List<Linea> list(String app);
	public List<Linea> listActives(String app);
	public Linea saveOrUpdate(String app, Linea linea) throws Exception;
	public void delete(String app, Linea linea) throws Exception;
	
}
