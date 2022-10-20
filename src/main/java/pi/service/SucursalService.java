package pi.service;

import java.util.List;

import pi.service.model.SucursalSerie;
import pi.service.model.empresa.Sucursal;

public interface SucursalService{
	
	public List<Sucursal> list(String app);
	public List<Sucursal> listActive(String app);
	public List<Sucursal> list(String app, int empresaId);
	public List<Sucursal> listActive(String app, int empresaId);
	public List<SucursalSerie> listSeries(String app, int sucursalId);
	public Sucursal save(String app, Sucursal sucursal) throws Exception;
	public void update(String app, Sucursal sucursal) throws Exception;
	public Sucursal saveOrUpdate(String app, Sucursal sucursal) throws Exception;
	public List<SucursalSerie> listSeries(String app, int sucursalId, int documentoTipoId);
	public Sucursal get(String app, int sucursalId);

}
