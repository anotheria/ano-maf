package net.anotheria.maf.mocks;

import jakarta.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;

/**
 * Factory for Mock HttpServletRequest creation.
 * Current Factory should be used for creating HttpServletRequestMockImpl instances.
 *
 * @author: h3llka
 */

public class MockServletRequestFactory {

	/**
	 * Creates HttpServletRequestMockImpl instance.
	 *
	 * @param session	 session instance - for which current request is associated.
	 * @param pArams	  request params map itself
	 * @param aTtributes  request attribites map itself
	 * @param cOntextPath request context path
	 * @param sErverName  server name
	 * @param loc		 current locale to use
	 * @param pOrt		server port
	 * @return create HttpServletRequestMockImpl instance
	 */
	public static HttpServletRequestMockImpl createMockedRequest(HttpSession session, Map<String, String> pArams, Map<String, Object> aTtributes, String cOntextPath, String sErverName, Locale loc, int pOrt) {
		HttpServletRequestMockImpl instance = new HttpServletRequestMockImpl(cOntextPath, sErverName, loc, pOrt);

		instance.setSession(session);

		for (String key : aTtributes.keySet()) {
			instance.setAttribute(key, aTtributes.get(key));
		}
		for (String key : pArams.keySet()) {
			instance.addParameter(key, pArams.get(key));
		}

		return instance;
	}

}
