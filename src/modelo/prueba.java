package modelo;

public class prueba {

    public static void main(String[] args) {

        JugadorDAOFSecuencialTexto prueba = new JugadorDAOFSecuencialTexto();
        JugadorDAOFSecuenciaBinario prueba2 = new JugadorDAOFSecuenciaBinario();

        Jugador jugador1 = new Jugador(1, 5, 100, 50, "Jugador233333");
        Jugador jugador2 = new Jugador(2, 5, 100, 50, "Jugador233333");
        Jugador jugador3 = new Jugador(3, 5, 100, 50, "Jugador3");
        Jugador jugadorModificar = new Jugador(3, 10, 200, 300, "JugadorModificado");

        System.out.println(prueba2.alta(jugador1));
        System.out.println(prueba2.alta(jugador2));

        System.out.println(prueba.listadoGeneral());

    }
}
