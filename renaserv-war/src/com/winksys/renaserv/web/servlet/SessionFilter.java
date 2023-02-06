package com.winksys.renaserv.web.servlet;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winksys.renaserv.data.Usuario;
import com.winksys.renaserv.web.servlet.CommandExecutor.ICommand;

/**
 * Servlet Filter implementation class SessionFilter
 */
@WebFilter("/api/*")
public class SessionFilter implements Filter {
	
	private static final HashMap<String, WeakReference<Usuario>> cacheTokens = new HashMap<String, WeakReference<Usuario>>();

    /**
     * Default constructor. 
     */
    public SessionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse hresponse = (HttpServletResponse) response;
		HttpServletRequest hrequest = (HttpServletRequest) request;
		String token = hrequest.getParameter("token");
		
		Usuario usuario = getUsuario(token, hrequest.getRemoteHost());
		hrequest.setAttribute("usuario", usuario);
		
		if (usuario != null) {
			chain.doFilter(request, response);
		} else {
			hresponse.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	private Usuario getUsuario(final String token, String ip) {
		WeakReference<Usuario> wr = cacheTokens.get(token);
		
		if (wr == null || wr.get() == null) {
			
			cacheTokens.remove(token);
			
			Usuario usuario = CommandExecutor.execute(new ICommand<Usuario>() {

				@Override
				public Usuario execute(EntityManager em) {
					
					Query query = em.createQuery("from Usuario where token=:token");
					query.setParameter("token", token);
					List<Usuario> usuarios = query.getResultList();
					
					for (Usuario usuario : usuarios) {
						if (usuario.getIp().equals(ip)) {
							usuario.setLastCheck(new Date());
							return em.merge(usuario);
						}
						return null;
					}
					
					return null;
				}
			});
			
			if (usuario != null) {
				return usuario;
			}
			
			
		}
		return null;
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
