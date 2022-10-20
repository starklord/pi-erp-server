package pi.service.model.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.model.almacen.PlantillaTransformacionDet;
import pi.service.model.almacen.TransformacionDet;

public class TransformacionDetModel implements Serializable {
	
	public TransformacionDet transformacionDet;
	public BigDecimal stock;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
		TransformacionDetModel other = (TransformacionDetModel) obj;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}

	
	
}
