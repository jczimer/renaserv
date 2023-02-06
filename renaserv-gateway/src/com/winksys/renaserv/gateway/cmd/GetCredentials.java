package com.winksys.renaserv.gateway.cmd;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GetCredentials implements ICommand {

	private int system;
	
	private GetCredentials() {
		
	}
	
	public GetCredentials(int system) {
		this.system = system;
	}
	
	@Override
	public <E> E execute(EntityManager em) {
		
		Query query = em.createQuery("from Credential where system=:system and status=0"); // busca somente os ativos
		query.setParameter("system", system);
		
		return (E) query.getResultList();
	}

}
