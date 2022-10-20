package pi.service.model.auxiliar;

import java.io.Serializable;
import java.math.BigDecimal;

import pi.service.model.almacen.PlantillaTransformacionDet;

public class PlantillaTransformacionDetModel implements Serializable {
	
	public PlantillaTransformacionDet plantillaDet;
	public BigDecimal stock;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plantillaDet == null) ? 0 : plantillaDet.hashCode());
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
		PlantillaTransformacionDetModel other = (PlantillaTransformacionDetModel) obj;
		if (plantillaDet == null) {
			if (other.plantillaDet != null)
				return false;
		} else if (!plantillaDet.equals(other.plantillaDet))
			return false;
		return true;
	}
	
	

}
