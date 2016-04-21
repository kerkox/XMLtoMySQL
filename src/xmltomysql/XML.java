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
            System.out.println("Tamaño list: " + list.size());
            Element tabla = (Element) list.get(0);
            String nombreTabla = tabla.getName();
            System.out.println("Tabla: " + nombreTabla);

            List lista_campos = tabla.getChildren();
            String[] campos2;
            String campos = "";
            for (int j = 0; j < lista_campos.size(); j++) {
//            for (int j = 0; j < 10; j++) {
                Element campo = (Element) lista_campos.get(j);
                campos += campo.getName() + "\t";
                
            }
            campos2 = campos.split("\t");
            System.out.println(campos);
            //Variable del tamaño de los campos
            int size = lista_campos.size();
            for (int i = 0; i < list.size(); i++) {
                tabla = (Element) list.get(i);
                lista_campos = tabla.getChildren();
                String[] datos = new String[size];
                getValuesCampos(campos2, datos, lista_campos);

                /**
                 * PENDIENTE: 
                 * +Detectar automaticamente si es cadena o numero para el sql
                 * +Pasar manualmente los tipos cadena o numero (longitud auto)
                 * +Pasar manualmente los tipos detallando longitud
                 */
                
                //Cambio especifico-----------------------------
                datos[6] = datos[6].substring(5, datos[6].length());
//                datos[6] = datos[6].replace("C.C. ", "");
                String datas = ",(";
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
                if (datos[9].contains("@")) {
                    System.out.println("######+++++DATOS SUPRIMIDOS");
                    System.out.println("Valor del ultimo: " + datos[10]);
                }
                System.out.println(datas);
            }
        } catch (IOException io) {
            System.out.println(io.getMessage());
            System.out.println("Un error de IOException");
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
            System.out.println("Un error de JDOMException");
        }

    }

    public void escribir(String data) {
        //Aqui escribimos el codigo para insertar un registro y este se convierte
//        en el codigo necesario para sql
    }
    
    
    public void getValuesCampos(String[] camposName,String[] camposValue, List<Element> lista_campos){
        int indexLista=0;
        for(int x=0;x<camposName.length;x++){
            Element campo = (Element) lista_campos.get(indexLista);
            
            if(camposName[x].equalsIgnoreCase(campo.getName())){
                camposValue[x]=campo.getValue();
                indexLista++;
            }else{
                camposValue[x]="0";
            }
            
            
        }
        
    }

}
