package net.anotheria.maf.mocks;


import net.anotheria.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mocked HttpSession Implementation.
 * For tests actually.
 *
 * @author h3llka
 */
public class HttpSessionMockImpl extends AbstractHttpSession {

	/**
	 * Attributes map.
	 */
	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	/**
	 * Creation Time.
	 */
	private long sessionCreationTime;

	/**
	 * Session id.
	 */
	private String sessionId;

	/**
	 * Session inactive interval.
	 */
	private int maxInactiveInterval;

	/**
	 * Protected Constructor.
	 */
	protected HttpSessionMockImpl() {
		attributes = new ConcurrentHashMap<String, Object>();
		sessionCreationTime = new Date().getTime();
		maxInactiveInterval = 600; //10minutes
		for (int i = 0; i < 10; i++) {
			Random rnd = new Random(10);
			sessionId += rnd.nextInt();
		}
	}


	@Override
	public long getCreationTime() {
		return sessionCreationTime;
	}

	@Override
	public String getId() {
		return sessionId;
	}

	@Override
	public void removeAttribute(String attributeName) {
		if (!StringUtils.isEmpty(attributeName))
			attributes.remove(attributeName);

	}

	@Override
	public Object getAttribute(String attributeName) {
		if (!StringUtils.isEmpty(attributeName))
			attributes.get(attributeName);
		return null;
	}

	@Override
	public void setAttribute(String key, Object value) {
		if (!StringUtils.isEmpty(key))
			attributes.put(key, value);
	}

	@Override
	public void setMaxInactiveInterval(int i) {
		this.maxInactiveInterval = i;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	@Override
	public void invalidate() {
		attributes.clear();
		sessionCreationTime = new Date().getTime();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		String attributesString = "";
		for (String key : attributes.keySet()) {
			String value = attributes.get(key) != null ? attributes.get(key).toString() : "null";
			attributesString += key + " _ " + value + " ;";
		}
		sb.append("HttpSessionMockImpl");
		sb.append("{attributes=").append(attributesString);
		sb.append(", sessionCreationTime=").append(sessionCreationTime);
		sb.append(", sessionId='").append(sessionId).append('\'');
		sb.append(", maxInactiveInterval=").append(maxInactiveInterval);
		sb.append('}');
		return sb.toString();
	}
}
