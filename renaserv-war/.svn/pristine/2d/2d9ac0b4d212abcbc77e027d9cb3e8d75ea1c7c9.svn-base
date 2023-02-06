package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		File file = new File(System.getProperty("java.io.tmpdir"), req.getParameter("f"));
		FileInputStream fis = new FileInputStream(file);
		
		resp.setHeader("Content-Length", file.length() + "");
		resp.setHeader("Content-Disposition", "filename=" + file.getName());
		resp.setContentType("application/file");
		ServletOutputStream os = resp.getOutputStream();
		
		byte[] buf = new byte[1024];
		for(;;) {
			int c = fis.read(buf);
			
			if (c < 0) {
				break;
			}
			os.write(buf,0,c);
			
		}
		
	}
	
}
