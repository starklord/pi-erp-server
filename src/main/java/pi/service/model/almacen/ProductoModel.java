package pi.service.model.almacen;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import pi.service.factory.Numbers;
import pi.service.util.Util;

public class ProductoModel implements Serializable {
    public Producto producto;
    public List<Articulo> articulos;
    public BigDecimal stock = BigDecimal.ZERO;

    public String getEstado(){
		return producto.activo?Util.OK:Util.ANULADO;
	}

	public String getControl(){
		String str = Util.PRODUCTO;
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
		return Numbers.getBD(BigDecimal.ZERO, 2);
	}

	public BigDecimal getCostoTotal(){
		return Numbers.getBD(stock.multiply(producto.costo_ultima_compra), 2);
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
        result = prime * result + ((producto == null) ? 0 : producto.hashCode());
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
        ProductoModel other = (ProductoModel) obj;
        if (producto == null) {
            if (other.producto != null)
                return false;
        } else if (!producto.equals(other.producto))
            return false;
        return true;
    }

    
    
}
