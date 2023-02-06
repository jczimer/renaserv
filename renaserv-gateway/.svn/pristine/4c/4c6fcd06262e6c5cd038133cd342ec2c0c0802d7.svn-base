package com.winksys.renaserv.gateway;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.gateway.cmd.GetCredentials;
import com.winksys.renaserv.gateway.cmd.ICommand;

public class HandlerRunnable implements Runnable, Context {

	
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	private static final Logger LOG = Logger.getLogger(HandlerRunnable.class);
	private Plugin plugin;
	
	public HandlerRunnable(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			try {			
				plugin.execute(this);
			} catch (Throwable e) {
				LOG.error("",e);
			}
			

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public List<Credential> getCredentials(int code) {
		List<Credential> list = DataProxy.getInstance().execute(new GetCredentials(code));
		
		// update check data
		DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				
				for (Credential c : list) {
					c.setLastCheck(new Date());
					em.merge(c);
				}
				
				return null;
			}
		});
		
		return list;
	}

	@Override
	public <E> E getProperty(String name) {
		synchronized (properties) {
			return (E) properties.get(name);
		}
	}

	@Override
	public <E> void setProperty(String name, E value) {
		synchronized (properties) {
			properties.put(name, value);
		}
	}

}
