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
        System.out.println("Comenzo a cargar el archivo XML");
        SAXBuilder builder = new SAXBuilder();
//        File xmlFile = new File("archivo.xml");
        try {
            System.out.println("Entro a comprobar algo");
            Document document = (Document) builder.build(xmlFile);

            Element rootNode = document.getRootElement();
            
//<ID>1</ID>
//<CODIGO>145860</CODIGO>
//<CODIGOP>200145860</CODIGOP>
//<PLAN>2711</PLAN>
//<APELLIDOS>TOVAR ROJAS</APELLIDOS>
//<NOMBRES>WILFRED</NOMBRES>
//<CEDULA>C.C. 14700218</CEDULA>
//<DIRECCION>CRA 14 # 13A-21</DIRECCION>
//<TELEFONO>922675757</TELEFONO>
//<CELULAR>-</CELULAR>
//<CORREO>wilfred.tovar@correounivalle.edu.co</CORREO>
            List list = rootNode.getChildren("UnivalleBD");
            System.out.println("Tamaño list: "+ list.size());
            for (int i = 0; i < list.size(); i++) {
                Element tabla = (Element) list.get(i);
                String nombreTabla = tabla.getName();
                System.out.println("Tabla: " + nombreTabla);
                List lista_campos = tabla.getChildren();
                String campos="";
                String datos="";
                for (int j = 0; j < lista_campos.size(); j++) {

                    Element campo = (Element) lista_campos.get(j);
                    campos+=campo.getName()+"\t";
                    datos+=campo.getValue()+"\t";

                }
                System.out.println(campos);
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

}
