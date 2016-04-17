package xmltomysql;

import java.io.File;
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

    public XML(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    
    public void cargarXML() {

        SAXBuilder builder = new SAXBuilder();
//        File xmlFile = new File("archivo.xml");
        try {
            Document document = (Document) builder.build(xmlFile);

            Element rootNode = document.getRootElement();

            List list = rootNode.getChildren("tabla");

            for (int i = 0; i < list.size(); i++) {
                Element tabla = (Element) list.get(i);
                String nombreTabla = tabla.getAttributeValue("nombre");
                System.out.println("Tabla: " + nombreTabla);
                List lista_campos = tabla.getChildren();
                System.out.println("\tNombre\t\tTipo\t\tValor");

                for (int j = 0; j < lista_campos.size(); j++) {
                    Element campo = (Element) lista_campos.get(j);

                    String nombre = campo.getChildTextTrim("nombre");

                    String tipo = campo.getChildTextTrim("tipo");

                    String valor = campo.getChildTextTrim("valor");

                    System.out.println("\t" + nombre + "\t\t" + tipo + "\t\t" + valor);

                }
            }
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }

    }

}
