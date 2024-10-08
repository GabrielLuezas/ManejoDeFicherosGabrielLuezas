package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFObjetosBinario implements IDAO<Jugador>{

    private static final String NOMBRE_DIRECTORIO = "Ficheros";
    private static final String NOMBRE_ARCHIVO = "FicheroObjetosBinario.dat";

    List<Jugador> listaJugadores = new ArrayList<>();

    File directorio = new File(NOMBRE_DIRECTORIO);
    File archivo = new File(directorio, NOMBRE_ARCHIVO);

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

    @Override    public String listadoPorId(Jugador o) {
        List<Jugador> listaJugadores = leerJugadores();

        // Inicia la respuesta como un mensaje que indica que no se encontró el jugador
        StringBuilder respuesta = new StringBuilder("No se ha encontrado a un jugador con ese id");

        for (Jugador j : listaJugadores) {
            if (j.getIdJugador() == o.getIdJugador()) {
                respuesta = new StringBuilder(); // Reiniciamos la respuesta si se encuentra el jugador
                respuesta.append("ID: ").append(j.getIdJugador()).append("\n")
                        .append("Jugador: ").append(j.getNick()).append("\n")
                        .append("Nivel de Experiencia: ").append(j.getNivelExperencia()).append("\n")
                        .append("Puntos de Vida: ").append(j.getVidaJugador()).append("\n")
                        .append("Monedas: ").append(j.getMonedas()).append("\n");
                break; // Salimos del bucle una vez encontrado el jugador
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
