package it.apice.sapere.space;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.impl.LSAFactoryImpl;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.space.impl.LSAspaceImpl;

/**
 * <p>
 * Trying integration test on LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestLSAspace extends AbstractTestLSAspace {

	private transient PrivilegedLSAFactory factory = new LSAFactoryImpl();

	@Override
	protected final LSAspace createSpace() {
		return new LSAspaceImpl(createPrivilegedFactory());
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
