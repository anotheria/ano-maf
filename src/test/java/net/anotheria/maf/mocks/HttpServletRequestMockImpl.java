package net.anotheria.maf.mocks;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HttpServletRequest mocked implementation - for tests.
 *
 * @author: h3llka
 */
public class HttpServletRequestMockImpl extends AbstractHttpServletRequest {

	/**
	 * Represents attribute Map - for current Request.
	 */
	private Map<String, Object> attributeMap;
	/**
	 * Represents parameters Map - for current Request.
	 */
	private Map<String, String> paramMap;
	/**
	 * Represents ContextPath.
	 */
	private String contextPath;

	/**
	 * Represent Locale for current Request. Default Locale.ENGLISH.
	 */
	private Locale currentLocale = Locale.ENGLISH;
	/**
	 * Represents serverName.
	 */
	private String serverName;
	/**
	 * Represents server port. Default value 80.
	 */
	private int serverPort = 80;
	/**
	 * Dummy ip address for getRemoteAddr.
	 */
	private String ipAddress = "127.0.0.1";


	/**
	 * Session instance.
	 */
	private HttpSession session;

	/**
	 * Default Constructor.
	 */
	public HttpServletRequestMockImpl() {
		this.paramMap = new ConcurrentHashMap<String, String>();
		this.attributeMap = new ConcurrentHashMap<String, Object>();
	}

	/**
	 * Constructor itself.
	 *
	 * @param cOntextPath String context path
	 * @param sErverName  String server name
	 * @param loc		 locale which should be used
	 * @param pOrt		int serverPort
	 */
	protected HttpServletRequestMockImpl(String cOntextPath, String sErverName, Locale loc, int pOrt) {
		this();
		// this.paramMap = new ConcurrentHashMap<String, String>();
		// this.attributeMap = new ConcurrentHashMap<String, Object>();
		this.contextPath = cOntextPath;
		this.serverName = sErverName;
		this.currentLocale = loc;
		this.serverPort = pOrt;
	}

	@Override
	public Locale getLocale() {
		return this.currentLocale;
	}

	@Override
	public String getContextPath() {
		return this.contextPath;
	}

	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	@Override
	public String getServerName() {
		return this.serverName;
	}

	@Override
	public Object getAttribute(String s) {
		return s != null ? attributeMap.get(s) : s;
	}

	@Override
	public void removeAttribute(String s) {
		if (s != null)
			attributeMap.remove(s);
	}

	@Override
	public void setAttribute(String s, Object o) {
		if (s != null)
			attributeMap.put(s, o);
	}

	@Override
	public Map getParameterMap() {
		return Collections.unmodifiableMap(paramMap);
	}

	@Override
	public String getParameter(String s) {
		return s != null ? paramMap.get(s) : s;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String dump() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this);
		buffer.append("Dumping HttpServletRequestMockImpl");
		buffer.append("Attributes :");
		appendCollection(paramMap, buffer);
		buffer.append("Parameters");
		appendCollection(attributeMap, buffer);
		return buffer.toString();
	}

	private void appendCollection(Map collection, StringBuffer buff) {
		for (Object key : collection.keySet()) {
			buff.append("key:").append(key).append(" value: ").append(collection.get(key));
		}
	}

	/**
	 * Allows adding parameters to the params map.
	 *
	 * @param key   key
	 * @param value value
	 */
	protected void addParameter(String key, String value) {
		if (key != null)
			this.paramMap.put(key, value);
	}

	@Override
	public String getRemoteAddr() {
		return getIpAddress();
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public void setSession(HttpSession httpSession) {
		this.session = httpSession;
	}
}
