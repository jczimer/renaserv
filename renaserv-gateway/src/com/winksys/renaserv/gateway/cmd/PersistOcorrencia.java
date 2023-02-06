package com.winksys.renaserv.gateway.cmd;

import javax.persistence.EntityManager;

import com.winksys.renaserv.data.Ocorrencia;

public class PersistOcorrencia implements ICommand {

	private Ocorrencia ocorrencia;
	
	public PersistOcorrencia() {
		
	}
	
	public PersistOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
	@Override
	public <E> E execute(EntityManager em) {
		
		Ocorrencia o = em.find(Ocorrencia.class, ocorrencia.getId());
		if (o == null) {
			return (E) em.merge(ocorrencia);
		} 
		return (E) ocorrencia;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

}
