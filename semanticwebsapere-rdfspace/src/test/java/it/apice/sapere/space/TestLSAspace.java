package it.apice.sapere.space;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.LSAspace;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * <p>
 * This class runs an integration test for the provided LSAspace implementation.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class TestLSAspace extends AbstractTestLSAspace {

	/** Reference to the LSAFactory. */
	@Inject
	private PrivilegedLSAFactory lsaFactory;

	/** Reference to the LSAspace to be tested. */
	@Inject
	private LSAspace lsaSpace;

	/**
	 * <p>
	 * Provides test configuration.
	 * </p>
	 *
	 * @return Choosen options
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
						.artifactId("semanticwebsapere-rdfspace"), CoreOptions
						.junitBundles(), CoreOptions.felix().version("4.0.2"));
	}

	@Override
	protected final LSAFactory createFactory() {
		return lsaFactory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		return lsaFactory;
	}

	@Override
	protected final LSAspace createSpace() {
		return lsaSpace;
	}

}
