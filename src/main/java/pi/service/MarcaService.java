package pi.service;

import java.util.List;
import pi.service.model.almacen.Marca;

public interface MarcaService{
	
	public List<Marca> list(String app, int empresaId);
	public List<Marca> listActives(String app, int empresaId);
	public void saveOrUpdate(String app, Marca marca) throws Exception;
	public void delete(String app, Marca marca) throws Exception;
	
}