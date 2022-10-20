package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.model.Utilidad;
import pi.service.model.empresa.Empresa;
import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

@TableDB(name="logistica.producto")
public class Producto implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String codigo;
	public String codigo_ubicacion;
	public Integer codigo_interno;
	public String nombre;
	public String descripcion;
	public Marca marca;
	public Linea linea;
	public Unidad unidad;
	public Unidad unidad_conversion;
	public BigDecimal factor_conversion;
	public BigDecimal peso;
	public Impuesto impuesto;
	
	public BigDecimal stock_minimo;
	public BigDecimal contenido;
	public Boolean es_servicio;
	public Boolean garantia;
	public Moneda moneda;
	public BigDecimal precio;
	public BigDecimal precio_promocion;
	public BigDecimal precio_distribuidor;
	public Empresa empresa;
	public BigDecimal costo_ultima_compra;
	public Integer lote;
	public String concentracion;
	public String presentacion;
	public String accion_farmacologica;
	public String laboratorio;
	public String codigo_barras1;
	public String codigo_barras2;
	public String codigo_barras3;
	
	@Override
	public String toString() {
		return nombre;
	}
	
	public String toCodigo() {
		return nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
