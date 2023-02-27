package net.anotheria.maf.errorhandling.annotatedactions;

import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.errorhandling.ErrorHandler;
import net.anotheria.maf.errorhandling.handlers.NullPointerExceptionHandler;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Test action error handlers via annotation implementation.
 */
public class ActionAnnotationsErrorHandlerTest {

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
						mappings.addMapping("actionPath", "net.anotheria.maf.errorhandling.annotatedactions.ActionWithActionErrorHandlerAnnotations");

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
					return ActionWithActionErrorHandlerAnnotations.class.getPackage().getName();
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
	public void shouldMappingsContainActionAnnotatedErrorHandlers() {
		Assert.assertNotNull(filterActionMappings);

		final List<Class<? extends ErrorHandler>> nullPointerHandlers = filterActionMappings.getActionErrorHandler(ActionWithActionErrorHandlerAnnotations.class.getName(), NullPointerException.class);
		Assert.assertEquals(1, nullPointerHandlers.size());
		Assert.assertSame(NullPointerExceptionHandler.class, nullPointerHandlers.get(0));

		final List<Class<? extends ErrorHandler>> runtimeHandlers = filterActionMappings.getActionErrorHandler(ActionWithActionErrorHandlerAnnotations.class.getName(), RuntimeException.class);
		Assert.assertEquals(2, runtimeHandlers.size());
		Assert.assertSame(RuntimeExceptionHandlerNoOperationCommand.class, runtimeHandlers.get(0));
		Assert.assertSame(RuntimeExceptionHandlerActionCommand.class, runtimeHandlers.get(1));
	}
}