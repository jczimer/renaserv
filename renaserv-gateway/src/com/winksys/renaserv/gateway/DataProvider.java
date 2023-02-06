package com.winksys.renaserv.gateway;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EventoMonitorado;
import com.winksys.renaserv.gateway.cmd.ICommand;

public class DataProvider {

	private static class Key {

		private Class clazz;
		private Serializable key;
		
		public Key() {
			
		}

		@Override
		public boolean equals(Object obj) {
			Key key = (Key) obj;
			return key.clazz == clazz && this.key.equals(key.key);
		}

		@Override
		public int hashCode() {
			return ((clazz.hashCode() << 12) & 0xffff) | key.hashCode() & 0xffff;
		}


	}

	private static class CachedData {

		private long time;
		private Object data;
		
		public CachedData() {
			this.time = System.currentTimeMillis();
		}

		public boolean isValid(int time) {
			return (this.time + time) >= System.currentTimeMillis();
		}
	}

	private static final Logger LOG = Logger.getLogger(DataProvider.class);

	public static HashMap<Key, CachedData> cache = new HashMap<Key, CachedData>();

	public static List<EventoMonitorado> getEventosMonitorados(Credential credential) {
		
		Key key = new Key();
		key.clazz = EventoMonitorado.class;
		key.key = credential.getId();
		
		CachedData data = getFromCache(key, 120000);
		if (data == null) {
			
			List<EventoMonitorado> eventos = DataProxy.getInstance().execute(new ICommand() {
				
				@Override
				public <E> E execute(EntityManager em) {
					Query query = em.createQuery("from EventoMonitorado where credential=:credential");
					query.setParameter("credential", credential);
					List<EventoMonitorado> resultList = query.getResultList();
					
					return (E) resultList;
				}
			});
			
			data = new CachedData();
			data.data = eventos;
			
			cacheData(key, data);
			
		}
		
		return (List<EventoMonitorado>) data.data;
		
	}

	private synchronized static void cacheData(Key key, CachedData data) {
		cache.put(key, data);
	}

	private synchronized static CachedData getFromCache(Key key) {
		return getFromCache(key,0);
	}
	
	private synchronized static CachedData getFromCache(Key key, int time) {
		CachedData data = cache.get(key);
		if (data != null) {
			if (time > 0 && !data.isValid(time)) {
				LOG.debug(String.format("Invalid Key(%s,%s)", key.clazz.getName(), key.key.toString()));
				
				cache.remove(key);
				return null;
			}
		}
		if (data != null) {
			LOG.debug(String.format("Get from cache Key(%s,%s)", key.clazz.getName(), key.key.toString()));
		}
		return data;
	}

}
