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

import java.util.ArrayList;
import java.util.List;
import pi.service.model.rrhh.CargoPermiso;
import pi.service.model.rrhh.Permiso;
import pi.service.PermisoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/PermisoService")
public class PermisoServiceImpl extends HessianServlet implements PermisoService {

	@Override
	public List<Permiso> list(String app) throws Exception {
		String[] req = {"area"};
		return CRUD.list(app,Permiso.class,req,"order by a.id asc");
	}

	@Override
	public List<Permiso> list(String app, int cargoId) throws Exception {
		String[] req = {"area","cargo","permiso"};
		String filter = "where cargo = " + cargoId + " order by a.id asc";
		List<CargoPermiso> list = CRUD.list(app,CargoPermiso.class,req,filter);
		List<Permiso> permisos= new ArrayList<>();
		for(CargoPermiso cp: list){
			permisos.add(cp.permiso);
		}
		return permisos;
		
	}

	@Override
	public Permiso saveOrUpdate(String app, boolean save, Permiso object) throws Exception {
		if(save){
			CRUD.save(app,object);
		}else{
			CRUD.update(app,object);
		}
		
		return object;
	}

}
