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
import pi.service.util.db.server.CRUDRecord;

/**
 * as
 * @author evanl
 */
public class RequireRecord {
    
	public StringBuilder select= new StringBuilder();
	private StringBuilder from = new StringBuilder();
    public String where;
    public String name;
    public String sequence;
    
    public RequireRecord(String name, String sequence) throws Exception {
        this.name 		= name;
        this.sequence 	= sequence;
        String table = this.name+"."+this.sequence;
        List<String> columns = CRUDRecord.TABLE_COLUMNS.get(table);
        select.append("select ");
        //from.append("from ").append(obj.tableName).append(" as ").append(obj.alias).append("\n");
		for (String column : columns) {
			select.append(column).append(",");
		}
		
		if(select.charAt(select.length()-1) == ','){
			select.deleteCharAt(select.length()-1);
		}
    }
    
    
    public List list() throws Exception {
        /*initJoins();
        this.query.where = where;*/
        List list = new ArrayList();
        return list;
        /*Object[][] rs = this.query.initResultSet();
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
        return list;*/
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
        for(int i  = 0; i < fieldNames.length;i++){
            String fieldName = fieldNames[i];
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
    
   
    
}
