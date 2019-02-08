package net.anotheria.maf.errorhandling;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.errorhandling.handlers.GlobalRuntimeExceptionHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Test global error handlers via annotation implementation.
 */
public class AnnotationGlobalErrorHandlerTest {

	/**
	 * Test {@link MAFFilter} instance.
	 */
	private ActionMappings filterActionMappings;

	@Before
	public void setup() throws Exception {
		filterActionMappings = null;

		final MAFFilter filter = new MAFFilter() {
			protected List<ActionMappingsConfigurator> getConfigurators() {
				ArrayList<ActionMappingsConfigurator> configurators = new ArrayList<>();

				configurators.add(new ActionMappingsConfigurator() {
					@Override
					public void configureActionMappings(ActionMappings mappings) {
						mappings.addMapping("testActionWithRuntimeException", "net.anotheria.maf.errorhandling.ActionWithRuntimeException");

						filterActionMappings = mappings;
					}
				});

				return configurators;
			}
		};

		filter.init(new FilterConfig() {
			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return null;
			}

			@Override
			public String getInitParameter(String name) {
				if ("configureByAnnotations".equals(name)) {
					return GlobalRuntimeExceptionHandler.class.getPackage().getName();
				}

				return null;
			}

			@Override
			public String getFilterName() {
				return null;
			}
		});
	}

	@Test
	public void shouldMappingsContainAnnotatedErrorHandler() {
		Assert.assertNotNull(filterActionMappings);

		final List<Class<? extends ErrorHandler>> errorHandlers = filterActionMappings.getGlobalErrorHandlers(RuntimeException.class);
		Assert.assertEquals(1, errorHandlers.size());
		Assert.assertSame(GlobalRuntimeExceptionHandler.class, errorHandlers.get(0));
	}
}