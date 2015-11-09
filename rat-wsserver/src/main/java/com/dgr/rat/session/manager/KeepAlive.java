/**
 * @author: Daniele Grignani
 * @date: Nov 3, 2015
 */

package com.dgr.rat.session.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import com.dgr.rat.ratwsserver.helpers.Constants;
import com.dgr.utils.AppProperties;

public class KeepAlive {
	private ScheduledExecutorService _scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private ServletContext _servletContext = null;
	private ScheduledFuture<KeepAliveTask> _scheduledFuture = null;
	
	public KeepAlive(ServletContext servletContext) {
		_servletContext = servletContext;
	}
	
	@SuppressWarnings("unchecked")
	public void start(){
		int keepAliveInitialDelay = AppProperties.getInstance().getIntProperty(Constants.KeepAliveInitialDelay);
		int keepAlivePeriod = AppProperties.getInstance().getIntProperty(Constants.KeepAlivePeriod);
		
		KeepAliveTask task = new KeepAliveTask(_servletContext);
		_scheduledFuture = (ScheduledFuture<KeepAliveTask>) _scheduledExecutorService.scheduleAtFixedRate(task, 
				keepAliveInitialDelay, keepAlivePeriod, TimeUnit.MILLISECONDS);
	}
	
	public void shutdown() throws Exception{
		System.out.println("Start shutdown ....");
		 try {
			 _scheduledFuture.cancel(true);
			 _scheduledExecutorService.shutdown();
			if (!_scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
				_scheduledExecutorService.shutdownNow();
			}
		 } 
		 catch (InterruptedException e) {
			 _scheduledExecutorService.shutdownNow();
			 throw new Exception(e);
		}
	}
}
