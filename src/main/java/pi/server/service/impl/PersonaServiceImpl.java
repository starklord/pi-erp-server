/** *****************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ****************************************************************************** */
package pi.server.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pi.service.PersonaService;
import pi.server.db.server.CRUD;
import pi.service.model.DocumentoIdentidad;
import pi.service.model.persona.Direccion;
import pi.service.model.persona.Persona;
import pi.service.util.Util;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/PersonaService")
public class PersonaServiceImpl extends HessianServlet implements PersonaService {

    public static Class table = Persona.class;

    @Override
    public Persona save(String app, Persona persona) throws Exception {
        CRUD.save(app, persona);
        return persona;
    }

    @Override
    public Persona save(String app, Persona persona, Direccion direccion) throws Exception {
        try {
            
            CRUD.save(app,persona);
            if (direccion != null) {
                direccion.persona = persona;
                CRUD.save(app,direccion);
            }
            
            return persona;

        } catch (Exception ex) {
            ex.printStackTrace();
            
            throw new Exception(ex.getMessage());
        }

    }

    @Override
    public void update(String app, Persona persona) throws Exception {
        CRUD.update(app,persona);
    }

    @Override
    public Persona getById(String app, int personaId) {
        String[] req = {"documento_identidad"};
        String filter = "where a.id='" + personaId + "' " + "order by apellidos asc limit 1";
        List<Persona> list = new ArrayList<>();
        try {
            list= CRUD.list(app,Persona.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty()?null:list.get(0);
    }

    @Override
    public Persona getByIdentificador(String app, String identificador) {
        String[] req = {"documento_identidad"};
        String filter = "where a.identificador='" + identificador + "' " + "order by apellidos asc limit 1";
        List<Persona> list = new ArrayList<>();
        try {
            list= CRUD.list(app,Persona.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.isEmpty()?null:list.get(0);
    }

    

    @Override
    public List<Persona> list(String app, String nombres, String apellidos, String identificador,
    String tipo_busqueda, String tipo_cliente) {
        String[] req = {"documento_identidad"};
        String filterNames = " and nombres ilike '%" + nombres + "%' ";
        if (nombres.isEmpty()) {
            filterNames = " and (nombres ilike '%" + nombres + "%' or nombres is null) ";
        }
        String filter = "where apellidos ilike '%" + apellidos + "%' ";
        filter += filterNames;
        filter += "and identificador ilike '%" + identificador + "%' ";
        if(tipo_busqueda.equals(Util.TIPO_BUSQUEDA_SOLO_CLIENTES)){
            filter+= " and es_proveedor is false";
        }
        if(tipo_busqueda.equals(Util.TIPO_BUSQUEDA_SOLO_PROVEEDORES)){
            filter+= " and es_proveedor is true";
        }
        if(tipo_cliente.equals(Util.TIPO_CLIENTE_NORMAL)){
            filter+= " and tipo_cliente = '"+Util.TIPO_CLIENTE_NORMAL_ID+"'";
        }
        if(tipo_cliente.equals(Util.TIPO_CLIENTE_PREFERENCIAL)){
            filter+= " and tipo_cliente = '"+Util.TIPO_CLIENTE_PREFERENCIAL_ID+"'";
        }
        if(tipo_cliente.equals(Util.TIPO_CLIENTE_LISTA_NEGRA)){
            filter+= " and tipo_cliente = '"+Util.TIPO_CLIENTE_LISTA_NEGRA_ID+"'";
        }

        filter += " order by apellidos asc";
        List<Persona> list = new ArrayList<>();
        try {
            list = CRUD.list(app,Persona.class, req, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void importClientsFromTxt(String app) throws Exception {
        readFile(app);
    }

    

    private void readFile(String app) throws Exception {
        try {
            
            String iso = "ISO-8859-1";
            String utf = "UTF-8";
            File file = new File("C:/Users/StarkLord/OneDrive/empresas/shirley/proveedores.txt");
            Scanner scan = new Scanner(file, iso);
            scan.useDelimiter("\n");
            PersonaServiceImpl personaService = new PersonaServiceImpl();
            DireccionServiceImpl direccionService = new DireccionServiceImpl();
            while (scan.hasNext()) {
                String line = scan.next();
                Scanner scanLine = new Scanner(line);
                scanLine.useDelimiter("\t");
                // lectura de datos: numero ruc razon estado fecha moneda monto
                String razonSocial = scanLine.next();
                String ruc = scanLine.next();
                String dir = scanLine.next();
                String telefonos = scanLine.next();

                // fin lectura de datos
                String ubigeo = "040101";
                Persona per = personaService.getByIdentificador(app, ruc);
                if (per!=null) {
                    continue;
                }
                Persona persona = new Persona();
                persona.activo = true;
                persona.apellidos = razonSocial;
                persona.nombres = "";
                persona.brevette = "";
                persona.creador = "root";
                persona.documento_identidad = new DocumentoIdentidad();
                persona.documento_identidad.id = Util.DOCUMENTO_IDENTIDAD_RUC;
                persona.email = "";
                persona.identificador = ruc;
                persona.sexo = 'M';
                persona.telefonos = telefonos;
                personaService.save(app, persona, null);
                Direccion direccion = new Direccion();
                direccion.activo = true;
                direccion.creador = "root";
                direccion.descripcion = dir;
                direccion.persona = persona;
                direccion.ubigeo = ubigeo;
                direccion.referencia = "";
                direccionService.save(app, direccion);
                scanLine.close();
            }
            scan.close();
            

        } catch (Exception ex) {
            ex.printStackTrace();
            
            throw new Exception(ex.getMessage());

        }
    }

}
