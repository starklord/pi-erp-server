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

import java.util.Date;
import java.util.List;
import pi.service.model.venta.Proforma;
import pi.service.model.venta.ProformaDet;


public interface ProformaService{
	public List<Proforma> listPendents(String app, int sucursalId, Date fechaInicio, Date fechaFin, int vendedorId) throws Exception;
	public List<Proforma> list(String app, int sucursalId, Date fechaInicio, Date fechaFin, int vendedorId) throws Exception;
	public List<ProformaDet> listDetalles(String app, int proformaId) throws Exception;
	public Proforma saveOrUpdate(String app, boolean save, Proforma proforma, List<ProformaDet> detalles) throws Exception;
}
