package vista;

import controlador.Controlador;

public interface IVista<T> {

    public abstract T getObject();

    public abstract void SetObject(T Jugador);

    public abstract void show();

    public abstract void setControlador(Controlador c);

    public abstract void mensaje(String string);


}
