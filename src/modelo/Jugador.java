package modelo;

import java.io.Serializable;

public class Jugador implements Serializable{

    private int idJugador;
    private String nick;
    private int nivelExperencia;
    private int vidaJugador;
    private int monedas;


    public Jugador(int idJugador, String nick, int nivelExperencia, int vidaJugador, int monedas) {
        this.setIdJugador(idJugador);
        this.setNick(nick);
        this.setNivelExperencia(nivelExperencia);
        this.setVidaJugador(vidaJugador);
        this.setMonedas(monedas);
    }

    public Jugador(int idJugador){
        this(idJugador,"",0,0,0);
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public int getNivelExperencia() {
        return nivelExperencia;
    }

    public void setNivelExperencia(int nivelExperencia) {
        this.nivelExperencia = nivelExperencia;
    }

    public int getVidaJugador() {
        return vidaJugador;
    }

    public void setVidaJugador(int vidaJugador) {
        this.vidaJugador = vidaJugador;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Jugador other = (Jugador) obj;
        return this.idJugador== other.idJugador;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "idJugador=" + idJugador +
                ", nick='" + nick + '\'' +
                ", nivelExperiencia=" + nivelExperencia +
                ", vidaJugador=" + vidaJugador +
                ", monedas=" + monedas +
                '}';
    }
}
