package it.apice.sapere.api.ecolaws.terms;

/**
 * <p>
 * This interface models a boolean condition for annotated variables.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @param <Type>
 *            The type of the term's value
 */
public interface Formula<Type> {

	/**
	 * <p>
	 * Checks the term and decides if represents a valid assignment.
	 * </p>
	 * 
	 * @param value
	 *            The value to be verified
	 * @return True if condition is satisfied (valid assignment), false
	 *         otherwise
	 */
	boolean accept(Type value);

	/**
	 * <p>
	 * Provides a formula representation as a String.
	 * </p>
	 * <p>
	 * This String should be composed as &lt;left-op&gt; &lt;operator&gt;
	 * &lt;right-op&gt;.
	 * </p>
	 * 
	 * @return The representation of the formula as a String
	 */
	String getStringRepr();

	/**
	 * <p>
	 * Retrieves the operator used in the formula (as a String).
	 * </p>
	 * 
	 * @return The operator as a String
	 */
	String getOperator();

	/**
	 * <p>
	 * Retrieves the right operand of the formula (as a String).
	 * </p>
	 * 
	 * @return The right operand as a String
	 */
	String getRightOp();

	/**
	 * <p>
	 * Retrieves the left operand of the formula (as a String). This value
	 * should be the name of the annotated variable.
	 * </p>
	 * 
	 * @param lOp
	 *            The variable name
	 */
	void setLeftOp(String lOp);
}
