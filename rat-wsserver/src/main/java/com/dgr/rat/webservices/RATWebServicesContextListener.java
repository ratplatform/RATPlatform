/*
 * @author Daniele Grignani
 * Mar 26, 2015
*/

package com.dgr.rat.webservices;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.dgr.rat.session.manager.KeepAlive;
import com.dgr.rat.session.manager.RATSessionManager;

public class RATWebServicesContextListener implements ServletContextListener{
    public final static String MessageSenderContextKey = "MessageSenderContextKey";
    public final static String KeepAliveSent = "KeepAliveSent";
    public final static String KeepAliveReceived = "KeepAliveReceived";
    public final static String KeepAliveStatus = "KeepAliveStatus";
    public final static String HibernateFactoryKey = "HibernateFactoryKey";
//    private ScheduledExecutorService _scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private KeepAlive _keepAlive = null;
    private static EntityManagerFactory _entityManagerFactory = null;
    
	public RATWebServicesContextListener() {
		System.out.println("RATWebServicesListener");
	}
	
    public static EntityManager createEntityManager() {
        if (_entityManagerFactory == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return _entityManagerFactory.createEntityManager();
    }

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("RATWebServicesListener.contextDestroyed");
		try {
			_entityManagerFactory.close();
			RATSessionManager.getInstance().shutdown();
		} 
		catch (Exception e) {
			//TODO log e gestione
			e.printStackTrace();
		}
//        try {
//        	_executor.awaitTermination(30, TimeUnit.SECONDS);
//        	_messagingClient.close();
//        } 
//        catch (Exception e) {
//            e.printStackTrace();
//            //TODO log e gestione
//        }
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("RATWebServicesListener.contextInitialized");
		try {
			ServletContext servletContext = servletContextEvent.getServletContext();
			
			RATSessionManager.init();
			
			String springProducer = servletContext.getInitParameter("spring-producer");
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(springProducer);
			servletContext.setAttribute(MessageSenderContextKey, context);
			
			_entityManagerFactory = Persistence.createEntityManagerFactory("ratwsserver");
			_keepAlive = new KeepAlive(servletContext);
			_keepAlive.start();
//			_scheduledExecutorService.scheduleAtFixedRate(keepAlive, 1, 2, TimeUnit.SECONDS);
		} 
		catch (Exception e) {
			//TODO log e gestione
			e.printStackTrace();
		}
	}
}
