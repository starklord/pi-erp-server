package pi.server.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import pi.service.model.DocumentoIdentidad;
import pi.service.model.persona.Direccion;
import pi.service.model.persona.Persona;
import pi.server.db.Update;
import pi.service.DireccionService;
import pi.service.db.server.CRUD;
import pi.service.util.Util;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/DireccionService")
public class DireccionServiceImpl extends HessianServlet implements DireccionService {

	public static Class table = Direccion.class;

	@Override
	public void annul(String app, int personaId) throws Exception {
		try {
			CRUD.execute(app, "delete from persona.direccion where persona = " + personaId);
			CRUD.execute(app, "delete from persona.persona where id = " + personaId);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}

	}

	@Override
	public void save(String app, Direccion direccion) throws Exception {
		CRUD.save(app,direccion);
	}

	@Override
	public void save(String app, Persona persona, Direccion direccion) throws Exception {
		try {
			Update.beginTransaction(app);
			CRUD.save(app,persona);
			direccion.persona = persona;
			CRUD.save(app,direccion);
			Update.commitTransaction(app);
		} catch (Exception ex) {
			Update.rollbackTransaction(app);
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public void update(String app, Direccion direccion) throws Exception {
		CRUD.update(app,direccion);
	}

	@Override
	public void saveOrUpdate(String app, Direccion direccion) throws Exception {
		if (direccion.id == null) {
			CRUD.save(app,direccion);
		} else {
			CRUD.update(app,direccion);
		}

	}

	@Override
	public List<Direccion> getList(String app, int personaId) {
		List<Direccion> list = new ArrayList<>();
		try {
			String[] required = {
					"persona"
			};
			list = CRUD.list(app,table, required, "where persona = " + personaId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}

	@Override
	public Direccion getDireccionSinCliente(String app) throws Exception {
		String[] required = {
				"persona"
		};
		List<Direccion> list = CRUD.list(app,table, required, "where persona = " + -1);
		return list.isEmpty() ? null : list.get(0);

	}

	@Override
	public List<Direccion> list(String app, String nombres, String apellidos, String identificador) {
		List<Direccion> list = new ArrayList<>();
		try {
			String[] req = {
					"persona",
					"persona.documento_identidad" };
			String filterNames = " and b.nombres ilike '%" + nombres + "%' ";
			if (nombres.isEmpty()) {
				filterNames = " and (b.nombres ilike '%" + nombres + "%' or b.nombres is null) ";
			}

			String filter = "where b.apellidos ilike '%" + apellidos + "%' ";
			filter += filterNames;
			filter += "and b.identificador ilike '%" + identificador + "%' ";

			filter += "order by b.apellidos asc";
			list = CRUD.list(app,Direccion.class, req, filter);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	@Override
	public Direccion getDireccionFromRuc(String app, String ruc) throws Exception {
		URL url = new URL("https://api.sunat.cloud/ruc/" + ruc);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		int responseCode = httpURLConnection.getResponseCode();

		System.out.println("url : " + url);
		System.out.println("Response code: " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);

		}
		in.close();
		System.out.println(response);
		if (response == null)
			return null;

		JSONObject myrespond = new JSONObject(response.toString());
		Direccion direccion = new Direccion();
		Persona persona = new Persona();
		String identificador = myrespond.getString("ruc");
		String razonSocial = myrespond.getString("razon_social");
		String domicilioFiscal = myrespond.getString("domicilio_fiscal");
		PersonaServiceImpl personaService = new PersonaServiceImpl();
		List<Persona> listCoincidencias = personaService.getListByIdentificador(app, identificador);
		if (listCoincidencias.isEmpty()) {
			persona.id = null;
			persona.activo = true;
			persona.apellidos = razonSocial;
			persona.identificador = identificador;
			persona.brevette = "-";
			persona.creador = "-";
			persona.documento_identidad = new DocumentoIdentidad();
			persona.documento_identidad.id = Util.DOCUMENTO_IDENTIDAD_RUC;
			persona.email = "-";
			persona.nombres = "-";
			persona.sexo = 'M';
			persona.telefonos = "-";

			CRUD.save(app,persona);
			direccion.id = null;
			direccion.activo = true;
			direccion.creador = "root";
			direccion.ubigeo = "040101";
			direccion.descripcion = domicilioFiscal;
			direccion.persona = persona;
			direccion.email = "";
			direccion.referencia = "";
			direccion.telefono = "";
			CRUD.save(app,direccion);
			return direccion;
		} else {
			Persona perTemp = listCoincidencias.get(0);
			perTemp.apellidos = razonSocial;
			CRUD.update(app,perTemp);
			DireccionServiceImpl dimpl = new DireccionServiceImpl();
			List<Direccion> listDirTemp = dimpl.getList(app,perTemp.id);
			Direccion dirTemp = listDirTemp.get(0);
			dirTemp.descripcion = domicilioFiscal;
			CRUD.update(app,dirTemp);
			return dirTemp;
		}

	}

	private String getResultMigo(String tipoDocumento, String identificador) throws Exception {
		String token = "5249faef-47f6-4bcd-91b1-ab3efb634c23-40341927-c005-4ad5-abfd-daa64fda5ad6";
		URL url = new URL("https://api.migoperu.pe/api/v1/" + tipoDocumento);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json; utf-8");

		con.setDoOutput(true);

		String jsonInputString = "{\"token\": \"" + token + "\", \"" + tipoDocumento + "\": \"" + identificador + "\"}";

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
		StringBuilder response = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		}
		System.out.println(response.toString());
		return response.toString();
	}

	@Override
	public List<Direccion> getDireccionDNI(String app, String dni) throws Exception {

		PersonaServiceImpl service = new PersonaServiceImpl();
		List<Persona> personas = service.getListByIdentificador(app, dni);
		List<Direccion> direcciones = new ArrayList<>();
		if (!personas.isEmpty()) {
			direcciones = getList(app,personas.get(0).id);
			return direcciones;
		}

		JSONObject myrespond = new JSONObject(getResultMigo("dni", dni));

		DocumentoIdentidad doc = new DocumentoIdentidad();
		doc.activo = true;
		doc.id = Util.DOCUMENTO_IDENTIDAD_DNI;

		Persona persona = new Persona();
		persona.creador = "-";
		persona.activo = true;
		persona.apellidos = myrespond.getString("nombre");
		persona.nombres = "";
		persona.documento_identidad = doc;
		persona.brevette = "-";
		persona.sexo = '-';
		persona.identificador = dni;
		persona.telefonos = "-";
		persona.email = "-";
		persona.nombre_comercial = "-";
		persona.es_agente_retencion = false;
		persona.tipo_cliente = 'N';
		persona.limite_credito = BigDecimal.ZERO;
		Direccion direccion = new Direccion();
		direccion.activo = true;
		direccion.creador = "-";
		direccion.persona = persona;
		direccion.ubigeo = "040101";
		try {
			direccion.descripcion = myrespond.getString("direccion");
		} catch (Exception ex) {
			direccion.descripcion = "-";
		}
		this.save(app, persona, direccion);
		direcciones = getList(app, persona.id);
		return direcciones;
	}

	@Override
	public List<Direccion> getDireccionRUC(String app, String ruc) throws Exception {
		PersonaServiceImpl service = new PersonaServiceImpl();
		List<Persona> personas = service.getListByIdentificador(app, ruc);
		List<Direccion> direcciones = new ArrayList<>();
		if (!personas.isEmpty()) {
			direcciones = getList(app, personas.get(0).id);
			return direcciones;
		}

		JSONObject myrespond = new JSONObject(getResultMigo("ruc", ruc));

		DocumentoIdentidad doc = new DocumentoIdentidad();
		doc.activo = true;
		doc.id = Util.DOCUMENTO_IDENTIDAD_RUC;

		Persona persona = new Persona();
		persona.creador = "-";
		persona.activo = true;
		persona.apellidos = myrespond.getString("nombre_o_razon_social");
		persona.nombres = "-";
		persona.documento_identidad = doc;
		persona.brevette = "-";
		persona.sexo = '-';
		persona.identificador = myrespond.getString("ruc");
		persona.telefonos = "-";
		persona.email = "-";
		persona.nombre_comercial = "-";
		persona.es_agente_retencion = false;
		persona.tipo_cliente = 'N';
		persona.limite_credito = BigDecimal.ZERO;

		Direccion direccion = new Direccion();
		direccion.activo = true;
		direccion.creador = "-";
		direccion.persona = persona;
		direccion.ubigeo = "040101";
		try {
			direccion.descripcion = myrespond.getString("direccion");
		} catch (Exception ex) {
			direccion.descripcion = "-";
		}
		this.save(app, persona, direccion);
		direcciones = getList(app, persona.id);
		return direcciones;
	}

}
