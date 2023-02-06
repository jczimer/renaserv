package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.windi.ui.tools.data.DataUtilsWindi;
import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.LogOcorrencia;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/api/listalogs")
public class ListaLogsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2974109856605722412L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		final String credential = req.getParameter("credential");
		final String veiculo = req.getParameter("veiculo");
		final String evento = req.getParameter("evento");
		final String dataDados = req.getParameter("dataDados");
		final String dataLogIni = req.getParameter("dataLogIni");
		final String dataLogFim = req.getParameter("dataLogFim");
		final String usuario = req.getParameter("usuario");
		final String pagina = req.getParameter("pagina");
		final String limite = req.getParameter("limite");
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				
				QueryBuilder builder = QueryBuilder.init()
						.addFrom("LogOcorrencia l")
						.addOrder("l.ocorrencia.id.dataDados desc")
				;
				
				if (credential != null) {
					builder.addWhere("l.ocorrencia.id.veiculo.id.credential.id=:credential");
					builder.setParameter("credential", Integer.parseInt(credential));
				}
				if (evento != null) {
					builder.addWhere("l.ocorrencia.id.evento.id=:evento");
					builder.setParameter("evento", Integer.parseInt(evento));
				}
				if (veiculo != null) {
					Credential cred = new Credential();
					cred.setId(Integer.parseInt(credential));
					
					VeiculoId vid = new VeiculoId();
					vid.setCredential(cred);
					vid.setId(Integer.parseInt(veiculo));
					
					builder.addWhere("l.ocorrencia.id.veiculo.id=:veiculo");
					builder.setParameter("veiculo", vid);
				}
				if (evento != null) {
					builder.addWhere("l.ocorrencia.id.dataDados=:dataDados");
					builder.setParameter("dataDados", new Date(Long.parseLong(dataDados)));
				}
				if (dataLogIni != null) {
					builder.addWhere("l.data>=:dataLog");
					builder.setParameter("dataDados", DataUtilsWindi.removeHoras(new Date(Long.parseLong(dataLogIni))));
				}
				if (dataLogFim != null) {
					builder.addWhere("l.data<=:dataLog");
					builder.setParameter("dataDados", DataUtilsWindi.removeHoras(new Date(Long.parseLong(dataLogFim))));
				}
				if (usuario != null) {
					builder.addWhere("l.usuario=:usuario");
					builder.setParameter("usuario", usuario);
				}
				
				Query query = builder.build(em);
				if (pagina != null && limite != null) {
					int l = Integer.parseInt(limite);
					int p = Integer.parseInt(pagina);
					
					if (p > 0) {					
						query.setFirstResult((p-1) * l);
						query.setMaxResults(l);
					}
				}
				
				JSONArray arr = new JSONArray();
				List<LogOcorrencia> logs = query.getResultList();
				for(LogOcorrencia l : logs) {
					JSONObject lojson = new JSONObject();
					lojson.put("credential", l.getOcorrencia().getId().getVeiculo().getId());
					lojson.put("veiculo", l.getOcorrencia().getId().getVeiculo().getId());
					lojson.put("evento", l.getOcorrencia().getId().getEvento().getId());
					lojson.put("dataDados", l.getOcorrencia().getId().getDataDados());
															
					JSONObject ljson = new JSONObject();
					ljson.put("id", l.getId());
					ljson.put("data", l.getData().getTime());
					ljson.put("usuario", l.getUsuario());
					ljson.put("status", l.getStatus());
					ljson.put("tratativa", l.getTratativa());
					ljson.put("ocorrencia", lojson);
					
					arr.add(ljson);
				}
				JSONObject ret = new JSONObject();
				ret.put("status", "OK");
				ret.put("logs", arr);
				
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}
	
}
