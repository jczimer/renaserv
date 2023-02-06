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

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EnStatus;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/api/listaclientes")
public class ListaClientesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8945370368446011006L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		final String argument = req.getParameter("argument") == null ? "" : req.getParameter("argument");
		final String status = req.getParameter("status") == null ? "" : req.getParameter("status");
		
		List<Credential> credentials = CommandExecutor.execute(new ICommand<List<Credential>>() {

			@Override
			public List<Credential> execute(EntityManager em) {
				QueryBuilder builder = QueryBuilder.init()
						.addFrom("Credential")
						.addOrder("nome desc");
				
				if (argument != null) {
					builder.addWhere("nome like :nome");
					builder.setParameter("nome", "%"+ argument +"%");
				}
				if (status != null && !status.isEmpty()) {
					builder.addWhere("status=:status");
					builder.setParameter("status", EnStatus.values()[Integer.parseInt(status)]);
				}
							
				
				Query query = builder.build(em);
				List<Credential> credentials = query.getResultList();
				return credentials;
			}
		});
		
		JSONArray ar = new JSONArray();
		for(Credential c : credentials) {
			JSONObject o = new JSONObject();
			o.put("id", c.getId());
			o.put("nome", c.getNome());
			o.put("_system", c.getSystem());
			o.put("_user", c.getUser());
			o.put("_key", c.getKey());
			o.put("status", c.getStatus().ordinal());
			
			ar.add(o);
		}
		
		resp.setContentType("application/json");
		resp.getWriter().append(ar.toString());
	}

}
