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

import pi.service.model.almacen.Articulo;
import pi.service.model.almacen.Kardex;
import pi.service.model.almacen.Producto;
import pi.service.model.auxiliar.MABCProducto;

public interface ProductoService{
	
	public Producto getByCodigo(String app, String codigo) throws Exception;
	public List<Producto> list(String app) throws Exception;
	public List<Producto> list(String app, int empresaId) throws Exception;
	public List<Producto> listActives(String app, int empresaId) throws Exception;
	public List<Producto> listActives(String app, int empresaId, int marcaId, int lineaId, int buscarPor, String txt) throws Exception;
	public void delete(String app, Producto object) throws Exception;
	public void annul(String app, int productoId) throws Exception;
	public Producto saveOrUpdate(String app, boolean save, Producto object) throws Exception;
	void importProductsFromTxt(String app) throws Exception;
	
	public Articulo getArticuloBySerie(String app, String serie,int productoId, int empresaId) throws Exception;
	public Articulo getArticuloBySerieCoincidences(String app, String serie,int productoId, int empresaId) throws Exception;
	public List<Articulo> listArticulosByLote(String app, int cantidad,int productoId, int almacenId) throws Exception;
	
	public Kardex getLastKardexFromProducto(String app, int productoId, int almacenId) throws Exception;
	
	public List<Kardex> listKardexFromProducto(String app, int productoId, int almacenId) throws Exception;
	public List<MABCProducto> listABCProductos(String app, Date inicio, Date fin) throws Exception;
        
        public List<Producto> listPagosMatricula(String app) throws Exception;

	public List<Kardex> listKardexDisponibleVenta(String app, int productoId, int almacenId) throws Exception;

	public List<Kardex> listKardexUsados(String app, int productoId, int almacenId) throws Exception;

	public List<Kardex> listKardexVencidos(String app, int mesesAVencer) throws Exception;
}
