package it.apice.sapere.api.space.match.functions.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase0;

/**
 * <p>
 * This class represents a custom function capable of generate a new globally
 * valid LSA-id.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class GenLSAidFunctImpl extends FunctionBase0 {

	/** Reference to a {@link PrivilegedLSAFactory}. */
	private final transient PrivilegedLSAFactory _factory;

	/**
	 * <p>
	 * Builds a new {@link GenLSAidFunctImpl} function.
	 * </p>
	 * 
	 * @param factory
	 *            Reference to the node's LSA factory
	 */
	public GenLSAidFunctImpl(final PrivilegedLSAFactory factory) {
		super();

		if (factory == null) {
			throw new IllegalArgumentException("Invalid factory provided");
		}

		_factory = factory;
	}

	@Override
	public NodeValue exec() {
		return NodeValue.makeNodeString(_factory.createLSAid().getId()
				.toString());
	}

}
