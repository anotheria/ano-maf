package net.anotheria.maf.annotationsmapping;

import org.junit.Assert;
import net.anotheria.maf.MAFFilter;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import org.junit.Test;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Kirill Reviakin
 */
public class AnnotationsMappingTest {
    @Test
    public void ShouldNotConfigureAnnotations() throws ServletException, IOException {
        final MappingsContainer mappingsContainer = new MappingsContainer();
        MAFFilter filter = new MAFFilter() {
            public String getDefaultActionName() {
                return "dummy";
            }

            @Override
            protected List<ActionMappingsConfigurator> getConfigurators() {
                ActionMappingsConfigurator configurator = new ActionMappingsConfigurator() {
                    @Override
                    public void configureActionMappings(ActionMappings m) {
                        mappingsContainer.mappings = m;
                    }
                };
                return Arrays.asList(configurator);
            }
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
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public String getFilterName() {
                return null;
            }
        });

        Assert.assertEquals(1, mappingsContainer.mappings.getMappings().size()); // Default MAF mapping
    }

    @Test
    public void ShouldConfigureAnnotations() throws ServletException, IOException {
        final MappingsContainer mappingsContainer = new MappingsContainer();
        MAFFilter filter = new MAFFilter() {
            public String getDefaultActionName() {
                return "dummy";
            }

            @Override
            protected List<ActionMappingsConfigurator> getConfigurators() {
                ActionMappingsConfigurator configurator = new ActionMappingsConfigurator() {
                    @Override
                    public void configureActionMappings(ActionMappings m) {
                        mappingsContainer.mappings = m;
                    }
                };
                return Arrays.asList(configurator);
            }
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
            public String getInitParameter(String name) {
                if ("configureByAnnotations".equals(name)) {
                    return AnnotationsMappingTest.class.getPackage().getName();
                }
                return null;
            }

            @Override
            public String getFilterName() {
                return null;
            }
        });

        Assert.assertEquals(4, mappingsContainer.mappings.getMappings().size());
        Assert.assertEquals(AnnotationsMappingTestActionFirst.class.getName(), mappingsContainer.mappings.findMapping("/firstactionpath").getType());
        Assert.assertEquals(AnnotationsMappingTestActionSecond.class.getName(), mappingsContainer.mappings.findMapping("/secondactionfirstpath").getType());
        Assert.assertEquals(AnnotationsMappingTestActionSecond.class.getName(), mappingsContainer.mappings.findMapping("/secondactionsecondpath").getType());
    }

    @Test
    public void ShouldConfigureAnnotationsForGivenPath() throws ServletException, IOException {
        final MappingsContainer mappingsContainer = new MappingsContainer();
        MAFFilter filter = new MAFFilter() {
            public String getDefaultActionName() {
                return "dummy";
            }

            @Override
            protected List<ActionMappingsConfigurator> getConfigurators() {
                ActionMappingsConfigurator configurator = new ActionMappingsConfigurator() {
                    @Override
                    public void configureActionMappings(ActionMappings m) {
                        mappingsContainer.mappings = m;
                    }
                };
                return Arrays.asList(configurator);
            }
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
            public String getInitParameter(String name) {
                if ("configureByAnnotations".equals(name)) {
                    return AnnotationsMappingTest.class.getPackage().getName();
                }
                if ("path".equals(name)) {
                    return "/test";
                }
                return null;
            }

            @Override
            public String getFilterName() {
                return null;
            }
        });

        Assert.assertEquals(3, mappingsContainer.mappings.getMappings().size());
        Assert.assertEquals(AnnotationsMappingTestActionThird.class.getName(), mappingsContainer.mappings.findMapping("/thirdactionfirstpath").getType());
        Assert.assertEquals(AnnotationsMappingTestActionThird.class.getName(), mappingsContainer.mappings.findMapping("/thirdactionsecondpath").getType());
    }

    private static class MappingsContainer {
        public ActionMappings mappings;
    }
}
