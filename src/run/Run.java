package run;

import controlador.Controlador;
import vista.IVista;
import vista.VConsola;

public class Run {

    public static void main(String[] args) {
        IVista vista = new VConsola();;

        Controlador controlador;

        controlador = new Controlador(vista);

        vista.setControlador(controlador);

        vista.show();

    }

}
