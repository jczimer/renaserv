package com.winksys.renaserv.gateway.cmd;

import javax.persistence.EntityManager;

import com.winksys.renaserv.data.Proprietario;

public class PersistCliente implements ICommand {

	private Proprietario proprietario;

	public PersistCliente(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	@Override
	public <E> E execute(EntityManager em) {
		Proprietario prop = em.find(Proprietario.class, proprietario.getId());
		return (E) em.merge(proprietario);
	}

}
