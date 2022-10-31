package pi.service;

import java.util.Date;
import java.util.List;

import pi.service.model.parroquia.ConstanciaBautismo;
import pi.service.model.parroquia.ConstanciaConfirmacion;
import pi.service.model.parroquia.ConstanciaMatrimonio;

public interface ConstanciaService {

    public ConstanciaBautismo getConstanciaBautismo(String app, int id);
    public ConstanciaBautismo saveOrUpdateConstanciaBautismo(String app, boolean save, ConstanciaBautismo constancia) throws Exception ;
    public List<ConstanciaBautismo> listConstanciasBautismo(
        String app,String filtroFecha, Date inicio, Date fin, String serie, Integer numero,
        String bautizado, String padre, String madre);



    public ConstanciaConfirmacion getConstanciaConfirmacion(String app, int id);
    public ConstanciaConfirmacion saveOrUpdateConstanciaConfirmacion(String app, boolean save, ConstanciaConfirmacion constancia) throws Exception;
    public List<ConstanciaConfirmacion> listConstanciasConfirmacion(
        String app,String filtroFecha, Date inicio, Date fin, String serie, Integer numero,
        String confirmante, String padre, String madre);
    
    public ConstanciaMatrimonio getConstanciaMatrimonio(String app, int id);
    public ConstanciaMatrimonio saveOrUpdateConstanciaMatrimonio(String app, boolean save, ConstanciaMatrimonio constancia) throws Exception ;
    public List<ConstanciaMatrimonio> listConstanciasMatrimonio(
        String app,String filtroFecha, Date inicio, Date fin, String serie, Integer numero,
        String esposo, String esposa);
    
}
