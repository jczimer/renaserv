package com.winksys.renaserv.gateway.driver.rodar;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

/**
 * Busca os clientes do sistema Rodar, retorna JsonArray com os dados do cliente.
 * 
 * @author winksys
 *
 */
public class GetClientesRequest {

	private static Logger LOG = Logger.getLogger(GetClientesRequest.class);
	
	private String url;
	private String user;
	private String password;
	
	public GetClientesRequest(String url, String user, String password) { 
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public JSONArray execute() throws ClientProtocolException, IOException {
		
		LOG.debug("Loading customer data from: " + user);
		HttpGet get = new HttpGet(url +"/api?u=" +  user + "&s=" + password + "&m=getclientes");
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		
		HttpResponse response = client.execute(get);
		JSONArray ret = (JSONArray) JSONSerializer.toJSON(EntityUtils.toString(response.getEntity()));
		
		return ret;
	}
	
}
