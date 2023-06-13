/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.common;

import javax.ejb.Remote;

/**
 * La interfaz IPartidaLogica define métodos para crear y finalizar partidas, así como para adivinar letras y obtener información sobre las palabras.
 * Es una interfaz remota, lo que indica que puede ser accedida de forma remota por los clientes.
 * 
 * author ivanr
 */
@Remote
public interface IPartidaLogica {
    /**
     * Crea una nueva partida con el jugador especificado, la dificultad, el número de vidas y los puntos iniciales.
     * 
     * @param jugador el jugador que participa en la partida
     * @param dificultad la dificultad de la partida
     * @param vidas el número de vidas disponibles para el jugador
     * @param puntos los puntos iniciales del jugador
     */
    void createMatch(Jugadores jugador, String dificultad, int vidas, int puntos);
    /**
     * Finaliza la partida actual.
     */
    void endMatch();
    /**
     * Finaliza la partida actual por tiempo.
     * 
     * @return una cadena que indica el resultado final de la partida por tiempo
     */
    String endMatchForTime();
    /**
     * Obtiene las palabras utilizadas en la partida actual.
     * 
     * @return un StringBuilder con las palabras utilizadas en la partida
     */
    StringBuilder getWords();
    /**
     * Adivina una letra en la partida actual.
     * 
     * @param letra la letra que se desea adivinar
     * @return una cadena que indica el resultado de la adivinanza de la letra
     */
    String adivinarLetra(char letra);

}