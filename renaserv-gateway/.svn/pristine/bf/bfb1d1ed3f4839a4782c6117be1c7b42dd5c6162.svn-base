package com.winksys.renaserv.gateway;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.winksys.renaserv.gateway.cmd.ICommand;

public class DataProxy {

	private static DataProxy instance;
	private EntityManagerFactory factory;
	
	private DataProxy() {
		
	}

	public <E> E execute(ICommand cmd) {
		
		EntityManager em = getEm();
				
		try {
			EntityTransaction transaction = em.getTransaction();
			return (E) execute(cmd, em, transaction);
		} finally {
			em.close();
		}
		
		
	}
	private Object execute(ICommand cmd, EntityManager em, EntityTransaction transaction) {
		transaction.begin();
		
		try {
			Object ret = cmd.execute(em);
			transaction.commit();
			return ret;
		} catch (Throwable e) {
			transaction.rollback();
			throw e;
		}
		
	}

	private EntityManager getEm() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("renaserv");
		}
		EntityManager em = factory.createEntityManager();
		return em;
	}
	
	public static DataProxy getInstance() {
		if (instance == null) {
			instance = new DataProxy();
		}
		return instance;
	}
	
}
