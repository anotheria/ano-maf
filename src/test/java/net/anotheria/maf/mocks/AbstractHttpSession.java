package net.anotheria.maf.mocks;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Abstract implementation of HttpSession.
 * Actually simple not implemented stub.
 *
 * @author h3llka
 */
public abstract class AbstractHttpSession implements HttpSession {
	@Override
	public long getCreationTime() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String getId() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public long getLastAccessedTime() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ServletContext getServletContext() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public void setMaxInactiveInterval(int i) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public int getMaxInactiveInterval() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Object getAttribute(String s) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Enumeration getAttributeNames() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public void setAttribute(String s, Object o) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public void removeAttribute(String s) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public void invalidate() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean isNew() {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
