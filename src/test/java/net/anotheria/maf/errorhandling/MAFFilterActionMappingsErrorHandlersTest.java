package net.anotheria.maf.errorhandling;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.errorhandling.handlers.NullPointerExceptionHandler;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;
import net.anotheria.maf.mocks.HttpServletRequestMockImpl;
import net.anotheria.maf.mocks.MockHttpSessionFactory;
import net.anotheria.maf.mocks.MockServletRequestFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Test error handlers via action mappings implementation in context of {@link MAFFilter}.
 */
public class MAFFilterActionMappingsErrorHandlersTest {
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
						// action without error handler
						mappings.addMapping("testActionWithAssertionError", "net.anotheria.maf.errorhandling.ActionWithAssertionError");

						// action with two global error handlers
						mappings.addMapping("testActionWithRuntimeException", "net.anotheria.maf.errorhandling.ActionWithRuntimeException");
						mappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
						mappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

						// action with error handler
						mappings.addMapping("testActionWithNullPointerException", ActionWithNullPointerException.class, NullPointerException.class, NullPointerExceptionHandler.class);
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

	@Test
	public void testActionWithNullPointerExceptionWasHandled() throws IOException, ServletException {
		final HttpServletRequestMockImpl request = MockServletRequestFactory.createMockedRequest(
				new MockHttpSessionFactory().createMockedSession(),
				new HashMap<String, String>(),
				new HashMap<String, Object>(),
				CONTEXT_PATH,
				SERVER_NAME,
				Locale.ENGLISH,
				80
		);

		request.setServletPath("testActionWithNullPointerException");

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
}