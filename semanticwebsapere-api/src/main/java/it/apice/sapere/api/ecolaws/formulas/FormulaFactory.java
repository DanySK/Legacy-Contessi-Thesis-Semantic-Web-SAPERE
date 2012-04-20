package it.apice.sapere.api.ecolaws.formulas;


/**
 * <p>
 * This interface models a factory, capable of creating a Formula for
 * AnnotatedVarTerm.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface FormulaFactory {

	/**
	 * <p>
	 * Creates a new IsFormula.
	 * </p>
	 * 
	 * @param rOpString
	 *            Representation as a String of the Right Operand
	 * @param rOp
	 *            Implementation of the Right Operand
	 * @param <Type>
	 *            Inner formula type
	 * @return The formula
	 */
	<Type> IsFormula<Type> createIsFormula(String rOpString,
			RightOperand<Type> rOp);

	/**
	 * <p>
	 * Creates a new IsFormula.
	 * </p>
	 * 
	 * @param rOpString
	 *            Representation as a String of the Right Operand
	 * @param rOp
	 *            Implementation of the Right Operand
	 * @param <Type>
	 *            Inner formula type
	 * @return The formula
	 */
	<Type extends Comparable<Type>> GtFormula<Type> createGtFormula(
			String rOpString, RightOperand<Type> rOp);

	/**
	 * <p>
	 * Creates a new IsFormula.
	 * </p>
	 * 
	 * @param rOpString
	 *            Representation as a String of the Right Operand
	 * @param rOp
	 *            Implementation of the Right Operand
	 * @param <Type>
	 *            Inner formula type
	 * @return The formula
	 */
	<Type extends Comparable<Type>> GtEqFormula<Type> createGtEqFormula(
			String rOpString, RightOperand<Type> rOp);

	/**
	 * <p>
	 * Creates a new IsFormula.
	 * </p>
	 * 
	 * @param rOpString
	 *            Representation as a String of the Right Operand
	 * @param rOp
	 *            Implementation of the Right Operand
	 * @param <Type>
	 *            Inner formula type
	 * @return The formula
	 */
	<Type extends Comparable<Type>> LtFormula<Type> createLtFormula(
			String rOpString, RightOperand<Type> rOp);

	/**
	 * <p>
	 * Creates a new IsFormula.
	 * </p>
	 * 
	 * @param rOpString
	 *            Representation as a String of the Right Operand
	 * @param rOp
	 *            Implementation of the Right Operand
	 * @param <Type>
	 *            Inner formula type
	 * @return The formula
	 */
	<Type extends Comparable<Type>> LtEqFormula<Type> createLtEqFormula(
			String rOpString, RightOperand<Type> rOp);
}
