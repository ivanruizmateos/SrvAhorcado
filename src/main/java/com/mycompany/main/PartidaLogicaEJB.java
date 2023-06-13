package com.mycompany.main;

import com.mycompany.common.IPartidaLogica;
import com.mycompany.common.Jugadores;
import com.mycompany.common.Partida;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * La clase PartidaLogicaEJB implementa la interfaz IPartidaLogica y proporciona funcionalidad para crear y gestionar partidas del juego "Ahorcado".
 * 
 */
@Stateful
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PartidaLogicaEJB implements IPartidaLogica {

    private Map<String, List<String>> wordMap;
    private StringBuilder palabraActual;
    private String palabraSecreta;
    private Jugadores jugador;
    private Partida partida;
    private int puntos;
    private String[] words;
    private int index;
    private int vidas;
    public int puntosGuardados;

    @PersistenceContext(unitName = "Exemple1PersistenceUnit")
    private EntityManager em;

    @Inject
    private UserTransaction userTransaction;

    /**
     * Crea una nueva partida con el jugador especificado, la dificultad, el número de vidas y los puntos iniciales.
     * 
     * @param jugador el jugador que participa en la partida
     * @param dificultad la dificultad de la partida (Facil, Media o Dificil)
     * @param vidas el número de vidas disponibles
     * @param puntos los puntos iniciales
     */
    @Override
    public void createMatch(Jugadores jugador, String dificultad, int vidas, int puntos) {
        // Inicialización de variables.
        this.jugador = jugador;
        this.vidas = vidas;
        this.puntos = puntos;
        index = 0;
        partida = new Partida(jugador, false,palabraSecreta);
        createPartida(partida);

        switch (dificultad) {
            case "Facil":
                loadWordsFromJson();
                words = shuffleWordsByDifficulty(dificultad);
                takeWord();
                break;

            case "Media":
                loadWordsFromJson();
                words = shuffleWordsByDifficulty(dificultad);
                takeWord();
                break;

            case "Dificil":
                loadWordsFromJson();
                words = shuffleWordsByDifficulty(dificultad);
                takeWord();
                break;
        }
    }

    /**
     * Finaliza la partida actual, actualiza los puntos del jugador y guarda los datos de la partida.
     */
    @Override
    public void endMatch() {
        
        updatePuntosJugador(jugador.getEmail());
        jugador.setPuntosGanados(puntosGuardados + puntos);
        updatePartida(partida);
        puntos=0;
        guardarDadesAmbTransaccioObject(jugador);
    }

    /**
     * Finaliza la partida actual y actualiza los puntos del jugador sin guardar los datos de la partida.
     * 
     * @return una cadena que indica el fin de la partida
     */
    @Override
    public String endMatchForTime() {
        jugador.setPuntosGanados(jugador.getPuntosGanados() + puntos);
        updatePartida(partida);
        puntos=0;
        return "fin";
    }

    /**
     * Carga las palabras del archivo JSON en el mapa de palabras.
     */
    public void loadWordsFromJson() {
        Gson gson = new Gson();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("files/words.json");
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            this.wordMap = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene las palabras según la dificultad especificada.
     * 
     * @param difficulty la dificultad de las palabras a obtener
     * @return una lista de palabras correspondientes a la dificultad especificada
     */
    public List<String> getWordsByDifficulty(String difficulty) {
        if (this.wordMap == null) {
            loadWordsFromJson();
        }
        return this.wordMap.getOrDefault(difficulty, new ArrayList<>());
    }

    /**
     * Realiza una adivinanza de la palabra y aumenta los puntos.
     */
    public void adivinarPalabra() {
        puntos += 500;
        
    }

    /**
     * Toma una nueva palabra para la partida.
     */
    public void takeWord() {
        if (index < words.length) {
            if (palabraActual == null || palabraActual.toString().equals("")) {
                palabraActual = new StringBuilder();
            }
            palabraSecreta = words[index];
            partida.setReto(palabraSecreta);
            jugador.setPuntosGanados(jugador.getPuntosGanados() + puntos);
            
            updatePartida(partida);
            
            palabraActual.setLength(0);
            palabraActual.append(palabraSecreta.replaceAll(".", "*")); // Inicialmente, cifra la palabra completamente.
            index++;
        } else {
            endMatch();
        }
    }

    /**
     * Cifra la palabra actual agregando asteriscos en lugar de las letras.
     * 
     * @param palabraActual la palabra actual
     * @return la palabra cifrada
     */
    public StringBuilder cifrarPalabra(StringBuilder palabraActual) {
        return palabraActual.append(palabraSecreta.replaceAll(".", "*"));
    }

    /**
     * Obtiene la palabra actual.
     * 
     * @return la palabra actual
     */
    @Override
    public StringBuilder getWords() {
        return palabraActual;
    }

    /**
     * Intenta adivinar una letra en la palabra secreta y actualiza los puntos y las vidas.
     * 
     * @param letra la letra a adivinar
     * @return una cadena que representa el estado de la adivinanza ("incorrecto", "sin vidas" o la palabra actual)
     */
    @Override
    public String adivinarLetra(char letra) {
        boolean acertada = false;
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                palabraActual.setCharAt(i, letra);
                
                puntos += 100;
                
                acertada = true;
            }
        }

        if (!acertada && vidas > 1) {
            vidas--;
            return "incorrecto";
        } else if (vidas == 1 && !acertada) {
            vidas--;
            endMatch();
            return "sin vidas";
        }

        if (palabraActual.toString().equals(palabraSecreta)) {
            adivinarPalabra();
            palabraActual.append("");
            takeWord();
        }

        return palabraActual.toString();
    }

    private Object guardarDadesAmbTransaccioObject(Object ob) {
        List<String> errors = Validadors.validaBean(ob);

        if (errors.isEmpty()) {
            try {

                userTransaction.begin();
                em.merge(ob);
                userTransaction.commit();

            } catch (Exception e) {
            }

        } else {
            String msg = "Errors de validació: " + errors.toString();

        }

        return ob;
    }

    private Object createPartida(Object ob) {
        try {
            userTransaction.begin();
            em.persist(ob);
            userTransaction.commit();
        } catch (NotSupportedException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ob;

    }

    private Object updatePartida(Object ob) {
        try {
            userTransaction.begin();
            em.merge(ob);
            userTransaction.commit();
        } catch (NotSupportedException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(PartidaLogicaEJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ob;
    }
    
    private String[] shuffleWordsByDifficulty(String difficulty) {
    List<String> wordList = getWordsByDifficulty(difficulty);
    Collections.shuffle(wordList);

    
    return wordList.toArray(new String[0]);
}
    public void updatePuntosJugador(String email) {
        try {
            Query query = em.createQuery("SELECT j.puntosGanados FROM Jugadores j WHERE :email=j.email");
            query.setParameter("email", email);
            puntosGuardados = (int) query.getSingleResult();

        } catch (Exception e) {
            e.toString();

        }
    }
}
