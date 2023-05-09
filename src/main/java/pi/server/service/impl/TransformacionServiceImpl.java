package pi.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.App;
import pi.server.db.Update;
import pi.server.db.server.CRUD;
import pi.service.TransformacionService;
import pi.service.model.logistica.PlantillaTransformacion;
import pi.service.model.logistica.PlantillaTransformacionDet;
import pi.service.model.logistica.Transformacion;

@WebServlet("pi/TransformacionService")
public class TransformacionServiceImpl extends HessianServlet implements TransformacionService {

    @Override
    public PlantillaTransformacion getLastPlantilla(String app) {
        List<PlantillaTransformacion> list = new ArrayList<>();
        
        try {
            list = CRUD.list(app, PlantillaTransformacion.class, " order by numero desc limit 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public PlantillaTransformacion getLastPlantillaByProducto(String app, int productoId) {
        List<PlantillaTransformacion> list = new ArrayList<>();

        try {
            list = CRUD.list(app, PlantillaTransformacion.class,
            		" where producto = " + productoId + " and a.activo is true limit 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PlantillaTransformacion> listPlantilla(String app) {
        return listPlantilla(app, null, null,null);
    }

    @Override
    public List<PlantillaTransformacion> listPlantilla(String app, int productoId) {
        return listPlantilla(app, null, null, productoId);
    }

    @Override
    public List<PlantillaTransformacion> listPlantilla(String app, Date inicio, Date fin, Integer productoId) {
        String[] require = { "producto" };
		String filter = "where a.id is not null ";
		if (inicio != null) {
			filter += " and fecha between '" + inicio + "' and '" + fin + "'";
		}
        if (productoId != null) {
			filter += " and producto = " + productoId;
		}

		filter += " order by numero desc";
        List<PlantillaTransformacion> list = new ArrayList<>();

		try {
            list = CRUD.list(app, PlantillaTransformacion.class, require, filter);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
		return list;
    }

    @Override
    public List<PlantillaTransformacionDet> listPlantillaDetalles(String app, int plantillaId) {
        String[] require = { "plantilla_transformacion", "producto" };
		String filter = "where plantilla_transformacion = " + plantillaId;
        List<PlantillaTransformacionDet> list = new ArrayList<>();
		try {
            list = CRUD.list(app, PlantillaTransformacionDet.class, require, filter);
        } catch (Exception e) {
            e.printStackTrace();  
        }
		return list;
    }

    @Override
    public PlantillaTransformacion saveOrUpdatePlantilla(String app, boolean save, PlantillaTransformacion trans,
            List<PlantillaTransformacionDet> detalles) throws Exception {
                try {
                    Update.beginTransaction(app);
                    if (save) {
                        PlantillaTransformacion transLast = getLastPlantilla(app);
                        int numero = transLast == null ? 1 : transLast.numero + 1;
                        trans.numero = numero;
                        CRUD.save(app, trans);
                        for (PlantillaTransformacionDet det : detalles) {
                            det.id = null;
                            det.plantilla_transformacion = trans;
                            CRUD.save(app, det);
                        }
                    } else {
                        CRUD.update(app, trans);
                        CRUD.execute(app, "delete from logistica.plantilla_transformacion_det where plantilla_transformacion = "
                                + trans.id);
                        for (PlantillaTransformacionDet det : detalles) {
                            det.id = null;
                            det.plantilla_transformacion = trans;
                            CRUD.save(app, det);
                        }
        
                    }
                    Update.commitTransaction(app);
                    return trans;
                } catch (Exception ex) {
                    Update.rollbackTransaction(app);
                    ex.printStackTrace();
                    throw new Exception(ex.getMessage());
        
                }
    }

    @Override
    public Transformacion save(String app, Transformacion entity) {
        entity.setNumero(getNextNumero());
        App.getDB().getTransformaciones().add(entity);
        App.store(App.getDB().getTransformaciones());
        return entity;
    }

    @Override
    public Transformacion update(String app, Transformacion entity) {
        // int index = App.getDB().getTransformaciones().indexOf(entity);
        // if (index>=0){
        //     App.getDB().getTransformaciones().set(index, entity);
        // }
        // App.store(App.getDB().getTransformaciones());
        int size = App.getDB().getTransformaciones().size();
        for(int i = size ; i < (size+100000); i++){
            Transformacion trans = new Transformacion();
            trans.detalles = new ArrayList<>();
            trans.detalles.addAll(entity.detalles);
            trans.creador = "prueba"+i;
            trans.numero=i;
            trans.fecha = entity.fecha;
            trans.observaciones = "nueva observacion " + i + " datos adicionales: " + entity.fecha.toString();
            trans.setNumero(getNextNumero());
            App.getDB().getTransformaciones().add(trans);
        }
        App.store(App.getDB().getTransformaciones());
        return entity;
    }

    @Override
    public Transformacion delete(String app, Transformacion entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Transformacion> list(String app, Date inicio, Date fin, String observaciones) {
        List<Transformacion> list = new ArrayList<>();
        list = App.getDB().getTransformaciones().stream().filter(e->{
            return e.getFecha().after(inicio) && 
            e.getFecha().before(fin) && 
            e.getObservaciones().toLowerCase().contains(observaciones.toLowerCase());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public Transformacion getLastTransformacion(String app) {
        List<Transformacion> list = new ArrayList<>();
        list = App.getDB().getTransformaciones().stream().filter(e->{
            return e.getNumero().intValue()==App.getDB().getTransformacionNumero();
        }).collect(Collectors.toList());
        return list.isEmpty()?null:list.get(0);
    }

    private Integer getNextNumero() {
        App.getDB().setTransformacionNumero(App.getDB().getTransformacionNumero()+1);
        App.storeRoot();
        return App.getDB().getTransformacionNumero();
    }
    
}
