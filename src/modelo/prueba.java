package modelo;

public class prueba {

    public static void main(String[] args) {

        JugadorDAOFSecuencialTexto prueba = new JugadorDAOFSecuencialTexto();
        JugadorDAOFSecuenciaBinario prueba2 = new JugadorDAOFSecuenciaBinario();

        Jugador jugador1 = new Jugador("Jugador1", 5, 100, 50);
        Jugador jugador2 = new Jugador("OMEGAINCREIBLEJUGADORLOLOLOLOLOLOLOLOLOLOLaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaO", 5, 100, 50);
        Jugador jugador3 = new Jugador(1, "JugadorModificado", 8, 300, 500);


        System.out.println(prueba.alta(jugador1));
        System.out.println(prueba.alta(jugador2));

        System.out.println(prueba.listadoGeneral());
    }
}

