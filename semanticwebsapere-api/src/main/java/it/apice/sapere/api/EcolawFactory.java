package it.apice.sapere.api;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.PropertyValue;

/**
 * <p>
 * An EcolawFactory provides basic functionalities that enables the creation of
 * Ecolaws according to the SAPERE model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface EcolawFactory {

	/* === ECOLAWS === */

	/**
	 * <p>
	 * Creates a new Ecolaw with ASAP Rate.
	 * </p>
	 * 
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw();

	/**
	 * <p>
	 * Creates a new Ecolaw with ASAP Rate.
	 * </p>
	 * 
	 * @param label
	 *            The label of the ecolaw
	 * @return A fresh Ecolaw
	 */
	Ecolaw createEcolaw(String label);

	/* === CHEMICAL PATTERNS === */

	/**
	 * <p>
	 * Creates a new Reactant.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @return A fresh Reactant
	 */
	Reactant createReactant(String name);

	/**
	 * <p>
	 * Creates a new Reactant.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern (as a term)
	 * @return A fresh Reactant
	 */
	Reactant createReactant(PatternNameTerm name);

	/**
	 * <p>
	 * Creates a new Product.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern
	 * @return A fresh Product
	 */
	Product createProduct(String name);

	/**
	 * <p>
	 * Creates a new Product.
	 * </p>
	 * 
	 * @param name
	 *            The name of the pattern (as a term)
	 * @return A fresh Product
	 */
	Product createProduct(PatternNameTerm name);

	/* === RATES === */

	/**
	 * <p>
	 * Creates a new Ecolaw scheduling rate which asserts that the law should be
	 * scheduled according to Continuous-time Markov Chains (CTMC).
	 * </p>
	 * 
	 * @param rate
	 *            The actual rate
	 * @return A Markovian Rate
	 */
	Rate<?> createMarkovianRate(double rate);

	/**
	 * <p>
	 * Creates a new Ecolaw scheduling rate which asserts that the law should be
	 * scheduled "As Soon As Possible".
	 * </p>
	 * 
	 * @return An ASAP Rate
	 */
	Rate<?> createASAPRate();

	/* === TERMS === */

	/**
	 * <p>
	 * Creates a new Ecolaw term for an unconstrained variable.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param name
	 *            Name of the variable
	 * @return A fresh Term
	 */
	<Type> VarTerm<Type> createVarTerm(String name);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an annotated variable.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param name
	 *            Name of the variable
	 * @param formula
	 *            Boolean condition for assignment acceptance
	 * @return A fresh Term
	 */
	<Type> AnnotatedVarTerm<Type> createAnnotatedVarTerm(String name,
			Formula<Type> formula);

	/**
	 * <p>
	 * Creates an Ecolaw term for a specific LSA property value (whose type is
	 * specified).
	 * </p>
	 * 
	 * @param <Type>
	 *            The inner type of the PropertyValue
	 * @param value
	 *            A Value
	 * @return A fresh term
	 */
	<Type extends PropertyValue<?>> ValueTerm<Type> createTypedValueTerm(
			PropertyValue<Type> value);

	/**
	 * <p>
	 * Creates an Ecolaw term for a specific LSA property value.
	 * </p>
	 * 
	 * @param value
	 *            A Value
	 * @return A fresh term
	 */
	ValueTerm<?> createValueTerm(PropertyValue<?> value);

	/**
	 * <p>
	 * Creates a new Ecolaw term for a list of values.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param values
	 *            PropertyValues that compose the list
	 * @return A fresh Term
	 */
	<Type extends PropertyValue<?>> ListTerm<Type> createListTerm(
			Type... values);

	/**
	 * <p>
	 * Creates a new Ecolaw term for a list of values.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param values
	 *            Terms that compose the list
	 * @return A fresh Term
	 */
	<Type extends PropertyValue<?>> ListTerm<Type> createListTerm(
			ValueTerm<Type>... values);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an unconstrained variable which will point
	 * to a list of values.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param name
	 *            Name of the variable
	 * @return A fresh Term
	 */
	<Type extends PropertyValue<?>> ListTerm<Type> createListTerm(String name);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an annotated variable which will point to a
	 * list of values.
	 * </p>
	 * 
	 * @param <Type>
	 *            Type of term's inner value
	 * @param name
	 *            Name of the variable
	 * @param formula
	 *            Boolean condition for assignment acceptance
	 * @return A fresh Term
	 */
	<Type extends PropertyValue<?>> ListTerm<Type> createListTerm(String name,
			Formula<Type> formula);

	/**
	 * <p>
	 * Creates a new Ecolaw term for a Property name.
	 * </p>
	 * 
	 * @param pname
	 *            The Property Name
	 * @return A fresh Term
	 */
	PropertyTerm createPropertyTerm(PropertyName pname);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an unconstrained variable which will point
	 * to a Property name.
	 * </p>
	 * 
	 * @param name
	 *            Name of the variable
	 * @return A fresh Term
	 */
	PropertyTerm createPropertyTerm(String name);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an annotated variable which will point to a
	 * Property name.
	 * </p>
	 * 
	 * @param name
	 *            Name of the variable
	 * @param formula
	 *            Boolean condition for assignment acceptance
	 * @return A fresh Term
	 */
	PropertyTerm createPropertyTerm(String name, Formula<PropertyName> formula);

	/**
	 * <p>
	 * Creates a new Ecolaw term for a Property name.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA whose Semantic Description should be copied
	 * @return A fresh Term
	 */
	SDescTerm createSDescTerm(LSA lsa);

	/**
	 * <p>
	 * Creates a new Ecolaw term for a Property name.
	 * </p>
	 * 
	 * @param sdesc
	 *            The LSA's Semantic Description
	 * @return A fresh Term
	 */
	SDescTerm createSDescTerm(SemanticDescription sdesc);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an unconstrained variable which will point
	 * to an LSA's Semantic Description.
	 * </p>
	 * 
	 * @param name
	 *            Name of the variable
	 * @return A fresh Term
	 */
	SDescTerm createSDescTerm(String name);

	/**
	 * <p>
	 * Creates a new Ecolaw term for an annotated variable which will point to
	 * an LSA's Semantic Description.
	 * </p>
	 * 
	 * @param name
	 *            Name of the variable
	 * @param formula
	 *            Boolean condition for assignment acceptance
	 * @return A fresh Term
	 */
	SDescTerm createSDescTerm(String name, Formula<PropertyName> formula);
}
