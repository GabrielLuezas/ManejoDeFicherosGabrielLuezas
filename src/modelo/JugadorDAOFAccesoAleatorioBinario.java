package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JugadorDAOFAccesoAleatorioBinario implements IDAO<Jugador>{

    public JugadorDAOFAccesoAleatorioBinario(String nombreDirectorio, String ruta){
        NOMBRE_DIRECTORIO = nombreDirectorio;
        directorio = new File(ruta, NOMBRE_DIRECTORIO);
        if(!directorio.isDirectory()){
            directorio.mkdir();
        }
        archivo = new File(directorio, NOMBRE_ARCHIVO);

    }
    private static final String NOMBRE_ARCHIVO = "FicheroAccesoAleatorioBinario.dat";

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
                try (RandomAccessFile use = new RandomAccessFile(archivo, "rw")) {
                    use.seek(archivo.length());

                    use.writeInt(o.getIdJugador());
                    use.writeUTF(o.getNick());
                    use.writeInt(o.getNivelExperencia());
                    use.writeInt(o.getVidaJugador());
                    use.writeInt(o.getMonedas());

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
                try (RandomAccessFile use = new RandomAccessFile(archivo, "rw")) {
                    use.setLength(0);
                    for (Jugador jugador : listaJugadores) {
                        use.writeInt(jugador.getIdJugador());
                        use.writeUTF(jugador.getNick());
                        use.writeInt(jugador.getNivelExperencia());
                        use.writeInt(jugador.getVidaJugador());
                        use.writeInt(jugador.getMonedas());
                    }

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
                try (RandomAccessFile use = new RandomAccessFile(archivo, "rw")) {
                    use.setLength(0);
                    for (Jugador jugador : listaJugadores) {
                        use.writeInt(jugador.getIdJugador());
                        use.writeUTF(jugador.getNick());
                        use.writeInt(jugador.getNivelExperencia());
                        use.writeInt(jugador.getVidaJugador());
                        use.writeInt(jugador.getMonedas());
                    }

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


    public List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();

        if(archivo.exists()){
            try (RandomAccessFile use = new RandomAccessFile(archivo, "r")) {
                use.seek(0);
                while (use.getFilePointer() < use.length()) {
                    int id = use.readInt();
                    String nombre = use.readUTF();
                    int nivelExperiencia = use.readInt();
                    int vidaJugador = use.readInt();
                    int monedas = use.readInt();

                    Jugador jugador = new Jugador(id, nombre, nivelExperiencia,vidaJugador,monedas);
                    listaJugadores.add(jugador);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Collections.sort(listaJugadores, Comparator.comparingInt(Jugador::getIdJugador));
            return listaJugadores;
        }
        return null;
    }
}
