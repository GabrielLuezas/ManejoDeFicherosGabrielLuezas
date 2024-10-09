package configuracion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ActualizarConfiguracion {

    public static void main(String[] args) {
        // Crear una instancia de Properties
        Properties config = new Properties();

        try {

            config.setProperty("nombrecarpetaguardadoficheros","Ficheros");
            config.setProperty("rutaguardadoficheros","C:\\Users\\Vespertino\\Desktop");
            config.setProperty("formatodearchivo", "");

            FileOutputStream output = new FileOutputStream("config.properties");
            config.store(output, "Configuración de la aplicacion");

            output.close();

            System.out.println("Archivo de configuración actualizado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
