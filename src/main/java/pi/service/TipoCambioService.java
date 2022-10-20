package pi.service;

import java.util.List;
import pi.service.model.TipoCambio;

public interface TipoCambioService{
	
	public List<TipoCambio> list(String app) throws Exception;
	public TipoCambio getLastTipoCambio(String app) throws Exception;
	public TipoCambio saveOrUpdate(String app, boolean save, TipoCambio tipoCambio) throws Exception;

}
