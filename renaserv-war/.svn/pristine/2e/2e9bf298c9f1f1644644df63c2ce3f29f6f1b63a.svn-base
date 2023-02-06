package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/api/listatipoeventos")
public class ListaTipoEvento extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8513045809268149846L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				
				Query query = em.createQuery("from Evento order by descricao");
				List<Evento> list = query.getResultList();
				
				JSONArray arrJson = new JSONArray();
				for(Evento e : list) {
					JSONObject ejson = new JSONObject();
					ejson.put("id", e.getId());
					ejson.put("descricao", e.getDescricao());
					arrJson.add(e);
				}
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("eventos", arrJson);
				
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}
	
	
	
}
