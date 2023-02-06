package com.winksys.renaserv.web.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/tipoevento")
public class TipoEventoServlet extends HttpServlet {
	
	private Logger LOG = Logger.getLogger(TipoEventoServlet.class);
	
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
		
		final JSONObject eventoJson = (JSONObject) JSONSerializer.toJSON(sb.toString());
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				Evento evento = em.find(Evento.class, eventoJson.getInt("id"));
				
				if (evento == null) {
					evento = new Evento();
				}
				evento.setDescricao(eventoJson.getString("descricao"));
				evento = em.merge(evento);
				
				eventoJson.put("id", evento.getId());
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("evento", eventoJson);
				
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int id = Integer.parseInt(req.getParameter("id"));
		
		try {
			JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {
	
				@Override
				public JSONObject execute(EntityManager em) {
					Evento evento = em.find(Evento.class, id);
					
					try {
						em.remove(evento);
						
						JSONObject ret = new JSONObject();
						ret.put("status", "OK");
						ret.put("id", id);
						
						return ret;
					
					} catch (Throwable e) {
						LOG.error("", e);
						
						JSONObject ret = new JSONObject();
						ret.put("status", "ERROR");
						ret.put("message", e.getMessage());
						return ret;
					}
				}
			});
			
			resp.setContentType("application/json");
			resp.getWriter().append(ret.toString());
			
		} catch (Exception e) {
			LOG.error("", e);
			
			JSONObject ret = new JSONObject();
			ret.put("status", "ERROR");
			ret.put("message", e.getMessage());
			ret.put("id", id);
			
			resp.setContentType("application/json");
			resp.getWriter().append(ret.toString());
		}
		
		
		
	}
	
}
