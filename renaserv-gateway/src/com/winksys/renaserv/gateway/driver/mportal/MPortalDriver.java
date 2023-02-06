package com.winksys.renaserv.gateway.driver.mportal;

import java.util.HashSet;
import java.util.List;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.gateway.Context;
import com.winksys.renaserv.gateway.Plugin;

import net.sf.json.JSONObject;

public class MPortalDriver implements Plugin {

	private HashSet<Integer> map = new HashSet<Integer>();
	

	@Override
	public void execute(Context ctx) throws Throwable {
		
	
		List<Credential> credentials = ctx.getCredentials(3);

		for (final Credential credential : credentials) {

			 if (!map.contains(credential.getId())) {
				 new Thread(new CredentialMportalProcessor(credential)).start();
				 
				 map.add(credential.getId());
			 }
			
		}

	}

	@Override
	public void init(JSONObject props) {
	}

}
