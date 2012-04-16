package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class implements {@link Product} interface.
 * </p>
 * 
 * @author Paolo Contessi
 *
 */
public class ProductImpl extends ChemicalPatternImpl<Product> implements
		Product {

	/**
	 * <p>
	 * Builds a new {@link ProductImpl}.
	 * </p>
	 *
	 * @param pname Product name
	 */
	public ProductImpl(final PatternNameTerm pname) {
		super(pname);
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

}
