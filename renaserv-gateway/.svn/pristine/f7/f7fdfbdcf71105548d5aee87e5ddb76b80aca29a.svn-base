package com.winksys.renaserv.gateway.driver.stc;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EventoMonitorado;
import com.winksys.renaserv.data.Ocorrencia;
import com.winksys.renaserv.data.Ocorrencia.OcorrenciaId;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.gateway.Context;
import com.winksys.renaserv.gateway.DataProvider;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.Plugin;
import com.winksys.renaserv.gateway.cmd.GetLastOcorrenciaId;
import com.winksys.renaserv.gateway.cmd.ICommand;
import com.winksys.renaserv.gateway.cmd.PersistOcorrencia;
import com.winksys.renaserv.gateway.cmd.PersistVeiculo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class STCDriver implements Plugin {

	protected static final Logger LOG = Logger.getLogger(STCDriver.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int lastid = 0;
	
	@Override
	public void init(JSONObject props) {
		
		
	}

	@Override
	public void execute(Context ctx) throws Throwable {
		
		
		List<Credential> credentials = ctx.getCredentials(2);
		
		for (Credential credential : credentials) {
			
			Date lastcheck = ctx.getProperty("lastcheck");
			
			// optimize
			// update customers and vehicles data details each 30 minutes
			boolean canUpdateData = false;
			if (lastcheck == null) {
				canUpdateData = true;
			} else  {
				
				long time = System.currentTimeMillis() - lastcheck.getTime();
				
				if (time > 1800000L) {  // 30 minutes
					canUpdateData = true;
				}
				
			}
		
			if (canUpdateData) {
				try {
					procClientes(credential, ctx);
					procVehicles(credential, ctx);
					
					ctx.setProperty("lastcheck", new Date());
				} catch (Exception e) {
					LOG.error(null, e);
				}
			}
			// ****
			
			
			String user = credential.getUser();
			
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(credential.getPassword().getBytes());
			String pas = new BigInteger(m.digest()).toString(16);
			
			HashMap<String, Veiculo> map = new HashMap<String, Veiculo>();
			
			{
				JSONObject req = new JSONObject();
				req.put("key", credential.getKey());
				req.put("user", user);
				req.put("pass", pas);
				
				LOG.debug(req.toString());
				
				StringEntity entity = new StringEntity(req.toString());
				
				HttpPost post = new HttpPost(credential.getUrl() + "ws/getClientVehicles");
				post.setEntity(entity);
				post.addHeader("Content-Type","application/json;charset=utf-8");
				
				DefaultHttpClient client = new DefaultHttpClient();
				HttpParams params = client.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 5000);
				HttpConnectionParams.setSoTimeout(params, 15000);
				HttpResponse response = client.execute(post);
				
				String json = EntityUtils.toString(response.getEntity()); 
//				LOG.debug(json);
				
				JSONObject respJson = (JSONObject) JSONSerializer.toJSON(json);
				
				if (respJson.getBoolean("success")) {
				
				
					JSONArray dataArray = respJson.getJSONArray("data");
					
					for(int i = 0; i < dataArray.size(); i++) {
						JSONObject veiculoJson = dataArray.getJSONObject(i);
						LOG.debug(veiculoJson.getString("plate"));
						Veiculo veiculo = handleVeiculo(credential, ctx, veiculoJson);
						
						map.put(veiculo.getPlaca(), veiculo);
					}
				}
			}
			
			Thread.sleep(1000);
			
			
			// ocorrencias
			lastid = ctx.getProperty("lastid") == null ? DataProxy.getInstance().execute(new GetLastOcorrenciaId(credential)) : ctx.getProperty("lastid");
			{			
				JSONObject req = new JSONObject();
				req.put("key", credential.getKey());
				req.put("user", user);
				req.put("pass", pas);
				req.put("eventId", lastid);
				
				StringEntity entity = new StringEntity(req.toString());
				
				HttpPost post = new HttpPost(credential.getUrl() + "ws/getClientEvents");
				post.setEntity(entity);
				post.addHeader("Content-Type","application/json;charset=utf-8");
				
				DefaultHttpClient client = new DefaultHttpClient();
				HttpParams params = client.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 5000);
				HttpConnectionParams.setSoTimeout(params, 15000);
				HttpResponse response = client.execute(post);
				
				String json = EntityUtils.toString(response.getEntity()); 
				
				JSONObject respJson = (JSONObject) JSONSerializer.toJSON(json);
				
				LOG.info(String.format("Handle events: lastid: %d", lastid));
				if (respJson.getBoolean("success")) {
					JSONArray dataArray = respJson.getJSONArray("data");
					
					
					
					
						for(int i = 0; i < dataArray.size(); i++) {
							JSONObject eventoJson = dataArray.getJSONObject(i);
							int lid = eventoJson.getInt("eventId");
							
							handleOcorrencia(credential, ctx, eventoJson, map);
							
							if (lid > lastid) {
								lastid = lid; 
							}
						}
						
						ctx.setProperty("lastid", lastid);
					
					
				} else {
					LOG.error("Erro ao carregar dados getVehicleEvents: " + respJson.getInt("error"));
				}
			} 
			
		}
		
		
	}

	private HashMap<String, Veiculo> procVehicles(Credential cred, Context ctx) throws ClientProtocolException, IOException {
		HashMap<String, Veiculo> map = new HashMap<String, Veiculo>();
		
		LOG.info("Processando veiculos");
		GetVehiclesReq req = new GetVehiclesReq(cred.getUrl(), "fe98fc123c80d7bc1253ea5ca5f834ae");
		
		do {
			LOG.info("Proc página " + req.getPage());
			JSONArray data = req.execute();
			
			new VehicleProcessor(DataProxy.getInstance(), data, cred).execute();
			
			
		} while (req.next());
		
		return map;
	}

	private void procClientes(Credential credential, Context ctx) throws ClientProtocolException, IOException {
		
		GetClientsReq req = new GetClientsReq(credential.getUrl(), credential.getKey());
		
		LOG.info("Processando clientes");
		do {
			LOG.info("Pagina " + req.getPage());
			JSONArray data = req.execute();
			
			new GetClientsProcessor(DataProxy.getInstance(), data, credential).execute();
			
		} while(req.next());
		
		
	}

	private void handleOcorrencia(Credential credential, Context ctx, JSONObject eventoJson, HashMap<String, Veiculo> map) throws ParseException {
		LOG.debug(eventoJson);
		
		String placa = eventoJson.getString("licensePlate");
		Date data = sdf.parse(eventoJson.getString("date"));
		int id = eventoJson.getInt("eventId");
		
		double speed = 0, lat = 0, lon = 0;
		String descricao = null;
		String ignition = null;
		
		if (eventoJson.containsKey("speed")) {
			String sspeed = eventoJson.getString("speed");
			if (!eventoJson.isNullObject()) {
				sspeed = sspeed.replaceAll("[^0-9\\.]", "0");
				speed = Double.parseDouble(sspeed);
			}
		}
		if (eventoJson.containsKey("latitude")) {
			lat = eventoJson.getDouble("latitude");
		}
		if (eventoJson.containsKey("longitude")) {
			lon = eventoJson.getDouble("longitude");
		}
		if (eventoJson.containsKey("description")) {
			descricao = eventoJson.getString("description");
		}
		if (eventoJson.containsKey("ignition")) {
			ignition = eventoJson.getString("ignition");
		}
		
		Veiculo veiculo = map.get(placa);
		
		if (id > lastid) {
			lastid = id;
		}
		
		List<EventoMonitorado> eventos = DataProvider.getEventosMonitorados(credential);
		
		for (EventoMonitorado eventoMonitorado : eventos) {
			
			if (descricao.equals(eventoMonitorado.getId().getEvento().getDescricao())) {
			
				OcorrenciaId ocorrenciaId = new OcorrenciaId();
				ocorrenciaId.setDataDados(data);
				ocorrenciaId.setVeiculo(veiculo);
				ocorrenciaId.setEvento(eventoMonitorado.getId().getEvento());
				
				Ocorrencia ocorrencia = new Ocorrencia();
				ocorrencia.setId(ocorrenciaId);
				ocorrencia.setDataCad(new Date());
				ocorrencia.setLat((float) lat); 
				ocorrencia.setLon((float) lon);
				ocorrencia.setSpeed((float) speed);
				ocorrencia.setIo("ON".equals(ignition) ? 1 : 0);
				ocorrencia.setEventId(id);
				
				DataProxy.getInstance().execute(new PersistOcorrencia(ocorrencia));
				break;
			
			}
		}
		
		
		
		
//		OcorrenciaId id = new OcorrenciaId();
//		id.setCredential(credential);
//		id.setDataDados(dataDados);
		
	}

	private Veiculo handleVeiculo(Credential credential, Context ctx, JSONObject veiculoJson) {
		
		LOG.debug(veiculoJson.toString());
		String placa = veiculoJson.getString("plate");
		String label = veiculoJson.getString("label");
		long id = veiculoJson.getLong("vehicleId");
		
		Date dataDados;
		try {
			dataDados = sdf.parse(veiculoJson.getString("date"));
		} catch (ParseException e) {
			dataDados = null;
		}
		
		int io;
		double lat,lon, direction, speed, tensaoBateria;
		try {
			io = "ON".equals(veiculoJson.getString("ignition")) ? 1 : 0;
			io |= "ON".equals(veiculoJson.getString("output1")) ? 0x10 : 0x00;
			io |= "ON".equals(veiculoJson.getString("output2")) ? 0x20 : 0x00;
			speed = veiculoJson.getDouble("speed");
			direction = veiculoJson.getDouble("direction");
			lat = veiculoJson.getDouble("latitude");
			lon = veiculoJson.getDouble("longitude");
			tensaoBateria = veiculoJson.getDouble("mainBattery");
		} catch (Exception e) {
			io = 0;
			lat = 0;
			lon = 0;
			direction = 0;
			speed = 0;
			tensaoBateria = 0.0;
		}
		
		
		VeiculoId vid = new VeiculoId();
		vid.setCredential(credential);
		vid.setId((int) id);
		
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca(placa);
		veiculo.setModelo(label);
		veiculo.setDataAtualizacao(new Date());
		veiculo.setDataDados(dataDados);
		veiculo.setId(vid);
		veiculo.setIo(io);
		veiculo.setLat((float) lat);
		veiculo.setLon((float) lon);
		veiculo.setSpeed((float) speed);
		veiculo.setDirection((int) direction); 
		veiculo.setTensaoBateria((float) tensaoBateria);
		
		veiculo = mergeVeiculo(veiculo);
		
		return DataProxy.getInstance().execute(new PersistVeiculo(veiculo));
		
	}

	private Veiculo mergeVeiculo(Veiculo veiculo) {
		Veiculo ret = DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				Veiculo v = em.find(Veiculo.class, veiculo.getId());
				
				if (v == null) {
					v = veiculo;
				}
				
				v.setDataAtualizacao(veiculo.getDataAtualizacao());
				v.setDataDados(veiculo.getDataDados());
				v.setLastCheck(veiculo.getLastCheck());
				v.setLat(veiculo.getLat());
				v.setLon(veiculo.getLon());
				v.setIo(veiculo.getIo());
				v.setSpeed(veiculo.getSpeed());
				v.setDirection(veiculo.getDirection());
				v.setTensaoBateria(veiculo.getTensaoBateria());
				v = em.merge(v);
				return (E) v;
			}
		});
		
		return ret;
	}

}
