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

public interface PersonaService {

	public Persona getByIdentificador(String app, String identificador);

	public Persona getById(String app, int personaId);

	public List<Persona> list(String app, String nombres, String apellidos, String identificador,
			String tipo_busqueda, String tipo_cliente);

	public Persona save(String app, Persona persona) throws Exception;

	public Persona save(String app, Persona persona, Direccion direccion) throws Exception;

	public void update(String app, Persona persona) throws Exception;

	public void importClientsFromTxt(String app) throws Exception;
}
