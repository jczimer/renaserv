package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Calendar;
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

import org.apache.velocity.tools.generic.DateTool;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.windi.ui.tools.mailsender.VelocityHelper;
import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.data.EnStatus;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

/**
 * This class check if gateway is doing your job.
 * <p>
 * If has existing any account that not is checked at last 10 minutes past, the
 * system will send a message to configured email in {@link}renaserv.alert.mail
 * property.
 * 
 * @author Jean
 *
 */
public class ValidatorJob implements Job {

	public static class VelocityContext {

		private String destName;
		private List<Credential> credentials;

		public String getDestName() {
			return destName;
		}

		public void setDestName(String destName) {
			this.destName = destName;
		}

		public List<Credential> getCredentials() {
			return credentials;
		}

		public void setCredentials(List<Credential> credentials) {
			this.credentials = credentials;
		}

	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("teste");

		List<Credential> credentials = getNonCheckedAccounts();

		if (!credentials.isEmpty()) {
			doSendMail(credentials);
		}

	}

	/*
	 * Send mail message about accounts with no communication
	 */
	private void doSendMail(List<Credential> credentials) {
		String body;
		try {
			body = generateBody(credentials);
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
			String mailDest = (String) props.get("winksys.mail.destination");
			
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
			
			URL resource = getClass().getClassLoader().getResource("renaserv.png");
			
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
			mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(mailDest));
			mimeMessage.setSubject("Notificação do sistema de monitoramento");
			mimeMessage.setContent(multipart);
			Transport.send(mimeMessage);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private String generateBody(List<Credential> credentials) throws Exception {
//		VelocityHelper.init(false);
		
		String destName = "Suporte";
		
		VelocityContext ctx = new VelocityContext();
		ctx.setCredentials(credentials);
		ctx.setDestName(destName);
		
		
		org.apache.velocity.VelocityContext velCtx = new org.apache.velocity.VelocityContext();
		velCtx.put("ctx", ctx);
		velCtx.put("date", new DateTool());
				
		return VelocityHelper.executeTemplate(velCtx, "template_checkacc.vm");
		
	}

	/**
	 * Get account that not checked in last 10 minutes past. Accounts can be active
	 * and not mail message sent in last hour.
	 * 
	 * @return
	 */
	private List<Credential> getNonCheckedAccounts() {
		List<Credential> credentials = CommandExecutor.execute(new ICommand() {

			@Override
			public Object execute(EntityManager em) {

				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MINUTE, -10);
				Date nextTimeOut = calendar.getTime();

				calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR, -1);
				Date nextSentMail = calendar.getTime();

				Query query = em.createQuery(
						"from Credential where lastCheck < :lastCheck and status=:status and (lastsentmail is null or lastsentmail < :lastsentmail)");
				query.setParameter("lastCheck", nextTimeOut);
				query.setParameter("status", EnStatus.ATIVO);
				query.setParameter("lastsentmail", nextSentMail);
				List<Credential> resultList = query.getResultList();

				return resultList;
			}

		});
		return credentials;
	}
	
	public static void main(String[] args) throws JobExecutionException {
		
		ValidatorJob j = new ValidatorJob();
		j.execute(null);
		
	}

}
