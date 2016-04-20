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

public void loadSalida() throws FileNotFoundException{
    salida = new File("/home/paulker/SQL/Salida.txt");
    fout = new FileOutputStream(salida);
    bout = new BufferedOutputStream(fout);
    
    //Aqui va la estructura basica del codigo a convertir en SQL
    String datasBasicos ="";
    
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
            
            List list = rootNode.getChildren("UnivalleBD");
            System.out.println("Tamaño list: "+ list.size());
            Element tabla = (Element) list.get(0);
                String nombreTabla = tabla.getName();
                System.out.println("Tabla: " + nombreTabla);
                
                List lista_campos = tabla.getChildren();
                String campos="";
                for (int j = 0; j < lista_campos.size(); j++) {
                    Element campo = (Element) lista_campos.get(j);
                    campos+=campo.getName()+"\t";
                
                }
                System.out.println(campos);
                
            for (int i = 0; i < list.size(); i++) {
                tabla = (Element) list.get(i);
                lista_campos = tabla.getChildren();
                String datos="";
                for (int j = 0; j < lista_campos.size(); j++) {
                    Element campo = (Element) lista_campos.get(j);
                    datos+=campo.getValue()+"\t";

                }

                System.out.println(datos);
                
            }
        } catch (IOException io) {
            System.out.println(io.getMessage());
            System.out.println("Un error de IOException");
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
            System.out.println("Un error de JDOMException");
        }

    }
    
    public void escribir(String data){
        //Aqui escribimos el codigo para insertar un registro y este se convierte
//        en el codigo necesario para sql
    }

}
