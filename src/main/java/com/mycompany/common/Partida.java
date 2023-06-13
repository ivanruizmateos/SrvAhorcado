package com.mycompany.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * La clase Partida representa una partida en el sistema. Contiene información sobre el ID de la partida,
 * si la partida ha finalizado, el reto actual y el jugador asociado a la partida.
 * 
 */
@Entity
@Table
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPartida;
    private boolean matchEnded;
    private String reto;

    @OneToOne
    private Jugadores jugador;

    /**
     * Constructor vacío de Partida.
     */
    public Partida() {
    }
    
    /**
     * Constructor de Partida que inicializa los campos de la partida.
     * 
     * @param jugador el jugador asociado a la partida
     * @param matchEnded indica si la partida ha finalizado o no
     * @param reto el reto actual de la partida
     */
    public Partida(Jugadores jugador, boolean matchEnded, String reto) {
        this.jugador = jugador;
        this.matchEnded = matchEnded;
        this.reto = reto;
    }
    
    /**
     * Obtiene el reto actual de la partida.
     * 
     * @return el reto actual de la partida
     */
    public String getReto() {
        return reto;
    }
    
    /**
     * Establece el reto actual de la partida.
     * 
     * @param reto el reto actual de la partida
     */
    public void setReto(String reto) {
        this.reto = reto;
    }

    /**
     * Obtiene el ID de la partida.
     * 
     * @return el ID de la partida
     */
    public Long getIdPartida() {
        return idPartida;
    }

    /**
     * Establece el ID de la partida.
     * 
     * @param idPartida el ID de la partida
     */
    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    /**
     * Obtiene el jugador asociado a la partida.
     * 
     * @return el jugador asociado a la partida
     */
    public Jugadores getJugador() {
        return jugador;
    }

    /**
     * Establece el jugador asociado a la partida.
     * 
     * @param jugador el jugador asociado a la partida
     */
    public void setJugador(Jugadores jugador) {
        this.jugador = jugador;
    }

     /**
     * Verifica si la partida ha finalizado.
     * 
     * @return true si la partida ha finalizado, false de lo contrario
     */
    public boolean isMatchEnded() {
        return matchEnded;
    }

    /**
     * Establece si la partida ha finalizado.
     * 
     * @param matchEnded true si la partida ha finalizado, false de lo contrario
     */
    public void setMatchEnded(boolean matchEnded) {
        this.matchEnded = matchEnded;
    }

    

    
}