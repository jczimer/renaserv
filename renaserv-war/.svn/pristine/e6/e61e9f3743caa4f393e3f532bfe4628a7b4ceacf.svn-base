package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Proprietario;
import com.winksys.renaserv.data.Veiculo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class EndpointServlet
 */
@WebServlet("/veiculos/lista")
public class VeiculosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VeiculosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("renaserv");
		EntityManager em = emf.createEntityManager();
		
		response.setContentType("application/json;charset=utf-8");
		
		Query query = em.createQuery("from Veiculo order by dataDados desc");
		List<Veiculo> veiculos = query.getResultList();
		
		JSONArray arr = new JSONArray();
		for (Veiculo veiculo : veiculos) {
			
			JSONObject idJson = new JSONObject();
			idJson.put("credential", veiculo.getId().getCredential().getId());
			idJson.put("id", veiculo.getId());
			
			JSONObject veiculoJson = new JSONObject();
			veiculoJson.put("id", idJson);
			veiculoJson.put("placa", veiculo.getPlaca());
			veiculoJson.put("modelo", veiculo.getModelo());
			veiculoJson.put("lat", veiculo.getLat());
			veiculoJson.put("lon", veiculo.getLon());
			veiculoJson.put("velocidade", veiculo.getSpeed());
			veiculoJson.put("io", veiculo.getIo());
			veiculoJson.put("dataDados", veiculo.getDataDados().getTime());
			
			if (veiculo.getProprietario() != null) {
				Proprietario prop = veiculo.getProprietario();
				JSONObject propJson = new JSONObject();
				propJson.put("id", prop.getId().getId());
				propJson.put("nome", prop.getNome());
				
				veiculoJson.put("proprietario", propJson.toString());
			}
			
			arr.add(veiculoJson);
		}
		
		JSONObject resp = new JSONObject();
		resp.put("veiculos", arr);
		
		response.getWriter().write(resp.toString());
		
		em.close();
		emf.close();
		
	}

}
