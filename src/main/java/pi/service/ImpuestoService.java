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
import pi.service.model.Impuesto;

public interface ImpuestoService{
	
	public List<Impuesto> list(String app);
	public List<Impuesto> listActives(String app);
	public void delete(String app, Impuesto impuesto) throws Exception;
	public Impuesto saveOrUpdate(String app, boolean save, Impuesto impuesto) throws Exception;
}
