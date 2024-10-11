package modelo;

import java.util.List;

public interface IDAO<T> {

    /**
     * Da de alta un objeto en el sistema.
     * @param o El objeto a dar de alta.
     * @return Un mensaje indicando el éxito o fracaso de la operación.
     */
    public abstract String alta(T o);
    /**
     * Da de baja un objeto del sistema.
     * @param viejo El objeto a dar de baja.
     * @return Un mensaje indicando el éxito o fracaso de la operación.
     */
    public abstract String baja (T viejo);
    /**
     * Modifica un objeto existente en el sistema.
     * @param nuevo El objeto con los nuevos datos.
     * @return Un mensaje indicando el éxito o fracaso de la operación.
     */
    public abstract String modificar (T nuevo);
    /**
     * Lista los detalles de un objeto identificado por su ID.
     * @param o El objeto con el ID a buscar.
     * @return Un mensaje con los detalles del objeto.
     */
    public abstract String listadoPorId (T o);
    /**
     * Lista todos los objetos registrados en el sistema.
     * @return Un mensaje con la lista completa de objetos.
     */
    public abstract String listadoGeneral ();

}
