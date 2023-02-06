package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/login")
public class LoginServlet extends HttpServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7621917274005625788L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().append("Olá mundo");
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
		System.out.println(sb.toString());
		
		final JSONObject loginObj = (JSONObject) JSONSerializer.toJSON(sb.toString());
		
		
		Usuario usuario = CommandExecutor.execute(new ICommand<Usuario>() {

			@Override
			public Usuario execute(EntityManager em) {
				
				Query query = em.createQuery("from Usuario where usuario=:login");
				query.setParameter("login", loginObj.get("login"));
				List<Usuario> usuarios = query.getResultList();
				
				if (usuarios.isEmpty()) {
					return null;
				}
				
				Usuario usuario = usuarios.get(0);
				
				if (!usuario.getSenha().equals(loginObj.get("senha"))) {
					return null;
				}
				
				String token = usuario.getUsuario() + usuario.getSenha() + System.currentTimeMillis();
				MessageDigest m;
				try {
					m = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
				m.update(token.getBytes());
				byte[] digest = m.digest();
				
				token = new BigInteger(1,digest).toString(16);
				
				usuario.setIp(req.getRemoteAddr());
				usuario.setDataLogin(new Date());
				usuario.setLastCheck(new Date());
				usuario.setToken(token);
				
				em.merge(usuario);
				
				return usuario;
				
			}
		});
		
		if (usuario == null) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		JSONObject respJson = new JSONObject();
		respJson.put("usuario", usuario.getUsuario());
		respJson.put("ip", usuario.getIp());
		respJson.put("token", usuario.getToken());
		respJson.put("tipo", usuario.getTipo());
		
		resp.setContentType("application/json");
		resp.getOutputStream().write(respJson.toString().getBytes());
		
	}

}
