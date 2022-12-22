package pi.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import pi.service.factory.Numbers;

public class Util {

    public static String[] EVAS = { "127.0.0.1", 
    "144.91.125.121", 
    "127.0.0.1", 
    "144.126.132.140", 
    "154.53.32.33",
    "154.53.32.33",
    "154.53.32.33",
    "154.53.32.33",
    "154.53.32.33",
    "154.53.32.33",
    "192.168.0.150",
    "192.168.1.150",
};
    public static int waitCursorCount = 0;
    // public static DialogBox dbxWait;

    public static final String WIDTH_FULL = "widthFull";

    // para las monedas
    public static int MONEDA_SOLES_ID = 1;
    public static int MONEDA_DOLARES_ID = 2;

    // para los tipos de sistema:
    public static char TIPO_SISTEMA_COMERCIAL = 'C';
    public static char TIPO_SISTEMA_DROGUERIA = 'D';
    public static char TIPO_SISTEMA_FARMACIA = 'F';
    public static char TIPO_SISTEMA_PARKING = 'K';
    public static char TIPO_SISTEMA_PARROQUIA = 'P';

    // para los productos
    public static String NACIONAL = "Nacional";
    public static String IMPORTADO = "Importado";
    public static String PRODUCTO = "Producto";
    public static String SERVICIO = "Servicio";
    public static String CODIGO = "Codigo";
    public static String SERIE = "Serie";
    public static String LOTE = "Lote";
    public static String CODIGO_PRODUCTO = "Codigo de Producto";
    public static String SERIE_ARTICULO = "Serie de Articulo";
    public static char TIPO_CONTROL_PRODUCTO = 'P';
    public static char TIPO_CONTROL_SERVICIO = 'S';

    // para todas:
    public static String TODAS_MARCAS = "Todas las Marcas";
    public static String TODAS_LINEAS = "Todas las Lineas";
    public static String OCULTAR_ANULADOS = "Ocultar Anulados";
    public static String VER_ANULADOS = "Ver Anulados";
    // unidades:
    public static String UNIDAD_KG = "KG";
    public static String UNIDAD_LT = "LT";
    public static String UNIDAD_M3 = "M3";
    public static String UNIDAD_MT = "MT";
    public static String UNIDAD_PZ = "PZ";
    public static String UNIDAD_UN = "UN";
    public static String UNIDAD_GA = "GA";// GALONES
    public static int UNIDAD_UN_ID = 0;

    // para las ordenes de entradas y salidas
    public static char TIPO_ORDEN_VENTA             = 'V';
    public static char TIPO_ORDEN_COMPRA            = 'C';
    public static char TIPO_ORDEN_RECEPCION         = 'R';
    public static char TIPO_ORDEN_TRASLADO_INTERNO  = 'T';
    public static char TIPO_ORDEN_TRANSFORMACION    = 'F';
    public static char TIPO_ORDEN_ENTRADA           = 'E';
    public static char TIPO_ORDEN_SALIDA            = 'S';

    //PARA LOS ALMACENES:
    public static int ALMACEN_PRINCIPAL_ID  = 1;
    public static int ALMACEN_CLIENTE_ID    = 2;
    public static int ALMACEN_PROVEEDOR_ID  = 3;

    public static final char MOVIMIENTO_EGRESO = 'E';
    public static final char MOVIMIENTO_INGRESO = 'I';

    public static final char MOVIMIENTO_ENTRADA = 'E';
    public static final char MOVIMIENTO_SALIDA = 'S';

    // PARA LOS PERMISOS
    public static final int PER_ROOT = 0;
    public static final int PER_VENDEDOR = 1;
    public static final int PER_JEFE_VENTAS = 2;
    public static final int PER_ALMACENERO = 20;
    public static final int PER_JEFE_ALMACEN = 21;
    public static final int PER_ASISTENTE_LOGISTICA = 30;
    public static final int PER_JEFE_LOGISTICA = 31;
    public static final int PER_ASISTENTE_FINANZAS = 40;
    public static final int PER_JEFE_FINANZAS = 41;
    public static final int PER_ASISTENTE_ADMINISTRATIVO = 50;
    public static final int PER_JEFE_ADMINISTRACION = 51;
    public static final int PER_ASISTENTE_RRHH = 60;
    public static final int PER_JEFE_RRHH = 61;
    public static final int PER_MEDICO = 63;
    public static final int PER_ENFERMERO = 64;
    public static final int PER_ALUMNO = 70;
    public static final int PER_DOCENTE = 71;

