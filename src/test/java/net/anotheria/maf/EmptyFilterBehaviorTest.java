package net.anotheria.maf;

import java.io.IOException;
import java.util.Enumeration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.anotheria.anoprise.mocking.MockFactory;
import net.anotheria.anoprise.mocking.Mocking;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmptyFilterBehaviorTest {
	//tests whether an unconfigured filters answers 404 to each request.
	@Test public void testUnconfiguredFilter() throws ServletException, IOException{
		MAFFilter filter = new MAFFilter(){
			public String getDefaultActionName(){ return "dummy"; }
		};
		filter.init(new FilterConfig() {
			
			@Override
			public ServletContext getServletContext() {
				return null;
			}
			
			@Override
			public Enumeration getInitParameterNames() {
				return null;
			}
			
			@Override
			public String getInitParameter(String arg0) {
				return null;
			}
			
			@Override
			public String getFilterName() {
				return null;
			}
		});
		
		HttpServletRequest req = MockFactory.createMock(HttpServletRequest.class, new MyServletRequest());
		MyServletResponse myResponse = new MyServletResponse();
		HttpServletResponse res = MockFactory.createMock(HttpServletResponse.class, myResponse);
		FilterChain myChain = new FilterChain() {
			boolean doFilterCalled = false;
			
			@Override
			public void doFilter(ServletRequest arg0, ServletResponse arg1)
					throws IOException, ServletException {
				doFilterCalled = true;
			}
		};
		
		filter.doFilter(req, res, myChain);
		
		assertEquals("404 Status code expected", 404, myResponse.statusCode);
		
		
	}
	
	public static class MyServletRequest implements Mocking{
		public String getServletPath(){
			return "/test";
		}
	}
	public static class MyServletResponse implements Mocking{
		int statusCode = 0;
		public void sendError(int code, String message){
			statusCode = code;
		}
	}
}
