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
import pi.service.model.logistica.GuiaRemision;
import pi.service.model.logistica.GuiaRemisionDet;
import pi.service.model.logistica.MotivoTraslado;

public interface GuiaService{
	
	public void annul(String app, int guiaId) throws Exception;
	public GuiaRemision getLastGuiaVenta(String app, String serie) throws Exception;
	public GuiaRemision getVenta(String app, int guiaVentaId) throws Exception;
	public GuiaRemision getVentaByOrdenVenta(String app, int ordenVentaId) throws Exception;
	public List<GuiaRemision> listVentaByOrdenVenta(String app, int ordenVentaId) throws Exception;
	public List<GuiaRemision> listVenta(String app, int sucursalId, Date fechaInicio, Date fechaFin) throws Exception;
	public List<GuiaRemisionDet> listDetallesVenta(String app, int guiaVentaId) throws Exception;
	public GuiaRemision saveOrUpdateVenta(String app, boolean save, GuiaRemision proforma, List<GuiaRemisionDet> detalles) throws Exception;
	
	public void sendToPrint(String app, int guiaVentaId, String user) throws Exception;
	
	//para los motivos de traslado
	public List<MotivoTraslado> listMotivosTraslado(String app) throws Exception;
}
