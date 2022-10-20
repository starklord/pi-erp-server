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
import pi.service.model.rrhh.Area;

public interface AreaService {
	public List<Area> list(String app) throws Exception;
	public List<Area> listActives(String app) throws Exception;
	public void delete(String app,Area object) throws Exception;
	public Area saveOrUpdate(String app,boolean save, Area object) throws Exception;
}
