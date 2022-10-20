package pi.service;

import java.util.List;
import pi.service.model.almacen.Unidad;
public interface UnidadService{
	
	public List<Unidad> list(String app) throws Exception;
	public List<Unidad> listActives(String app) throws Exception;
	public void saveOrUpdate(String app, Unidad unidad) throws Exception;
	public void delete(String app, Unidad unidad) throws Exception;
	
}