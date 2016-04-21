package xmltomysql;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author paulker
 */
public class XML {

    private File xmlFile;
    private File salida;
    private FileOutputStream fout;
    private BufferedOutputStream bout;
    private String sql = "";
    private String values = "";
    private String[] camposTiposSQL;
    private int[] camposTipo;
    private int[] camposSize;

    public XML(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void loadSalida() throws FileNotFoundException {
        salida = new File("/home/paulker/SQL/Salida.txt");
        fout = new FileOutputStream(salida);
        bout = new BufferedOutputStream(fout);

        //Aqui va la estructura basica del codigo a convertir en SQL
        String datasBasicos = "";

    }

    public void getCampoSize(int[] sizeCampo) {
        for (int x = 0; x < sizeCampo.length; x++) {
            if (sizeCampo[x] <= 10) {
                sizeCampo[x] = 10;
            } else if (sizeCampo[x] <= 20) {
                sizeCampo[x] = 20;
            } else if (sizeCampo[x] <= 50) {
                sizeCampo[x] = 50;
            } else if (sizeCampo[x] <= 100) {
                sizeCampo[x] = 100;
            } else if (sizeCampo[x] <= 150) {
                sizeCampo[x] = 150;
            }
        }

    }

    public void getCamposType(String[] camposType) {
        int largo = camposTipo.length;
        for (int x = 0; x < largo; x++) {
            camposType[x] = getTipoCampoSql(camposTipo[x], camposSize[x]);
        }

    }

    public int getTipoCampo(String campoValue) {
        int type = 0;

        char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] dato = campoValue.toCharArray();
        for (int x = 0; x < dato.length; x++) {

            for (int y = 0; y < numbers.length; y++) {
                if (dato[x] == numbers[y]) {
                    type = 1;
                    break;
                }
                if (y == numbers.length-1) {
                    type = 2;
                    break;
                }
            }
            if (type == 2) {
                break;
            }
        }
        return type;
    }

    public String getTipoCampoSql(int type, int size) {
        String tipoSql = "";
        switch (type) {
            case 1:
                tipoSql = "int(" + size + ")";
                break;
            case 2:
                tipoSql = "varchar(" + size + ") COLLATE utf8_spanish_ci";
                break;
            case 0:
                tipoSql = "varchar(100)";
                break;
        }

        return tipoSql;
    }

    public void cargarXML() {
        System.out.println("Comenzo a cargar el archivo XML");
        SAXBuilder builder = new SAXBuilder();
//        File xmlFile = new File("archivo.xml");
//Este valor es de prueba        

        try {
            loadSalida();
            System.out.println("Entro a comprobar algo");
            Document document = (Document) builder.build(xmlFile);

            Element rootNode = document.getRootElement();

            List list = rootNode.getChildren();
//            System.out.println("Tamaño list: " + list.size());
            Element tabla = (Element) list.get(0);
            String nombreTabla = tabla.getName();
//            System.out.println("Tabla: " + nombreTabla);

            List lista_campos = tabla.getChildren();
            String[] camposNames;
            String campos = "";
            for (int j = 0; j < lista_campos.size(); j++) {
//            for (int j = 0; j < 10; j++) {
                Element campo = (Element) lista_campos.get(j);
                campos += campo.getName() + "\t";

            }
            camposNames = campos.split("\t");
//            System.out.println(campos);
            //Variable del tamaño de los campos
            int size = lista_campos.size();
            camposSize = new int[size];
            camposTipo = new int[size];
            camposTiposSQL = new String[size];
            //Incializacion en 0
            for (int x = 0; x < camposSize.length; x++) {
                camposSize[x] = 0;
                camposTipo[x] = -1;
            }

            //**************************************************
            //**************************************************
            //Incio del ciclo del recorrido de todos los datos
            for (int i = 0; i < list.size(); i++) {
                tabla = (Element) list.get(i);
                lista_campos = tabla.getChildren();
                //Asignacion de tamaño de los arreglos segun el primer dato
                String[] datos = new String[size];

                //Obtencion de los datos de la lista de los camposs
                getValuesCampos(camposNames, datos, lista_campos);

                /**
                 * PENDIENTE: ++++++Detectar automaticamente si es cadena o
                 * numero para el sql +Pasar manualmente los tipos cadena o
                 * numero (longitud auto) +Pasar manualmente los tipos
                 * detallando longitud
                 */
                //Cambio especifico-----------------------------
//                datos[6] = datos[6].substring(5, datos[6].length());
//                datos[6] = datos[6].replace("C.C. ", "");
                String datas = ",(";
                if (i == 0) {
                    datas = "(";
                }
                
                //##############################################
                //##############################################
                //##############################################
                //##############################################
                //Corregir para evaluar cuando son varchar o int
                for (int x = 0; x < datos.length; x++) {
                    if (x == 10) {
                        datas += "'" + datos[x] + "'";
                    } else if (x == 4 || x == 5 || x == 7 || x == 8 || x == 9) {
                        datas += "'" + datos[x] + "'" + ",";
                    } else {
                        datas += datos[x] + ",";
                    }

                }
                datas += ")";
                values += datas + "\n";
//                System.out.println(datas);
            }
            values += ";";
            //**************************************************
            //**************************************************
//            System.out.println(values);

            getCampoSize(camposSize);
            getCamposType(camposTiposSQL);

            LoadSQL(nombreTabla, camposNames, camposTiposSQL);
            System.out.println(sql);
        } catch (IOException io) {
            System.out.println(io.getMessage());
            System.out.println("Un error de IOException");
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
            System.out.println("Un error de JDOMException");
        }

    }

    public void escribir(String data) {

    }

    /**
     *
     * @param TablaName nombre de la tabla proporcionado por el XML
     * @param camposName Nombres de los campos
     * @param camposType tipos de los campos para sql
     */
    public void LoadSQL(String TablaName, String[] camposName, String[] camposType) {
        //Aqui escribimos el codigo para insertar un registro y este se convierte
//        en el codigo necesario para sql
        sql = "SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";\n"
                + "SET time_zone = \"+00:00\";\n"
                + "\n"
                + "CREATE TABLE `" + TablaName + "` (\n";

        for (int x = 0; x < camposName.length; x++) {
            sql += " '" + camposName[x] + "' " + camposType[x] + " NOT NULL,\n";
        }
        sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;\n\n\n\n";
        sql += "INSERT INTO '" + TablaName + "' (";
        for (int x = 0; x < camposName.length; x++) {
            if (x == camposName.length - 1) {
                sql += "'" + camposName[x] + "'";
                //para el ultimo dato no quede con una ","
            } else {
                sql += "'" + camposName[x] + "', ";
            }
        }
        sql += ") VALUES\n" + values;
    }

    public void getValuesCampos(String[] camposName, String[] camposValue, List<Element> lista_campos) {
        int indexLista = 0;
        for (int x = 0; x < camposName.length; x++) {
            Element campo = (Element) lista_campos.get(indexLista);

            if (camposName[x].equalsIgnoreCase(campo.getName())) {
                camposValue[x] = campo.getValue();
                indexLista++;

            } else {
                camposValue[x] = "0";
            }

            //obtiene el tipo de campo segun sus valores
            //si este ya se tomo como tipo 2 (varchar) no se vuelve calcular
            if (camposTipo[x] != 2) {
                camposTipo[x] = getTipoCampo(camposValue[x]);
            }
            //Calculo del tamaño mas grande segun todos los valores
            if (camposValue[x].length() > camposSize[x]) {
                camposSize[x] = camposValue[x].length();
            }

        }

    }

}
