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
public interface DireccionService{
	
	public void annul(String app, int personaId) throws Exception;
	public void save(String app, Direccion direccion) throws Exception;
	public void save(String app, Persona persona, Direccion direccion) throws Exception;
	public void update(String app, Direccion direccion) throws Exception;
	public void saveOrUpdate(String app, Direccion direccion) throws Exception;
	public List<Direccion> getList(String app, int personaId);
	public List<Direccion> list(String app, String nombres, String apellidos, String identificador);
	public Direccion getDireccionSinCliente(String app) throws Exception;
	
	public Direccion getDireccionFromRuc(String app, String ruc) throws Exception;
	public List<Direccion> getDireccionDNI(String app, String dni) throws Exception;
	public List<Direccion> getDireccionRUC(String app, String ruc) throws Exception;
}
