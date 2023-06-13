/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * La clase Jugadores representa a un jugador en el sistema. Contiene información sobre el email del jugador, su nombre de usuario
 * y la cantidad de puntos ganados.
 * 
 * author ivanr
 */
@Entity
@Table
public class Jugadores implements Serializable {
    @Id
    @Column
    private String email;
    @Column
    private String username;
    @Column
    private int puntosGanados;
    
    /**
     * Constructor vacío de Jugadores.
     */
    public Jugadores() {
    }
    
    /**
     * Constructor de Jugadores que inicializa los campos del jugador.
     * 
     * @param email el email del jugador
     * @param username el nombre de usuario del jugador
     * @param puntosGanados la cantidad de puntos ganados por el jugador
     */
    public Jugadores(String email, String username, int puntosGanados) {
        this.email = email;
        this.username = username;
        this.puntosGanados = puntosGanados;
    }
    
    /**
     * Obtiene el email del jugador.
     * 
     * @return el email del jugador
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email del jugador.
     * 
     * @param email el email del jugador
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtiene el nombre de usuario del jugador.
     * 
     * @return el nombre de usuario del jugador
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Establece el nombre de usuario del jugador.
     * 
     * @param username el nombre de usuario del jugador
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Obtiene la cantidad de puntos ganados por el jugador.
     * 
     * @return la cantidad de puntos ganados por el jugador
     */
    public int getPuntosGanados() {
        return puntosGanados;
    }

    /**
     * Establece la cantidad de puntos ganados por el jugador.
     * 
     * @param puntosGanados la cantidad de puntos ganados por el jugador
     */
    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }

}
