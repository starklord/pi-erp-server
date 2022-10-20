package pi.service.util.db.server;

import java.util.List;

import pi.App;
import pi.service.util.db.CConexion;
import pi.service.util.db.Update;
import pi.service.util.db.require.Require;

public class CRUD {

    public static final String ALIASES = "abcdefghijklmnopqrstuvwxyz";

    public static List list(String app_name, Class classTable) throws Exception {
        return list(app_name, classTable, null, null);
    }

    public static List list(String app_name, Class classTable, String filter) throws Exception {
        return list(app_name, classTable, null, filter);
    }

    public static List list(String app_name, Class classTable, String[] required) throws Exception {
        return list(app_name, classTable, required, null);
    }

    public static List list(String app_name, Class classTable, String[] required, String filter) throws Exception {
        try {
            List list;
            Require require = new Require(classTable, ALIASES.charAt(0) + "", app_name);

            if (required != null) {
                for (int i = 0; i < required.length; i++) {
                    require.add(required[i], ALIASES.charAt(i + 1) + "");
                }
            }
            require.where = filter;
            list = require.list();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public static List listF(String app_name, Class classTable, String[][] requiredWithExplicitFields, String filter) throws Exception {
        try {
            List list;
            Require require = new Require(classTable, ALIASES.charAt(0) + "",app_name);

            if (requiredWithExplicitFields != null) {
                for (int i = 0; i < requiredWithExplicitFields.length; i++) {
                    int fieldsSize = requiredWithExplicitFields.length;
                    String required = requiredWithExplicitFields[i][0];
                    String fields[] = new String[fieldsSize - 1];
                    for (int j = 1; j < fieldsSize; j++) {
                        fields[j - 1] = requiredWithExplicitFields[i][j];
                    }
                    //require.add(required, ALIASES.charAt(i+1)+"",fields);
                }
            }
            require.where = filter;
            list = require.list();
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public static void save(String app_name, Object object) throws Exception {
        try {
            Update update = new Update(app_name);
            update.save(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    public static void update(String app_name, Object object) throws Exception {
        try {
            Update update = new Update(app_name);
            update.update(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public static void saveOrUpdate(String app_name, boolean save, Object object) throws Exception {
        Update update = new Update(app_name);
        if (save) {
            update.save(object);
        } else {
            update.update(object);
        }
    }

    public static void delete(String app_name,Object object) throws Exception {
        try {
            Update update = new Update(app_name);
            update.delete(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    public static void execute(String app_name, String exe) throws Exception {
        System.out.println("\n");
        System.out.println(exe);
        CConexion.getInstance(app_name).update(exe);
    }
}
