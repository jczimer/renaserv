package com.winksys.renaserv.gateway.driver.rodar;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Proprietario.ProprietarioId;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.cmd.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * Processa um JSON com os dados do Rodar, registra cada um dos proprietários no banco de dados.
 * 
 * @author winksys
 *
 */
public class GetClientesProcessor {
	
	private static final Logger LOG = Logger.getLogger(GetClientesProcessor.class);

	private DataProxy dataProxy;
	private Credential cred;
	
	public GetClientesProcessor(DataProxy proxy, Credential cred) {
		this.dataProxy = proxy;
		this.cred = cred;
	}
	
	public void execute(JSONArray data) {
		
		LOG.info("Processing customer from: " + cred.getNome());
		
		for(int i = 0; i < data.size(); i++) {
			
			JSONObject cliJson = data.getJSONObject(i);
			
			LOG.debug("Customer: " + cliJson.getInt("id") + " - " + cliJson.getString("razaoSocial"));
			
			ProprietarioId pid = new ProprietarioId();
			pid.setCredential(cred);
			pid.setId(cliJson.getInt("id"));
			
			Proprietario prop = find(pid);
			
			if (prop == null) {
				prop = new Proprietario();
				prop.setId(pid);
			}
			
			if (cliJson.containsKey("razaoSocial")) {
				prop.setNome(cliJson.getString("razaoSocial"));
			}
			if (cliJson.containsKey("endereco")) {
				prop.setEndereco(cliJson.getString("endereco"));
			}
			if (cliJson.containsKey("complemento")) {
				prop.setComplemento(cliJson.getString("complemento"));
			}
			if (cliJson.containsKey("bairro")) {
				prop.setBairro(cliJson.getString("bairro"));
			}
			if (cliJson.containsKey("email")) {
				prop.setEmail(cliJson.getString("email"));
			}
			StringBuilder telefone = new StringBuilder();
			if (cliJson.containsKey("telefone")) {
				telefone.append(cliJson.getString("telefone"));
				telefone.append(" / ");
			}
			if (cliJson.containsKey("celular")) {
				telefone.append(cliJson.getString("celular"));
			}
			prop.setTelefone(telefone.toString());
			if (cliJson.containsKey("observacao")) {
				prop.setContatos(cliJson.getString("observacao"));
			}
			
			save(prop);
			
		}
		
	}

	private Proprietario save(final Proprietario prop) {
		return dataProxy.execute(new ICommand() {

			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.merge(prop);
			}
			
		});
	}

	private Proprietario find(ProprietarioId pid) {
		
		dataProxy.execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.find(Proprietario.class, pid);
			}
		});
		
		return null;
	}

}
