package it.apice.sapere.space;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.LSAspace;

import org.apache.felix.ipojo.junit4osgi.helpers.IPOJOHelper;

/**
 * <p>
 * Trying integration test on LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestLSAspace extends AbstractTestLSAspace {
	private IPOJOHelper helper;

	public void setUp() {
		helper = new IPOJOHelper(this);
	}

	@Override
	protected final LSAspace createSpace() {
		return (LSAspace) helper.createComponentInstance("LSAspaceService");
	}

	@Override
	protected final LSAFactory createFactory() {
		return (LSAFactory) helper.createComponentInstance("LSAFactoryService");
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		return (PrivilegedLSAFactory) helper
				.createComponentInstance("LSAFactoryService");
	}

	public void tearDown() {
		helper.dispose();
	}
}
