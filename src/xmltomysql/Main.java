
package xmltomysql;

import java.io.File;
import view.Ventana;

/**
 *
 * @author paulker
 */
public class Main {

    
    public static void main(String[] args) {
//        Ventana window = new Ventana();
//        window.setVisible(true);
        File xmlFile = new File("/home/paulker/SQL/UnivalleBD.xml");
        XML fichero = new XML(xmlFile);
        fichero.cargarXML();
    }
    
}
