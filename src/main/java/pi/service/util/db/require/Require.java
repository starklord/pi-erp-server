/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.util.db.require;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pi.service.util.db.ObjectDB;
import pi.service.util.db.Query;

/**
 * as
 * @author evanl
 */
public class Require {
    
    private Query query;
    private Class c;
    private String alias;
    public String where;
    boolean isPosgresql = true;

    public Require(Class c, String alias, String app_name) throws Exception {
        this(null,c,alias,app_name);
    }
    
    public Require(String selectBeginning,Class c, String alias, String app_name) throws Exception {
    	this.isPosgresql = true;
    	this.c = c;
        this.alias = alias;
        this.query = new Query(app_name,selectBeginning);
        ObjectDB objectDB = new ObjectDB(c, alias,query);
        
        this.query.select.add(objectDB);
        this.query.from.add(objectDB);
    }
    
    public Require add(String attribute, String alias) throws Exception {
        ObjectDB objectDB = getNewObjectDBFromAttribute(attribute, alias);
        this.query.select.add(objectDB);
        return this;
    }
    
    public List list() throws Exception {
        initJoins();
        this.query.where = where;
        List list = new ArrayList();
        Object[][] rs = this.query.initResultSet();
        if(rs!=null){
            List<ObjectDB> listOdbs = this.query.select.listObjectsDB;
            for(int i = 0 ; i < rs.length; i++){
                Object object=listOdbs.get(0).getNewObject(i);
                for (int j = 1; j< listOdbs.size();j++) {
                    ObjectDB odb = listOdbs.get(j);
                    setValuesToObjectFromObjectDB(object, odb, i);
                }
                
                list.add(object);
            }
        }
        return list;
    }
    
    private void setValuesToObjectFromObjectDB(Object object, ObjectDB odb, int index) throws Exception {
        
        String[] fieldNames = odb.attribute.split("\\.");
        Object value = odb.getNewObject(index);
        
        if(value==null){
            return;
        }
            
        Object objectTmp = object;
        Object objectParent = object;
        Field field =objectTmp.getClass().getDeclaredField(fieldNames[0]);
        for (String fieldName : fieldNames) {
            field = objectTmp.getClass().getDeclaredField(fieldName);
            objectParent = objectTmp;
            if(field.get(objectTmp)==null){
                Object obj = field.getType().newInstance();
                field.setAccessible(true);
                field.set(objectTmp, obj);
                objectTmp=obj;
            }else{
                objectTmp = field.get(objectTmp);
            }
        }
        field.set(objectParent, value);
        
        
    }
    
    private void initJoins() throws Exception {
        List<ObjectDB> listOdbs = this.query.select.listObjectsDB;
        for (ObjectDB odb : listOdbs) {
            if(odb.attribute==null){
                continue;
            }
            ObjectDB parent = getParentFromObjectDB(odb);
            String fieldName = getFieldName(odb);
            this.query.join.add(odb, parent, fieldName);
        }
        
    }
    
    private String getFieldName(ObjectDB odb) {
        String attribute = odb.attribute;
        String[] fields = attribute.split("\\.");
        return fields[fields.length-1];
    }
    
    private ObjectDB getParentFromObjectDB(ObjectDB odb) {
        List<ObjectDB> listOdbs = this.query.select.listObjectsDB;
        String parentAttribute = getParentAttribute(odb.attribute);
        ObjectDB parent = listOdbs.get(0);
        if(parentAttribute !=null){
            for (ObjectDB odbParent : listOdbs) {
                if(odbParent.attribute==null){
                    continue;
                }
                if(odbParent.attribute.equals(parentAttribute)){
                    return odbParent;
                }
            }
        }
        return parent;
    }
    
    private String getParentAttribute(String attribute){
        String[] fields = attribute.split("\\.");
        StringBuilder parentAttribute = new StringBuilder("");
        if(fields.length>1){
            for(int i = 0 ; i < (fields.length-1); i++){
                parentAttribute.append(fields[i]).append(".");
            }
            parentAttribute.deleteCharAt(parentAttribute.length()-1);
            return parentAttribute.toString();
        }else{
            return null;
        }
        
    }
    
    public final ObjectDB getNewObjectDBFromAttribute(String attribute, String alias) throws Exception {
        String[] fields = attribute.split("\\.");
        Class classTemp = c;
        for (String field : fields) {
            Field f = classTemp.getDeclaredField(field);
            classTemp = f.getType();
        }
        ObjectDB objectDB = new ObjectDB(classTemp, alias, query);
        objectDB.attribute = attribute;
        return objectDB;
    }
    
    
    
}
