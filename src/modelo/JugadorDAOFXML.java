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

    private static final String NOMBRE_DIRECTORIO = "Ficheros";
    private static final String NOMBRE_ARCHIVO = "FicheroXML.xml";

    List<Jugador> listaJugadores = new ArrayList<>();

    File directorio = new File(NOMBRE_DIRECTORIO);
    File archivo = new File(directorio, NOMBRE_ARCHIVO);

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

    @Override
    public void setNOMBRE_DIRECTORIO(String o) {

    }

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

    private void guardarCambios(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(archivo);
        transformer.transform(source, result);
    }


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
