package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFSecuencialTexto implements IDAO<Jugador> {


    public JugadorDAOFSecuencialTexto(String nombreDirectorio, String ruta){
        NOMBRE_DIRECTORIO = nombreDirectorio;

        if(ruta == null || ruta.equals("")){
            directorio = new File(NOMBRE_DIRECTORIO);
        }else{
            directorio = new File(ruta, NOMBRE_DIRECTORIO);
        }
        if(!directorio.isDirectory()){
            directorio.mkdir();
        }
        archivo = new File(directorio, NOMBRE_ARCHIVO);
    }
    private String NOMBRE_DIRECTORIO;
    private static final String NOMBRE_ARCHIVO = "FicheroSecuencialTexto.txt";

    List<Jugador> listaJugadores = new ArrayList<>();

    File directorio;
    File archivo;

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
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                    writer.write("[USER_ID = " + o.getIdJugador() + "," +
                            " NICK_NAME = " + o.getNick() + "," +
                            " EXPERIENCE = " + o.getNivelExperencia() + "," +
                            " LIFE_LEVEL = " + o.getVidaJugador() + "," +
                            " COINS = " + o.getMonedas() + "]");
                    writer.newLine();
                    return "Jugador añadido correctamente";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al escribir en el archivo";
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
            return 0; // Si no hay jugadores, el ID es 0
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

            if (!(listaJugadores.contains(viejo))) {
                return "El jugador que se intenta borrar no existe";
            } else {
                try {
                    List<String> fileContent = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            String[] datos = linea.split(",");
                            int idJugadorExistente = Integer.parseInt(datos[0].substring(datos[0].lastIndexOf(" ") + 1));

                            if (idJugadorExistente != viejo.getIdJugador()) {
                                fileContent.add(linea);
                            }
                        }
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
                        for (String line : fileContent) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error al escribir en el archivo";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al leer el archivo";
                }
                return "Jugador eliminado correctamente";
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

            if (!(listaJugadores.contains(nuevo))) {
                return "El jugador que se intenta modificar no existe";
            } else {
                try {
                    List<String> fileContent = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            String[] datos = linea.split(",");
                            int idJugadorExistente = Integer.parseInt(datos[0].substring(datos[0].lastIndexOf(" ") + 1));

                            if (idJugadorExistente != nuevo.getIdJugador()) {
                                fileContent.add(linea);
                            }else{
                                String lineaNueva = "[USER_ID = " + idJugadorExistente + "," +
                                        " NICK_NAME = " + nuevo.getNick() + "," +
                                        " EXPERIENCE = " + nuevo.getNivelExperencia() + "," +
                                        " LIFE_LEVEL = " + nuevo.getVidaJugador() + "," +
                                        " COINS = " + nuevo.getMonedas() + "]";
                                fileContent.add(lineaNueva);
                            }
                        }
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
                        for (String line : fileContent) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error al escribir en el archivo";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error al leer el archivo";
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
     * Lee una lista de jugadores desde un archivo utilizando un BufferedReader.
     * Este método verifica si el archivo existe y, si es así, intenta leer una lista de
     * objetos Jugador desde el archivo. Después de la lectura, la lista de jugadores
     * se ordena en función del ID de cada jugador. Si el archivo no existe, el método
     * devuelve null. Si se produce una excepción durante la lectura, se imprime
     * la traza de la excepción y se devuelve la lista de jugadores leída hasta ese momento.
     * @return Una lista de jugadores leída desde el archivo, o  null si el archivo no existe.
     */
    private List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();

        if(archivo.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(",");
                    String substringID = datos[0].substring(datos[0].lastIndexOf(" ") + 1);
                    String substringNick = datos[1].substring(datos[1].lastIndexOf(" ") + 1);
                    String substringExp = datos[2].substring(datos[2].lastIndexOf(" ") + 1);
                    String substringVIda = datos[3].substring(datos[3].lastIndexOf(" ") + 1);
                    String substringMonedas = datos[4].substring(datos[4].lastIndexOf(" ") + 1);
                    substringMonedas = substringMonedas.replaceAll("[^0-9]", "");
                    int idJugador = Integer.parseInt(substringID);
                    String nick = substringNick;
                    int nivelExperiencia = Integer.parseInt(substringExp);
                    int vidaJugador = Integer.parseInt(substringVIda);
                    int monedas = Integer.parseInt(substringMonedas);

                    Jugador jugador = new Jugador(idJugador, nick,nivelExperiencia, vidaJugador, monedas);
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