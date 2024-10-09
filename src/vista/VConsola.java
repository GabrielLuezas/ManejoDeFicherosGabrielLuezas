package vista;

import controlador.Controlador;
import modelo.*;

import java.util.Scanner;

public class VConsola implements IVista<Jugador>{

    private final Scanner scanner;
    private Jugador jugadorAuxiliar;
    private Controlador controlador;

    public VConsola() {
        this.scanner = new Scanner(System.in);
    }


    @Override
    public Jugador getObject() {
        return  jugadorAuxiliar;
    }

    @Override
    public void SetObject(Jugador jugador) {
        this.jugadorAuxiliar = jugador;
    }

    @Override
    public void show() {
        iniciarUI();
    }

    private void iniciarUI() {
        int op;
        if(controlador.getModelo()==null){
            desplegarEleccionDeArchivos();
        }
        do {
            desplegarMenu();
            op = scanner.nextInt();
            switch (op) {
                case 1 -> {
                    jugadorAuxiliar = new Jugador();
                    eventosInsertar();
                }
                case 2 -> {
                    jugadorAuxiliar = new Jugador();
                    eventosBorrar();
                }
                case 3 -> {
                    jugadorAuxiliar = new Jugador();
                    eventosModificar();
                }
                case 4 -> {
                    jugadorAuxiliar = new Jugador();
                    eventosConsultarPorNumPokedex();
                }
                case 5 -> {
                    controlador.operacion(5);
                }
                case 6 ->
                        desplegarEleccionDeArchivos();
                case 7 -> {
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                }
                default ->
                        System.out.println("Introduce una entrada valida (del 1 al 7).");
            }
        } while (op!=7);
    }

    private void eventosConsultarPorNumPokedex() {
        System.out.println("========= Consulta de un jugador por ID =========");

        System.out.print("Introduce el id del jugador para ver sus datos:");
        jugadorAuxiliar.setIdJugador(scanner.nextInt());

        controlador.operacion(4);
    }

    private void eventosModificar() {
        System.out.println("========= Modificación de un Jugador =========");

        System.out.print("Introduce el id del jugador que quieres modificar:");
        jugadorAuxiliar.setIdJugador(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Introduce el nuevo nombre del jugador con el id " + jugadorAuxiliar.getIdJugador() + ":");
        jugadorAuxiliar.setNick(scanner.nextLine());

        System.out.print("Introduce el nuevo nivel de experiencia del jugador con el id" + jugadorAuxiliar.getIdJugador()+ ":");
        jugadorAuxiliar.setNivelExperencia(scanner.nextInt());

        System.out.print("Introduce los nuevos puntos de vida del jugador con el id" + jugadorAuxiliar.getIdJugador()+ ":");
        jugadorAuxiliar.setVidaJugador(scanner.nextInt());

        System.out.print("Introduce el nuevo numero de monedas del jugador con el id" + jugadorAuxiliar.getIdJugador()+ ":");
        jugadorAuxiliar.setMonedas(scanner.nextInt());

        controlador.operacion(3);

    }

    private void eventosBorrar() {
        System.out.println("========= Borrado de un Jugador =========");

        System.out.print("Introduce el id del jugador que quieres borrar: ");
        jugadorAuxiliar.setIdJugador(scanner.nextInt());
        controlador.operacion(2);
    }

    private void eventosInsertar() {
        System.out.println("========= Insertar Jugador =========");
        scanner.nextLine();

        System.out.print("Nick: ");
        jugadorAuxiliar.setNick(scanner.nextLine());

        System.out.print("Nivel de Experiencia: ");
        jugadorAuxiliar.setNivelExperencia(scanner.nextInt());

        System.out.print("Puntos de vida: ");
        jugadorAuxiliar.setVidaJugador(scanner.nextInt());
        System.out.print("Monedas: ");
        jugadorAuxiliar.setMonedas(scanner.nextInt());

        controlador.operacion(1);
    }

    private void desplegarMenu() {
        System.out.println("========= Menu Principal =========");
        System.out.println("Indique la operacion a realizar:");
        System.out.println("1. Insertar un nuevo Jugador");
        System.out.println("2. Borrar un Jugador");
        System.out.println("3. Modificar un Jugador");
        System.out.println("4. Listado de un Jugador por ID");
        System.out.println("5. Listado de todos los jugadores");
        System.out.println("6. Formato de guardado de datos");
        System.out.println("7. Salir del programa");
        System.out.print("Ingrese su opcion: ");
    }

    private void desplegarEleccionDeArchivos() {
        int opcion;
        do {
            System.out.println("Seleccione el tipo de almacenamiento:");
            System.out.println("1. Fichero secuencial de texto (BufferedReader/BufferedWriter)");
            System.out.println("2. Fichero secuencial binario (DataInputStream/DataOutputStream)");
            System.out.println("3. Fichero de objetos binario (ObjectInputStream/ObjectOutputStream)");
            System.out.println("4. Fichero de acceso aleatorio binario (RandomAccessFile)");
            System.out.println("5. Fichero de texto XML (DOM)");
            System.out.println("6. Salir");

            System.out.print("Ingrese su opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Ha seleccionado: Fichero secuencial de texto (BufferedReader/BufferedWriter)");
                    controlador.setModelo(new JugadorDAOFSecuencialTexto());
                    break;
                case 2:
                    System.out.println("Ha seleccionado: Fichero secuencial binario (DataInputStream/DataOutputStream)");
                    controlador.setModelo(new JugadorDAOFSecuenciaBinario());
                    break;
                case 3:
                    System.out.println("Ha seleccionado: Fichero de objetos binario (ObjectInputStream/ObjectOutputStream)");
                    controlador.setModelo(new JugadorDAOFObjetosBinario());
                    break;
                case 4:
                    System.out.println("Ha seleccionado: Fichero de acceso aleatorio binario (RandomAccessFile)");
                    controlador.setModelo(new JugadorDAOFAccesoAleatorioBinario());
                    break;
                case 5:
                    System.out.println("Ha seleccionado: Fichero de texto XML (DOM)");
                    controlador.setModelo(new JugadorDAOFXML());
                    break;
                case 6:
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
            System.out.println();
        } while (opcion <1 || opcion >6);
    }

    @Override
    public void setControlador(Controlador c) {
        this.controlador = c;
    }

    @Override
    public void mensaje(String string) {
        System.out.println(string);
    }
}
