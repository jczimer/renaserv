package com.winksys.renaserv.gateway.driver.stc;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class GetClientsReq {

	private String key;
	private String url;
	private int page = 1;
	private int total = 0;
	private int perPage = 0;
	private JSONArray data;
	
	public GetClientsReq(String url, String key) {
		this.url = url;
		this.key = key;
	}
	
	public JSONArray execute() throws ClientProtocolException, IOException {
		
		JSONObject req = new JSONObject();
		req.put("key", this.key);
		req.put("page", page);
		
		HttpPost post = new HttpPost(url + "ws/client/list");
		post.setEntity(new StringEntity(req.toString()));
		post.addHeader("Content-Type", "application/json;charset=utf-8");
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		HttpResponse response = client.execute(post);
		HttpEntity entityResp = response.getEntity();
		
		JSONObject respJson =  (JSONObject) JSONSerializer.toJSON(EntityUtils.toString(entityResp));
		
		String msg = respJson.getString("msg");
		
		if ("success".equals(msg)) {
			JSONObject dataJson = respJson.getJSONObject("data");
			total = dataJson.getInt("total");
			perPage = dataJson.getInt("per_page");
			
			this.data = dataJson.getJSONArray("data");
			return data;
		}
		throw new IllegalStateException();
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getPage() {
		return page;
	}
	
	public int getPerPage() {
		return perPage;
	}
	
	public JSONArray getData() {
		return data;
	}
	
	public int qtPages() {
		int pages = total / perPage;
		int mod = total % perPage;
		
		if (mod > 0) {
			pages++;
		}
		return pages;
	}
	
	public boolean next() {
		if (page < qtPages()) {
			page++;
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		GetClientsReq req = new GetClientsReq("http://ap3.stc.srv.br/integration/prod/", "fe98fc123c80d7bc1253ea5ca5f834ae");
		
		do {
			req.execute();
					
			System.out.println("Itens Per Page: " + req.getPerPage());
			System.out.println("Page: " + req.getPage());
			System.out.println("Total: " + req.getTotal());
			System.out.println("Pages: " + req.qtPages());
			System.out.println("Data: " + req.getData());
		} while (req.next());
	}
}
