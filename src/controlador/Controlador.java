package controlador;

import modelo.IDAO;
import modelo.Jugador;
import vista.IVista;

public class Controlador {

    private IVista<Jugador> vista;
    private IDAO<Jugador> dao;

    private String nombreCarpeta;
    private String rutaFichero;
    private int sistemaFicheros;


    public Controlador(IVista vista) {
        this.vista = vista;
    }

    public void setModelo(IDAO<Jugador> modelo) {
        this.dao = modelo;
    }

    public IDAO<Jugador> getModelo() {
        return dao;
    }

    /**
     * Ejecuta una operación en función del número de operación proporcionado.
     * Las operaciones son las siguientes:
     *   1: Alta de un jugador.
     *   2: Baja de un jugador.
     *   3: Modificar un jugador.
     *   4: Buscar jugador por ID.
     *   5: Listado general de jugadores.
     * @param op La opción de operación que se va a ejecutar.
     */
    public void operacion(int op){
        Jugador aux;
        switch (op){
            case 1 -> {
                aux = vista.getObject();
                vista.mensaje(dao.alta(aux));
            }
            case 2 -> {
                aux = vista.getObject();
                vista.mensaje(dao.baja(aux));
            }
            case 3 -> {
                aux = vista.getObject();
                vista.mensaje(dao.modificar(aux));
            }
            case 4 -> {
                aux = vista.getObject();
                vista.mensaje(dao.listadoPorId(aux));
            }
            case 5 -> {
                vista.mensaje(dao.listadoGeneral());
            }
        }

    }
    /**
     * Aplica las configuraciones proporcionadas para actualizar los parámetros del sistema de archivos.
     * @param configuraciones Un array de cadenas que contiene las configuraciones:
     *   Nombre de la carpeta de guardado.
     *   Ruta del fichero.
     *   Sistema de archivos utilizado.
     *
     */
    public void aplicarConfiguraciones(String[] configuraciones){
        setNombreCarpeta(configuraciones[0]);
        setRutaFichero(configuraciones[1]);
        if(configuraciones[2].isEmpty()){
            setSistemaFicheros(0);
        }else{
            setSistemaFicheros(Integer.parseInt(configuraciones[2]));
        }
    }


    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreFichero) {
        this.nombreCarpeta = nombreFichero;
    }

    public String getRutaFichero() {
        return rutaFichero;
    }

    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    public int getSistemaFicheros() {
        return sistemaFicheros;
    }

    public void setSistemaFicheros(int sistemaFicheros) {
        this.sistemaFicheros = sistemaFicheros;
    }
}
