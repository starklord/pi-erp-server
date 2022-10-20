package pi.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.logmanager.handlers.SslTcpOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import io.vertx.core.json.JsonObject;


public class Server {
    public static String DB_URL;
    public static String DB_DRIVER;
    public static String DB_USR;
    public static String DB_PWD;
    public static String IP_SERVER = "154.53.32.33";
    public static int DB_PORT = 7077;
    
    private static DocumentBuilderFactory DBF;
    public static String PATH_WEBAPPS = "/var/lib/tomcat9/webapps/";


    // para las situaciones en el facturador sunat
    public static final String COD_SITU_DESCARGAR_CDR_OBS = "12";
    public static final String COD_SITU_DESCARGAR_CDR = "11";
    public static final String COD_SITU_ENVIADO_RECHAZADO = "10";
    public static final String COD_SITU_ENVIADO_PROCESANDO = "09";
    public static final String COD_SITU_ENVIADO_POR_PROCESAR = "08";
    public static final String COD_SITU_XML_VALIDAR = "07";
    public static final String COD_SITU_CON_ERRORES = "06";
    public static final String COD_SITU_ENVIADO_ANULADO = "05";
    public static final String COD_SITU_ENVIADO_ACEPTADO_OBSERVADO = "04";
    public static final String COD_SITU_ENVIADO_ACEPTADO = "03";
    public static final String COD_SITU_XML_GENERADO = "02";
    public static final String COD_SITU_POR_GENERAR_XML = "01";

    // para la situacion de las notas de credito

    public static final String COD_NOTA_CREDITO = "07";
    public static final String COD_NOTA_DEBITO = "08";
    public static final String COD_TIPONC_ANULACION_OPERACION = "01";
    public static final String COD_TIPONC_ANULACION_ERROR_RUC = "02";
    public static final String COD_TIPONC_CORRECION_ERROR_DESCRIPCION = "03";
    public static final String COD_TIPONC_DESCUENTO_GLOBAL = "04";
    public static final String COD_TIPONC_DESCUENTO_ITEM = "05";
    public static final String COD_TIPONC_DEVOLUCION_TOTAL = "06";
    public static final String COD_TIPONC_DEVOLUCION_ITEM = "07";
    public static final String COD_TIPONC_BONIFICACION = "08";
    public static final String COD_TIPONC_DISMINUCION_VALOR = "09";
    public static final String COD_TIPONC_OTROS_CONCEPTOS = "10";
    public static final String COD_TIPONC_AJUSTES_OPERACIONES_EXP = "11";
    public static final String COD_TIPONC_AJUSTES_AFECTOS_IVAP = "12";

    public static final String TIPONC_ANULACION_OPERACION = "ANULACION DE LA OPERACION";
    public static final String TIPONC_ANULACION_ERROR_RUC = "ANULACION POR ERROR EN EL RUC";
    public static final String TIPONC_CORRECION_ERROR_DESCRIPCION = "CORRECCION POR ERROR EN LA DESCRIPCION";
    public static final String TIPONC_DESCUENTO_GLOBAL = "DESCUENTO GLOBAL";
    public static final String TIPONC_DESCUENTO_ITEM = "DESCUENTO POR ITEM";
    public static final String TIPONC_DEVOLUCION_TOTAL = "DEVOLUCION TOTAL";
    public static final String TIPONC_DEVOLUCION_ITEM = "DEVOLUCION POR ITEM";
    public static final String TIPONC_BONIFICACION = "BONIFICACION";
    public static final String TIPONC_DISMINUCION_VALOR = "DISMINUCION EN EL VALOR";
    public static final String TIPONC_OTROS_CONCEPTOS = "OTROS CONCEPTOS";
    public static final String TIPONC_AJUSTES_OPERACIONES_EXP = "AJUSTES DE OPERACIONES DE EXPORTACION";
    public static final String TIPONC_AJUSTES_AFECTOS_IVAP = "AJUSTES AFECTOS AL IVAP";

    public static String INGRESOS = "Solo Ingresos";
	public static String EGRESOS = "Solo Egresos";
	public static String INGRESOS_EGRESOS = "Ingresos y Egresos";

    
    /**
     * converts the given width(pixels) into width of that table relative to the
     * given width
     *
     * @param width
     * @return
     */
    public static int getCellWidth(int width) {
        return width * (35);
    }

    public static Document getDocument(String fileName) throws Exception {
        if(Server.DBF==null){
            Server.DBF = DocumentBuilderFactory.newInstance();
        }
        DocumentBuilder db = Server.DBF.newDocumentBuilder();
        return db.parse(new File(fileName));
    }

    public static String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            buff.append(" ").append(tokens.nextToken());
        }
        return buff.toString().trim();
    }

    public static void writeFile(String filePath, String content) throws Exception {
        System.out.println("grabando en: " + filePath);
        System.out.println("contenido: " + content);
        FileWriter fw = new FileWriter(filePath);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(content);
        fw.close();

    }

    public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        int total_rows = resultSet.getMetaData().getColumnCount();
        System.out.println("length: " + total_rows);
        while (resultSet.next()) {
            
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= total_rows; i++) {
                String label = resultSet.getMetaData().getColumnLabel(i);
                Object value = resultSet.getObject(i);
                obj.put(label, value==null?JSONObject.NULL:value);
            }
            jsonArray.put(obj);
        }
        System.out.println("json array tostring: " + jsonArray.toString());
        return jsonArray;
    }

    
    
}
