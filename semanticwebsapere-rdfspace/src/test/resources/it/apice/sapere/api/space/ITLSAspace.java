package it.apice.sapere.api.space;

import org.apache.felix.ipojo.annotations.Requires;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.space.impl.LSAspaceImpl;

/**
 * <p>
 * Test LSA-space.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class ITLSAspace extends AbstractITLSAspace {

	/** Reference to LSA Factory. */
	@Requires
	private transient PrivilegedLSAFactory factory;

	@Override
	protected final LSAspace createSpace() {
		return new LSAspaceImpl();
	}

	@Override
	protected final LSAFactory createFactory() {
		return factory;
	}

	@Override
	protected final PrivilegedLSAFactory createPrivilegedFactory() {
		return factory;
	}

}
