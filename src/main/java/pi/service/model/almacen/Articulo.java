package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.logistica.OrdenArt;
import pi.service.util.Util;

@TableDB(name="logistica.articulo")
public class Articulo implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public Producto producto;
	public OrdenArt orden_art;
	public String serie;
	public String lote;
	public Date fecha_vencimiento;
	
	@Override
	public String toString() {
		return serie;
	}

	public String getEstado(){
		return activo?Util.OK:Util.ANULADO;
	}

	public String getControl(){
		String str = Util.PRODUCTO;
		if(producto.tipo_control==Util.TIPO_CONTROL_ARTICULO){
			str = Util.ARTICULO;
		}
		if(producto.tipo_control==Util.TIPO_CONTROL_SERVICIO){
			str = Util.SERVICIO;
		}
		return str;
	}

	public String getCodigo(){
		return Util.completeWithZeros(producto.codigo,6);
	}

	public String getCodigoUbicacion(){
		return producto.codigo_ubicacion;
	}

	public String getMarcaStr(){
		return producto.marca.toString();
	}

	public String getLineaStr(){
		return producto.linea.toString();
	}

	public String getNombre(){
		return producto.nombre;
	}

	public String getUnidadStr(){
		return producto.unidad.nombre;
	}

	public String getMonedaStr(){
		return producto.moneda.simbolo;
	}

	public BigDecimal getPrecio(){
		return Numbers.getBD(producto.precio, 2);
	}

	public BigDecimal getCosto(){
		return Numbers.getBD(producto.costo_ultima_compra, 2);
	}

	public BigDecimal getStock(){
		return Numbers.getBD(orden_art.stock, 2);
	}

	public BigDecimal getCostoTotal(){
		return Numbers.getBD(orden_art.stock.multiply(producto.costo_ultima_compra), 2);
	}

	public String getProcedencia(){
		return producto.procedencia=='N'?"Nacional":"Importado";
	}

	public String getCodigoDigemid(){
		return producto.cod_dig;
	}

	public String getRegistroSanitario(){
		return producto.registro_sanitario;
	}

	public String getBarCode(){
		return producto.codigo_barras1+" " + producto.codigo_barras2;
	}
	
	public String toCodigo() {
		return producto.nombre;
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
		Articulo other = (Articulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
