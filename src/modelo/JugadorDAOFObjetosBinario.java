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
        return null;
    }

    @Override
    public String listadoPorId(Jugador o) {
        List<Jugador> listaJugadores = leerJugadores();
        String respuesta = "No se ha encontrado a un jugador con ese id";

        for (Jugador j : listaJugadores) {
            if (j.getIdJugador() == o.getIdJugador()) {
                respuesta = j.toString();
            }
        }
        return respuesta;
    }

    @Override
    public String listadoGeneral() {
        List<Jugador> listaJugadores = leerJugadores();
        return listaJugadores.stream()
                .map(Jugador::toString)
                .collect(Collectors.joining("\n"));
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
