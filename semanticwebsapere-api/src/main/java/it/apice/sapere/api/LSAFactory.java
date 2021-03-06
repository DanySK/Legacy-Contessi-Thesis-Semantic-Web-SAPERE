package it.apice.sapere.api;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.BooleanValue;
import it.apice.sapere.api.lsas.values.DateTimeValue;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.FloatValue;
import it.apice.sapere.api.lsas.values.IntegerValue;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.LiteralValue;
import it.apice.sapere.api.lsas.values.LongValue;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.SDescValue;
import it.apice.sapere.api.lsas.values.URIValue;

import java.net.URI;
import java.util.Date;

/**
 * <p>
 * An LSAFactory provides basic functionalities that enables the creation of
 * LSAs according to the SAPERE model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAFactory {

	/* === Node Unique identifier. === */

	/**
	 * <p>
	 * Retrieves a String which is a system-wide node identifier.
	 * </p>
	 * 
	 * @return The node ID
	 */
	String getNodeID();

	/* === LSAS === */

	/**
	 * <p>
	 * Creates a new synthetic LSA instance.
	 * </p>
	 * 
	 * @return A fresh LSA
	 */
	LSA createLSA();

	/* === LSA'S PROPERTIES === */

	/**
	 * <p>
	 * Creates a new Property.
	 * </p>
	 * <p>
	 * For the sake of contextualization this property should be inserted in an
	 * LSA's Semantic Description.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property
	 * @return A fresh property
	 */
	Property createProperty(URI name);

	/**
	 * <p>
	 * Creates a new Property.
	 * </p>
	 * <p>
	 * For the sake of contextualization this property should be inserted in an
	 * LSA's Semantic Description.
	 * </p>
	 * 
	 * @param name
	 *            The name of the property
	 * @param initialValues
	 *            Initial property values
	 * @return A fresh property
	 */
	Property createProperty(URI name, PropertyValue<?, ?>... initialValues);

	/* === LSA'S PROPERTY VALUES === */

	/**
	 * <p>
	 * Creates a new Property Value starting from an URI.
	 * </p>
	 * 
	 * @param value
	 *            A valid URI
	 * @return A fresh property value
	 */
	URIValue createPropertyValue(URI value);

	/**
	 * <p>
	 * Creates a new Property Value starting from an LSA-id.
	 * </p>
	 * 
	 * @param value
	 *            A valid LSA-id
	 * @return A fresh property value
	 */
	LSAidValue createPropertyValue(LSAid value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a String.
	 * </p>
	 * <p>
	 * The resulting value is considered a Literal.
	 * </p>
	 * 
	 * @param value
	 *            A String
	 * @return A fresh property value
	 */
	LiteralValue createPropertyValue(String value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a String.
	 * </p>
	 * <p>
	 * The resulting value is considered a Literal.
	 * </p>
	 * 
	 * @param value
	 *            A String
	 * @param languageCode
	 *            A recognizable language code, that will be useful in order to
	 *            understand the provided String
	 * @return A fresh property value
	 */
	LiteralValue createPropertyValue(String value, String languageCode);

	/**
	 * <p>
	 * Creates a new Property Value starting from a number.
	 * </p>
	 * 
	 * @param value
	 *            An integer
	 * @return A fresh property value
	 */
	IntegerValue createPropertyValue(int value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a number.
	 * </p>
	 * 
	 * @param value
	 *            A long
	 * @return A fresh property value
	 */
	LongValue createPropertyValue(long value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a boolean.
	 * </p>
	 * 
	 * @param value
	 *            A boolean
	 * @return A fresh property value
	 */
	BooleanValue createPropertyValue(boolean value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a number.
	 * </p>
	 * 
	 * @param value
	 *            An float
	 * @return A fresh property value
	 */
	FloatValue createPropertyValue(float value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a number.
	 * </p>
	 * 
	 * @param value
	 *            An double
	 * @return A fresh property value
	 */
	DoubleValue createPropertyValue(double value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a date.
	 * </p>
	 * 
	 * @param value
	 *            A date
	 * @return A fresh property value
	 */
	DateTimeValue createPropertyValue(Date value);

	/**
	 * <p>
	 * Creates a new Property Value starting from a semantic description.
	 * </p>
	 * 
	 * @return A fresh property value
	 */
	SDescValue createNestingPropertyValue();

	/* === LSA-ID === */

	/**
	 * <p>
	 * Creates a new LSA-id.
	 * </p>
	 * 
	 * @return A fresh LSA-id
	 */
	LSAid createLSAid();

	/* === LSA'S PROPERTY NAMES === */

	/**
	 * <p>
	 * Creates a new Property Name.
	 * </p>
	 * 
	 * @param value
	 *            The URI which is the LSA-id
	 * @return A fresh PropertyName
	 */
	PropertyName createPropertyName(URI value);
	//
	// /* === AGENTS === */
	//
	// /**
	// * <p>
	// * Creates a new Agent.
	// * </p>
	// *
	// * @param id
	// * An URI which identifies the agent
	// * @param behav
	// * A behaviour for the agent
	// * @return A fresh SAPERE Agent
	// */
	// SAPEREAgent createAgent(URI id, SAPEREAgentBehaviour behav);

}