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

import javax.servlet.annotation.WebServlet;

import com.caucho.hessian.server.HessianServlet;

import pi.service.model.rrhh.Area;
import pi.service.AreaService;
import pi.server.db.server.CRUD;

@WebServlet("pi/AreaService")
public class AreaServiceImpl extends HessianServlet implements AreaService {

	@Override
	public List<Area> list(String app) throws Exception {
		String filter = " order by descripcion asc";
		return CRUD.list(app,Area.class,filter);
	}
	
	@Override
	public List<Area> listActives(String app) throws Exception {
		String filter = " order by descripcion asc";
		return CRUD.list(app,Area.class, "where activo is true" + filter);
	}

	@Override
	public void delete(String app, Area object) throws Exception {
		
		
	}

	@Override
	public Area saveOrUpdate(String app, boolean save, Area object) throws Exception {
		try {
			if(save){
				CRUD.save(app,object);
			}else{
				CRUD.update(app,object);
			}
			
			return object;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

}
