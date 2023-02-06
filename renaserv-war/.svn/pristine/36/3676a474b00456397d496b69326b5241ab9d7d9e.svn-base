package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.DateTool;

import com.windi.ui.tools.mailsender.VelocityHelper;
import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.Evento;
import com.winksys.renaserv.data.LogOcorrencia;
import com.winksys.renaserv.data.Ocorrencia;
import com.winksys.renaserv.data.Ocorrencia.OcorrenciaId;
import com.winksys.renaserv.data.Usuario;
import com.winksys.renaserv.data.Veiculo;
import com.winksys.renaserv.data.Veiculo.VeiculoId;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@WebServlet("/api/tratarocorrencia")
public class TratarOcorrenciaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -91343452706645026L;
	
	private static final Logger LOG = Logger.getLogger(TratarOcorrenciaServlet.class);
	
	public static class VelocityContext {

		private String destName;
		private Ocorrencia ocorrencia;
		private Veiculo veiculo;
		private Evento evento;

		public String getDestName() {
			return destName;
		}

		public void setDestName(String destName) {
			this.destName = destName;
		}

		public Ocorrencia getOcorrencia() {
			return ocorrencia;
		}

		public void setOcorrencia(Ocorrencia ocorrencia) {
			this.ocorrencia = ocorrencia;
		}
		
		public Veiculo getVeiculo() {
			return veiculo;
		}

		public String getTratativa() {
			return ocorrencia.getTratativa().replaceAll("[\n,\r]", "<br>");
		}

		public void setVeiculo(Veiculo veiculo) {
			this.veiculo = veiculo;
		}

		public Evento getEvento() {
			return evento;
		}

		public void setEvento(Evento evento) {
			this.evento = evento;
		}

		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		final Usuario usuario = (Usuario) req.getAttribute("usuario");
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
		
		JSONObject ret = CommandExecutor.execute(new ICommand<JSONObject>() {

			@Override
			public JSONObject execute(EntityManager em) {
				// tratara todas as ocorrências parecidas
				
				JSONObject req = (JSONObject) JSONSerializer.toJSON(sb.toString());
				JSONObject idJson = req.getJSONObject("id");
				
				Credential credential = new Credential();
				credential.setId(idJson.getInt("cliente"));
				
				VeiculoId vid = new VeiculoId();
				vid.setId(idJson.getInt("veiculo"));
				vid.setCredential(credential);
				
				Veiculo veiculo = new Veiculo();
				veiculo.setId(vid);
				
				Evento evento = new Evento();
				evento.setId(idJson.getInt("evento"));
				
				Date dataDados = new Date(idJson.getLong("dataDados"));
				String tratativa = req.getString("tratativa");
				
				boolean finalizar = req.getBoolean("finalizar");
				
				OcorrenciaId id = new Ocorrencia.OcorrenciaId();
				id.setDataDados(dataDados);
				id.setEvento(evento);
				id.setVeiculo(veiculo);
				
				// trata todos as ocorrências semelhantes
				Query q = em.createQuery("from Ocorrencia o where o.id.veiculo=:veiculo and o.id.evento=:evento and o.status in (0,1)");
				q.setParameter("veiculo", veiculo);
				q.setParameter("evento", evento);
				List<Ocorrencia> ocorrencias = q.getResultList();				
								
				for (Ocorrencia ocorrencia : ocorrencias) {
				
					if (ocorrencia.getStatus() != 2) {
						ocorrencia.setStatus(finalizar ? 2 : 1);
						ocorrencia.setDataTratativa(new Date());
						ocorrencia.setUsuarioTratativa(usuario.getUsuario());
						ocorrencia.setTratativa(tratativa);
					}
					if (ocorrencia.getDataPrimeiraTratativa() == null) {
						ocorrencia.setDataPrimeiraTratativa(new Date());
					}
					ocorrencia = em.merge(ocorrencia);
					
					LogOcorrencia log = new LogOcorrencia();
					log.setData(new Date());
					log.setOcorrencia(ocorrencia);
					log.setTratativa(tratativa);
					log.setUsuario(usuario.getUsuario());
					log.setStatus(ocorrencia.getStatus());
					log = em.merge(log);
							
				}
				
				// 
				Ocorrencia ocorrencia = em.find(Ocorrencia.class, id);

//				LogOcorrencia log = new LogOcorrencia();
//				log.setData(new Date());
//				log.setOcorrencia(ocorrencia);
//				log.setTratativa(tratativa);
//				log.setUsuario(usuario.getUsuario());
//				log.setStatus(ocorrencia.getStatus());
//				log = em.merge(log);
				

				JSONObject logJson = new JSONObject();
//				logJson.put("id", log.getId());
				logJson.put("data", System.currentTimeMillis());
				logJson.put("usuario", usuario.getUsuario());
				
				req.put("id", ocorrencia.getId());
				JSONObject ret = new JSONObject();
				ret.put(req, req);
				ret.put("status", "OK");
				
				if (finalizar) {
					final Ocorrencia o = ocorrencia;
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							doSendMail(o);
						}
					}).start();
				}
				
				
				return ret;
			}
		});
		
		resp.setContentType("application/json");
		resp.getWriter().append(ret.toString());
		
	}
	
	private String generateBody(final Ocorrencia ocorrencia, final Credential credential) throws Exception {
//		VelocityHelper.init(false);
		
		final VeiculoId id = ocorrencia.getId().getVeiculo().getId();
		Veiculo veiculo = CommandExecutor.execute(new ICommand<Veiculo>() {

			@Override
			public Veiculo execute(EntityManager em) {
				return em.find(Veiculo.class, id);
			}
		});
		Evento evento = CommandExecutor.execute(new ICommand<Evento>() {

			@Override
			public Evento execute(EntityManager em) {
				return em.find(Evento.class, ocorrencia.getId().getEvento().getId());
			}
		});
		
		String destName = credential.getNome();
		
		VelocityContext ctx = new VelocityContext();
		ctx.setOcorrencia(ocorrencia);
		ctx.setDestName(destName);
		ctx.setVeiculo(veiculo);
		ctx.setEvento(evento);
		
		
		org.apache.velocity.VelocityContext velCtx = new org.apache.velocity.VelocityContext();
		velCtx.put("ctx", ctx);
		velCtx.put("date", new DateTool());
				
		return VelocityHelper.executeTemplate(velCtx, "template_tratativa.htm");
		
	}
	
	/*
	 * Send mail message about accounts with no communication
	 */
	private void doSendMail(final Ocorrencia ocorrencia) {
		LOG.debug("Enviando email ocorrência");
		String body;
		try {
			Credential cred = CommandExecutor.execute(new ICommand<Credential>() {

				@Override
				public Credential execute(EntityManager em) {
					return em.find(Credential.class,ocorrencia.getId().getVeiculo().getId().getCredential().getId());
					
				}
			});
			
			body = generateBody(ocorrencia, cred);
			Session session;
			
			Properties props = new Properties();
			props.load(new FileInputStream(new File(System.getProperty("user.home"), "mail-config.properties")));
			
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.host", "mail.winksysmail.com.br");
//			props.put("mail.smtp.port", "587");
////			props.put("mail.smtp.starttls.trust", "mail.winksysmail.com.br");
//			props.put("mail.smtp.timeout", "5000");
////			props.put("mail.smtp.starttls.enable", "true");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.ssl.enable","false");
			
			String mailUser = (String) props.get("winksys.mail.user");
			String mailPass = (String) props.get("winksys.mail.pass");
			String mailFrom = (String) props.get("winksys.mail.from");
			String mailName = (String) props.get("winksys.mail.name");
			
			
			if (mailUser != null) {
			
				session = Session.getInstance(props,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(mailUser, mailPass);
							}
						  });
			} else {
				session = Session.getInstance(props);
			}

			URL resource;
			if (cred.getImage() == null) {
				resource = getClass().getClassLoader().getResource("renaserv.png");
			} else {
				File f = new File(new File(System.getProperty("user.home") + "/uploads", Integer.toString(cred.getId())), cred.getImage());
				resource = f.toURL();
			}
			
			
			MimeBodyPart imagePart = new MimeBodyPart();
			imagePart.setHeader("Content-ID", "<AbcXyz123>");
			imagePart.setDisposition(MimeBodyPart.INLINE);
			// attach the image file
			imagePart.attachFile(resource.getFile());
			
			// creates message part
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(body, "text/html; charset=\"UTF-8\"");
			
			 // creates multi-part
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	        multipart.addBodyPart(imagePart);
			
			
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSentDate(new Date());
			mimeMessage.setFrom(new InternetAddress(mailFrom, mailName));
			mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(cred.getEmail(), cred.getNome()));
			mimeMessage.setSubject("Registro de Ocorrência");
			mimeMessage.setContent(multipart);
			Transport.send(mimeMessage);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
