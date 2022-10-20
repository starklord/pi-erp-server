package pi.service;

import java.util.List;
import pi.service.model.almacen.Linea;

public interface LineaService{
	
	public List<Linea> list(String app, int empresaId);
	public List<Linea> listActives(String app, int empresaId);
	public void saveOrUpdate(String app, Linea linea) throws Exception;
	public void delete(String app, Linea linea) throws Exception;
	
}
