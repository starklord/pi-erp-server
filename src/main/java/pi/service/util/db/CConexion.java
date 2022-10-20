package pi.service.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import pi.server.Server;

/**
 *
 * @author etorres
 */

/**
 *
 * @author etorres
 */
public class CConexion {

    private static int CONNECTIONS_COUNT = 0;

    public String strUrl = null;
    public static String IP_SERVER = "154.53.32.33";
    public static int port = 7077;
    public static String strDriver = null;
    public static String strUsr = null;
    public static String strPwd = null;

    Connection cnConexion = null;

    private static boolean isTransaction = false;
    private static int intentos = 0;

    private static Map<String, CConexion> CCONEXIONES = new HashMap<>();

    private static void createNewConnection(String app_name) {
        String db_name = app_name + "db";
        try {
            CConexion.strDriver = Server.DB_DRIVER;
            CConexion.strPwd = Server.DB_PWD;
            CConexion.strUsr = Server.DB_USR;
            CConexion con = new CConexion(db_name);
            CCONEXIONES.put(app_name, con);
            System.out.println("conexion creada: " + db_name + " " + db_name);
        } catch (Exception ex) {
            System.out.println("conexion no creada: " + db_name + " " + db_name);
            ex.printStackTrace();
        }
    }

    public CConexion(String db_name) throws SQLException, ClassNotFoundException, NullPointerException, Exception {
        Properties props = new Properties();
        props.setProperty("user", strUsr);
        props.setProperty("password", strPwd);
        props.setProperty("connectTimeout", "10");
        this.strUrl = "jdbc:postgresql://"+IP_SERVER+":" + port + "/" + db_name;
        // this.strUrl = "jdbc:postgresql://localhost:" + port + "/" + db_name;
        // System.out.println(this.strUrl);
        Class.forName(strDriver);
        intentos++;
        CONNECTIONS_COUNT++;
        cnConexion = DriverManager.getConnection(strUrl, props);
    }

    public Connection getConnection() {
        return cnConexion;
    }

    public void closeConnection(String app_name) {
        CConexion INSTANCE = CCONEXIONES.get(app_name);
        if (INSTANCE != null) {
            if (INSTANCE.cnConexion != null) {
                try {
                    INSTANCE.cnConexion.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void killConnection(String app_name) {
        CConexion INSTANCE = CCONEXIONES.get(app_name);
        if (INSTANCE != null) {
            if (INSTANCE.cnConexion != null) {
                try {
                    // INSTANCE.rollbackTransaction();
                    INSTANCE.cnConexion.close();
                    INSTANCE = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void execute(String sSql) throws Exception {
        if (cnConexion == null) {
            throw new SQLException("Se perdio la conexion.");
        }
        Statement stmt = null;
        stmt = cnConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        stmt.execute(sSql);
    }

    public ResultSet select(String sSql) throws Exception {
        if (cnConexion == null) {
            throw new SQLException("No hay conexion con el servidor.");
        }
        Statement stmt = null;
        ResultSet rs = null;
        stmt = cnConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery(sSql);
        return rs;
    }

    public int update(String sSql) throws Exception {
        if (cnConexion == null)
            throw new SQLException("Se perdio la conexion.");
        int nro = 0;
        Statement stmt = null;
        stmt = cnConexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        nro = stmt.executeUpdate(sSql);
        stmt.close();
        return nro;
    }

    public void startTransaction() throws Exception {
        execute("BEGIN WORK;");
        isTransaction = true;
    }

    public void rollbackTransaction() throws Exception {
        execute("ROLLBACK WORK;");
        isTransaction = false;
    }

    public void commitTransaction() throws Exception {
        execute("COMMIT WORK;");
        isTransaction = false;
    }

    @Override
    public String toString() {
        return this.strUrl;
    }
    /*
     * private static synchronized void createInstance() throws
     * SQLException,Exception{
     * if(INSTANCE == null){
     * INSTANCE = new CConexion();
     * }
     * }
     */

    public static CConexion getInstance(String app_name) throws SQLException, Exception {

        try {
            CConexion INSTANCE = CCONEXIONES.get(app_name);
            if (INSTANCE == null) {
                createNewConnection(app_name);
                INSTANCE = CCONEXIONES.get(app_name);
                System.out.println("Creando nueva conexion : " + CONNECTIONS_COUNT);
            }
            if (INSTANCE.getConnection() == null) {
                createNewConnection(app_name);
                INSTANCE = CCONEXIONES.get(app_name);
                System.out.println("Creando nueva conexion : " + CONNECTIONS_COUNT);
            }

            if (INSTANCE.getConnection().isClosed()) {
                createNewConnection(app_name);
                INSTANCE = CCONEXIONES.get(app_name);
                System.out.println("Creando nueva conexion por cerrarse la anterior :" + CONNECTIONS_COUNT);
            }
            return INSTANCE;

        } catch (Exception ex) {
            ex.printStackTrace();
            isTransaction = false;
            throw new Exception("Operacion fallida. vuelva a intentarlo");
        }

    }
}
