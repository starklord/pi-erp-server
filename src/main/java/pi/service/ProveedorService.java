package pi.service;

import java.util.List;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.model.logistica.Proveedor;

public interface ProveedorService{

	List<Proveedor> list(String app, int empresaId) throws Exception;
	List<Proveedor> listActives(String app, int empresaId) throws Exception;
	public Proveedor getByIdentificador(String app, String identificador) throws Exception;
	public OrdenCompraDet getLastCompra(String app, int proveedorId, int productoId) throws Exception;
	public void delete(String app, Proveedor proveedor) throws Exception;
	public Proveedor saveOrUpdate(String app, boolean save, Proveedor proveedor) throws Exception;
	
	public void importProveedoresFromTxt(String app) throws Exception;

}
