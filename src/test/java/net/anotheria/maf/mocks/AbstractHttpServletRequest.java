package net.anotheria.maf.mocks;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author: h3llka
 */
public class AbstractHttpServletRequest implements HttpServletRequest {
	private String servletPath;
	private String pathInfo;
	private HttpSession session;

	public AbstractHttpServletRequest() {
		this.session = new HttpSession() {

			private Map<String, Object> sessionMap = new HashMap<String, Object>();

			@Override
			public void setMaxInactiveInterval(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setAttribute(String arg0, Object arg1) {
				sessionMap.put(arg0, arg1);
			}

			@Override
			public void putValue(String s, Object o) {

			}

			@Override
			public void removeAttribute(String arg0) {
				sessionMap.remove(arg0);

			}

			@Override
			public void removeValue(String s) {

			}

			@Override
			public boolean isNew() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void invalidate() {
				// TODO Auto-generated method stub

			}

			@Override
			public ServletContext getServletContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getMaxInactiveInterval() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public HttpSessionContext getSessionContext() {
				return null;
			}

			@Override
			public long getLastAccessedTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return UUID.randomUUID().toString();
			}

			@Override
			public long getCreationTime() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Enumeration<String> getAttributeNames() {
				final Iterator<String> it = sessionMap.keySet().iterator();
				Enumeration<String> enumer = new Enumeration<String>() {
					@Override
					public boolean hasMoreElements() {
						return it.hasNext();
					}

					@Override
					public String nextElement() {
						return it.next();
					}

				};

				return enumer;
			}

			@Override
			public String[] getValueNames() {
				return new String[0];
			}

			@Override
			public Object getAttribute(String arg0) {
				return sessionMap.get(arg0);
			}

			@Override
			public Object getValue(String s) {
				return null;
			}
		};
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getAttribute(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Enumeration getAttributeNames() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getCharacterEncoding() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public int getContentLength() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getContentType() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getParameter(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Enumeration getParameterNames() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String[] getParameterValues(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Map getParameterMap() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getProtocol() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getScheme() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getServerName() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public int getServerPort() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRemoteAddr() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRemoteHost() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public void setAttribute(String s, Object o) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public void removeAttribute(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Locale getLocale() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Enumeration getLocales() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public boolean isSecure() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRealPath(String s) {
		return null;
	}

	@Override
	public String getAuthType() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Cookie[] getCookies() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public long getDateHeader(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getHeader(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Enumeration getHeaders(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Enumeration getHeaderNames() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public int getIntHeader(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getMethod() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getPathInfo() {
		return pathInfo;
	}

	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getContextPath() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getQueryString() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRemoteUser() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public boolean isUserInRole(String s) {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public Principal getUserPrincipal() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRequestedSessionId() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getRequestURI() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public StringBuffer getRequestURL() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	@Override
	public HttpSession getSession(boolean b) {
		return session;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException("Implement me please!");
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public String changeSessionId() {
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
		return false;
	}

	@Override
	public void login(String s, String s2) throws ServletException {

	}

	@Override
	public void logout() throws ServletException {

	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public Part getPart(String s) throws IOException, ServletException {
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> tClass) throws IOException, ServletException {
		return null;
	}

	@Override
	public long getContentLengthLong() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}
}