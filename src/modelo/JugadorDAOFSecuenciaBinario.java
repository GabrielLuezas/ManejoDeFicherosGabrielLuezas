package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFSecuenciaBinario implements IDAO<Jugador> {

    public JugadorDAOFSecuenciaBinario(String nombreDirectorio, String ruta){
        NOMBRE_DIRECTORIO = nombreDirectorio;
        directorio = new File(ruta, NOMBRE_DIRECTORIO);
        if(!directorio.isDirectory()){
            directorio.mkdir();
        }
        archivo = new File(directorio, NOMBRE_ARCHIVO);

    }

    private static final String NOMBRE_ARCHIVO = "FicheroSecuencialBinario.dat";
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

            listaJugadores = leerJugadores();

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

            listaJugadores = leerJugadores();

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
     * Lee una lista de jugadores desde un archivo utilizando un DataInputStream.
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(listaJugadores, Comparator.comparingInt(Jugador::getIdJugador));
        return listaJugadores;
    }
}