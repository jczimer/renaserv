package com.winksys.renaserv.gateway.driver.stc;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Proprietario.ProprietarioId;
import com.winksys.renaserv.gateway.DataProxy;
import com.winksys.renaserv.gateway.cmd.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetClientsProcessor {

	
	private static final Logger LOG = Logger.getLogger(GetClientsProcessor.class);
	private DataProxy dataProxy;
	private JSONArray data;
	private Credential credential;
	
	public GetClientsProcessor(DataProxy dataProxy, JSONArray data, Credential credential) {
		this.dataProxy = dataProxy;
		this.data = data;
		this.credential = credential;
	}
	
	public void execute() {
		
		for(int i = 0; i < data.size(); i++) {
			JSONObject cliJson = data.getJSONObject(i);
	
			LOG.debug(cliJson);
			
			Proprietario prop  = getProprietario(cliJson.getInt("id"));
			prop.setNome(cliJson.getString("name"));
			prop.setEmail(cliJson.getString("email"));
			prop.setEndereco(cliJson.getString("address"));
			prop.setBairro(cliJson.getString("neighbourhood"));
			prop.setComplemento(cliJson.getString("complement"));
			prop.setCep(cliJson.getString("zipcode"));
			prop.setDocumento(cliJson.getString("cpfcnpj"));
			
			
			StringBuilder sb = new StringBuilder();
			JSONArray arrContactsJson = cliJson.getJSONArray("contacts");
			for(int j = 0; j < arrContactsJson.size(); j++) {
				JSONObject contactJson = arrContactsJson.getJSONObject(j);
				sb.append(contactJson.getString("name")).append("\n");
				sb.append("Fone1:").append(contactJson.getString("phone1")).append("\n");
				sb.append("Fone2:").append(contactJson.getString("phone2")).append("\n");
				sb.append("Fone3:").append(contactJson.getString("phone3")).append("\n");
				sb.append("Fone4:").append(contactJson.getString("phone4")).append("\n");
				sb.append("----").append("\n");
			}
			
			prop.setContatos(sb.toString());
			store(prop);
			
			
		}
		
	}

	private void store(Proprietario prop) {
		dataProxy.execute(new ICommand() {

			@Override
			public <E> E execute(EntityManager em) {
				return (E) em.merge(prop);				
			}
			
		});
	}

	private Proprietario getProprietario(int id) {
		
		Proprietario prop = dataProxy.execute(new ICommand() {
			
			@Override
			public <E> E execute(EntityManager em) {
				Query query = em.createQuery("from Proprietario where id.credential=:cred and id.id=:id");
				query.setParameter("cred", credential);
				query.setParameter("id", id);
				List<Proprietario> props = query.getResultList();
				
				Proprietario prop = null;
				if (!props.isEmpty()) {
					prop = props.get(0);
				} else {
					ProprietarioId pid = new ProprietarioId();
					pid.setCredential(credential);
					pid.setId(id);
					
					prop = new Proprietario();
					prop.setId(pid);
				}
				
				return (E) prop;
			}
		});
		
		return prop;
	}
	
	
}
