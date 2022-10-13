package net.anotheria.maf;

import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandForward;
import net.anotheria.maf.mocks.HttpServletRequestMockImpl;
import net.anotheria.maf.mocks.MockHttpSessionFactory;
import net.anotheria.maf.mocks.MockServletRequestFactory;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class MAFFilterTest {
    private static final String CONTEXT_PATH = "/";
    private static final String SERVER_NAME = "localhost";


    private MAFFilter filter;
	@Before
    public void setup() throws Exception{

		filter = new MAFFilter(){
			protected List<ActionMappingsConfigurator> getConfigurators(){
				ArrayList<ActionMappingsConfigurator> configurators = new ArrayList<ActionMappingsConfigurator>();
				configurators.add(new ActionMappingsConfigurator() {

					@Override
					public void configureActionMappings(ActionMappings mappings) {
						mappings.addMapping("simple", "test.SimpleClass", new CommandForward("simple", "Simple.jsp"));
						mappings.addMapping("testAction", "net.anotheria.maf.TestCustomAction", new CommandForward("simple", "Simple.jsp"));

						mappings.addMapping("multi", "test.MultiClass",
								new CommandForward("varianta", "VariantA.jsp"),
								new CommandForward("variantb", "VariantB.jsp"),
								new CommandForward("variantc", "VariantC.jsp")
						);

						mappings.addAlias("verysimple", "simple");
						mappings.addAlias("notverymulti", "multi");

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
    public void shouldMapAnnotatedFormBean() throws  IOException, ServletException {
        // given
        Map<String, String> params = new HashMap<String, String>();
		params.put("requestId", "1");
		params.put("subject", "7");
		Map<String, Object> attributes = new HashMap<String, Object>();
		HttpSession mockedSession = new MockHttpSessionFactory().createMockedSession();
        HttpServletRequestMockImpl request = MockServletRequestFactory.createMockedRequest(mockedSession, params, attributes, CONTEXT_PATH, SERVER_NAME,Locale.ENGLISH, 80);
		

		filter.doFilter(request, null, new FilterChain(){
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
            }
        });



	}
}