    public static String OK = "OK";
    public static String ANULADO = "Anulado";
    public static String SI = "SI";
    public static String NO = "NO";
    public static String SIN_ELEGIR = "SIN ELEGIR";
    public static String PENDIENTE = "PENDIENTE";
    public static String NUESTRO = "Nuestro";
    public static String PARTICULAR = "Particular";
    public static final String ACTIVO = "Activo";
    public static final String INACTIVO = "Inactivo";
    public static final String Masculino = "Masculino";
    public static final String Femenimo = "Femenino";
    public static final String ES_PRODUCTO = "ES PRODUCTO";
    public static final String ES_SERVICIO = "ES SERVICIO";

    public static String TODOS = "TODOS";
    public static String NORMAL = "Normal";
    public static String PREFERENCIAL = "Preferencial";
    public static String LISTA_NEGRA = "Lista Negra";
    public static String PROVEEDOR = "Proveedor";
    public static String TIPO_BUSQUEDA_SOLO_PROVEEDORES = "Solo Proveedores";
    public static String TIPO_BUSQUEDA_SOLO_CLIENTES = "Solo Clientes";
    public static String TIPO_CLIENTE_NORMAL = "Normales";
    public static String TIPO_CLIENTE_PREFERENCIAL = "Preferenciales";
    public static String TIPO_CLIENTE_LISTA_NEGRA = "Lista Negra";
    public static final char TIPO_CLIENTE_NORMAL_ID = 'N';
    public static final char TIPO_CLIENTE_PREFERENCIAL_ID = 'P';
    public static final char TIPO_CLIENTE_LISTA_NEGRA_ID = 'L';

    public static final String IGV_18_STR = "18%";
    public static final String IGV_INAFECTO_STR = "Inafecto";
    public static final String IGV_EXONERADO_STR = "Exonerado";

    public static final BigDecimal TARIFAXHORA = new BigDecimal(3);
    public static final BigDecimal TARIFAXNOCHE = new BigDecimal(10);
    public static final BigDecimal TARIFAXDIA = new BigDecimal(15);

    // para los documentos de pago
    public static final int IMPUESTO_NO_AFECTO = 0;
    public static final int DOCUMENTO_TIPO_NOTA_PEDIDO = 0;
    public static final int DOCUMENTO_TIPO_FACTURA = 1;
    public static final int DOCUMENTO_TIPO_BOLETA = 3;
    public static final int DOCUMENTO_TIPO_GUIA = 9;
    public static final int DOCUMENTO_TIPO_RECIBO_TEMPORAL = 54;
    public static final int DOCUMENTO_TIPO_ORDEN_VENTA = 55;

    // para el igv 1: IGV, 2: EXO, 3: INA, 4: EXP
    public static final int TIPO_IMPUESTO_IGV = 1;
    public static final int TIPO_IMPUESTO_EXO = 2;
    public static final int TIPO_IMPUESTO_INA = 3;
    public static final int TIPO_IMPUESTO_EXP = 4;

    // para los documentos de identidad:
    public static int DOCUMENTO_IDENTIDAD_DNI = 1;
    public static int DOCUMENTO_IDENTIDAD_EXTRANGERIA = 4;
    public static int DOCUMENTO_IDENTIDAD_RUC = 6;
    public static int DOCUMENTO_IDENTIDAD_PASAPORTE = 7;

    public static final String CREDITO = "Credito";
    public static final String CONTADO = "Contado";
    public static final int FP_EFECTIVO = 1;
    public static final int FP_TARJETA = 3;
    public static final int FP_CREDITO = 4;
    public static final int FP_MULTIPLE = 6;
    public static final int FP_CHEQUE = 7;
    public static final int FP_DEPOSITO = 8;
    public static final int FP_LETRAS = 9;

