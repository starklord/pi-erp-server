package pi.server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import pi.service.model.finanza.Banco;
import pi.service.model.finanza.Caja;
import pi.service.model.finanza.Recibo;
import pi.service.model.finanza.ReciboTipo;
import pi.service.model.logistica.Orden;
import pi.service.model.logistica.OrdenCompra;
import pi.service.model.venta.OrdenVenta;
import pi.service.factory.Numbers;
import pi.server.Server;
import pi.server.db.Query;
import pi.server.db.Update;
import pi.service.FinanzaService;
import pi.server.db.server.CRUD;
import pi.server.factory.Services;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/FinanzaService")
public class FinanzaServiceImpl extends HessianServlet implements FinanzaService {

	@Override
	public List<Banco> listBancos(String app) throws Exception {
		return CRUD.list(app,Banco.class, "order by nombre asc");
	}

	@Override
	public Banco saveOrUpdateBanco(String app, boolean save, Banco banco) throws Exception {
		if (save) {
			CRUD.save(app,banco);
		} else {
			CRUD.update(app,banco);
		}
		return banco;
	}

	@Override
	public void deleteBanco(String app, Banco banco) throws Exception {
		CRUD.delete(app, banco);
	}

	@Override
	public List<Caja> listCajas(String app, boolean onlyActives) throws Exception {
		String[] require = { "sucursal" };
		String filter = onlyActives ? "where a.activo is true" : "";
		return CRUD.list(app,Caja.class, require, filter + " order by nombre asc");
	}

	@Override
	public List<Caja> listCajas(String app, boolean onlyActives, int sucursalId) throws Exception {
		String[] require = { "sucursal" };
		String filter = onlyActives ? "where a.activo is true" : "";
		return CRUD.list(app,Caja.class, require, filter + " and sucursal = " + sucursalId + " order by nombre asc");
	}

	@Override
	public Caja saveOrUpdateCaja(String app, boolean save, Caja caja) throws Exception {
		if (save) {
			CRUD.save(app,caja);
		} else {
			CRUD.update(app,caja);
		}
		return caja;
	}

	@Override
	public void deleteCaja(String app, Caja object) throws Exception {
		CRUD.delete(app, object);

	}

	@Override
	public List<ReciboTipo> listRecibosTipo(String app, boolean ingreso) throws Exception {
		String filter = "where ingreso is " + ingreso + " order by nombre asc";
		return CRUD.list(app,ReciboTipo.class, filter);
	}

