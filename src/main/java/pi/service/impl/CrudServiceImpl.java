package pi.service.impl;

import java.sql.ResultSet;

import org.json.JSONArray;

import pi.server.Server;
import pi.service.CrudService;
import pi.service.util.db.CConexion;

import com.caucho.hessian.server.HessianServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet("pi/CrudService")
public class CrudServiceImpl extends HessianServlet implements CrudService {

    @Override
    public String get(String app_name, String sql) {
        String str = null;
        try {
            JSONArray array = Server.convertToJSON(CConexion.getInstance(app_name).select(sql));
            if (!array.isEmpty()) {
                str = array.getJSONObject(0).toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    @Override
    public String list(String app_name, String sql) {
        String str = null;
        try {
            JSONArray array = Server.convertToJSON(CConexion.getInstance(app_name).select(sql));
            if (!array.isEmpty()) {
                str = array.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    @Override
    public String getJson(String app_name, String sql) {
        sql = "select row_to_json(lol) from ("+sql+") as lol";
        String str = null;
        try {
            ResultSet rs = CConexion.getInstance(app_name).select(sql);
            boolean isEmpty = !rs.last();
            if (isEmpty) {
                return null;
            }
            rs.beforeFirst();
            while (rs.next()) {
                    str = rs.getObject(1).toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    @Override
    public String listJson(String app_name, String sql) {
        sql = "select json_agg(lol) from ("+sql+") as lol";
        String str = null;
        try {
            ResultSet rs = CConexion.getInstance(app_name).select(sql);
            boolean isEmpty = !rs.last();
            if (isEmpty) {
                return null;
            }
            rs.beforeFirst();
            while (rs.next()) {
                    str = rs.getObject(1).toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    @Override
    public int execute(String app_name, String sql) throws Exception {
        return CConexion.getInstance(app_name).update(sql);
    }

}
