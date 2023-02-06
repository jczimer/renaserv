package com.winksys.renaserv.gateway.driver.mportal;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.data.EventoMonitorado;
import com.winksys.renaserv.data.Ocorrencia;
import com.winksys.renaserv.data.Ocorrencia.OcorrenciaId;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.gateway.CommandThreadPool.Task;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.cmd.ICommand;
import com.winksys.renaserv.gateway.cmd.PersistOcorrencia;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class MPortalTask implements Task {

	private static Logger LOG = Logger.getLogger(MPortalTask.class);

	
	private JSONObject handshake;
	private Credential credential;

	private HashMap<String, Evento> mapaEventosSistema = new HashMap<String, Evento>();
	private HashMap<Integer, String> mapaEventosMportal = new HashMap<Integer, String>();
	
	public MPortalTask(JSONObject handshake, Credential credential) {
		super();
		
		this.handshake = handshake;
		this.credential = credential;

	}

	@Override
	public void execute() throws Throwable {
		// adia a proxima verificacao
//				nextTime = System.currentTimeMillis() + 60000;

		// Busca dados desde do ultimo registro
		
		LOG.info("Buscando eventos");
		loadEventos();
		
		LOG.info("Buscando eventos portal");
		loadEventosMportal();
		
		LOG.info("Buscando dados novos");
		HttpPost post = new HttpPost(String.format("%s/integracao/dados_novos", credential.getUrl()));
		post.addHeader("Content-Type", "application/json");
		post.addHeader("token", handshake.getString("token"));		
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		HttpResponse response = client.execute(post);

		String resp = EntityUtils.toString(response.getEntity());
		
		JSONObject posicoesRequest = (JSONObject) JSONSerializer.toJSON(resp);
		JSONArray veiculos = posicoesRequest.getJSONArray("object");

		for(int i = 0; i < veiculos.size(); i++) {
			JSONObject veiculo = veiculos.getJSONObject(i);
			processVeiculo(veiculo);
		}
		
		
	}

	

	private void processVeiculo(JSONObject veiculo) {
		
		LOG.debug("Buscando dados de veículo: " + veiculo.getString("placa"));
		
		JSONArray dispositivos = veiculo.getJSONArray("dispositivos");
		for(int i = 0; i < dispositivos.size(); i++) {
			JSONObject dispositivo = dispositivos.getJSONObject(i);
			processDispostivo(veiculo, dispositivo);
		}
		
		
	}

	private void processDispostivo(JSONObject veiculo, JSONObject dispositivo) {
		
		LOG.debug("Buscando dados de dispositivo: " + dispositivo.getString("numero"));
		
		JSONArray posicoes = dispositivo.getJSONArray("posicoes");
		for(int i = 0; i < posicoes.size(); i++) {
			JSONObject posicao = posicoes.getJSONObject(i);
			processPosicao(veiculo, dispositivo, posicao);
		}
		
	}


	private void processPosicao(JSONObject veiculoJson, JSONObject dispositivoJson, JSONObject posicaoJson) {
	
		
		String eventName = mapaEventosMportal.get(posicaoJson.getInt("eventoId"));
		Evento evento = mapaEventosSistema.get(eventName);
		Veiculo veiculo = findVeiculo(veiculoJson.getInt("id"));
		
		LOG.debug("Processando evento: " + eventName);
		
		Date dataDados = new Date(posicaoJson.getLong("dataGPS"));
		float lat = (float) posicaoJson.getDouble("latitude");
		float lon = (float) posicaoJson.getDouble("longitude");
		int proa = posicaoJson.getInt("proa");
		float speed = (float) posicaoJson.getDouble("velocidade");
		
		// atualiza veiculo
		{
			if (veiculo.getDataDados() == null || dataDados.compareTo(veiculo.getDataDados()) > 0) {
				veiculo.setDataDados(dataDados);
				veiculo.setDataAtualizacao(new Date());
				veiculo.setDirection(proa);
				veiculo.setLat(lat);
				veiculo.setLon(lon);
				udpateVeiculo(veiculo);
			}
			
		}
		
		// if is monitorated
		if (evento != null) {
			
			OcorrenciaId id = new OcorrenciaId();
			id.setEvento(evento);
			id.setDataDados(dataDados);
			id.setVeiculo(veiculo);
			
			Ocorrencia ocorrencia = findOcorrencia(id);
			if (ocorrencia == null) {
				ocorrencia = new Ocorrencia();				
				ocorrencia.setDataCad(new Date());
				ocorrencia.setLat(lat);
				ocorrencia.setLon(lon);
				ocorrencia.setDirection(proa);
				ocorrencia.setIo(0);
				ocorrencia.setSpeed(speed);
				
				save(ocorrencia);
				
			}
						
		}
		
		
	}
	
	private void udpateVeiculo(Veiculo veiculo) {
		DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				em.merge(veiculo);
				return null;
			}
		});
	}

	private void save(Ocorrencia ocorrencia) {
		DataProxy.getInstance().execute(new PersistOcorrencia(ocorrencia));
	}

	private Ocorrencia findOcorrencia(OcorrenciaId id) {
		
		Ocorrencia ret = DataProxy.getInstance().execute(new ICommand() {

			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.find(Ocorrencia.class, id);
			}
			
		});
		
		return ret;
	}

	private Veiculo findVeiculo(int id) {
		VeiculoId vid = new VeiculoId();
		vid.setCredential(credential);
		vid.setId(id);
		
		Veiculo v = DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.find(Veiculo.class, vid);
			}
		});
		
		return v;
	}

	private void loadEventos() {
		
		List<EventoMonitorado> eventos = DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				
				Query query = em.createQuery("from EventoMonitorado where id.credential=:credential");
				query.setParameter("credential", credential);
				
				return (E) query.getResultList();
			}
		});
		
		for(EventoMonitorado evento : eventos) {
			mapaEventosSistema.put(evento.getId().getEvento().getDescricao(), evento.getId().getEvento());
		}
		
	}
	
	private void loadEventosMportal() throws ClientProtocolException, IOException {
		
		HttpPost post = new HttpPost(String.format("%s/info/eventos", credential.getUrl()));
		post.addHeader("Content-Type", "application/json");
		post.addHeader("token", handshake.getString("token"));		
		

		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		HttpResponse response = client.execute(post);
		
		JSONObject jsonResp = (JSONObject) JSONSerializer.toJSON(EntityUtils.toString(response.getEntity()));
		JSONArray data = jsonResp.getJSONArray("object");
		
		for(int i = 0; i < data.size(); i++) {
			JSONObject evento = data.getJSONObject(i);
			mapaEventosMportal.put(evento.getInt("id"), evento.getString("nome"));
		}
		
	}

}
