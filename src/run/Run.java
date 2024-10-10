package run;

import configuracion.ActualizarConfiguracion;
import configuracion.LecturaConfiguracion;
import controlador.Controlador;
import vista.IVista;
import vista.VConsola;

public class Run {

    public static void main(String[] args) {
        IVista vista = new VConsola();;
        Controlador controlador;
        LecturaConfiguracion lecturaConfiguracion = new LecturaConfiguracion();
        controlador = new Controlador(vista);
        vista.setControlador(controlador);
        controlador.aplicarConfiguraciones(lecturaConfiguracion.leerConfiguracion());

        vista.show();

    }

}
