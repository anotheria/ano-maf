package net.anotheria.maf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandForward;
import net.anotheria.maf.action.CommandRedirect;

import org.junit.Before;
import org.junit.Test;

public class MappingsTest {
	@Before public void setup() throws Exception{
		
		MAFFilter filter = new MAFFilter(){
			protected List<ActionMappingsConfigurator> getConfigurators(){
				ArrayList<ActionMappingsConfigurator> configurators = new ArrayList<ActionMappingsConfigurator>();
				configurators.add(new ActionMappingsConfigurator() {
					
					@Override
					public void configureActionMappings() {
						ActionMappings.addMapping("simple", "test.SimpleClass", new CommandForward("simple", "Simple.jsp"));

						ActionMappings.addMapping("multi", "test.MultiClass", 
								new CommandForward("varianta", "VariantA.jsp"),
								new CommandForward("variantb", "VariantB.jsp"),
								new CommandForward("variantc", "VariantC.jsp")
						);
						
						ActionMappings.addAlias("verysimple", "simple");
						ActionMappings.addAlias("notverymulti", "multi");
						
						ActionMappings.addMapping("delete", "test.SimpleClass", 
								new CommandRedirect("refresh", "list?"),
								new CommandRedirect("rebuild", "list?", 301)
						
						);
					
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
			public Enumeration<?> getInitParameterNames() {
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
	
	@Test public void testMappings(){
		assertNotNull(ActionMappings.findMapping("simple"));
		assertNotNull(ActionMappings.findMapping("multi"));
		
		assertNull(ActionMappings.findMapping("nonexisting"));
		
		assertNotNull(ActionMappings.findMapping("simple").findCommand("simple"));
		assertNull(ActionMappings.findMapping("simple").findCommand("not-existing"));
		
	}
	
	@Test public void testAlias(){
		assertNotNull(ActionMappings.findMapping("verysimple"));
		assertNotNull(ActionMappings.findMapping("simple").findCommand("simple"));
		assertNull(ActionMappings.findMapping("simple").findCommand("not-existing"));
	}
	
	@Test public void testForward(){
		ActionMapping mapping = ActionMappings.findMapping("multi");
		ActionCommand f1 = mapping.findCommand("varianta");
		ActionCommand f1_1 = mapping.findCommand("varianta");
		ActionCommand f2 = mapping.findCommand("variantb");
		ActionCommand f3 = mapping.findCommand("variantc");
		ActionCommand f4 = mapping.findCommand("not-existing");
		
		assertEquals(f1, f1_1);
		assertFalse(f1.equals(f2));
		assertFalse(f1.equals(f3));
		assertFalse(f1.equals(f4));
		
	}


}