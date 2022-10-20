package pi.service.util.db.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CRUDRecord {
	
	public static final String ALIASES = "abcdefghijklmnopqrstuvwxyz";
	
	public static final Map<String, List<String>> TABLE_COLUMNS = new HashMap<>();
	 /*
	public static List<Record> list(String name, String sequence, String[] required, String filter) throws Exception {
       try {
            List list;
            Require require = new Require(classTable, ALIASES.charAt(0) + "");

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
*/
}
