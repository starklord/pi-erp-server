/*******************************************************************************
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
 *******************************************************************************/
package pi.service;

import java.util.List;
import pi.service.model.persona.Direccion;
import pi.service.model.persona.Persona;

public interface PersonaService{
	
	public Persona save(String app, Persona persona, Direccion direccion) throws Exception;
	public void update(String app, Persona persona) throws Exception;
        
//        public Persona saveOrUpdate(boolean save, Curso curso, Ciclo ciclo)throws Exception ;
        public Persona getByCodigoAndClavePortal(String app, String codigo, String clavePortal) throws Exception;
	
	public List<Persona> getList(String app, String nombres, String apellidos, String identificador) throws Exception;
	public List<Persona> getListByNombre(String app, String nombres, String apellidos) throws Exception;
	public List<Persona> getListByIdentificador(String app, String identificador) throws Exception;
	public List<Persona> getListByID(String app, int personaId) throws Exception;
	public List<Persona> getListByRazonSocial(String app, String razonSocial) throws Exception;
	public List<Persona> getList(String app, String coincidence) throws Exception;
	
	//import
	public void importClientsFromTxt(String app) throws Exception;
	List<Persona> getListPersonas(String app, String nombres, String apellidos, String identificador) throws Exception;
}
