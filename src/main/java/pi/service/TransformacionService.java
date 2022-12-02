package pi.service;
import java.util.Date;
import java.util.List;

import pi.service.model.logistica.PlantillaTransformacion;
import pi.service.model.logistica.PlantillaTransformacionDet;

public interface TransformacionService {
	
	public PlantillaTransformacion getLastPlantilla(String app);
	
	public PlantillaTransformacion getLastPlantillaByProducto(String app, int productoId);
	
	public List<PlantillaTransformacion> listPlantilla(String app);
	
	public List<PlantillaTransformacion> listPlantilla(String app, Date inicio, Date fin);
	
	public List<PlantillaTransformacionDet> listPlantillaDetalles(String app, int transformacionId);
	
	public PlantillaTransformacion saveOrUpdatePlantilla(String app, boolean save, PlantillaTransformacion trans, List<PlantillaTransformacionDet> detalles) throws Exception;

}
