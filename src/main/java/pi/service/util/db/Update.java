/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.service.util.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pi.App;
import pi.service.util.db.client.TableDB;


/**
 *
 * @author evanl
 */
public class Update {

    public String app_name;
    public Object object;
    public String[] fields;
    public String[] fieldsPassed;
    public Field[] fieldsClass;
    public String tableName;
    public String sequenceName;
    
    public Update(String app_name,String... fields) throws Exception {
        this.app_name = app_name;
        this.fieldsPassed = fields;
    }
    
    private void initFields(String... fieldsStr) throws Exception {

        if (fieldsStr != null) {
            fields = new String[fieldsStr.length];
            fieldsClass = new Field[fieldsStr.length];
            for (int i = 0; i < fieldsStr.length; i++) {
                Field f = object.getClass().getDeclaredField(fieldsStr[i]);
                String field = ObjectDB.getFieldNameOrAnnotation(f);
                fields[i] = field;
                fieldsClass[i] = f;
            }
        } else {
            fieldsClass = object.getClass().getDeclaredFields();
            fields = new String[fieldsClass.length];
            for (int i = 0; i < fieldsClass.length; i++) {
                Field f = fieldsClass[i];
                String field = ObjectDB.getFieldNameOrAnnotation(f);
                fields[i] = field;
            }
        }

    }
    
    private void initTableAndSequenceName() throws Exception {
    	try{
    		Annotation[] annotations = object.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof TableDB) {
                	TableDB tableDB = (TableDB) annotation;
                    this.tableName = tableDB.name();
                    this.sequenceName = tableDB.sequence();
                    if(tableDB.sequence().isEmpty()){
                    	this.sequenceName = this.tableName+"_id_seq";
                    }
                    return;
                }
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	throw new Exception("El objeto no tiene asignado un nombre de tabla, verificar");
        
    }
    
    /**
     * just to cast if the obj has a String, Character, Date
     * @param obj the object to cast
     * @return if the object has a String class or a Character class returns the object inside simple quotation marks
     */
    public static Object parseField(Object obj) {
        if(obj == null) return obj;
        
        List objList;
        if (obj.getClass().equals(ArrayList.class)) {
            objList = (List) obj;
            StringBuilder strB = new StringBuilder();
            strB.append('(');
            if (objList.get(0).getClass().equals(String.class)             ||
                objList.get(0).getClass().equals(Character.class)          ||
                objList.get(0).getClass().equals(java.util.Date.class)     ||
                objList.get(0).getClass().equals(java.sql.Date.class)      ||
                objList.get(0).getClass().equals(java.sql.Time.class)      ||
                objList.get(0).getClass().equals(java.sql.Timestamp.class)
                //||
                //!objList.get(0).getClass().equals(queries.modelo.MOperation.class)
                ) {
                for (int i = 0; i < objList.size(); i++)
                    strB.append("'").append(objList.get(i)).append("'").append(',');
            } else {
                for (int i = 0; i < objList.size(); i++)
                    strB.append(objList.get(i)).append(',');
            }
            strB.deleteCharAt(strB.length() - 1);
            strB.append(')');
            return strB.toString();
        }

        if (obj.getClass().equals(String.class)             ||
            obj.getClass().equals(Character.class)          ||
            obj.getClass().equals(java.util.Date.class)     ||
            obj.getClass().equals(java.sql.Date.class)      ||
            obj.getClass().equals(java.sql.Time.class)      ||
            obj.getClass().equals(java.sql.Timestamp.class)
            //||
            //!obj.getClass().equals(queries.modelo.MOperation.class)
            ) {
            return "'" + obj + "'";
        }

        return obj;
    }

    public static void beginTransaction(String app_name) throws Exception {
        CConexion.getInstance(app_name).startTransaction();
    }

    public static void commitTransaction(String app_name) throws Exception {
        CConexion.getInstance(app_name).commitTransaction();
    }

