package pi.service.model.finanza;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.FieldDB;
import pi.service.db.client.TableDB;
import pi.service.model.Moneda;
import pi.service.model.empresa.Empresa;


@TableDB(name="finanza.letra")
public class Letra implements Serializable{

	public Integer id;
	public String creador;
	public Boolean activo;
	public Empresa empresa;
	public Integer numero;
	public Date fecha;
	
	@FieldDB("fecha_vencimiento")
	public Date fechaVencimiento;
	public Moneda moneda;
	public BigDecimal monto;
	
	@FieldDB("monto_pagado")
	public BigDecimal montoPagado;
	public Banco banco;
	
	
}
