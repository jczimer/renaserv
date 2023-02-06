package com.winksys.renaserv.gateway.cmd;

import javax.persistence.EntityManager;

import com.winksys.renaserv.data.Veiculo;

public class PersistVeiculo implements ICommand {
	
	private Veiculo veiculo;
	
	public PersistVeiculo() {
		
	}
	
	public PersistVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	@Override
	public <E> E execute(EntityManager em) {
		return (E) em.merge(veiculo);
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

}
