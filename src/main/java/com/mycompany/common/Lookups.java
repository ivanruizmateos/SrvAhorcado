/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mycompany.main.LoginEJB;
import com.mycompany.main.PartidaLogicaEJB;


/**
 * Classe encarregada de fer les connexions amb els EJB remots
 * @author manel
 */
public class Lookups {
    
     private static final String APP_VERSION = "1.0.0";

    private static final String wildFlyInitialContextFactory = "org.wildfly.naming.client.WildFlyInitialContextFactory";

    private static final String appName = "ahorcado_servidor-" + APP_VERSION;
    

     /**
     * Realiza la búsqueda y obtención de la referencia remota al EJB ILogin.
     * 
     * @return una referencia al EJB ILogin remoto
     * @throws NamingException si ocurre un error durante la búsqueda del EJB remoto
     */
   public static ILogin loginEJBRemoteLookup() throws NamingException {
        String strlookup = "ejb:/" + appName + "/" + LoginEJB.class.getSimpleName() + "!" + ILogin.class.getName();

        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, wildFlyInitialContextFactory);

        Context context = new InitialContext(jndiProperties);

        return (ILogin) context.lookup(strlookup);
    }
     
   /**
     * Realiza la búsqueda y obtención de la referencia remota al EJB IPartidaLogica.
     * 
     * @return una referencia al EJB IPartidaLogica remoto
     * @throws NamingException si ocurre un error durante la búsqueda del EJB remoto
     */
           public static IPartidaLogica partidaEJBRemoteLookup() throws NamingException
    {
        String strlookup = "ejb:/" + appName + "/" + PartidaLogicaEJB.class.getSimpleName() + "!" + IPartidaLogica.class.getName();
            
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  wildFlyInitialContextFactory);
        
        Context context = new InitialContext(jndiProperties);

        return (IPartidaLogica) context.lookup(strlookup);
    }

}
