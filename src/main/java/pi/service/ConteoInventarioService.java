package pi.service;

import java.util.List;

import pi.service.model.almacen.ConteoInventario;

public interface ConteoInventarioService {
    
    public ConteoInventario save(String app, ConteoInventario entity) throws Exception ;
    public void delete(String app, ConteoInventario entity) throws Exception ;
    public ConteoInventario update(String app, ConteoInventario entity) throws Exception ;
    public List<ConteoInventario> list(String app, String usuario, int laboratorioId, String lote);
}
