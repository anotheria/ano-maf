package net.anotheria.maf.errorhandling;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.errorhandling.handlers.GlobalRuntimeExceptionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
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

	@BeforeEach
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
		Assertions.assertNotNull(filterActionMappings);

		final List<Class<? extends ErrorHandler>> errorHandlers = filterActionMappings.getGlobalErrorHandlers(RuntimeException.class);
		Assertions.assertEquals(1, errorHandlers.size());
		Assertions.assertSame(GlobalRuntimeExceptionHandler.class, errorHandlers.get(0));
	}
}