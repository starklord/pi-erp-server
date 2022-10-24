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
import pi.service.model.FormaPago;
import pi.service.FormaPagoService;
import pi.server.db.server.CRUD;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/FormaPagoService")
public class FormaPagoServiceImpl extends HessianServlet implements FormaPagoService {
	
	private static final long serialVersionUID = 1L;
	final static Class table = FormaPago.class;
	

	
	@Override
	public List<FormaPago> list(String app) throws Exception {
		String filter = "where activo is true order by descripcion asc";
		return CRUD.list(app,table, filter);
	}
}
