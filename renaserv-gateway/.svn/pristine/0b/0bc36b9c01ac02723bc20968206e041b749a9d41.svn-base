package com.winksys.renaserv.gateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Gateway {
	
	private HashMap<String, Plugin> drivers = new HashMap<String, Plugin>();

	public static void main(String[] args) throws Exception {
		
		new Gateway().execute();
		
		
	}
	
	public void execute() throws Exception {
		// procura arquivo de configuração na pasta do projeto
		File file = new File("conf/config.json");
		FileInputStream fis = new FileInputStream(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		StringBuilder sb = new StringBuilder();
				
		while (true) {
			int readed = reader.read();
			if (readed == -1) {
				break;
			}
			sb.append((char) readed);
			
		}
		fis.close();
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(sb.toString());
		init(json);
		
		for(Plugin plugin : drivers.values()) {
			
			new Thread(new HandlerRunnable(plugin)).start();
			
		}
	}

	private  void init(JSONObject json) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		JSONArray handlers = json.getJSONArray("handlers");
		
		for (int i = 0; i < handlers.size(); i++) {
			JSONObject handler = handlers.getJSONObject(i);
			
			Class<Plugin> clDriver = (Class<Plugin>) Class.forName(handler.getString("className"));
			Plugin plugin = clDriver.newInstance();
			
			if (handler.containsKey("props")) {
				plugin.init(handler.getJSONObject("props"));
			} else {
				plugin.init(null);
			}
			
			this.drivers.put(handler.getString("name"), plugin);
			
		}
		
	}
	
}