    public static final int RT_OTROS_INGRESOS = 0;
    public static final int RT_COBRO_VENTA = 2;
    public static final int RT_COBRO_DEPOSITO = 23;
    public static final int RT_COBRO_CHEQUE = 4;
    public static final int RT_COBRO_TARJETA = 3;
    public static final int RT_COBRO_EFECTIVO = 9;
    public static final int RT_OTROS_EGRESOS = -1;
    public static final int RT_PAGO_EFECTIVO = 5;
    public static final int RT_PAGO_DEPOSITO = 6;
    public static final int RT_PAGO_TARJETA = 7;
    public static final int RT_PAGO_CHEQUE = 8;

    public static final SimpleDateFormat SDF_HOURS = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat SDF_DATE_HOURS = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat SDF_DATE_HOURS_SQLTE = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static int PXH_DIF = 35;
    public static int PXW_DIF = 200;

    // para el tratamiento de los errores:
    public static String NO_HA_INGRESADO = "NO HA INGRESADO UN DATO: ";
    public static String FORMATO_INCORRECTO = "FORMATO INCORRECTO DE DATO: ";
    public static String CAMPO_REQUERIDO = "Campo requerido";

    public static BigDecimal FACTOR_IGV = new BigDecimal("1.18");
    public static BigDecimal IGV = new BigDecimal("18");

    // para los datos de los pdfs a imprimir
    public static String PDF_TO_USE = "reportes_sistemas/";
    public static String PDF_CONSTANCIA = "reportes_sistemas/constancia_";
    public static String PDF_DOC_PAGO = "reportes_sistemas/docpago_";

    public final static int HALIGN_LEFT = 1;
    public final static int HORIZONTAL_ALIGN_TO_MIDDLE = 2;
    public final static int HALIGN_RIGHT = 3;

    public final static int VERTICAL_ALIGN_TO_TOP = 4;
    public final static int VALIGN_MIDDLE = 5;
    public final static int VERTICAL_ALIGN_TO_BOTTOM = 6;

    public static String PREDEFINED_BUTTON_SI = "Si";
    public static String PREDEFINED_BUTTON_NO = "No";

    // para las busquedas
    public static String BUSQUEDA_NOMBRES = "Nombres";
    public static String BUSQUEDA_RUCDNI = "RUC / DNI";

    public static int BUSCAR_PRODUCTO_CODIGO = 1;
    public static int BUSCAR_PRODUCTO_DESCRIPCION = 2;
    public static int BUSCAR_PRODUCTO_SERIE = 3;

    // para las situaciones en el facturador sunat
    public static Map<String, String> MAP_EFACT_SITUACIONES = new HashMap<>();

    // para las situaciones en el facturador sunat
    public static String COD_SITU_DESCARGAR_CDR_OBS = "12";
    public static String COD_SITU_DESCARGAR_CDR = "11";
    public static String COD_SITU_ENVIADO_RECHAZADO = "10";
    public static String COD_SITU_ENVIADO_PROCESANDO = "09";
    public static String COD_SITU_ENVIADO_POR_PROCESAR = "08";
    public static String COD_SITU_XML_VALIDAR = "07";
    public static String COD_SITU_CON_ERRORES = "06";
    public static String COD_SITU_ENVIADO_ANULADO = "05";
    public static String COD_SITU_ENVIADO_ACEPTADO_OBSERVADO = "04";
    public static String COD_SITU_ENVIADO_ACEPTADO = "03";
    public static String COD_SITU_XML_GENERADO = "02";
    public static String COD_SITU_POR_GENERAR_XML = "01";

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


    //para los datos de la parroquia
    public static final String PARROQUIA_FECHA_NACIMIENTO = "Fecha Nacimiento";
    public static final String PARROQUIA_FECHA_BAUTISMO = "Fecha Bautismo";
    public static final String PARROQUIA_FECHA_CONFIRMACION = "Fecha Confirmacion";
    public static final String PARROQUIA_FECHA_MATRIMONIO = "Fecha Matrimonio";

    // para los datos de las personas
    // para los handlers
    // public static List<ResizeWindowHandler> RESIZERS = new ArrayList<>();

