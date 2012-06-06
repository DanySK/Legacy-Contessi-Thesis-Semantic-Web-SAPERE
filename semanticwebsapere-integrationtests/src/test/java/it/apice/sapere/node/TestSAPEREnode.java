package it.apice.sapere.node;

import static org.junit.Assert.assertNotNull;
import it.apice.sapere.api.node.LoggerFactory;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * <p>
 * This class runs an integration test for SAPEREnode start-up (needs VM args:
 * -d32).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class TestSAPEREnode {

	/** Reference to Bundle Context. */
	@Inject
	private BundleContext context;

	/**
	 * <p>
	 * Provides test configuration.
	 * </p>
	 * 
	 * @return Chosen options
	 */
	@Configuration
	public final Option[] configure() {
		return CoreOptions.options(
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-api"),
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-requirements"),
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-testplan"),
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-rdfmodel"),
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-rdfspace"),
				CoreOptions.mavenBundle().groupId("it.apice.sapere")
						.artifactId("semanticwebsapere-node"), CoreOptions
						.felix().version("4.0.2"));
	}

	/**
	 * <p>
	 * Tests the availability of SAPERE-node published services.
	 * </p>
	 * <p>
	 * If all tests passes then the SAPERE-node implementation is working.
	 * </p>
	 * 
	 * @throws IOException
	 *             Error while reading
	 */
	@Test
	public final void testServicesAvailablity() throws IOException {
		final ServiceReference<?> ref = context
				.getServiceReference(LoggerFactory.class);
		assertNotNull(ref);
		assertNotNull(context.getService(ref));
		Assert.assertTrue(true);
	
		final ServiceReference<SAPEREAgentsFactory> ref2 = context
				.getServiceReference(SAPEREAgentsFactory.class);
		assertNotNull(ref2);
		assertNotNull(context.getService(ref2));
	}
}
