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
import pi.service.model.rrhh.Empleado;
import pi.service.model.rrhh.Permiso;

public interface EmpleadoService{
	
	public void cambiarClave(String app, int empleadoId, String claveActual, String claveNueva, String claveNuevaRep) throws Exception;
	public List<Empleado> list(String app, int empresaId) throws Exception;
	public List<Empleado> listUsuariosSistema(String app, int empresaId) throws Exception;
	public List<Permiso> listPermisosSession(String app) throws Exception;
	public Empleado save(String app, Empleado object) throws Exception;
	public Empleado saveOrUpdate(String app, boolean save, Empleado object) throws Exception;
	
	public List<Empleado> listVendedores(String app, int empresaId) throws Exception;
	public Empleado getByPin(String app, int pin) throws Exception;
	
}