	@Override
	public Recibo getLastRecibo(String app, int sucursalId, char movimiento, int cajaId) throws Exception {
		String require[] = { "sucursal", "caja", "moneda" };
		String filter = "where a.sucursal = " + sucursalId + " and movimiento = '" + movimiento
				+ "' and caja = " + cajaId
				+ " order by numero desc limit 1";
		List<Recibo> list = CRUD.list(app,Recibo.class, require, filter);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Recibo saveOrUpdateRecibo(String app, boolean save, Recibo recibo) throws Exception {
		if (save) {
			Recibo lastRecibo = getLastRecibo(app,recibo.sucursal.id, recibo.movimiento,recibo.caja.id);
			int numero = lastRecibo == null ? 1 : lastRecibo.numero + 1;
			recibo.numero = numero;
			Orden ov = recibo.orden; 
			BigDecimal totalAux;
			if (ov != null) {
				ov = Services.getOrden().getOrden(app, ov.id);
				if (ov.total.compareTo(ov.total_cobrado.add(recibo.total)) < 0) {
					throw new Exception("La orden de venta tiene un saldo menor de cobranza al total del recibo");
				}
				if (ov.moneda.id.intValue() != recibo.moneda.id) {
					if (recibo.moneda.id == 1) {
						totalAux = Numbers.divide(recibo.total, recibo.tipo_cambio, 4);
						System.out.println("TOTAL AUX " + totalAux);
						ov.total_cobrado = ov.total_cobrado.add(totalAux).setScale(2, RoundingMode.HALF_UP);
					} else {
						totalAux = recibo.total.multiply(recibo.tipo_cambio);
						System.out.println("TOTAL AUX " + totalAux);
						ov.total_cobrado = ov.total_cobrado.add(totalAux).setScale(2, RoundingMode.HALF_UP);
					} 
				} else {
					ov.total_cobrado = ov.total_cobrado.add(recibo.total).setScale(2, RoundingMode.HALF_UP);
				}
				CRUD.update(app,ov);
			}
			CRUD.save(app,recibo);
		} else {
			if (!recibo.activo) {
				Orden ov = recibo.orden;
				BigDecimal totalAux;
				if (ov != null) {
					if (ov.moneda.id.intValue() != recibo.moneda.id) {
						if (recibo.moneda.id == 1) {
							totalAux = Numbers.divide(recibo.total, recibo.tipo_cambio, 4);
							System.out.println("TOTAL AUX " + totalAux);
							ov.total_cobrado = ov.total_cobrado.add(totalAux).setScale(2, RoundingMode.HALF_UP);
						} else {
							totalAux = recibo.total.multiply(recibo.tipo_cambio);
							System.out.println("TOTAL AUX " + totalAux);
							ov.total_cobrado = ov.total_cobrado.add(totalAux).setScale(2, RoundingMode.HALF_UP);
						}
					} else {
						ov.total_cobrado = ov.total_cobrado.subtract(recibo.total).setScale(2, RoundingMode.HALF_UP);
					}
					CRUD.update(app,ov);
				}
			}
			CRUD.update(app,recibo);
		}
		return recibo;
	}

	@Override
	public List<Recibo> listRecibos(String app, Date inicio, Date fin, int cajaId, int monedaId, int turno, String movimiento) throws Exception {
		String require[] = { "sucursal", "caja", "moneda", "recibo_tipo", "orden_venta", "orden_venta.vendedor",
				"orden_venta.vendedor.persona", "orden_compra", "letra", "area" };
		String filter = "where a.fecha between '" + inicio + "' and '" + fin + "' and a.moneda = " + monedaId;
		if (cajaId != -1) {
			filter += " and a.caja = " + cajaId;
		}
		if (turno != -1) {
			filter += " and a.turno = " + turno;
		}
		if(movimiento == Server.EGRESOS){
			filter+=" and movimiento = 'E'";
		}
		if(movimiento == Server.INGRESOS){
			filter+=" and movimiento = 'I'";
		}
		 filter += " order by movimiento desc";
		return CRUD.list(app,Recibo.class, require, filter);
	}

	@Override
	public BigDecimal getSaldoInicial(String app, int cajaId, int monedaId, Date fechaLimite) {

		try {
			Query query = new Query(app,null);
			String select = "select SUM(CASE WHEN movimiento = 'I' THEN total ELSE 0 END) as ingresos,\r\n" +
					"SUM(CASE WHEN movimiento = 'E' THEN total ELSE 0 END) as egresos,\r\n" +
					"SUM(CASE WHEN movimiento = 'I' THEN total ELSE 0 END) -\r\n" +
					"SUM(CASE WHEN movimiento = 'E' THEN total ELSE 0 END) as saldo\r\n" +
					"from finanza.recibo\r\n";
			query.select.set(select);
			query.where = " where activo is true and fecha <'" + fechaLimite.toString()
					+ "' and moneda=" + monedaId
					+ " and cerrado is false";
			if (cajaId != -1) {
				query.where += " and caja = " + cajaId;
			}

			Object[][] rs = query.initResultSet();

			if (rs.length == 0) {
				return BigDecimal.ZERO;
			}
			BigDecimal saldoInicial = (BigDecimal) rs[0][2];
			return saldoInicial == null ? BigDecimal.ZERO : saldoInicial;
		} catch (Exception ex) {
			ex.printStackTrace();
			return BigDecimal.ZERO;
		}

	}

	@Override
	public void annul(String app, Recibo recibo) throws Exception {
		try {
			recibo.activo = false;
			CRUD.update(app,recibo);
			BigDecimal totalAux;
			if (recibo.orden != null) {
				recibo.orden = Services.getOrden().getOrden(app, recibo.orden.id);
				if (recibo.orden.moneda.id.intValue() != recibo.moneda.id) {
					if (recibo.moneda.id == 1) {
						totalAux = Numbers.divide(recibo.total, recibo.tipo_cambio, 4);
						System.out.println("TOTAL AUX " + totalAux);
						recibo.orden.total_cobrado = recibo.orden.total_cobrado.subtract(totalAux)
								.setScale(2, RoundingMode.HALF_UP);
					} else {
						totalAux = recibo.total.multiply(recibo.tipo_cambio);
						System.out.println("TOTAL AUX " + totalAux);
						recibo.orden.total_cobrado = recibo.orden.total_cobrado.subtract(totalAux)
								.setScale(2, RoundingMode.HALF_UP);
					}
				} else {
					recibo.orden.total_cobrado = recibo.orden.total_cobrado.subtract(recibo.total)
							.setScale(2, RoundingMode.HALF_UP);
				}
				CRUD.update(app,recibo.orden);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public void cerrarCaja(String app, Date fecha, int cajaId, int monedaId) throws Exception {
		List<Recibo> list = listRecibos(app, fecha, fecha, cajaId, monedaId, -1, Server.INGRESOS_EGRESOS);
		for (Recibo r : list) {
			r.cerrado = true;
			CRUD.update(app,r);
		}
	}

	@Override
	public List<Recibo> listRecibosByOrdenVenta(String app, int ordenId) throws Exception {
		String require[] = { "sucursal", "caja", "moneda", "recibo_tipo", "orden_venta", "orden_venta.vendedor",
				"orden_venta.vendedor.persona", "orden_compra", "letra" };
		String filter = "where f.id = " + ordenId;
		return CRUD.list(app,Recibo.class, require, filter);
	}

}
