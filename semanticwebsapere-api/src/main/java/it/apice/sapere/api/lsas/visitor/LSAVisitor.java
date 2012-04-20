package it.apice.sapere.api.lsas.visitor;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.PropertyValue;

/**
 * <p>
 * This interface models an LSA visitor.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAVisitor {

	/**
	 * <p>
	 * Visits an LSAid.
	 * </p>
	 * 
	 * @param lsaId
	 *            instance
	 */
	void visit(LSAid lsaId);

	/**
	 * <p>
	 * Visits a Property.
	 * </p>
	 * 
	 * @param prop
	 *            instance
	 */
	void visit(Property prop);

	/**
	 * <p>
	 * Visits a Property Name.
	 * </p>
	 * 
	 * @param pname
	 *            instance
	 */
	void visit(PropertyName pname);

	/**
	 * <p>
	 * Visits an Semantic Description.
	 * </p>
	 * 
	 * @param desc
	 *            instance
	 */
	void visit(SemanticDescription desc);

	/**
	 * <p>
	 * Visits a Property Value.
	 * </p>
	 * 
	 * @param val
	 *            instance
	 */
	void visit(PropertyValue<?, ?> val);

	/**
	 * <p>
	 * Visits an LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            instance
	 */
	void visit(LSA lsa);
}
