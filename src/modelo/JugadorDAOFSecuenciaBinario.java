package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFSecuenciaBinario implements IDAO<Jugador> {

    private static final String NOMBRE_DIRECTORIO = "Ficheros";
    private static final String NOMBRE_ARCHIVO = "FicheroSecuencialBinario.dat";
    File directorio = new File(NOMBRE_DIRECTORIO);
    File archivo = new File(directorio, NOMBRE_ARCHIVO);

    @Override
    public String alta(Jugador o) {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            List<Jugador> listaJugadores = leerJugadores();

            int nuevoId = getNextIdDisponible(listaJugadores); // Obtener el siguiente ID disponible
            o.setIdJugador(nuevoId); // Asignar el nuevo ID

            if (listaJugadores.contains(o)) {
                return "El jugador que intentas introducir ya está dentro del fichero";
            } else {
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, true))) {
                    dos.writeInt(o.getIdJugador());
                    dos.writeUTF(o.getNick());
                    dos.writeInt(o.getNivelExperencia());
                    dos.writeInt(o.getVidaJugador());
                    dos.writeInt(o.getMonedas());
                    return "Jugador añadido correctamente";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al escribir en el archivo .dat";
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private int getNextIdDisponible(List<Jugador> jugadores) {
        if (jugadores.isEmpty()) {
            return 0;
        }
        // Buscar el primer ID libre
        for (int i = 0; i <= jugadores.size(); i++) {
            boolean idOcupado = false;
            for (Jugador jugador : jugadores) {
                if (jugador.getIdJugador() == i) {
                    idOcupado = true;
                    break;
                }
            }
            if (!idOcupado) {
                return i; // El primer ID no ocupado
            }
        }
        return jugadores.size(); // Si no hay huecos, devolver el siguiente ID secuencial
    }

    @Override
    public String baja(Jugador viejo) {
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            List<Jugador> listaJugadores = leerJugadores();

            if (!listaJugadores.contains(viejo)) {
                return "El jugador que se intenta borrar no existe";
            } else {
                List<Jugador> jugadoresFiltrados = new ArrayList<>();

                try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                    while (dis.available() > 0) {
                        int idJugador = dis.readInt();
                        String nick = dis.readUTF();
                        int nivelExperiencia = dis.readInt();
                        int vidaJugador = dis.readInt();
                        int monedas = dis.readInt();

                        if (idJugador != viejo.getIdJugador()) {
                            jugadoresFiltrados.add(new Jugador(idJugador, nick, nivelExperiencia, vidaJugador, monedas));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al leer el archivo .dat";
                }

                // Escribimos de nuevo todos los jugadores filtrados
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, false))) {
                    for (Jugador jugador : jugadoresFiltrados) {
                        dos.writeInt(jugador.getIdJugador());
                        dos.writeUTF(jugador.getNick());
                        dos.writeInt(jugador.getNivelExperencia());
                        dos.writeInt(jugador.getVidaJugador());
                        dos.writeInt(jugador.getMonedas());
                    }
                    return "Jugador eliminado correctamente";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al escribir en el archivo .dat";
                }
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

            List<Jugador> listaJugadores = leerJugadores();

            if (!listaJugadores.contains(nuevo)) {
                return "El jugador que se intenta modificar no existe";
            } else {
                List<Jugador> jugadoresFiltrados = new ArrayList<>();

                try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                    while (dis.available() > 0) {
                        int idJugador = dis.readInt();
                        String nick = dis.readUTF();
                        int nivelExperiencia = dis.readInt();
                        int vidaJugador = dis.readInt();
                        int monedas = dis.readInt();

                        // Reemplazamos el jugador existente con el nuevo
                        if (idJugador == nuevo.getIdJugador()) {
                            jugadoresFiltrados.add(nuevo);
                        } else {
                            jugadoresFiltrados.add(new Jugador(idJugador, nick, nivelExperiencia, vidaJugador, monedas));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al leer el archivo .dat";
                }

                // Escribimos de nuevo todos los jugadores
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, false))) {
                    for (Jugador jugador : jugadoresFiltrados) {
                        dos.writeInt(jugador.getIdJugador());
                        dos.writeUTF(jugador.getNick());
                        dos.writeInt(jugador.getNivelExperencia());
                        dos.writeInt(jugador.getVidaJugador());
                        dos.writeInt(jugador.getMonedas());
                    }
                    return "Jugador modificado correctamente";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al escribir en el archivo .dat";
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
        List<Jugador> listaJugadores = new ArrayList<>();
        listaJugadores = leerJugadores();
        return listaJugadores.stream()
                .map(Jugador::toString)
                .collect(Collectors.joining("\n")); // Unir con salto de línea
    }

    public List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();
        if (archivo.exists()) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(archivo))) {
                while (dis.available() > 0) {
                    int idJugador = dis.readInt();
                    String nick = dis.readUTF();
                    int nivelExperiencia = dis.readInt();
                    int vidaJugador = dis.readInt();
                    int monedas = dis.readInt();

                    Jugador jugador = new Jugador(idJugador, nick, nivelExperiencia, vidaJugador, monedas);
                    listaJugadores.add(jugador);
                }
            } catch (EOFException e) {
                // Manejar EOFException de manera silenciosa, ya que es normal al final del archivo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(listaJugadores, Comparator.comparingInt(Jugador::getIdJugador));
        return listaJugadores;
    }
}