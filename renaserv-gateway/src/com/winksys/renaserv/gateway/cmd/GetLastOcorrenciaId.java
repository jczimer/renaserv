package com.winksys.renaserv.gateway.cmd;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Veiculo;

public class GetLastOcorrenciaId implements ICommand {

	private Credential credential;
	private Veiculo veiculo;
	
	public GetLastOcorrenciaId(Credential credential) {
		this(credential, null);
	}
	
	public GetLastOcorrenciaId(Credential credential, Veiculo veiculo) {
		this.credential = credential;
		this.veiculo = veiculo;
	}
	
	@Override
	public <E> E execute(EntityManager em) {
		String expression = "select max(o.eventId) from Ocorrencia o where o.id.veiculo.id.credential=:credential";
		
		if (veiculo != null) {
			expression += " and id.veiculo=:veiculo";
		}
		
		Query query = em.createQuery(expression);
		query.setParameter("credential", credential);
		
		// 
		if (veiculo != null) {
			query.setParameter("veiculo", veiculo);
		}
		
		List<Number> resultList = query.getResultList();
		for (Number number : resultList) {
			if (number != null) {
				return (E) Integer.valueOf(number.intValue());
			}
		}
		return (E) Integer.valueOf(0);
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

}
