package com.winksys.renaserv.web.servlet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class CommandExecutor {
	
	public static interface ICommand<E> { 
		
		E execute(EntityManager em);
		
	}

	private static final Logger LOG = Logger.getLogger(CommandExecutor.class);
	final private static CommandExecutor singleton;
	
	private EntityManagerFactory emf;
	
	static {
		singleton = new CommandExecutor();
	}
	
	public static <E> E execute(ICommand cmd) {
		return singleton.innerExecute(cmd);
		
	}
	
	private <E> E innerExecute(ICommand cmd) {
		
		emf = getEmf();
		EntityManager em = emf.createEntityManager();
			
		try {
			EntityTransaction transaction = em.getTransaction();		
			transaction.begin();
			
			try {
				E e = (E) cmd.execute(em);
				transaction.commit();
				
				return e;
			} catch (Throwable e) {
				LOG.error("Erro cmd: " + cmd.getClass().getName(), e);
				transaction.rollback();
				throw e;
			}
		} finally {
			em.close();
		}
				
	}

	private EntityManagerFactory getEmf() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("renaserv");
		}
		return emf;
	}
	
	
}
