package configuracion;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ActualizarConfiguracion {
    Properties config = new Properties();

    public ActualizarConfiguracion(){
        LecturaConfiguracion lecturaConfiguracion = new LecturaConfiguracion();
        String[] configuraciones = lecturaConfiguracion.leerConfiguracion();
        nombreCarpetaGuardadoFicheros = configuraciones[0];
        rutaGuardadoFicheros = configuraciones[1];
        formatoDeArchivo = configuraciones[2];
    }

    private String nombreCarpetaGuardadoFicheros;
    private String rutaGuardadoFicheros;
    private String formatoDeArchivo;

    /**
     * Actualiza el archivo de configuración con los valores actuales de las propiedades.
     * Guarda las propiedades de nombre de carpeta, ruta de guardado y formato de archivo.
     */
    public void actualizarFicheroConfiguracion() {
        try {

            config.setProperty("nombrecarpetaguardadoficheros",nombreCarpetaGuardadoFicheros);
            config.setProperty("rutaguardadoficheros",rutaGuardadoFicheros);
            /*
            1 -> SecuencialTexto
            2 -> SecuencialBinario
            3 -> ObjetosBinario
            4 -> AccesoAleatorioBinario
            5 -> XML
            */
            config.setProperty("formatodearchivo", formatoDeArchivo);

            FileOutputStream output = new FileOutputStream("config.properties");
            config.store(output, "Configuración de la aplicacion");

            output.close();

            System.out.println("Archivo de configuración actualizado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNombreCarpetaGuardadoFicheros() {
        return nombreCarpetaGuardadoFicheros;
    }

    public void setNombreCarpetaGuardadoFicheros(String nombreCarpetaGuardadoFicheros) {
        this.nombreCarpetaGuardadoFicheros = nombreCarpetaGuardadoFicheros;
    }

    public String getRutaGuardadoFicheros() {
        return rutaGuardadoFicheros;
    }

    public void setRutaGuardadoFicheros(String rutaGuardadoFicheros) {
        this.rutaGuardadoFicheros = rutaGuardadoFicheros;
    }

    public String getFormatoDeArchivo() {
        return formatoDeArchivo;
    }

    public void setFormatoDeArchivo(String formatoDeArchivo) {
        this.formatoDeArchivo = formatoDeArchivo;
    }

    /**
     * Actualiza el archivo de configuración con el nuevo sistema de archivo y las propiedades actuales.
     * @param sistema El nuevo sistema de archivo en uso (como cadena).
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void actualizarSistemaArchivo(String sistema) throws IOException {
        config.setProperty("nombrecarpetaguardadoficheros",nombreCarpetaGuardadoFicheros);
        config.setProperty("rutaguardadoficheros",rutaGuardadoFicheros);
        config.setProperty("formatodearchivo", formatoDeArchivo);
        FileOutputStream output = new FileOutputStream("config.properties");
        config.store(output, "Configuración de la aplicacion");
        output.close();
    }

}
