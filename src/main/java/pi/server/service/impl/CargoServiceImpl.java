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





import pi.service.model.rrhh.Cargo;
import pi.service.model.rrhh.CargoPermiso;
import pi.service.model.rrhh.Permiso;
import pi.service.CargoService;
import pi.service.util.db.Update;
import pi.service.util.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/CargoService")
public class CargoServiceImpl extends HessianServlet implements CargoService {
	
	@Override
	public List<Cargo> list(String app) throws Exception {
		String[] req = {"area"};
		return CRUD.list(app,Cargo.class,req,"order by b.descripcion asc");
	}

	@Override
	public List<Cargo> list(String app, int areaId) throws Exception {
		return CRUD.list(app,Cargo.class,"where area = " + areaId);
	}

	@Override
	public List<Cargo> listActives(String app, int areaId) throws Exception {
		String[] req={"area"};
		return CRUD.list(app,Cargo.class, req, "where activo is true and area=" + areaId);
	}
	
	@Override
	public Cargo saveOrUpdate(String app, Cargo cargo, List<Permiso> permisos, String usuario) throws Exception {
		try {
			if (cargo.id == null) {
				CRUD.save(app,cargo);
			} else {
				CRUD.update(app,cargo);
				CRUD.execute(app, "delete from rrhh.cargo_permiso where cargo = " + cargo.id);
			}
			for(Permiso permiso: permisos){
				CargoPermiso cp = new CargoPermiso();
				cp.cargo = cargo;
				cp.creador = usuario;
				cp.permiso = permiso;
				CRUD.save(app,cp);
			}
			return cargo;

		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

	@Override
	public void delete(String app, Cargo object) throws Exception {
		CRUD.delete(app, object);
		
	}

	@Override
	public Cargo saveOrUpdate(String app, boolean save, Cargo object) throws Exception {
		CRUD.save(app,object);
		return object;
	}
}
