package pi.server.service.impl;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import pi.service.model.empresa.Empresa;
import pi.service.model.logistica.OrdenCompraDet;
import pi.service.model.logistica.Proveedor;
import pi.service.model.persona.Persona;
import pi.server.db.Update;
import pi.service.ProveedorService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/ProveedorService")
public class ProveedorServiceImpl extends HessianServlet implements ProveedorService{
	
	@Override
	public List<Proveedor> list(String app, int empresaId) throws Exception {
		String[] req = {"id","empresa"};
		String filter = "where empresa="+empresaId+" order by b.apellidos asc";
		return CRUD.list(app,Proveedor.class,req, filter);
	}

	@Override
	public List<Proveedor> listActives(String app, int empresaId) throws Exception {
		String[] req = {"id","empresa"};
		String filter = "where empresa="+empresaId+" and a.activo is true order by b.apellidos asc";
		return CRUD.list(app,Proveedor.class,req, filter);
	}

	@Override
	public OrdenCompraDet getLastCompra(String app, int proveedorId, int productoId) throws Exception {
		String[] req = {"ordenCompra","ordenCompra.direccionProveedor","producto","ordenCompra.moneda"};
		String filter = "where c.persona = " + proveedorId +" and d.id = " + productoId+
						" order by a.id desc limit 1";
		List<OrdenCompraDet> list = CRUD.list(app,OrdenCompraDet.class,req,filter);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
		
	}
	
	@Override
	public Proveedor getByIdentificador(String app, String identificador) throws Exception {
		String[] req = {"id"};
		String filter 	= "where b.identificador ='" + identificador +"' "
				+ "order by b.apellidos asc";
		List<Proveedor> list = CRUD.list(app,Proveedor.class, req, filter);
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public void delete(String app, Proveedor proveedor) throws Exception {
		CRUD.delete(app, proveedor);
	}

	@Override
	public Proveedor saveOrUpdate(String app, boolean save, Proveedor object) throws Exception {
		if(save){
			CRUD.save(app,object);
		}else{
			CRUD.update(app,object);
		}
		return object;
	}
	
	private void readFile(String app) throws Exception {
		try {
			
			String iso = "ISO-8859-1";
			String utf = "UTF-8";
			File file = new File("C:/Users/StarkLord/OneDrive/empresas/shirley/proveedores_ruc.txt");
			Scanner scan = new Scanner(file, iso);
			scan.useDelimiter("\n");
			ProveedorServiceImpl proveedorService = new ProveedorServiceImpl();
			PersonaServiceImpl personaService = new PersonaServiceImpl();
			while (scan.hasNext()) {
				String line = scan.next();
				Scanner scanLine = new Scanner(line);
				scanLine.useDelimiter("\t");
				String ruc = scanLine.next().trim();
				if(ruc.length()!=11){
					continue;
				}
				
				Proveedor prov = proveedorService.getByIdentificador(app, ruc);
				if(prov!=null){
					continue;
				}
				List<Persona> list = personaService.getListByIdentificador(app, ruc);
				Persona p = list.get(0);
				Proveedor proveedor = new Proveedor();
				proveedor.activo = true;
				proveedor.creador = "root";
				proveedor.empresa = new Empresa();
				proveedor.empresa.id  = 0;
				proveedor.id = p;
				
				proveedorService.saveOrUpdate(app, true, proveedor);
				scanLine.close();
			}
			scan.close();
			

		} catch (Exception ex) {
			ex.printStackTrace();
			
			throw new Exception(ex.getMessage());

		}
	}
	
	@Override public void importProveedoresFromTxt(String app) throws Exception {
		readFile(app);
	}

	

}
