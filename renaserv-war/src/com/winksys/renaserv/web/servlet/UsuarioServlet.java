package com.winksys.renaserv.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/usuario")
public class UsuarioServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pidUsuario = req.getParameter("id");
		
		JSONObject respJson = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				int idUsuario;
				try {
					idUsuario = Integer.parseInt(pidUsuario);
					
				} catch (Exception e) {
					return error("Formato do identificador do usuário não é válido");
				}
				
				Usuario usuario = em.find(Usuario.class, idUsuario);
				JSONObject usuarioJson = new JSONObject();
				usuarioJson.put("id", usuario.getId());
				usuarioJson.put("usuario", usuario.getUsuario());
				usuarioJson.put("senha", usuario.getSenha());
				usuarioJson.put("token", usuario.getToken());
				usuarioJson.put("ip", usuario.getIp());
				usuarioJson.put("tipo", usuario.getTipo());
				if (usuario.getDataLogin() != null) {
					usuarioJson.put("dataLogin", usuario.getDataLogin().getTime());
				}
				if (usuario.getLastCheck() != null) {
					usuarioJson.put("lastCheck", usuario.getLastCheck().getTime());
				}
				
				JSONObject resp = new JSONObject();
				resp.put("status", "OK");
				resp.put("usuario", usuarioJson);
				
				return resp;
			}

			
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(respJson.toString());
		
		
	}
	
	private JSONObject error(String message) {
		JSONObject json = new JSONObject();
		json.put("status", "ERROR");
		json.put("message", message);
		return json;
	}
	

	private boolean checkAdmin(EntityManager em, int idUsuario) {
		Query query;
		Number qtdUsuarios;
		query = em.createQuery("select count(*) from Usuario where tipo = 0 and id <> :id");
		query.setParameter("id", idUsuario);
		qtdUsuarios = (Number) query.getSingleResult();
		return (qtdUsuarios.intValue() > 0);
	}
	
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
		final JSONObject usuarioJson = (JSONObject) JSONSerializer.toJSON(json);
		
		JSONObject respJson = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				int idUsuario = usuarioJson.getInt("id");
				
				// verifica se tem outro usuário com o mesmo nome
				Query query = em.createQuery("select count(*) from Usuario where usuario like :usuario and id <> :id");
				query.setParameter("usuario", usuarioJson.getString("usuario"));
				query.setParameter("id", idUsuario);
				Number qtdUsuarios = (Number) query.getSingleResult();
				if (qtdUsuarios.intValue() > 0) {
					return error("Já existe outro usuário com este nome!");
				}
				
				// verifica se tem pelo menos um admin
				if (usuarioJson.getInt("tipo") == 1 && !checkAdmin(em, idUsuario)) {
					return error("Deve existir pelo menos um usuário admin!");
				}
				Usuario usuario = em.find(Usuario.class, idUsuario);
								
				if (usuario == null) {
					usuario = new Usuario();
					usuario.setId(usuarioJson.getInt("id"));
				}
				usuario.setUsuario(usuarioJson.getString("usuario"));
				usuario.setSenha(usuarioJson.getString("senha"));
				usuario.setTipo(usuarioJson.getInt("tipo"));
				usuario = em.merge(usuario);
				
				
					
				JSONObject resp = new JSONObject();
				resp.put("status", "OK");
				resp.put("usuario", usuarioJson);
				
				return resp;
			}

			
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(respJson.toString());
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pidUsuario = req.getParameter("id");
		
		JSONObject respJson = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				int idUsuario;
				try {
					idUsuario = Integer.parseInt(pidUsuario);
					
				} catch (Exception e) {
					return error("Formato do identificador do usuário não é válido");
				}
				
				// verifica se tem pelo menos um admin
				if (!checkAdmin(em, idUsuario)) {
					return error("Deve existir pelo menos um usuário admin!");
				}
				
				Usuario usuario = em.find(Usuario.class, idUsuario);
				em.remove(usuario);
				
				JSONObject resp = new JSONObject();
				resp.put("status", "OK");
				resp.put("message", "Usuário removido com sucesso!");
				
				return resp;
			}

			private JSONObject error(String message) {
				JSONObject json = new JSONObject();
				json.put("status", "ERROR");
				json.put("message", message);
				return json;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(respJson.toString());
	}
	
	
	
}
