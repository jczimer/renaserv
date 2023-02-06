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

import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/listaveiculos")
public class ListaVeiculosServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		
		ServletInputStream sis = request.getInputStream();
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
 		while (true) {
			int readed = sis.read(buf);
			
			if (readed == -1) {
				break;
			}
			
			baos.write(buf,0,readed);
		}
		JSONObject obj = (JSONObject) JSONSerializer.toJSON(new String(baos.toByteArray()));
		
		final String argument = obj.getString("argument");
		final int page = obj.getInt("page");
		final int limit = obj.getInt("limit");
		final int cliente = obj.containsKey("cliente") ? obj.getInt("cliente") : 0;
				
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				
				QueryBuilder builder = new QueryBuilder();
				builder.addFrom("Veiculo v");
				builder.addFrom("left join v.proprietario p");
				builder.addWhere("(v.placa like :argument or p.nome like :argument)");
				builder.addOrder("v.dataDados desc, v.placa");
				builder.setParameter("argument", "%" + argument + "%");
				builder.addProjection("count(*)");
				
				if (cliente != 0) {
					builder.addWhere("v.id.credential.id=:cliente");
					builder.setParameter("cliente", cliente);
				}
				
				Query q = builder.build(em);
				int rows = ((Number) q.getSingleResult()).intValue();
				
				builder.clearProjection();
				builder.addProjection("v");
				q = builder.build(em);
				
				// limite a quantidade
				q.setMaxResults(limit);
				q.setFirstResult((page-1)*50);
				
				List<Veiculo> veiculos = q.getResultList();
				
				JSONArray arr = new JSONArray();
				for (Veiculo veiculo : veiculos) {
					
					
					JSONObject v = new JSONObject();
					v.put("id", veiculo.getId().getId());
					v.put("placa", veiculo.getPlaca());
					v.put("modelo", veiculo.getModelo());
					
					
					JSONObject proprietario = new JSONObject();
					if (veiculo.getProprietario() != null) {
						proprietario.put("id", veiculo.getProprietario().getId().getId());
						proprietario.put("nome", veiculo.getProprietario().getNome());
					}
					v.put("proprietario", proprietario);		
					
					
					JSONObject posicao = new JSONObject();
					posicao.put("io", veiculo.getIo());
					posicao.put("lat", veiculo.getLat());
					posicao.put("lon", veiculo.getLon());
					posicao.put("dataDados", veiculo.getDataDados() != null ? veiculo.getDataDados().getTime() : 0);
					
					v.put("posicao", posicao);
					
					JSONObject cred = new JSONObject();
					cred.put("id", veiculo.getId().getCredential().getId());
					cred.put("nome", veiculo.getId().getCredential().getNome());
					cred.put("_system", veiculo.getId().getCredential().getSystem());
					v.put("credential", cred);
					
					arr.add(v);
					
				}
				
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("qtd", rows);
				ret.put("limit", limit);
				ret.put("veiculos", arr);
								
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
		
	}
	
}
