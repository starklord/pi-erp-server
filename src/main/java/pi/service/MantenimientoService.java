package pi.service;

import pi.service.model.empresa.Empresa;
import pi.service.model.empresa.Sucursal;

public interface MantenimientoService {

    public void importarDocumentosPago(String app, int limite) throws Exception;
    public void ActualizarAnulados(String app) throws Exception;

    public void saveMantenimientoSistema(String app, Empresa empresa, Sucursal sucursal) throws Exception;
    
}
