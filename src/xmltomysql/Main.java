
package xmltomysql;

import java.io.File;

/**
 *
 * @author paulker
 */
public class Main {

    
    public static void main(String[] args) {
//        Ventana window = new Ventana();
//        window.setVisible(true);
        File xmlFile=null;
        String ruta="";
        String OS = System.getProperty("os.name");
        OS = OS.toLowerCase();
        if(OS.contains("linux")){
                xmlFile = new File("/home/paulker/SQL/UnivalleBD.xml");
          }else if(OS.contains("windows")){
                xmlFile = new File("D:\\Documents\\Univalle data\\UnivalleBD.xml");
        }

        XML fichero = new XML(xmlFile);
        fichero.cargarXML();
    }
    
}
