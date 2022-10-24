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





import pi.service.model.Utilidad;
import pi.service.UtilidadService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/UtilidadService")
public class UtilidadServiceImpl extends HessianServlet implements UtilidadService {

	@Override
	public List<Utilidad> list(String app) throws Exception {
		return CRUD.list(app,Utilidad.class,"order by nombre asc");
	}
	
	@Override
	public List<Utilidad> listActives(String app) throws Exception {
		return CRUD.list(app,Utilidad.class,"where activo is true order by nombre asc");
	}

	@Override
	public void saveOrUpdate(String app, Utilidad utilidad) throws Exception {
		if(utilidad.id==null){
			CRUD.save(app,utilidad);
		}else{
			CRUD.update(app,utilidad);
		}
	}

	@Override
	public void delete(String app, Utilidad utilidad) throws Exception {
		CRUD.delete(app, utilidad);
	}

	
}
