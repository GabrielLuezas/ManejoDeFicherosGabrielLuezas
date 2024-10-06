package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JugadorDAOFSecuencialTexto implements IDAO<Jugador> {

    private static final String NOMBRE_DIRECTORIO = "Ficheros";
    private static final String NOMBRE_ARCHIVO = "FicheroSecuencialTexto.txt";

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

    // Método que encuentra el próximo ID disponible
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
        String respuesta = "No se ha encontrado a un jugador con ese id";

        for(Jugador j : listaJugadores){
            if(j.getIdJugador()==o.getIdJugador()){
                respuesta = j.toString();
            }
        }
        return respuesta;
    }

    @Override
    public String listadoGeneral() {
        listaJugadores = leerJugadores();

        return listaJugadores.stream()
                .map(Jugador::toString)
                .collect(Collectors.joining("\n"));
    }

    private List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();

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

        return listaJugadores;
    }

}