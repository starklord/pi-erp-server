package pi.service;

import java.util.Date;
import java.util.List;
import pi.service.model.almacen.Ensamblador;
import pi.service.model.almacen.PlantillaTransformacion;
import pi.service.model.almacen.PlantillaTransformacionDet;
import pi.service.model.almacen.Transformacion;
import pi.service.model.almacen.TransformacionDet;
import pi.service.model.auxiliar.PlantillaTransformacionDetModel;
import pi.service.model.auxiliar.TransformacionDetModel;


public interface TransformacionService{
	public void annul(String app, Transformacion trans) throws Exception ;
        public List<PlantillaTransformacionDetModel> listPlantillaDetallesByProducto(String app, int productoId, int almacenId)
			throws Exception;
        public List<TransformacionDetModel> listDetallesByModel(String app, int transformacionId) throws Exception ;
	public Transformacion get(String app, int transformacionId) throws Exception;
	public Transformacion getLast(String app, int empresaId) throws Exception;
	
	public List<Transformacion> list(String app, int empresaId) throws Exception;
	
	public List<Transformacion> list(String app, int empresaId, int ensambladorId, Date inicio, Date fin) throws Exception;
	
	public List<TransformacionDet> listDetalles(String app, int transformacionId) throws Exception;
	
	public Transformacion saveOrUpdate(String app, boolean save, Transformacion trans, List<TransformacionDet> detalles) throws Exception;
	
	public PlantillaTransformacion getLastPlantilla(String app, int empresaId) throws Exception;
	
	public PlantillaTransformacion getLastPlantillaByProducto(String app, int productoId) throws Exception;
	
	public List<PlantillaTransformacion> listPlantilla(String app, int empresaId) throws Exception;
	
	public List<PlantillaTransformacion> listPlantilla(String app, int empresaId, Date inicio, Date fin) throws Exception;
	
	public List<PlantillaTransformacionDet> listPlantillaDetalles(String app, int transformacionId) throws Exception;
	
	public List<PlantillaTransformacionDet> listPlantillaDetallesByProducto(String app, int productoId) throws Exception;
	
	public PlantillaTransformacion saveOrUpdatePlantilla(String app, boolean save, PlantillaTransformacion trans, List<PlantillaTransformacionDet> detalles) throws Exception;
	
	
	
	public List<Ensamblador> listEnsambladores(String app, int empresaId) throws Exception;
	
	public Ensamblador saveOrUpdateEnsamblador(String app, boolean save, Ensamblador ensamblador) throws Exception;
	
	public void sendToPrint(String app, int transformacionId, String user) throws Exception;

}
