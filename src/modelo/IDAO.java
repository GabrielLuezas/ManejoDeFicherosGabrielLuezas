package modelo;

import java.util.List;

public interface IDAO<T> {

    public abstract String alta(T o);
    public abstract String baja (T viejo);
    public abstract String modificar (T nuevo);
    public abstract String listadoPorId (T o);
    public abstract String listadoGeneral ();

}
