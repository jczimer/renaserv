package com.winksys.renaserv.gateway.driver.rodar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.gateway.CommandThreadPool;
import com.winksys.renaserv.gateway.CommandThreadPool.Task;
import com.winksys.renaserv.gateway.Context;
import com.winksys.renaserv.gateway.Plugin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class RodarDownloader implements Plugin {

	private String host;
	private CommandThreadPool pool;
	private long nextTimeout = 0;
	
	private HashMap<Integer, Long> cache = new HashMap<Integer, Long>();

	@Override
	public void init(JSONObject props) {
		
		pool = new CommandThreadPool(5);
		pool.init();
		
	}

	@Override
	public void execute(Context ctx) throws Exception {
		
		long time = System.currentTimeMillis();
		if (time > nextTimeout) {
			
			List<com.winksys.renaserv.data.Credential> credentials = ctx.getCredentials(1);
			
			for (Credential credential : credentials) {
				
				HttpGet get = new HttpGet(credential.getUrl() +"/api?u=" +  credential.getUser() + "&s=" + credential.getPassword() + "&m=getcomm&format=json");
				DefaultHttpClient client = new DefaultHttpClient();
				
				try {
					HttpResponse response = client.execute(get);
					String string = EntityUtils.toString(response.getEntity());
					
					JSONArray veiculos = (JSONArray) JSONSerializer.toJSON(string);
					for(int i = 0; i < veiculos.size(); i++) {
						JSONObject  veiculo = (JSONObject) veiculos.getJSONObject(i);
						handleVeiculo(veiculo, credential);
					}
					
				} catch (Exception e) {
					throw e;
				}
				
			}
			nextTimeout = System.currentTimeMillis() + 60000;
		}
		
		
		
	}

	private void handleVeiculo(final JSONObject veiculo, Credential credential) throws ClientProtocolException, IOException {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		pool.addTask(new Task() {

			@Override
			public void execute() throws ClientProtocolException, IOException {
						
				
				int id = veiculo.getInt("id");
				System.out.println(id + " - " +  veiculo.getString("placa"));
				
				Long last = null;
				synchronized (cache) {
					last = cache.get(veiculo.getInt("id"));
					if (last == null) {
						last = 0L;
					}
				}
				
				int p = 0;
				while (true) {
					System.out.println("Executing " + veiculo.getString("placa") + ",p=" + p);
					
					File f = new File("output/v" + veiculo.getString("placa"));
					f.mkdirs();
					f = new File(f, "p" + p + ".json");
					
					HttpGet get = new HttpGet(credential.getUrl() +"/api?u=" +  credential.getUser() + //
							"&s=" + credential.getPassword() + "&m=gethist&format=json&v=" + id +  //
							"&lastid=" +last +
							"&p=" + p);
					DefaultHttpClient client = new DefaultHttpClient();
					HttpResponse response = client.execute(get);
					String string = EntityUtils.toString(response.getEntity());

//					if ("[]".equals(string)) {
//						System.out.println("Finish " + veiculo.getString("placa"));
//						break;
//					}
//					
//					FileOutputStream fos = new FileOutputStream(f);
//					fos.write(string.getBytes());
//					fos.close();
					
					JSONArray posicoes = (JSONArray) JSONSerializer.toJSON(string);
					
					
					
					for(int i = 0; i < posicoes.size(); i++) {
						JSONObject posicao = posicoes.getJSONObject(i);
						
						long aux = posicao.getLong("id");
						if (aux > last) {
							last = aux;
						}
						String date = sdf.format(new Date(posicao.getLong("data")));
						System.out.println(date + " " + posicao.getDouble("lat") + "," + posicao.getLong("lon") + "," + posicao.getLong("data"));
						
					}
					p++;
					synchronized (cache) {
						cache.put(id, last);
						nextTimeout =  System.currentTimeMillis() + 60000;
					}
					
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				}
			}
			
		});
		
		
		
		
//		System.out.println(string);
		
		
	}

}
