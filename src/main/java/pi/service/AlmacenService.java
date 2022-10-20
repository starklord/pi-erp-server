package pi.service;

import java.util.List;
import pi.service.model.almacen.Almacen;


public interface AlmacenService{
	
	public List<Almacen> list(String app,int empresaId) throws Exception;//this is a new change
	public List<Almacen> listActives(String app,int empresaId);
	public void delete(String app, Almacen almacen) throws Exception;
	public Almacen saveOrUpdate(String app, boolean save, Almacen almacen) throws Exception;

}
