package controlador;

import modelo.IDAO;
import modelo.Jugador;
import vista.IVista;

public class Controlador {

    private IVista<Jugador> vista;
    private IDAO<Jugador> dao;

    public Controlador(IVista vista) {
        this.vista = vista;
    }

    public void setModelo(IDAO<Jugador> modelo) {
        this.dao = modelo;
    }

    public IDAO<Jugador> getModelo() {
        return dao;
    }


    public  void operacion(int op){
        Jugador aux;
        switch (op){
            case 1 -> {
                aux = vista.getObject();
                vista.mensaje(dao.alta(aux));
            }
            case 2 -> {
                aux = vista.getObject();
                vista.mensaje(dao.baja(aux));
            }
            case 3 -> {
                aux = vista.getObject();
                vista.mensaje(dao.modificar(aux));
            }
            case 4 -> {
                aux = vista.getObject();
                vista.mensaje(dao.listadoPorId(aux));
            }
            case 5 -> {
                vista.mensaje(dao.listadoGeneral());
            }
        }

    }

}
