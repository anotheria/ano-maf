package net.anotheria.maf;

import net.anotheria.maf.action.*;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MonitoringActionFactoryTest {
    @Test
    public void testActionCreation() throws ActionFactoryException {
        ActionFactory f = new MonitoringActionFactory();
        Action a1 = f.getInstanceOf("net.anotheria.maf.TestAction");
        Action a2 = f.getInstanceOf("net.anotheria.maf.TestAction");
        assertSame(a1,a2);

        //also test that we have a new producer
        IProducerRegistryAPI api = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
        assertEquals(1, api.getAllProducersByCategory("action").size());
        assertEquals("TestAction-1", api.getAllProducersByCategory("action").get(0).getProducerId());
    }



}
