package modelo;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JugadorDAOFXML implements IDAO<Jugador>{

    public JugadorDAOFXML(String nombreDirectorio, String ruta){
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
    private static final String NOMBRE_ARCHIVO = "FicheroXML.xml";

    List<Jugador> listaJugadores = new ArrayList<>();

    File directorio;
    File archivo;

    @Override
    public String alta(Jugador o) {
        try {
            Document document = obtenerDocumento();
            Element root = document.getDocumentElement(); // Nodo raíz <jugadores>

            // Crear el nodo <jugador> con sus hijos
            Element jugadorElement = document.createElement("jugador");

            // Crear los subelementos dentro de <jugador>
            Element id = document.createElement("id");
            listaJugadores = leerJugadores();
            o.setIdJugador(getNextIdDisponible(listaJugadores));
            id.setTextContent(String.valueOf(o.getIdJugador()));

            Element nick = document.createElement("nick");
            nick.setTextContent(o.getNick());

            Element nivelExperiencia = document.createElement("nivelExperiencia");
            nivelExperiencia.setTextContent(String.valueOf(o.getNivelExperencia()));

            Element vidaJugador = document.createElement("vidaJugador");
            vidaJugador.setTextContent(String.valueOf(o.getVidaJugador()));

            Element monedas = document.createElement("monedas");
            monedas.setTextContent(String.valueOf(o.getMonedas()));

            jugadorElement.appendChild(id);
            jugadorElement.appendChild(nick);
            jugadorElement.appendChild(nivelExperiencia);
            jugadorElement.appendChild(vidaJugador);
            jugadorElement.appendChild(monedas);

            root.appendChild(jugadorElement);

            guardarCambios(document);
            return "Jugador añadido correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al añadir jugador";
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
            Document document = obtenerDocumento();
            NodeList jugadores = document.getElementsByTagName("jugador");

            if (!listaJugadores.contains(viejo)) {
                return "El jugador que se intenta borrar no existe";
            } else {
                for (int i = 0; i < jugadores.getLength(); i++) {
                    Node jugador = jugadores.item(i);
                    if (jugador.getNodeType() == Node.ELEMENT_NODE) {
                        Element jugadorElement = (Element) jugador;
                        String idJugador = jugadorElement.getElementsByTagName("id").item(0).getTextContent();
                        if (Integer.parseInt(idJugador) == viejo.getIdJugador()) {
                            jugadorElement.getParentNode().removeChild(jugadorElement);
                            guardarCambios(document);
                        }
                    }
                }
                return "Jugador eliminado correctamente";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al eliminar jugador";
        }
    }

    @Override
    public String modificar(Jugador nuevo) {
        try {
            Document document = obtenerDocumento();
            NodeList jugadores = document.getElementsByTagName("jugador");


            if (!listaJugadores.contains(nuevo)) {
                return "El jugador que se intenta modificar no existe";
            } else {
                for (int i = 0; i < jugadores.getLength(); i++) {
                    Node jugador = jugadores.item(i);
                    if (jugador.getNodeType() == Node.ELEMENT_NODE) {
                        Element jugadorElement = (Element) jugador;
                        String idJugador = jugadorElement.getElementsByTagName("id").item(0).getTextContent();

                        if (Integer.parseInt(idJugador) == nuevo.getIdJugador()) {
                            jugadorElement.getElementsByTagName("nick").item(0).setTextContent(nuevo.getNick());
                            jugadorElement.getElementsByTagName("nivelExperiencia").item(0).setTextContent(String.valueOf(nuevo.getNivelExperencia()));
                            jugadorElement.getElementsByTagName("vidaJugador").item(0).setTextContent(String.valueOf(nuevo.getVidaJugador()));
                            jugadorElement.getElementsByTagName("monedas").item(0).setTextContent(String.valueOf(nuevo.getMonedas()));

                            guardarCambios(document);
                        }
                    }
                }
                return "Jugador modificado correctamente";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al modificar jugador";
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
     * Obtiene un objeto Document que representa el contenido del archivo XML.
     * Si el archivo existe, se analiza y se carga su contenido en un objeto Document.
     * Si el archivo no existe, se crea un nuevo documento XML con un elemento raíz "jugadores".
     *
     * @return Un objeto Document que contiene el contenido del archivo XML o un nuevo documento
     *         si el archivo no existía.
     * @throws Exception Si ocurre un error durante la lectura o el análisis del archivo XML.
     */
    private Document obtenerDocumento() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document;

        if (archivo.exists()) {
            document = dBuilder.parse(archivo);
        } else {
            document = dBuilder.newDocument();
            Element root = document.createElement("jugadores");
            document.appendChild(root);
        }

        return document;
    }

    /**
     * Guarda los cambios realizados en un objeto Document en el archivo XML correspondiente.
     * Este método transforma el objeto Document en un formato XML y lo escribe en el archivo.
     *
     * @param document El objeto Document que contiene los cambios que se desean guardar.
     * @throws TransformerException Si ocurre un error durante el proceso de transformación o escritura del documento en el archivo XML.
     */
    private void guardarCambios(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(archivo);
        transformer.transform(source, result);
    }

    /**
     * Lee los datos de los jugadores desde un archivo XML y devuelve una lista de objetos Jugador.
     * Este método obtiene un objeto Document utilizando el método obtenerDocumento(),
     * luego extrae los elementos "jugador" y sus datos (ID, nick, nivel de experiencia, vida y monedas)
     * para crear objetos Jugador. La lista se ordena por el ID del jugador antes de devolverla.
     *
     * @return Una lista de objetos Jugador leídos del archivo XML, o una lista vacía si ocurre un error durante la lectura.
     */
    public List<Jugador> leerJugadores() {
        List<Jugador> listaJugadores = new ArrayList<>();

        try {
            Document document = obtenerDocumento();

            NodeList jugadores = document.getElementsByTagName("jugador");

            for (int i = 0; i < jugadores.getLength(); i++) {
                Node jugador = jugadores.item(i);

                if (jugador.getNodeType() == Node.ELEMENT_NODE) {

                    Element jugadorElement = (Element) jugador;
                    int id = Integer.parseInt(jugadorElement.getElementsByTagName("id").item(0).getTextContent());
                    String nick = jugadorElement.getElementsByTagName("nick").item(0).getTextContent();
                    int nivelExperiencia = Integer.parseInt(jugadorElement.getElementsByTagName("nivelExperiencia").item(0).getTextContent());
                    int vidaJugador = Integer.parseInt(jugadorElement.getElementsByTagName("vidaJugador").item(0).getTextContent());
                    int monedas = Integer.parseInt(jugadorElement.getElementsByTagName("monedas").item(0).getTextContent());

                    Jugador jugadorA = new Jugador(id, nick, nivelExperiencia, vidaJugador, monedas);

                    listaJugadores.add(jugadorA);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(listaJugadores, Comparator.comparingInt(Jugador::getIdJugador));
        return listaJugadores;
    }


}
