package net.anotheria.maf;

import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;
import net.anotheria.maf.mocks.HttpServletRequestMockImpl;
import net.anotheria.maf.mocks.MockHttpSessionFactory;
import net.anotheria.maf.mocks.MockServletRequestFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Test error handlers implementation in context of {@link MAFFilter}.
 */
public class MAFFilterErrorHandlersTest {
	private static final String CONTEXT_PATH = "/";
	private static final String SERVER_NAME = "localhost";

	/**
	 * Test {@link MAFFilter} instance.
	 */
	private MAFFilter filter;

	@Before
	public void setup() throws Exception {
		filter = new MAFFilter() {
			protected List<ActionMappingsConfigurator> getConfigurators() {
				ArrayList<ActionMappingsConfigurator> configurators = new ArrayList<>();

				configurators.add(new ActionMappingsConfigurator() {
					@Override
					public void configureActionMappings(ActionMappings mappings) {
						mappings.addMapping("testActionWithRuntimeException", "net.anotheria.maf.TestActionWithRuntimeException");
						mappings.addMapping("testActionWithAssertionError", "net.anotheria.maf.TestActionWithAssertionError");
						mappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
						mappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);
					}
				});

				return configurators;
			}
		};

		FilterConfig config = new FilterConfig() {
			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
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
		};

		filter.init(config);
	}

	@Test
	public void testRuntimeExceptionWasHandled() throws IOException, ServletException {
		final HttpServletRequestMockImpl request = MockServletRequestFactory.createMockedRequest(
				new MockHttpSessionFactory().createMockedSession(),
				new HashMap<String, String>(),
				new HashMap<String, Object>(),
				CONTEXT_PATH,
				SERVER_NAME,
				Locale.ENGLISH,
				80
		);

		request.setServletPath("testActionWithRuntimeException");

		try {
			filter.doFilter(request, null, new FilterChain() {
				@Override
				public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {

				}
			});
		} catch (ServletException e) {
			Assert.fail();
		}
	}

	@Test(expected = ServletException.class)
	public void testAssertionErrorWasNotHandled() throws IOException, ServletException {
		final HttpServletRequestMockImpl request = MockServletRequestFactory.createMockedRequest(
				new MockHttpSessionFactory().createMockedSession(),
				new HashMap<String, String>(),
				new HashMap<String, Object>(),
				CONTEXT_PATH,
				SERVER_NAME,
				Locale.ENGLISH,
				80
		);

		request.setServletPath("testActionWithAssertionError");

		filter.doFilter(request, null, new FilterChain() {
			@Override
			public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {

			}
		});

		Assert.fail();
	}
}