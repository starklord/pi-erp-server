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
package pi.server.service.impl;

import java.util.List;





import pi.service.model.rrhh.CargoPermiso;
import pi.service.CargoPermisoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/CargoPermisoService")
public class CargoPermisoServiceImpl extends HessianServlet implements CargoPermisoService {

	@Override
	public List<CargoPermiso> list(String app, int cargoId) throws Exception {
		String[] req = {"cargo","cargo.area","permiso"};
		return CRUD.list(app,CargoPermiso.class,req,"where a.cargo = " + cargoId + " order by c.descripcion asc");
	}

	@Override
	public List<CargoPermiso> listActives(String app, int cargoId) throws Exception {
		String[] req = {"cargo","cargo.area","permiso"};
		return CRUD.list(app,CargoPermiso.class,req,"where a.activo is true and a.cargo = " + cargoId + "order by c.descripcion asc ");
	}

	@Override
	public List<CargoPermiso> list(String app) throws Exception {
		String[] req = {"cargo","cargo.area","permiso"};
		return CRUD.list(app,CargoPermiso.class,req, " order by c.descripcion asc");
	}

	@Override
	public List<CargoPermiso> listActives(String app) throws Exception {
		String[] req = {"cargo","cargo.area","permiso"};
		return CRUD.list(app,CargoPermiso.class,req,"where a.activo is true order by c.descripcion asc");
	}

	@Override
	public void deletePermiso(String app, int cargoId, int permisoId) throws Exception {
		CRUD.execute(app, "delete from rrhh.cargo_permiso where cargo = " + cargoId + " and permiso = " + permisoId);
	}
}
