/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.common;

import java.util.List;
import javax.ejb.Remote;

/**
 * La interfaz ILogin define m�todos para iniciar sesi�n y registrar usuarios, obtener el usuario actualmente conectado
 * y obtener la lista del Sal�n de la Fama.
 * Es una interfaz remota, lo que indica que puede ser accedida de forma remota por los clientes.
 * 
 * @author ivanr
 */
@Remote
public interface ILogin {
    /**
     * Realiza una operaci�n de inicio de sesi�n para el email especificado.
     * 
     * @param email el email del usuario
     * @return una cadena que indica el estado o mensaje del inicio de sesi�n
     */
    public String login(String email);
    /**
     * Registra un nuevo usuario con el email y nombre de usuario especificados.
     * 
     * @param email el email del usuario
     * @param username el nombre de usuario del usuario
     * @return una cadena que indica el estado o mensaje del registro
     */
    public String register (String email, String username);
     /**
     * Obtiene el usuario actualmente conectado.
     * 
     * @return el usuario conectado como objeto Jugadores
     */
    public Jugadores getLoggedUser();
    /**
     * Obtiene la lista del Sal�n de la Fama.
     * 
     * @return una lista de entradas del Sal�n de la Fama
     */
    public List getHallOfFame();
}
