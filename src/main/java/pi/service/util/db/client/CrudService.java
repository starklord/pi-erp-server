/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.util.db.client;

import java.util.List;

/**
 *
 * @author User
 */
public interface CrudService<T> {
    
    
    public List<T> list(Class clazz);
    public List<T> list(Class clazz,String where);
    public List<T> list(Class clazz, String[] require, String where);
    public T save(T entity);
    public T update(T entity);
    public boolean delete(T entity);
}
