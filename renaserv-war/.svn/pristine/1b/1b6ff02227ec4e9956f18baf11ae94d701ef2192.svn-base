package com.winksys.renaserv.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

import com.windi.ui.tools.data.DataUtilsWindi;
import com.winksys.renaserv.data.Ocorrencia;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Servlet implementation class EventosServlet
 */
@WebServlet("/api/listaeventos")
public class ListaEventosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaEventosServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
 		
		
		
		final long pDataInicio = obj.getLong("dataInicio");
		final long pDataFinal = obj.getLong("dataFinal");
		final String pCliente = obj.getString("cliente");
		final int pStatus = obj.getInt("status");
		final int pPagina = obj.containsKey("pagina") ? obj.getInt("pagina") : 1;
		final String placa = obj.containsKey("placa") ? obj.getString("placa") : null;
		final boolean exportar = obj.containsKey("exportar") ? obj.getBoolean("exportar") : false;
				
		
		JSONObject resp = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				
				if (pDataInicio == 0) {
					return error("Informe data de início");
				}
				if (pDataFinal == 0) {
					return error("Informe data final");
				}
								
				Date dataInicio = DataUtilsWindi.removeHoras(new Date(pDataInicio));
				Date dataFinal = DataUtilsWindi.ultimaHora(new Date(pDataFinal));
				int pagina = pPagina;
				
				if (pStatus < 2) {
					return getOcorrencias(pCliente, pStatus, placa, exportar, em, dataInicio, dataFinal, pagina);
				}
				return getHistorico(pCliente, pStatus, placa, exportar, em, dataInicio, dataFinal, pagina);
			}

			/**
			 * Retorna as ocorrencias sem agrupamento
			 * 
			 * @param pCliente
			 * @param pStatus
			 * @param placa
			 * @param exportar
			 * @param em
			 * @param dataInicio
			 * @param dataFinal
			 * @param pagina
			 * @return
			 */
			private JSONObject getHistorico(String pCliente, int pStatus, String placa, boolean exportar,
					EntityManager em, Date dataInicio, Date dataFinal, int pagina) {
				
					JSONArray resumo = loadQtds(dataInicio, dataFinal, em);
				
				
					QueryBuilder builder = QueryBuilder.init()
						.addFrom("Ocorrencia o")
						.addWhere("o.id.dataDados between :dtini and :dtfim")
						.addOrder("o.id.dataDados desc")					
						.setParameter("dtini", dataInicio)
						.setParameter("dtfim", dataFinal)
					;
					
					builder.addProjection("count(*)");
					
					if (pStatus == -1) {
						builder.addWhere("o.status in (0,1)");
					} else {
						builder.addWhere("o.status in :status");
						builder.setParameter("status", pStatus);
					}
					if (placa != null) {
						builder.addWhere("o.id.veiculo.placa like :placa");
						builder.setParameter("placa", "%" + placa + "%");
					}
					
					if (pCliente != null) {
						builder.addWhere("o.id.veiculo.id.credential.nome like :nome");
						builder.setParameter("nome", "%" + pCliente + "%");
					}
					Query query = builder.build(em);
					Number rowCount = (Number) query.getResultList().get(0);
					
					builder.clearProjection();
										
					query = builder.build(em);
					
					if (!exportar) {
						query.setFirstResult((pagina-1)*50);
						query.setMaxResults(50);
					}
					
					
					
					List<Ocorrencia> items = query.getResultList();
					
					JSONArray arr = new JSONArray();
					
					for (Ocorrencia ocorrencia : items) {
						
						JSONObject ocorrenciaIdJson = new JSONObject();
						ocorrenciaIdJson.put("id", ocorrencia.getId().getVeiculo().getId().getCredential().getId());
						ocorrenciaIdJson.put("dataDados", ocorrencia.getId().getDataDados().getTime());
						ocorrenciaIdJson.put("evento", ocorrencia.getId().getEvento().getId());
						ocorrenciaIdJson.put("veiculo", ocorrencia.getId().getVeiculo().getId().getId());
						
						JSONObject veiculoJson = new JSONObject();
						veiculoJson.put("id", ocorrencia.getId().getVeiculo().getId().getId());
						veiculoJson.put("placa", ocorrencia.getId().getVeiculo().getPlaca());
						veiculoJson.put("modelo", ocorrencia.getId().getVeiculo().getModelo());
											
						JSONObject clienteJson = new JSONObject();
						clienteJson.put("id", ocorrencia.getId().getVeiculo().getId().getCredential().getId());
						clienteJson.put("nome", ocorrencia.getId().getVeiculo().getId().getCredential().getNome());
						
						JSONObject tratativaJson = new JSONObject();
						tratativaJson.put("usuario", ocorrencia.getUsuarioTratativa());
						tratativaJson.put("data", ocorrencia.getDataTratativa() == null ? null : ocorrencia.getDataTratativa().getTime());
						tratativaJson.put("tratativa", ocorrencia.getTratativa());
						
						JSONObject ocorrenciaJson = new JSONObject();		
						ocorrenciaJson.put("id", ocorrenciaIdJson);
						ocorrenciaJson.put("veiculo", veiculoJson);
						ocorrenciaJson.put("cliente", clienteJson);
						ocorrenciaJson.put("evento", ocorrencia.getId().getEvento().getDescricao());
						ocorrenciaJson.put("dataCad", ocorrencia.getDataCad().getTime());
						ocorrenciaJson.put("status", ocorrencia.getStatus());
						ocorrenciaJson.put("tratativa", tratativaJson);
												
						if (ocorrencia.getDataPrimeiraTratativa() != null) {
							ocorrenciaJson.put("dataPrimeiraTratativa", ocorrencia.getDataPrimeiraTratativa().getTime());
						}
						
						arr.add(ocorrenciaJson);					
						
					}
					
					
					JSONObject resp = new JSONObject();
					
					if (!exportar) {
						resp.put("ocorrencias", arr);
					} else {
						GenerateListaEventoExcel action = new GenerateListaEventoExcel(arr);
						File file = action.generate();
						resp.put("report", file.getName());
					}
					resp.put("status", "OK");
					resp.put("qtd", rowCount.intValue());
					resp.put("page", pagina);
					resp.put("resumo", resumo);
					
					return resp;
			}

			private JSONArray loadQtds(Date dataInicio, Date dataFinal, EntityManager em) {
				
				QueryBuilder builder = QueryBuilder.init()
						.addFrom("Ocorrencia o")
						.addWhere("o.id.dataDados between :dtini and :dtfim")
						.addOrder("o.id.dataDados desc")					
						.setParameter("dtini", dataInicio)
						.setParameter("dtfim", dataFinal);
				builder.addProjection("o.status,count(*)");
				builder.addGroup("o.status");
				builder.addWhere("o.status in (0,1)");
				
				if (pCliente != null) {
					builder.addWhere("o.id.veiculo.id.credential.nome like :nome");
					builder.setParameter("nome", "%" + pCliente + "%");
				}
				
				Query query = builder.build(em);
				List<Object[]> resultList = query.getResultList();
				
				JSONArray arr = new JSONArray();
				for(Object[] o : resultList) {
					Number status = ((Number) o[0]).intValue();
					Number qtd = ((Number) o[1]).intValue();
					
					JSONObject total = new JSONObject();
					total.put("status", status);
					total.put("qtd", qtd);
					
					arr.add(total);
				}
				
				return arr;
				
			}

			/**
			 * Retorna as ocorrencias com agrupamento por veículo e evento
			 * @param pCliente
			 * @param pStatus
			 * @param placa
			 * @param exportar
			 * @param em
			 * @param dataInicio
			 * @param dataFinal
			 * @param pagina
			 * @return
			 */
			private JSONObject getOcorrencias(final String pCliente, final int pStatus, final String placa,
					final boolean exportar, EntityManager em, Date dataInicio, Date dataFinal, int pagina) {
				
				JSONArray resumo = loadQtds(dataInicio, dataFinal, em);
				
				QueryBuilder builder = QueryBuilder.init()
					.addFrom("Ocorrencia o")
					.addWhere("o.id.dataDados between :dtini and :dtfim")
					.addOrder("o.id.dataDados desc")					
					.setParameter("dtini", dataInicio)
					.setParameter("dtfim", dataFinal)				
				;
							
				
				builder.addProjection("count(*)");
				
				if (pStatus == -1) {
					builder.addWhere("o.status in (0,1)");
				} else {
					builder.addWhere("o.status in :status");
					builder.setParameter("status", pStatus);
				}
				if (placa != null) {
					builder.addWhere("o.id.veiculo.placa like :placa");
					builder.setParameter("placa", "%" + placa + "%");
				}
				
				if (pCliente != null) {
					builder.addWhere("o.id.veiculo.id.credential.nome like :nome");
					builder.setParameter("nome", "%" + pCliente + "%");
				}
				Query query = builder.build(em);
				Number rowCount = (Number) query.getResultList().size();
				
				builder.clearProjection();
				
				builder.addGroup("o.id.veiculo,o.id.evento");
				builder.addProjection("o");
				builder.addProjection("count(*)");
				
				query = builder.build(em);
				
				if (!exportar) {
					query.setFirstResult((pagina-1)*50);
					query.setMaxResults(50);
				}
				
				
				
				List<Object[]> items = query.getResultList();
				
				JSONArray arr = new JSONArray();
				
				for (Object[] item : items) {
					Ocorrencia ocorrencia = (Ocorrencia) item[0];
					Long count = (Long) item[1];
					
					JSONObject ocorrenciaIdJson = new JSONObject();
					ocorrenciaIdJson.put("id", ocorrencia.getId().getVeiculo().getId().getCredential().getId());
					ocorrenciaIdJson.put("dataDados", ocorrencia.getId().getDataDados().getTime());
					ocorrenciaIdJson.put("evento", ocorrencia.getId().getEvento().getId());
					ocorrenciaIdJson.put("veiculo", ocorrencia.getId().getVeiculo().getId().getId());
					
					JSONObject veiculoJson = new JSONObject();
					veiculoJson.put("id", ocorrencia.getId().getVeiculo().getId().getId());
					veiculoJson.put("placa", ocorrencia.getId().getVeiculo().getPlaca());
					veiculoJson.put("modelo", ocorrencia.getId().getVeiculo().getModelo());
										
					JSONObject clienteJson = new JSONObject();
					clienteJson.put("id", ocorrencia.getId().getVeiculo().getId().getCredential().getId());
					clienteJson.put("nome", ocorrencia.getId().getVeiculo().getId().getCredential().getNome());
					
					JSONObject tratativaJson = new JSONObject();
					tratativaJson.put("usuario", ocorrencia.getUsuarioTratativa());
					tratativaJson.put("data", ocorrencia.getDataTratativa() == null ? null : ocorrencia.getDataTratativa().getTime());
					tratativaJson.put("tratativa", ocorrencia.getTratativa());
					
					JSONObject ocorrenciaJson = new JSONObject();		
					ocorrenciaJson.put("id", ocorrenciaIdJson);
					ocorrenciaJson.put("veiculo", veiculoJson);
					ocorrenciaJson.put("cliente", clienteJson);
					ocorrenciaJson.put("evento", ocorrencia.getId().getEvento().getDescricao());
					ocorrenciaJson.put("dataCad", ocorrencia.getDataCad().getTime());
					ocorrenciaJson.put("status", ocorrencia.getStatus());
					ocorrenciaJson.put("tratativa", tratativaJson);
					ocorrenciaJson.put("quantidade", count);
					
					if (ocorrencia.getDataPrimeiraTratativa() != null) {
						ocorrenciaJson.put("dataPrimeiraTratativa", ocorrencia.getDataPrimeiraTratativa().getTime());
					}
					
					arr.add(ocorrenciaJson);					
					
				}
				
				
				JSONObject resp = new JSONObject();
				
				if (!exportar) {
					resp.put("ocorrencias", arr);
				} else {
					GenerateListaEventoExcel action = new GenerateListaEventoExcel(arr);
					File file = action.generate();
					resp.put("report", file.getName());
				}
				resp.put("status", "OK");
				resp.put("qtd", rowCount.intValue());
				resp.put("page", pagina);
				resp.put("resumo", resumo);
				
				return resp;
			}

			private JSONObject error(String errorMessage) {
				JSONObject ret = new JSONObject();
				ret.put("status", "ERROR");
				ret.put("mensagem", errorMessage);
				return ret;
			}
			
			
		});
		
		response.setContentType("application/json");
		response.getWriter().write(resp.toString());
		
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

}
