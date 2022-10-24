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
import pi.service.model.DocumentoTipo;
import pi.service.DocumentoTipoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/DocumentoTipoService")
public class DocumentoTipoServiceImpl extends HessianServlet implements DocumentoTipoService {

	public static Class table = DocumentoTipo.class;
	
	@Override
	public List<DocumentoTipo> list(String app) throws Exception {
		List<DocumentoTipo> list = new ArrayList<>();
		try {
			list = CRUD.list(app,table);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<DocumentoTipo> listIngresos(String app) throws Exception {
		return CRUD.list(app,table,"where ingreso is true");
	}

	@Override
	public List<DocumentoTipo> listActives(String app) {
		List<DocumentoTipo> list = new ArrayList<>();
		try {
			list = CRUD.list(app,table,"where activo is true order by nombre asc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
