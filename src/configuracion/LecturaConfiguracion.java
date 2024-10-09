package configuracion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class LecturaConfiguracion {

    public String[] leerConfiguracion(){
        Properties config = new Properties();

        String[] configuraciones = new String[3];
        try {
            // Cargar el archivo de configuración
            FileInputStream input = new FileInputStream("config.properties");
            config.load(input);

            String nombreFichero = config.getProperty("nombrecarpetaguardadoficheros");
            String rutaFichero = config.getProperty("rutaguardadoficheros");
            String sistemaFicheros = config.getProperty("formatodearchivo");

            configuraciones[0] = nombreFichero;
            configuraciones[1] = rutaFichero;
            configuraciones[2] = sistemaFicheros;

            // Cerrar el archivo
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configuraciones;

    }


}
