/*
 * @author Daniele Grignani
 * Mar 26, 2015
*/

package com.dgr.rat.webservices;

import java.io.File;
import java.nio.file.FileSystems;

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
import com.dgr.rat.session.manager.RATSessionManager;

public class RATWebServicesContextListener implements ServletContextListener{
    public final static String MessageSenderContextKey = "MessageSenderContextKey";
    public final static String HibernateFactoryKey = "HibernateFactoryKey";
    //public final static String ThreadPool = "ThreadPool";
    
    // TODO: fare attenzione: io li metto nel servletcontext, ma il servletcontext è unico per tutte le applicazioni web
    // del tomcat corrente! Ciò significa che tutti i messaggi di tutte le istanze di questa web app vanno nell'unico
    // thread dell'ExecutorService
	//private RATMessagingClient _messagingClient = null;
	//private ExecutorService _executor = (ExecutorService)Executors.newFixedThreadPool(1);
	//private CompletionService<String> _pool = new ExecutorCompletionService<String>(_executor);
	//private RATSessionManager _sessionManager = new RATSessionManager();
	
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
			//String shiroIniPath = servletContext.getInitParameter("shiro-ini");
			//Authenticator.init(shiroIniPath);
			
			String springProducer = servletContext.getInitParameter("spring-producer");
			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(springProducer);
			servletContext.setAttribute(MessageSenderContextKey, context);
			
			_entityManagerFactory = Persistence.createEntityManagerFactory("ratwsserver");
			
			/*
			Configuration configuration = new Configuration();
			String persistence = servletContext.getInitParameter("persistence");
	        configuration.configure(new File(persistence));
	        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	        servletContext.setAttribute(HibernateFactoryKey, sessionFactory);
	        */
			
//			String path = "conf" + FileSystems.getDefault().getSeparator() + "spring-consumer.xml";
//			FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(path);
//			_messagingServer = (MessageSender)context.getBean("MessageSender");
			
//			_messagingClient = new RATMessagingClient();
//			
//			// TODO: fare attenzione li metto nel servletcontext, ma il servletcontext è unico per tutte le applicazioni web
//			// sarebbe meglio usare la session
//			ServletContext servletContext = servletContextEvent.getServletContext();
//			servletContext.setAttribute(MessageSender, _messagingClient);
//			servletContext.setAttribute(ThreadPool, _pool);
		} 
		catch (Exception e) {
			//TODO log e gestione
			e.printStackTrace();
		}
	}
}
