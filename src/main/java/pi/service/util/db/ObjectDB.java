package pi.service.util.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import pi.service.util.db.client.FieldDB;
import pi.service.util.db.client.TableDB;

import java.io.Serializable;

public class ObjectDB implements Serializable { 

    public String attribute = null;
    public String alias;
    public Class c;
    public String[] fields;
    public Field[] fieldsClass;
    private Query query;
    public int beginIndex;
    public int endIndex;

    public String tableName;
    public String sequenceName;

    public ObjectDB(Class c, String alias, Query query, String... fieldsStr) throws Exception {
        this.c = c;
        this.alias = alias;
        this.query = query;
        initTableAndSequenceName(this.c);
        if (fieldsStr != null && fieldsStr.length == 0) {
            fieldsStr = null;
        }
        initFields(fieldsStr);
    }

    private void initFields(String... fieldsStr) throws Exception {

        if (fieldsStr != null) {
            fields = new String[fieldsStr.length];
            fieldsClass = new Field[fieldsStr.length];
            for (int i = 0; i < fieldsStr.length; i++) {
                Field f = c.getDeclaredField(fieldsStr[i]);
                String field = getFieldNameOrAnnotation(f);
                fields[i] = field;
                fieldsClass[i] = f;
            }
        } else {
            fieldsClass = this.c.getDeclaredFields();
            fields = new String[fieldsClass.length];
            for (int i = 0; i < fieldsClass.length; i++) {
                Field f = fieldsClass[i];
                String field = getFieldNameOrAnnotation(f);
                fields[i] = field;
            }
        }

    }
    
    public boolean completeNewObject(Object object, int row)throws Exception {
        boolean isNull = true;
        for (int i = 0; i < fieldsClass.length; i++) {
            Field f = fieldsClass[i];
            Object finalObject = object;
            f.setAccessible(true);
            if (isTableType(f)) {
                Object obj2 = f.getType().newInstance();
                f.setAccessible(true);
                f.set(object, obj2);
                f = f.getType().getDeclaredField("id");
                finalObject = obj2;
                if (isTableType(f)) {
                    Object obj3 = f.getType().newInstance();
                    f.setAccessible(true);
                    f.set(obj2, obj3);
                    f = f.getType().getDeclaredField("id");
                    finalObject = obj3;
                }
            }
            Object resultSetObject = query.resultSet[row][i + beginIndex];
            if (resultSetObject instanceof String && f.getType().equals(Character.class)) {
                String str = (String) resultSetObject;
                f.set(finalObject, str.charAt(0));
            } else {
                f.set(finalObject, resultSetObject);
            }
            if (resultSetObject != null) {
                isNull = false;
            }
        }
        return isNull;
        
    }

    public Object getNewObject(int row) throws Exception {
        Object obj = c.newInstance();
        if(isNull(row)){
            return null;
        }
        for (int i = 0; i < fieldsClass.length; i++) {
            Object resultSetObject = query.resultSet[row][i + beginIndex];
            Field f = fieldsClass[i];
            Object finalObject = obj;
            f.setAccessible(true);
            
            if (isTableType(f)&&resultSetObject!=null) {
                Object obj2 = f.getType().newInstance();
                f.setAccessible(true);
                f.set(obj, obj2);
                f = f.getType().getDeclaredField("id");
                finalObject = obj2;
                if (isTableType(f)) {
                    Object obj3 = f.getType().newInstance();
                    f.setAccessible(true);
                    f.set(obj2, obj3);
                    f = f.getType().getDeclaredField("id");
                    finalObject = obj3;
                }
            }
            
            if (resultSetObject != null) {
                if (resultSetObject instanceof String && f.getType().equals(Character.class)) {
                    String str = (String) resultSetObject;
                    f.set(finalObject, str.charAt(0));
                } else {
                    f.set(finalObject, resultSetObject);
                }
            }
        }
        return obj;
    }
    
    private boolean isNull(int row){
        for (int i = 0; i < fieldsClass.length; i++) {
            Object resultSetObject = query.resultSet[row][i + beginIndex];
            if(resultSetObject!=null){
                return false;
            }
        }
        return true;
    }
    
    public static boolean isTableType(Field f) {
        Class c = f.getType();
        return !(c.equals(String.class) || c.equals(Integer.class) || c.equals(Boolean.class)
        		|| c.equals(Double.class)
                || c.equals(BigDecimal.class) || c.equals(Character.class) || c.equals(Date.class)
                || c.equals(Time.class)||c.equals(Long.class));
    }

    public static String getFieldNameOrAnnotation(Field f) {
        String field = f.getName();
        if (f.getAnnotation(FieldDB.class) != null) {
            field = f.getAnnotation(FieldDB.class).value();
        }
        return field;
    }

    private void initTableAndSequenceName(Class c) throws Exception {
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof TableDB) {
                TableDB tableDB = (TableDB) annotation;
                this.tableName = tableDB.name();
                String sequence = tableDB.sequence();
                this.sequenceName = sequence.isEmpty()?this.tableName+"_id_seq":sequence;
                return;
            }
        }
        throw new Exception("El objeto no tiene asignado un nombre de tabla, verificar");
    }

}


