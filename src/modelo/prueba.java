package modelo;

public class prueba {

    public static void main(String[] args) {

        JugadorDAOFSecuencialTexto prueba = new JugadorDAOFSecuencialTexto();
        JugadorDAOFSecuenciaBinario prueba2 = new JugadorDAOFSecuenciaBinario();

        Jugador jugador1 = new Jugador(1, "Jugador1", 5, 100, 50);
        Jugador jugador2 = new Jugador(2, "Jugador2", 5, 100, 50);
        Jugador jugador3 = new Jugador(1, "JugadorModificado", 8, 300, 500);


        System.out.println(prueba2.alta(jugador1));
        System.out.println(prueba2.alta(jugador2));

        System.out.println(prueba2.baja(jugador2));
        System.out.println(prueba2.modificar(jugador3));

        System.out.println(prueba2.listadoGeneral());
        System.out.println(prueba2.alta(jugador2));
        System.out.println(prueba2.listadoPorId(jugador2));
        System.out.println(prueba2.listadoGeneral());
    }
}

