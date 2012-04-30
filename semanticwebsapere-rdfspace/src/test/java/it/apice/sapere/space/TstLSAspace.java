package it.apice.sapere.space;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.LSAspaceCore;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

import com.hp.hpl.jena.rdf.model.StmtIterator;

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
public class TstLSAspace extends AbstractTestLSAspaceCore<StmtIterator> {

	/** Reference to the LSAFactory. */
	@Inject
	private PrivilegedLSAFactory lsaFactory;

	/** Reference to the LSAspace to be tested. */
	@Inject
	private LSAspaceCore<StmtIterator> lsaSpace;

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
						.artifactId("semanticwebsapere-rdfspace")
						.version("0.1.0"), CoreOptions.junitBundles(),
				CoreOptions.felix().version("4.0.2"));
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
	protected final LSAspaceCore<StmtIterator> createSpace() {
		return lsaSpace;
	}

}
