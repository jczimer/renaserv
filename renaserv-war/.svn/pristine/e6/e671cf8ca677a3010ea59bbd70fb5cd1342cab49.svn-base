package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logo")
public class LogoDownload extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String filename = req.getParameter("filename");
		
		File f = new File(System.getProperty("user.home"), "uploads");
		f = new File(f, filename);
		
		FileInputStream fis = new FileInputStream(f);
		while (true) {
			byte[] buf = new byte[1024];
			int readed = fis.read(buf);
			
			resp.getOutputStream().write(buf,0,readed);
			
			if (readed < 1024) {
				break;
			}
		}
		fis.close();
				
	}
	
}
