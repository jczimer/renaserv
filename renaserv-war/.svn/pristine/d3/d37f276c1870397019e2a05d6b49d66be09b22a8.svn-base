package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EnStatus;
import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.data.EventoMonitorado;
import com.winksys.renaserv.data.EventoMonitorado.EventoMonitoradoId;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/cliente")
public class ClienteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5922689690815464608L;

	private JSONObject getCliente(Credential cliente) {
		JSONObject clienteJson = new JSONObject();
		clienteJson.put("id", cliente.getId());
		clienteJson.put("nome", cliente.getNome());
		clienteJson.put("_url", cliente.getUrl());
		clienteJson.put("_system", cliente.getSystem());
		clienteJson.put("_password", cliente.getPassword());
		clienteJson.put("_user", cliente.getUser());
		clienteJson.put("_key", cliente.getKey());
		clienteJson.put("email", cliente.getEmail());
		clienteJson.put("status", cliente.getStatus().ordinal());
		return clienteJson;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pcliente = req.getParameter("id");
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				Credential cliente = em.find(Credential.class, Integer.parseInt(pcliente));
				JSONObject clienteJson = getCliente(cliente);
				
				Query query = em.createQuery("from EventoMonitorado where id.credential.id=:id");
				query.setParameter("id", cliente.getId());
				List<EventoMonitorado> eventos =  query.getResultList();
				
				JSONArray arrevtJson = new JSONArray();
				for(EventoMonitorado e : eventos) {
					JSONObject emjson = new JSONObject();
					emjson.put("id", e.getId().getEvento().getId());
					emjson.put("descricao", e.getId().getEvento().getDescricao());
					arrevtJson.add(emjson);
				}
				clienteJson.put("eventos", arrevtJson);			
				clienteJson.put("image", cliente.getImage());
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("credential", clienteJson);
				
				
				
				
			
				return ret;
			}

		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ServletInputStream sis = req.getInputStream();
		byte[] buf = new byte[1024];
		
		StringBuilder sb = new StringBuilder();
		while (true) {
			int readed = sis.read(buf);
			if (readed == -1) {
				break;
			}
			sb.append(new String(buf,0,readed));
		}
		
		final JSONObject clienteJson = (JSONObject) JSONSerializer.toJSON(sb.toString());
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				Credential cliente;
				if (clienteJson.containsKey("id")) {
					cliente = em.find(Credential.class, clienteJson.getInt("id"));
				} else {
					cliente = new Credential();
				}
				
				cliente.setNome(clienteJson.getString("nome"));
				if (clienteJson.containsKey("_url")) {
					cliente.setUrl(clienteJson.getString("_url"));
				}
				cliente.setSystem(clienteJson.getInt("_system"));
				cliente.setEmail(clienteJson.getString("email"));
				cliente.setStatus(EnStatus.values()[clienteJson.getInt("status")]);
				
				if (clienteJson.containsKey("image")) {
					cliente.setImage(clienteJson.getString("image"));
				}
				if (clienteJson.containsKey("_password")) {
					cliente.setPassword(clienteJson.getString("_password"));
				}
				if (clienteJson.containsKey("_user")) {
					cliente.setUser(clienteJson.getString("_user"));
				}				
				if (clienteJson.containsKey("_key")) {
					cliente.setKey(clienteJson.getString("_key"));
				}
				cliente = em.merge(cliente);
				
				// verifica eventos
				List<EventoMonitorado> eventosMonitorados = getEventosMonitorados(em, cliente.getId());
				for (EventoMonitorado evento : eventosMonitorados) {
					em.remove(evento);
				}
				
				JSONArray eventosJson = clienteJson.getJSONArray("eventos");
				for(int i = 0; i < eventosJson.size(); i++) {
					JSONObject eventoJson = eventosJson.getJSONObject(i);
					
					Evento evento = new Evento();
					evento.setId(eventoJson.getInt("id"));
					
					EventoMonitoradoId id = new EventoMonitoradoId();
					id.setCredential(cliente);
					id.setEvento(evento);
					
					EventoMonitorado emd = new EventoMonitorado();
					emd.setId(id);
					em.merge(emd);
					
				}
				
				clienteJson.put("id", cliente.getId());
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("credential", clienteJson);
				
				
				
			
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
	}

	private List<EventoMonitorado> getEventosMonitorados(EntityManager em, int id) {
		QueryBuilder builder = QueryBuilder.init();
		builder.addFrom("EventoMonitorado");
		builder.addWhere("id.credential.id=:id");
		builder.setParameter("id", id);
		Query query = builder.build(em);
		return query.getResultList();		
	}
	
}
