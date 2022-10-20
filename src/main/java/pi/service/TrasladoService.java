package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.almacen.Almacen;
import pi.service.model.logistica.TrasladoInternoCab;
import pi.service.model.logistica.TrasladoInternoDet;

public interface TrasladoService {
	public void saveOrUpdate(String app, TrasladoInternoCab trasCab, List<TrasladoInternoDet> trasDet,Boolean save, boolean isRequerimientoPaciente) throws Exception;
	public void delete(String app, TrasladoInternoDet trasDet) throws Exception;
	public void updateAnul(String app, TrasladoInternoCab trasCab)throws Exception;
	public List<TrasladoInternoCab> list(String app, Date fechaInicio, Date fechaFin,Almacen alOrigen,Almacen alDestino) throws Exception;
	public List<TrasladoInternoDet> list(String app, TrasladoInternoCab trasCab)throws Exception;
	public List<TrasladoInternoCab> listRequerimientosPaciente(String app, Date fechaInicio, Date fechaFin, Almacen alOrigen, Almacen alDestino)
			throws Exception;
	public String codigo(String app, String codigo,Integer almacenId)throws Exception;
	public String getTrasladoPDF(String app, Integer ventaId, String usuarioId) throws Exception;
}
