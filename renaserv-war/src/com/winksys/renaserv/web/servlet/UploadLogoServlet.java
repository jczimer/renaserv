package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.winksys.renaserv.data.Credential;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

import net.sf.json.JSONObject;

@WebServlet("/api/uploadlogo")
public class UploadLogoServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Location to save data that is larger than maxMemSize.
		File file = new File(System.getProperty("user.home"), "uploads");
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// factory.setSizeThreshold(1000*1024);
//		factory.setRepository(file);
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List<FileItem> files = upload.parseRequest(req);
			DiskFileItem dfi = (DiskFileItem) files.get(0);
			final DiskFileItem dfi2 = (DiskFileItem) files.get(1);
			
			InputStream is = dfi.getInputStream();
			
			file = new File(file, dfi2.getString());
			file.mkdirs();
			
			// salva na pasta do cliente
			File f = new File(file, dfi.getName());
			FileOutputStream fos = new FileOutputStream(f);
			
			while (true) {

				byte[] buf = new byte[1024];
				int readed = is.read(buf);
				
				fos.write(buf, 0, readed);
				
				if (readed < 1024) {
					break;
				}
			}
			fos.close();
			
			// salva cadastro do cliente
			CommandExecutor.execute(new ICommand<Void>() {

				@Override
				public Void execute(EntityManager em) {
					int idCliente = Integer.parseInt(dfi2.getString());
					
					Credential cred = em.find(Credential.class, idCliente);
					cred.setImage(dfi.getName());
					em.merge(cred);
					return null;
				}
				
			});
			
			JSONObject ret = new JSONObject();
			ret.put("success", true);
			
			resp.setContentType("application/json");
			resp.getOutputStream().write(ret.toString().getBytes());
						
		} catch (FileUploadException e) {
			e.printStackTrace();
			
			JSONObject ret = new JSONObject();
			ret.put("success", false);
			ret.put("mensagem", e.getMessage());
			
			resp.setContentType("application/json");
			resp.getOutputStream().write(ret.toString().getBytes());
		}
			
		
	}
	
}
