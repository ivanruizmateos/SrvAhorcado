package com.mycompany.main;

import com.mycompany.common.ILogin;
import com.mycompany.common.Jugadores;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * La clase LoginEJB implementa la interfaz ILogin y proporciona funcionalidad
 * para el inicio de sesión, registro de usuarios, obtención del usuario
 * conectado y obtención del Salón de la Fama.
 *
 */
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) //Simply put, in container-managed concurrency, the container controls how clients' access to methods
@TransactionManagement(value = TransactionManagementType.BEAN)

public class LoginEJB implements ILogin {

    public Jugadores jugador;
    @PersistenceContext(unitName = "Exemple1PersistenceUnit")
    private EntityManager em;
    @Inject
    private UserTransaction userTransaction;

    /**
     * Realiza el inicio de sesión para el email especificado.
     *
     * @param email el email del usuario que desea iniciar sesión
     * @return una cadena que indica el estado del inicio de sesión ("Correcto"
     * o "error")
     */
    @Override
    public String login(String email) {
        try {
            Query query = em.createQuery("SELECT j FROM Jugadores j WHERE :email=j.email");
            query.setParameter("email", email);

            jugador = (Jugadores) query.getSingleResult();

            if (jugador == null) {
                return "error";

            }

            return "Correcto";
        } catch (Exception e) {
            return e.toString();

        }

    }

    /**
     * Registra un nuevo usuario con el email y nombre de usuario especificados.
     *
     * @param email el email del nuevo usuario
     * @param username el nombre de usuario del nuevo usuario
     * @return una cadena que indica el estado del registro ("Registro
     * correcto..." o el mensaje de error)
     */
    @Override
    public String register(String email, String username) {
        try {
            jugador = new Jugadores(email, username, 0);

            persisteixAmbTransaccio(jugador);

            return "Registro correcto...";

        } catch (Exception e) {
            return e.toString();
        }

    }

    /**
     * Obtiene el usuario actualmente conectado.
     *
     * @return el usuario actualmente conectado
     */
    @Override
    public Jugadores getLoggedUser() {

        return jugador;
    }

    private Object persisteixAmbTransaccio(Object ob) {
        List<String> errors = Validadors.validaBean(ob);

        if (errors.isEmpty()) {
            try {

                userTransaction.begin();
                em.persist(ob);
                userTransaction.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                String msg = "Error desant: " + errors.toString();

            }

        } else {

        }

        return ob;
    }

    /**
     * Obtiene la lista del Salón de la Fama ordenada por puntos ganados de
     * forma descendente.
     *
     * @return la lista del Salón de la Fama
     */
    @Override
    public List getHallOfFame() {
        try {
            Query query = em.createQuery("SELECT j FROM Jugadores j order by puntosGanados desc");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
