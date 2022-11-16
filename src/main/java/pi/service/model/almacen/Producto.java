package pi.service.model.almacen;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.db.client.TableDB;
import pi.service.factory.Numbers;
import pi.service.model.Impuesto;
import pi.service.model.Moneda;
import pi.service.util.Util;

@TableDB(name="logistica.producto")
public class Producto implements Serializable {
	
	public Integer id;
	public String creador;
	public Boolean activo;
	public String codigo;
	public Integer codigo_interno;
	public String codigo_ubicacion;
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
	public Boolean garantia;
	public Moneda moneda;
	public BigDecimal precio;
	public BigDecimal costo_ultima_compra;
	public String codigo_barras1;
	public String codigo_barras2;
	public Character procedencia;
	public String cod_dig;
	public String registro_sanitario;
	public Character tipo_control;//Producto Articulo Servicio
	
	@Override
	public String toString() {
		return nombre;
	}

	public String getEstado(){
		return activo?Util.OK:Util.ANULADO;
	}

	public String getControl(){
		String str = Util.PRODUCTO;
		if(this.tipo_control==Util.TIPO_CONTROL_ARTICULO){
			str = Util.ARTICULO;
		}
		if(this.tipo_control==Util.TIPO_CONTROL_SERVICIO){
			str = Util.SERVICIO;
		}
		return str;
	}

	public String getCodigo(){
		return Util.completeWithZeros(codigo,6);
	}

	public String getCodigoUbicacion(){
		return codigo_ubicacion;
	}

	public String getMarcaStr(){
		return marca.toString();
	}

	public String getLineaStr(){
		return linea.toString();
	}

	public String getNombre(){
		return nombre;
	}

	public String getUnidadStr(){
		return unidad.nombre;
	}

	public String getMonedaStr(){
		return moneda.simbolo;
	}

	public BigDecimal getPrecio(){
		return Numbers.getBD(precio, 2);
	}

	public BigDecimal getCosto(){
		return Numbers.getBD(costo_ultima_compra, 2);
	}

	public BigDecimal getStock(){
		return Numbers.getBD(BigDecimal.ZERO, 2);
	}

	public BigDecimal getCostoTotal(){
		return Numbers.getBD(precio.multiply(costo_ultima_compra), 2);
	}

	public String getProcedencia(){
		return procedencia=='N'?"Nacional":"Importado";
	}

	public String getCodigoDigemid(){
		return cod_dig;
	}

	public String getRegistroSanitario(){
		return registro_sanitario;
	}

	public String getBarCode(){
		return codigo_barras1+" " + codigo_barras2;
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
