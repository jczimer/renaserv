package com.winksys.renaserv.web.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONObject;

@WebServlet("/api/veiculo")
public class VeiculoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final int id = Integer.parseInt(req.getParameter("id"));
		final int credential = Integer.parseInt(req.getParameter("credential"));
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				// 
				
				Query query = em.createQuery("from Veiculo where id.credential.id=:credential and id.id=:id");
				query.setParameter("credential", credential);
				query.setParameter("id", id);
				Veiculo veiculo = (Veiculo) query.getSingleResult();
								
				JSONObject veiculoJson = new JSONObject();
				veiculoJson.put("id", veiculo.getId().getId());
				veiculoJson.put("placa", veiculo.getPlaca());
				veiculoJson.put("modelo", veiculo.getModelo());
				veiculoJson.put("ano", veiculo.getAno());
				veiculoJson.put("cor", veiculo.getCor());
				veiculoJson.put("chassi", veiculo.getChassi());
				veiculoJson.put("renavan", veiculo.getRenavam());
				
				
				Proprietario proprietario = veiculo.getProprietario();
				
				if (proprietario != null) {
					JSONObject propJson = new JSONObject();
					propJson.put("id", proprietario.getId().getId());
					propJson.put("nome", proprietario.getNome());
					propJson.put("contatos", proprietario.getContatos());
					propJson.put("telefone", proprietario.getTelefone());
					veiculoJson.put("proprietario", propJson);
				}
				
				Credential cred = veiculo.getId().getCredential();
				JSONObject credJson = new JSONObject();
				credJson.put("id", cred.getId());
				credJson.put("nome", cred.getNome());
				credJson.put("_system", cred.getSystem());
				veiculoJson.put("credential", credJson);
				
				JSONObject posJson = new JSONObject();
				posJson.put("lat", veiculo.getLat());
				posJson.put("lon", veiculo.getLon());
				
				if (veiculo.getDataDados() != null) {
					posJson.put("data", veiculo.getDataDados().getTime());
				}
				posJson.put("io", veiculo.getIo());
				posJson.put("direction", veiculo.getDirection());
				veiculoJson.put("posicao", posJson);
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("veiculo", veiculoJson);
				
				
				return ret;
			}
		});
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}
	
}
