package pi.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import pi.service.model.finanza.Banco;
import pi.service.model.finanza.Caja;
import pi.service.model.finanza.Recibo;
import pi.service.model.finanza.ReciboTipo;

public interface FinanzaService{
	
	public List<Banco> listBancos(String app) throws Exception;
	public Banco saveOrUpdateBanco(String app,boolean save, Banco banco) throws Exception;
	public void deleteBanco(String app, Banco banco) throws Exception;
	public List<Caja> listCajas(String app, boolean onlyActives) throws Exception;
	public List<Caja> listCajas(String app, boolean onlyActives, int sucursalId) throws Exception;
	public Caja saveOrUpdateCaja(String app, boolean save, Caja object) throws Exception;
	public void deleteCaja(String app, Caja object) throws Exception;
	
	//para los recibos

	public void annul(String app, Recibo recibo) throws Exception;
	public BigDecimal getSaldoInicial(String app, int cajaId,int monedaId, Date fechaLimite);
	public Recibo getLastRecibo(String app, int sucursalId, char movimiento, int cajaId) throws Exception;
	public List<ReciboTipo> listRecibosTipo(String app, boolean ingreso) throws Exception;
	public List<Recibo> listRecibos(String app, Date inicio, Date fin, int cajaId, int monedaId, int turno, String movimiento) throws Exception;
	public Recibo saveOrUpdateRecibo(String app, boolean save, Recibo recibo) throws Exception;

	public void cerrarCaja(String app, Date fecha, int cajaId, int monedaId) throws Exception;
	
	public List<Recibo> listRecibosByOrdenVenta(String app, int ordenId) throws Exception;

}
