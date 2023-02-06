package com.winksys.renaserv.gateway.driver.rodar;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Proprietario.ProprietarioId;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.gateway.CommandThreadPool.Task;
import com.winksys.renaserv.gateway.Context;
import com.winksys.renaserv.gateway.DataProvider;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.cmd.ICommand;
import com.winksys.renaserv.gateway.cmd.PersistCliente;
import com.winksys.renaserv.gateway.cmd.PersistOcorrencia;
import com.winksys.renaserv.gateway.cmd.PersistVeiculo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class HandleVeiculoTask implements Task {

	private static final Logger LOG = Logger.getLogger(HandleVeiculoTask.class);
	
	private JSONObject veiculo;
	private Credential credential;
	private Context ctx;


	public HandleVeiculoTask(JSONObject veiculo, Credential credential, Context ctx) {
		this.veiculo = veiculo;
		this.credential = credential;
		this.ctx = ctx;
	}

	@Override
	public void execute() throws ClientProtocolException, IOException {
				
		
		int id = veiculo.getInt("id");
		String placa = veiculo.getString("placa");
		LOG.debug(String.format("%d - %s", id, placa));
		
		Proprietario prop = updateCliente(veiculo, credential);
		
		// atualiza dados do veiculo
		Veiculo veiculoEntity = updateVeiculo(veiculo, credential, id, placa, prop);
		
	}

	private Proprietario updateCliente(JSONObject veiculo, Credential credential) {
		
		LOG.debug(String.format("Credential: %d, %s", credential.getId(), credential.getNome()));
		
		
				
		ProprietarioId id = new ProprietarioId();
		id.setCredential(credential);
		id.setId(veiculo.getInt("cliente"));
		
		// localiza os dados do proprietario no banco de dados
		Proprietario proprietario = DataProxy.getInstance().execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.find(Proprietario.class, id);
			}
		});
		
		if (proprietario == null) {
			proprietario = new Proprietario();
			proprietario.setId(id);
		}
//		proprietario.setNome("");
		
		if (veiculo.containsKey("proprietario")) {
			JSONObject propJson = (JSONObject) veiculo.getJSONObject("proprietario");
			
			if (propJson.containsKey("nome")) {
				proprietario.setNome(propJson.getString("nome"));
			}
			if (propJson.containsKey("telefone")) {
				proprietario.setTelefone(propJson.getString("telefone"));
			}
		}
				
		return DataProxy.getInstance().execute(new PersistCliente(proprietario));
		
	}

	private Veiculo updateVeiculo(final JSONObject veiculo, final Credential credential, int id, String placa, Proprietario prop) {
		

		VeiculoId veiculoId = new VeiculoId();
		veiculoId.setCredential(credential);
		veiculoId.setId(id);
		
		Veiculo v = DataProxy.getInstance().execute(new ICommand() {

			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.find(Veiculo.class, veiculoId);
			}
			
		});

		
		// persist posicao
		{
			
			if (v == null) {
				v = new Veiculo();
				v.setId(veiculoId);
			}
			v.setPlaca(placa);
			v.setPlaca(veiculo.getString("placa"));
			v.setModelo(veiculo.getString("modelo"));
			v.setProprietario(prop);

			JSONObject posicao = veiculo.getJSONObject("posicao");
			v.setDirection(posicao.getInt("curso"));
			v.setLat((float) posicao.getDouble("lat"));
			v.setLon((float) posicao.getDouble("lon"));
			v.setSpeed((float) posicao.getDouble("velocidade"));
			v.setDataAtualizacao(new Date());
			v.setDataDados(new Date(posicao.getLong("dataDados")));
			v.setIo(posicao.getInt("io"));
			
			// persist veiculo
			return DataProxy.getInstance().execute(new PersistVeiculo(v));
		}
	}
	

}
