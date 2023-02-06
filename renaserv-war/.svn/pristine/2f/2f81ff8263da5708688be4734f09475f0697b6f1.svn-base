package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.data.Ocorrencia;
import com.winksys.renaserv.data.Ocorrencia.OcorrenciaId;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONObject;

@WebServlet("/api/getocorrencia")
public class GetOcorrencia extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7855143212565634558L;
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse resp) throws ServletException, IOException {
		
		JSONObject retornoJson = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				
				if (request.getParameter("cliente") == null) {
					return error("Cliente não informado");
				}
				if (request.getParameter("veiculo") == null) {
					return error("Veículo não informado");
				}
				if (request.getParameter("evento") == null) {
					return error("Evento não informado");
				}
				if (request.getParameter("dataDados") == null) {
					return error("Data não informada");
				}
				
				int cliente, codVeiculo, codEvento;
				long codData;
				try {
					cliente = Integer.parseInt(request.getParameter("cliente"));
					codVeiculo = Integer.parseInt(request.getParameter("veiculo"));
					codEvento = Integer.parseInt(request.getParameter("evento"));
					codData = Long.parseLong(request.getParameter("dataDados"));
				} catch (Exception e) {
					e.printStackTrace();
					return error(e.getMessage());
				}
				Credential credential = em.find(Credential.class, cliente);
				Evento evento = em.find(Evento.class, codEvento);
				
				VeiculoId vid = new VeiculoId();
				vid.setCredential(credential);
				vid.setId(codVeiculo);
								
				Veiculo veiculo = em.find(Veiculo.class, vid);
				Date dataDados = new Date(codData);				
				
				OcorrenciaId id = new OcorrenciaId();
				id.setVeiculo(veiculo);
				id.setEvento(evento);
				id.setDataDados(dataDados);
				
				Ocorrencia ocorrencia = em.find(Ocorrencia.class, id);
				
				if (ocorrencia == null) {
					return error("Ocorrência não encontrada");
				}
				
				JSONObject ocorrenciaIdJson = new JSONObject();
				ocorrenciaIdJson.put("credential", cliente);
				ocorrenciaIdJson.put("veiculo", codVeiculo);
				ocorrenciaIdJson.put("evento", codEvento);
				ocorrenciaIdJson.put("dataDados", codData);
				
				JSONObject veiculoJson = new JSONObject();
				veiculoJson.put("id", ocorrencia.getId().getVeiculo().getId());
				veiculoJson.put("placa", ocorrencia.getId().getVeiculo().getPlaca());
				veiculoJson.put("modelo", ocorrencia.getId().getVeiculo().getModelo());
				veiculoJson.put("ano", ocorrencia.getId().getVeiculo().getAno());
				veiculoJson.put("cor", ocorrencia.getId().getVeiculo().getCor());
				veiculoJson.put("chassi", ocorrencia.getId().getVeiculo().getChassi());
				veiculoJson.put("renavam", ocorrencia.getId().getVeiculo().getRenavam());
				
				
				JSONObject propJson = new JSONObject();
				
				if (ocorrencia.getId().getVeiculo().getProprietario() != null) {
					propJson.put("id", ocorrencia.getId().getVeiculo().getProprietario().getId().getId());
					propJson.put("nome", ocorrencia.getId().getVeiculo().getProprietario().getNome());
					propJson.put("telefone", ocorrencia.getId().getVeiculo().getProprietario().getTelefone());
					propJson.put("contatos", ocorrencia.getId().getVeiculo().getProprietario().getContatos());
				}
				JSONObject eventoJson = new JSONObject();
				eventoJson.put("id", ocorrencia.getId().getEvento().getId());
				eventoJson.put("descricao", ocorrencia.getId().getEvento().getDescricao());
				
				Credential oCred = ocorrencia.getId().getVeiculo().getId().getCredential();
				String plataforma = "";
				switch (oCred.getSystem()) {
				case 1:
					plataforma = "Rodar";
					break;
				case 2:
					plataforma = "STC";
					break;
				case 3:
					plataforma = "Multiportal";
					break;					
				}
				
				JSONObject clienteJson = new JSONObject();
				clienteJson.put("id", oCred.getId());
				clienteJson.put("nome", oCred.getNome());
				clienteJson.put("usuario", oCred.getUser());
				clienteJson.put("senha", oCred.getPassword());
				clienteJson.put("plataforma", plataforma);
				
				JSONObject tratativaJson = new JSONObject();
				tratativaJson.put("dataTratativa", ocorrencia.getDataTratativa() == null ? null : ocorrencia.getDataTratativa().getTime());
				tratativaJson.put("usuarioTratativa", ocorrencia.getUsuarioTratativa());
				
				JSONObject posicaoJson = new JSONObject();
				
				posicaoJson.put("data", ocorrencia.getId().getVeiculo().getDataDados() == null ? 0 : ocorrencia.getId().getVeiculo().getDataDados().getTime());
				posicaoJson.put("lat", ocorrencia.getId().getVeiculo().getLat());
				posicaoJson.put("lon", ocorrencia.getId().getVeiculo().getLon());
				posicaoJson.put("direction", ocorrencia.getId().getVeiculo().getDirection());
				posicaoJson.put("io", ocorrencia.getId().getVeiculo().getIo());
				posicaoJson.put("speed", ocorrencia.getId().getVeiculo().getSpeed());
				posicaoJson.put("tensaoBateria", ocorrencia.getId().getVeiculo().getTensaoBateria());
				
				
				JSONObject ocorrenciaJson = new JSONObject();
				ocorrenciaJson.put("id", ocorrenciaIdJson);
				ocorrenciaJson.put("veiculo", veiculoJson);
				ocorrenciaJson.put("evento", eventoJson);
				ocorrenciaJson.put("cliente", clienteJson);
				ocorrenciaJson.put("proprietario", propJson);
				ocorrenciaJson.put("posicaoAtual", posicaoJson);
				ocorrenciaJson.put("dataCad", ocorrencia.getDataCad().getTime());
				ocorrenciaJson.put("lat", ocorrencia.getLat());
				ocorrenciaJson.put("lon", ocorrencia.getLon());	
				ocorrenciaJson.put("direction", ocorrencia.getDirection());
				ocorrenciaJson.put("io", ocorrencia.getIo());
				ocorrenciaJson.put("speed", ocorrencia.getSpeed());
				ocorrenciaJson.put("tratativa", tratativaJson);
				ocorrenciaJson.put("status", ocorrencia.getStatus());
				
				JSONObject retornoJson = new JSONObject();
				retornoJson.put("status", "OK");
				retornoJson.put("ocorrencia", ocorrenciaJson);
				
				return retornoJson;
			}

			private JSONObject error(String message) {
				JSONObject retornoJson = new JSONObject();
				retornoJson.put("status", "ERRO");
				retornoJson.put("mensagem", message);
				return retornoJson;
			}
			
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(retornoJson.toString());
		
	}

}
