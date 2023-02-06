package com.winksys.renaserv.gateway;

import java.util.List;

import com.winksys.renaserv.data.Credential;

public interface Context {

	List<Credential> getCredentials(int code);
	
	<E> E getProperty(String name);

	<E> void setProperty(String name, E value);
	
}
