package com.winksys.renaserv.gateway.cmd;

import javax.persistence.EntityManager;

public interface ICommand {

	<E> E execute(EntityManager em);
	
}
