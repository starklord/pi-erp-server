package pi.service;
import java.util.Date;
import java.util.List;

import pi.service.model.logistica.PlantillaTransformacion;
import pi.service.model.logistica.PlantillaTransformacionDet;
import pi.service.model.logistica.Transformacion;

public interface TransformacionService {
	
	
	public Transformacion save(String app, Transformacion entity);
    public Transformacion update(String app, Transformacion entity);
    public Transformacion delete(String app, Transformacion entity);
    public List<Transformacion> list(String app, Date inicio, Date fin, String observaciones);

	public Transformacion getLastTransformacion(String app);

	public PlantillaTransformacion getLastPlantilla(String app);
	
	public PlantillaTransformacion getLastPlantillaByProducto(String app, int productoId);
	
	public List<PlantillaTransformacion> listPlantilla(String app);
	
	public List<PlantillaTransformacion> listPlantilla(String app, int productoId);
	
	public List<PlantillaTransformacion> listPlantilla(String app, Date inicio, Date fin, Integer productoId);
	
	public List<PlantillaTransformacionDet> listPlantillaDetalles(String app, int transformacionId);
	
	public PlantillaTransformacion saveOrUpdatePlantilla(String app, boolean save, PlantillaTransformacion trans, List<PlantillaTransformacionDet> detalles) throws Exception;


}
