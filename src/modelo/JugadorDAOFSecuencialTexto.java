package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

            if (listaJugadores.contains(o)) {
                return "El jugador que intentas introducir ya está dentro del fichero";
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                    writer.write(o.getIdJugador() + "," +
                            o.getNivelExperencia() + "," +
                            o.getVidaJugador() + "," +
                            o.getMonedas() + "," +
                            o.getNick());
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
                            int idJugadorExistente = Integer.parseInt(datos[0]);

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
                            int idJugadorExistente = Integer.parseInt(datos[0]);

                            if (idJugadorExistente != nuevo.getIdJugador()) {
                                fileContent.add(linea);
                            }else{
                                String lineaNueva;
                                lineaNueva =  idJugadorExistente + "," + nuevo.getNivelExperencia() + "," + nuevo.getVidaJugador() + "," + nuevo.getMonedas() + "," + nuevo.getNick();
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

        StringBuilder respuesta = new StringBuilder();

        listaJugadores = leerJugadores();

        for(Jugador j : listaJugadores){

            respuesta.append(j.toString());
            respuesta.append("\n");

        }

        return respuesta.toString();
    }

    private List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();

        // Use the correct file path for reading
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                int idJugador = Integer.parseInt(datos[0]);
                int nivelExperiencia = Integer.parseInt(datos[1]);
                int vidaJugador = Integer.parseInt(datos[2]);
                int monedas = Integer.parseInt(datos[3]);
                String nick = datos[4];

                Jugador jugador = new Jugador(idJugador, nivelExperiencia, vidaJugador, monedas, nick);
                listaJugadores.add(jugador);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaJugadores;
    }

}