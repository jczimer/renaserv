package com.winksys.renaserv.gateway.driver.mportal;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EnStatus;
import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Proprietario.ProprietarioId;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.cmd.ICommand;
import com.winksys.renaserv.gateway.cmd.PersistCliente;
import com.winksys.renaserv.gateway.cmd.PersistVeiculo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class CredentialMportalProcessor implements Runnable {

	public static Logger LOG = Logger.getLogger(CredentialMportalProcessor.class);

	

	private Credential credential;

	public CredentialMportalProcessor(Credential credential) {
		this.credential = credential;
	}

	@Override
	public void run() {

		while (true) {
			try {
				Credential cred = DataProxy.getInstance().execute(new ICommand() {
					
					@Override
					public <E> E execute(EntityManager em) {
						return (E) em.find(Credential.class, credential.getId());
					}
				});
				
				if (cred.getStatus() == EnStatus.ATIVO) {
					execute();
				}
				
	
			} catch (Throwable e) {
				LOG.error("", e);
			}
			
			// espera 60s
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				
			}
		}
	}

	private void execute() throws Throwable {
		// Autenticação
		final JSONObject handshake = new JSONObject();
		handshake.put("username", credential.getUser());
		handshake.put("password", credential.getPassword());
		handshake.put("appid", credential.getKey());

		LOG.debug(handshake.toString());

		StringEntity entity = new StringEntity(handshake.toString());

		HttpPost post = new HttpPost(String.format("%s/seguranca/logon", credential.getUrl()));
		post.addHeader("Content-Type", "application/json");
		post.setEntity(entity);

		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpResponse response = client.execute(post);

		String resp = EntityUtils.toString(response.getEntity());
		LOG.debug(resp);

		JSONObject authJson = (JSONObject) JSONSerializer.toJSON(resp);

		if (authJson.containsKey("object")) {

			handshake.put("token", authJson.getJSONObject("object").getString("token"));

			// Busca veiculos
			post = new HttpPost(String.format("%s/veiculos", credential.getUrl()));
			post.addHeader("Content-Type", "application/json");
			post.addHeader("token", handshake.getString("token"));

			response = client.execute(post);
			resp = EntityUtils.toString(response.getEntity());

			JSONObject veiculosRespJson = (JSONObject) JSONSerializer.toJSON(resp);
			JSONArray veiculos = veiculosRespJson.getJSONArray("object");

			for (int i = 0; i < veiculos.size(); i++) {
				final JSONObject veiculoJson = veiculos.getJSONObject(i);
				LOG.debug(veiculoJson.toString());

				JSONArray dispositivos = veiculoJson.getJSONArray("dispositivos");

				if (dispositivos == null || dispositivos.isEmpty()) {
					continue;
				}

				int id = veiculoJson.getInt("id");
				String placa = veiculoJson.getString("placa");
				String marca = veiculoJson.getString("marca");
				String modelo = veiculoJson.getString("modelo");
				String proprietario = veiculoJson.getString("proprietario");
				int idProprietario = veiculoJson.getInt("proprietarioId");

				String chassi = null, renavam = null, cor = null, ano = null;

				if (veiculoJson.containsKey("chassi")) {
					chassi = veiculoJson.getString("chassi");
				}
				if (veiculoJson.containsKey("renavam")) {
					renavam = veiculoJson.getString("renavam");
				}
				if (veiculoJson.containsKey("cor")) {
					cor = veiculoJson.getString("cor");
				}
				if (veiculoJson.containsKey("anoModelo")) {
					ano = veiculoJson.getString("anoModelo");
				}

				LOG.debug(String.format("Placa: %s", placa));

				ProprietarioId propId = new ProprietarioId();
				propId.setCredential(credential);
				propId.setId(idProprietario);

				Proprietario prop = new Proprietario();
				prop.setId(propId);
				prop.setNome(proprietario);

				DataProxy.getInstance().execute(new PersistCliente(prop));

				VeiculoId vid = new VeiculoId();
				vid.setId(id);
				vid.setCredential(credential);

				Veiculo v = DataProxy.getInstance().execute(new ICommand() {

					@Override
					public <E> E execute(EntityManager em) {
						return (E) em.find(Veiculo.class, vid);
					}

				});

				if (v == null) {
					v = new Veiculo();
					v.setId(vid);
				}

				final Veiculo veiculoEntity = v;
				veiculoEntity.setDataAtualizacao(new Date());
				veiculoEntity.setPlaca(placa);
				veiculoEntity.setChassi(chassi);
				veiculoEntity.setRenavam(renavam);
				veiculoEntity.setCor(cor);
				veiculoEntity.setAno(ano);
				veiculoEntity.setModelo(String.format("%s/%s", marca, modelo));
				veiculoEntity.setProprietario(prop);

				DataProxy.getInstance().execute(new PersistVeiculo(veiculoEntity));

				// Inicio da rotina de tratamento de eventos
				// Multithread

				

			}
			
			new MPortalTask(handshake, credential).execute();

		}
	}

	private Date getDataInicio(Credential credential, int id) {
		Date dataInicio = DataProxy.getInstance().execute(new ICommand() {

			@Override
			public <E> E execute(EntityManager em) {
				VeiculoId vid = new VeiculoId();
				vid.setCredential(credential);
				vid.setId(id);

				Query query = em.createQuery("select max(v.lastCheck) from Veiculo v where v.id=:veiculo");
				query.setParameter("veiculo", vid);
				List<Long> resultList = query.getResultList();

				if (resultList.isEmpty()) {
					return null;
				}
				Long data = resultList.get(0);

				if (data == null) {
					return (E) new Date();
				}

				long current = System.currentTimeMillis() - 60000;

				if (data > current) {
					data = current;
				} else if (data == 0) {
					data = System.currentTimeMillis();
				}

				return (E) new Date(data);
			}
		});

		if (dataInicio == null) {
			dataInicio = new Date();
			dataInicio.setHours(0);
			dataInicio.setMinutes(0);
			dataInicio.setSeconds(0);
		}
		return dataInicio;
	}

}
