package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFObjetosBinario implements IDAO<Jugador>{

    public JugadorDAOFObjetosBinario(String nombreDirectorio, String ruta){
        NOMBRE_DIRECTORIO = nombreDirectorio;
        directorio = new File(ruta, NOMBRE_DIRECTORIO);
        if(!directorio.isDirectory()){
            directorio.mkdir();
        }
        archivo = new File(directorio, NOMBRE_ARCHIVO);

    }
    private static final String NOMBRE_ARCHIVO = "FicheroObjetosBinario.dat";

    private String NOMBRE_DIRECTORIO;
    File directorio;
    File archivo;
    List<Jugador> listaJugadores = new ArrayList<>();
    @Override
    public String alta(Jugador o) {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            listaJugadores = leerJugadores();

            int nuevoId = getNextIdDisponible(listaJugadores);
            o.setIdJugador(nuevoId);

            if (listaJugadores.contains(o)) {
                return "El jugador que intentas introducir ya está dentro del fichero";
            } else {
                listaJugadores.add(o);
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oos.writeObject(listaJugadores);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Jugador añadido correctamente";
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Obtiene el siguiente ID disponible para un jugador en la lista.
     * Este método revisa la lista de jugadores para encontrar el primer ID no ocupado,
     * comenzando desde 0. Si no hay jugadores en la lista, devuelve 0. Si la lista no está vacía,
     * busca el primer número entero no utilizado como ID.
     * @param jugadores La lista de jugadores existentes en el sistema.
     * @return El siguiente ID disponible que no está ocupado por ningún jugador.
     */
    private int getNextIdDisponible(List<Jugador> jugadores) {
        if (jugadores.isEmpty()) {
            return 0;
        }
        for (int i = 0; i <= jugadores.size(); i++) {
            boolean idOcupado = false;
            for (Jugador jugador : jugadores) {
                if (jugador.getIdJugador() == i) {
                    idOcupado = true;
                    break;
                }
            }
            if (!idOcupado) {
                return i;
            }
        }
        return jugadores.size();
    }

    @Override
    public String baja(Jugador viejo) {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            listaJugadores = leerJugadores();

            if (!listaJugadores.contains(viejo)) {
                return "El jugador que se intenta borrar no existe";
            } else {
                listaJugadores.remove(viejo);
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oos.writeObject(listaJugadores);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Jugador borrado correctamente";
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String modificar(Jugador nuevo) {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            listaJugadores = leerJugadores();

            if (!listaJugadores.contains(nuevo)) {
                return "El jugador que se intenta modificar no existe";
            } else {
                listaJugadores.set(nuevo.getIdJugador(),nuevo);
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oos.writeObject(listaJugadores);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Jugador modificado correctamente";
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String listadoPorId(Jugador o) {

        listaJugadores = leerJugadores();

        StringBuilder respuesta = new StringBuilder("No se ha encontrado a un jugador con ese id");

        if (listaJugadores != null && !listaJugadores.isEmpty()){
            for (Jugador j : listaJugadores) {
                if (j.getIdJugador() == o.getIdJugador()) {
                    respuesta = new StringBuilder();
                    respuesta.append("-------------------\n");
                    respuesta.append("ID: ").append(j.getIdJugador()).append("\n")
                            .append("Jugador: ").append(j.getNick()).append("\n")
                            .append("Nivel de Experiencia: ").append(j.getNivelExperencia()).append("\n")
                            .append("Puntos de Vida: ").append(j.getVidaJugador()).append("\n")
                            .append("Monedas: ").append(j.getMonedas()).append("\n")
                            .append("-------------------");
                    break;
                }
            }
        }
        return respuesta.toString();
    }

    @Override
    public String listadoGeneral() {
        listaJugadores = leerJugadores();

        if (listaJugadores != null && !listaJugadores.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Listado de todos los jugadores\n");
            for (Jugador jugador : listaJugadores) {
                sb.append("-------------------\n");
                sb.append("ID: ").append(jugador.getIdJugador()).append("\n")
                        .append("Jugador: ").append(jugador.getNick()).append("\n")
                        .append("Nivel de Experiencia: ").append(jugador.getNivelExperencia()).append("\n")
                        .append("Puntos de Vida: ").append(jugador.getVidaJugador()).append("\n")
                        .append("Monedas: ").append(jugador.getMonedas()).append("\n")
                        .append("-------------------");
            }

            return sb.toString();
        } else {
            return "El fichero de texto está vacío";
        }
    }
    /**
     * Lee una lista de jugadores desde un archivo utilizando un ObjectInputStream.
     * Este método verifica si el archivo existe y, si es así, intenta leer una lista de
     * objetos Jugador desde el archivo. Después de la lectura, la lista de jugadores
     * se ordena en función del ID de cada jugador. Si el archivo no existe, el método
     * devuelve null. Si se produce una excepción durante la lectura, se imprime
     * la traza de la excepción y se devuelve la lista de jugadores leída hasta ese momento.
     * @return Una lista de jugadores leída desde el archivo, o  null si el archivo no existe.
     */
    public List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                listaJugadores = (List<Jugador>) ois.readObject();
            } catch (EOFException e) {
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(listaJugadores, Comparator.comparingInt(Jugador::getIdJugador));

        return listaJugadores;
    }
}