    public static void rollbackTransaction(String app_name) {
        try{
            CConexion.getInstance(app_name).rollbackTransaction();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
    public void save(Object obj) throws Exception {
        initDataObject(obj);
        StringBuilder query = new StringBuilder(" INSERT INTO ").append(tableName).append('\n');
        query.append('(');
        Object values[] = new Object[fieldsClass.length];
        for (int i = 0; i < fieldsClass.length; i++) {
            Field f = fieldsClass[i];
            if (ObjectDB.isTableType(f)) {
                Object obj2 = f.get(object);
                if (obj2 == null) {
                    values[i] = null;
                } else {
                    values[i] = obj2.getClass().getDeclaredField("id").get(obj2);////////////////
                        /////////////////////////////////////
                        Field f2 = obj2.getClass().getDeclaredField("id");
                        if(ObjectDB.isTableType(f2)){
                                Object obj3 = f2.get(obj2);
                                values[i] = obj3.getClass().getDeclaredField("id").get(obj3);
                            
                        }
                        //////////////////////////////////////
                }
            } else {
                values[i] = f.get(object);
            }
            query.append(fields[i]).append(',');
        }
        query.deleteCharAt(query.length() - 1);
        query.append(')').append('\n').append(" VALUES ").append('\n').append('(');
        for (int i = 0; i < fieldsClass.length; i++) {
            Field f = fieldsClass[i];
            f.setAccessible(true);
            if (f.getName().equals("id")) {
                Object sequence = f.get(object);
                if(sequence == null){
                    sequence = Query.getNextValSequence(sequenceName,app_name);
                    values[i] = sequence;
                }
                f.set(object, sequence);
            }
            query.append(parseField(values[i])).append(',');
        }
        query.deleteCharAt(query.length() - 1);
        query.append(')').append('\n');
        System.out.println(query.toString());
        int r = CConexion.getInstance(app_name).update(query.toString());
        System.out.println("number of statement: " + r);
        if (r == 0) {
            throw new Exception("No se grabo ningun dato");
        }
    }

    public void update(Object obj) throws Exception {
        initDataObject(obj);
        StringBuilder query = new StringBuilder(" UPDATE ").append(this.tableName).append(" SET").append('\n');
        String strId = null;
        Object objId = null;
        for (int i = 0; i < fieldsClass.length; i++) {
            Field f = fieldsClass[i];
            if(f.getName().equals("id")){
            	String field = fields[i];
                strId = fields[i];
                objId = f.get(object);
                if (ObjectDB.isTableType(f)) {
                    Object obj2 = f.get(object);
                    if (obj2 != null) {
                        Field f2 = obj2.getClass().getDeclaredField("id");
                        objId = f2.get(obj2);
                    }

                }
            }
            if (f.getType().isPrimitive()) {
                throw new Exception("los datos de las clases a grabar no pueden ser primitivos");
            }
            String field = fields[i];
            if (ObjectDB.isTableType(f)) {
                Object obj2 = f.get(object);
                if (obj2 != null) {
                    Field f2 = obj2.getClass().getDeclaredField("id");
                    if (ObjectDB.isTableType(f2)) {
                        Object obj3 = f2.get(obj2);
                        Field f3 = obj3.getClass().getDeclaredField("id");
                        query.append(field).append('=').append(parseField(f3.get(obj3))).append(',');

                    } else {
                        query.append(field).append('=').append(parseField(f2.get(obj2))).append(',');
                    }
                }

            }else{
                query.append(field).append('=').append(parseField(f.get(object))).append(',');
            }
        }
        query.deleteCharAt(query.length() - 1).append('\n');
        Field fieldId = obj.getClass().getDeclaredField("id");
        if(fieldId ==null){
            throw new Exception("El objeto a actualizar no tiene un campo id");
        }
        Object id = fieldId.get(obj);
        //
        if(ObjectDB.isTableType(fieldId)){
        	Object objTmp = fieldId.get(obj);
            fieldId = objTmp.getClass().getDeclaredField("id");
            id = fieldId.get(objTmp);
        }
        //
        
        if(id ==null){
            throw new Exception("El objeto a actualizar no tiene ningun valor asignado en el campo id");
        }
        query.append("WHERE ").append(strId).append("=").append(objId);
        System.out.println(query.toString());
        int r = CConexion.getInstance(app_name).update(query.toString());
        System.out.println("number of statement: " + r);
        if (r == 0) {
            throw new Exception("No se actualizo ningun dato");
        }
    }

    public void delete(Object obj) throws Exception {
        initDataObject(obj);
        Field f = null;
        String strId = null;
        for(int i = 0 ; i < fieldsClass.length ; i++){
            Field f2 = fieldsClass[i];
            if(f2.getName().equals("id")){
                f= f2;
                strId = fields[i];
                break;
            }
        }
        if(f==null){
            throw new Exception("El objeto a eliminar no tiene un campo id");
        }
        Object id = f.get(object);
        //
        if(ObjectDB.isTableType(f)){
        	Object objTmp = f.get(obj);
            f = objTmp.getClass().getDeclaredField("id");
            id = f.get(objTmp);
        }
        //
        if (id == null) {
            throw new Exception("El objeto a eliminar no tiene ningun valor asignado en el campo id");
        }
        String query = String.valueOf(String.valueOf("DELETE FROM " + tableName + '\n' + "where " + strId) + Character.toString('=')) + String.valueOf(id);
        System.out.println(query);
        int r = CConexion.getInstance(app_name).update(query);
        System.out.println("number of statement: " + r);
        if (r == 0) {
            throw new Exception("No se grabo ningun dato");
        }
    }
    
    public int getNextValSequence(Object object) throws Exception {
        initDataObject(object);
        return Query.getNextValSequence(sequenceName,app_name);
    }
    
    private void initDataObject(Object object) throws Exception {
        
        this.object = object;
         initTableAndSequenceName();
        if (fieldsPassed != null && fieldsPassed.length == 0) {
            fields = null;
        }else{
            fields=fieldsPassed;
        }
        initFields(fields);
    }
    
}