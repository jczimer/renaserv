package com.winksys.renaserv.web.servlet;

import java.io.ByteArrayOutputStream;
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

import com.winksys.renaserv.data.Usuario;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/listausuarios")
public class ListaUsuariosServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5123954963721911881L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ServletInputStream sis = req.getInputStream();
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			int readed = sis.read(buf);
			
			if (readed == -1) {
				break;
			}
			baos.write(buf,0,readed);
		}
		String json = new String(baos.toByteArray());
		final JSONObject jsonReq = (JSONObject) JSONSerializer.toJSON(json);
		
		JSONObject respJson = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				QueryBuilder builder = QueryBuilder.init()
						.addFrom("Usuario u")
						.addWhere("usuario like :argument")
						.setParameter("argument", "%" + jsonReq.getString("argument") + "%")
						;
				
				Query q = builder.build(em);
				List<Usuario> resultList = q.getResultList();
				
				JSONArray arr = new JSONArray();
				for(Usuario usuario : resultList) {
					JSONObject usuarioJson = new JSONObject();
					usuarioJson.put("id", usuario.getId());
					usuarioJson.put("usuario", usuario.getUsuario());
					usuarioJson.put("senha", usuario.getSenha());
					usuarioJson.put("ip", usuario.getIp());
					usuarioJson.put("tipo", usuario.getTipo());
					
					if (usuario.getDataLogin() != null) {
						usuarioJson.put("dataLogin", usuario.getDataLogin().getTime());
					}
					if (usuario.getLastCheck() != null) {
						usuarioJson.put("lastCheck", usuario.getLastCheck().getTime());
					}
					
					arr.add(usuarioJson);
				}
				
				JSONObject resp = new JSONObject();
				resp.put("status", "OK");
				resp.put("usuarios", arr);
										
				return resp;
			}
		});
		resp.setContentType("application/json");
		resp.getWriter().append(respJson.toString());
		
	}
	
}