    public static final String[] MESES = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
    "Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"}; 

    public static final String[] DIAS = {
        "CERO","UNO","DOS","TRES","CUATRO","CINCO","SEIS","SIETE","OCHO","NUEVE","DIEZ",
        "ONCE","DOCE","TRECE","CATORCE","QUINCE","DIECISEIS","DIECISIETE","DIECIOCHO","DIECINUEVE","VEINTE",
        "VENTIUNO","VEINTIDOS","VEINTITRES","VEINTICUATRO","VEINTICINCO","VEINTISEIS","VEINTISIETE","VEINTIOCHO","VEINTINUEVE","TREINTA",
        "TREINTA Y UNO"
    };

    public static float round2(float num) {
        float result = num * 100;
        result = Math.round(result);
        result = result / 100;
        return result;
    }

    public static float round4(float num) {
        float result = num * 10000;
        result = Math.round(result);
        result = result / 10000;
        return result;
    }

    public static String formatDate(Date date, SimpleDateFormat sdf) {
        return sdf.format(date);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatDateDMY(Date date) {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     *
     * @param date1 first date
     * @param date2 second date
     * @return 1,0,-1 if date1 is >, ==,or less than date2
     */
    public static int compareDates(Date date1, Date date2) {
        int year1 = date1.getYear() + 1900;
        int month1 = date1.getMonth();
        int day1 = date1.getDate();
        int year2 = date2.getYear() + 1900;
        int month2 = date2.getMonth();
        int day2 = date2.getDate();

        if (year2 < year1) {
            return 1;
        }
        if (year2 == year1 && month2 < month1) {
            return 1;
        }
        if (year2 == year1 && month2 == month1 && day2 < day1) {
            return 1;
        }
        if (year2 == year1 && month2 == month1 && day2 == day1) {
            return 0;
        }
        return -1;
    }

    /**
     * complete the given string with zeroes at init
     *
     * @param text
     * @param length
     * @return
     */
    public static String completeWithZeros(String text, int length) {
        int size = text.length();
        int dif = length - size;
        String zeroes = "";
        for (int i = 0; i < dif; i++) {
            zeroes += "0";
        }
        return zeroes + text;
    }

    public static float calcGlnToTn(float gln) {
        return (float) (gln * 4.32 / 1000);
    }

    public static float calcKgToGln(float kg) {
        return (float) (kg / 4.32);
    }

    public static boolean isPar(int n) {
        return n % 2 == 0;
    }

    public static String formatDateToHourAndMinutes(Date date) {
        if (date == null) {
            return "--:--";
        }
        int hours = date.getHours();
        int minutes = date.getMinutes();
        String strHours = String.valueOf(hours);
        String strMinutes = String.valueOf(minutes);

        if (hours >= 0 && hours <= 9) {
            strHours = "0" + hours;
        }

        if (minutes >= 0 && minutes <= 9) {
            strMinutes = "0" + minutes;
        }

        return strHours + ":" + strMinutes;
    }

    public static int compareObjects(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        if (o1.getClass().equals(Integer.class)) {
            return ((Integer) o1).compareTo((Integer) o2);
        }
        if (o1.getClass().equals(Date.class)) {
            return ((Date) o1).compareTo((Date) o2);
        }
        if (o1.getClass().equals(BigDecimal.class)) {
            return ((BigDecimal) o1).compareTo((BigDecimal) o2);
        }
        if (o1.getClass().equals(String.class)) {
            return ((String) o1).compareTo((String) o2);
        }
        if (o1.getClass().equals(Float.class)) {
            return ((Float) o1).compareTo((Float) o2);
        }
        if (o1.getClass().equals(Character.class)) {
            return ((Character) o1).compareTo((Character) o2);
        }
        if (o1.getClass().equals(Boolean.class)) {
            return ((Boolean) o1).compareTo((Boolean) o2);
        }
        return 0;
    }
    // // encrypts one string into a code

    public static String encrypt(String raw) {
        String result = "";
        int letra;
        int valor = 0;
        int newvalor = 0;
        char let;
        for (int i = 0; i < raw.length(); i++) {
            let = raw.charAt(i);
            letra = stringToAscii(let);
            valor = valor + letra;
        }

        valor = (valor % 31);
        if (valor == 0) {
            valor = 19;
        }
        for (int i = 0; i < raw.length(); i++) {
            let = raw.charAt(i);
            letra = stringToAscii(let);
            if (i == 0) {
                newvalor = letra + valor;
            } else if ((i % 2) == 0) {
                newvalor = letra + valor;
            } else if ((i % 2) != 0) {
                newvalor = letra - valor;
            }
            valor--;
            result += Integer.toString(newvalor, 16);
        }
        return result;
    }
    //
    // public static String convertNumberToLetters(double number) {
    // return NumberToLetterConverter.convertNumberToLetter(number);
    // }

    // convertir una letra en su valor asccii
    private static int stringToAscii(char string) {
        int ascii = string;
        return ascii;
    }

    public static String getNumero(String cadena) {
        char[] cadena_char = cadena.toCharArray();
        String cadena_numero = "";
        for (int i = 0; i < cadena_char.length; i++) {
            // if(cadena_char[i]>='0' && cadena_char[i]<='9')
            if (Character.isDigit(cadena_char[i])) {
                cadena_numero += cadena_char[i];
            }
        }
        return cadena_numero;
    }

    public static String getCadena(String cadena) {
        char[] cadena_char = cadena.toCharArray();
        String cadena_numero = "";
        for (int i = 0; i < cadena_char.length; i++) {
            if (!Character.isDigit(cadena_char[i])) {
                cadena_numero += cadena_char[i];
            }
        }
        return cadena_numero.trim();
    }

    public static Date addDays(Date date, int days) {
        final long MILLSECS_PER_DAYS = days * 24 * 60 * 60 * 1000;
        long dateTime = date.getTime() + MILLSECS_PER_DAYS;
        Date dateEnd = new Date();
        dateEnd.setTime(dateTime);
        return dateEnd;
    }

    public static Date asDate(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        date.setSeconds(now.getSeconds());
        date.setMinutes(now.getMinutes());
        date.setHours(now.getHours());
        return date;
    }

    public static Time asTime(LocalTime localTime) {
        return Time.valueOf(localTime);
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalTime asLocalTime(Time time) {
        return time.toLocalTime();
    }

    public static LocalTime asLocalTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalTime();
    }
    //
    // public static Date subtractDays(Date date,int days) {
    // GregorianCalendar cal = new GregorianCalendar();
    // cal.setTime(date);
    // cal.add(Calendar.DATE, -days);
    // return cal.getTime();
    // }

    public static int daysBetweenDates(Date inicio, Date fin) {
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
        long startTime = inicio.getTime();
        long endTime = fin.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / MILLSECS_PER_DAY;

        return (int) diffDays;
    }

    public static void deleteFilesOnDir(String path, String coincidence) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (coincidence != null) {
            files = dir.listFiles((FilenameFilter) (d, name) -> name.contains(coincidence));

        }
        for (File file : files) {
            file.delete();
        }

    }

    public static String readFile(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
        return sb.toString();
    }

    public static String readFile(String filePath) throws Exception {
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        fr.read(chars);
        fr.close();
        return new String(chars);
    }

    public static void writeFile(String filePath, String content) throws Exception {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(content);
        fw.close();
    }

    public static String desencriptar(String textoEncriptado) {
        String secretKey = "qualityinfosolutions";
        String base64EncryptedString = "";
        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(2, key);
            byte[] plainText = decipher.doFinal(message);
            base64EncryptedString = new String(plainText, "UTF-8");
            return base64EncryptedString;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error al des encriptar", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al des encriptar", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Error al des encriptar", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Error al des encriptar", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Error al des encriptar", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("Error al des encriptar", e);
        }
    }

    public static String encriptar(String texto) {
        try {
            String secretKey = "qualityinfosolutions";
            String base64EncryptedString = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(1, key);
            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);
            return base64EncryptedString;
        } catch (Exception e) {
            throw new RuntimeException("Error al des encriptar", e);
        }
    }

    public static BigDecimal getOpGravadas(BigDecimal total, int decimals, BigDecimal valor_impuesto) {

        BigDecimal divisor = valor_impuesto.add(BigDecimal.ONE);
        return Numbers.getBD(Numbers.divide(total, divisor, 8), decimals);
    }

    public static BigDecimal getIgv(BigDecimal total, int decimals, BigDecimal valor_impuesto) {
        BigDecimal divisor = valor_impuesto.add(BigDecimal.ONE);
        return Numbers.getBD(total.subtract(Numbers.divide(total, divisor, 8)), decimals);
    }

    public static Boolean isPossitive(BigDecimal val){
        return val.compareTo(new BigDecimal(0)) >0;
    }

}
