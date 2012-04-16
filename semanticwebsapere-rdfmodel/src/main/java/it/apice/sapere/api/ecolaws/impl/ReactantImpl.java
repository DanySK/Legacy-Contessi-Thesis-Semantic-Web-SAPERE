package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class implements {@link Reactant} interface.
 * </p>
 * 
 * @author Paolo Contessi
 *
 */
public class ReactantImpl extends ChemicalPatternImpl<Reactant> implements
		Reactant {

	/**
	 * <p>
	 * Builds a new {@link ReactantImpl}.
	 * </p>
	 *
	 * @param pname Product name
	 */
	public ReactantImpl(final PatternNameTerm pname) {
		super(pname);
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

}
