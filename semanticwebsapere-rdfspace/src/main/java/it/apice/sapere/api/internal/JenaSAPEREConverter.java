package it.apice.sapere.api.internal;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.values.PropertyValue;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * Utilty class which provides translation from Jena to SAPERE model and
 * backward.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class JenaSAPEREConverter {

	/** URI of rdf:type property. */
	private static final transient String RDF_TYPE_PROP = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/** URI of sapere:LSA class. */
	private static final transient String LSA_CLASS = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#LSA";

	/** URI of sapere:isOwned class. */
	private static final transient String IS_OWNED_PROP = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#LSA";

	/** SAPERE Model Factory. */
	private final transient ExtSAPEREFactory factory;

	/**
	 * <p>
	 * Builds a new JenaLSAConverter.
	 * </p>
	 * 
	 * @param aFactory
	 *            Extended SAPERE Factory
	 */
	public JenaSAPEREConverter(final ExtSAPEREFactory aFactory) {
		factory = aFactory;
	}

	/**
	 * <p>
	 * Extracts LSAs from a model and parses them.
	 * </p>
	 * 
	 * @param model
	 *            The model to be processed
	 * @return All found LSAs
	 * @throws Exception
	 *             Cannot process
	 */
	public final Set<LSA> parseJenaModel(final Model model) throws Exception {
		final Set<LSA> res = new HashSet<LSA>();
		final ResIterator iter = model.listSubjectsWithProperty(
				model.createProperty(RDF_TYPE_PROP),
				model.createResource(LSA_CLASS));
		while (iter.hasNext()) {
			res.add(parseLSA(iter.next(), model));
		}

		return res;
	}

	/**
	 * <p>
	 * Parses the provided resource in order to extract the LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            The resource to be parsed
	 * @param model
	 *            The model from which LSAs have been extracted
	 * @return The parsed LSA
	 * @throws Exception
	 *             Invalid URI (should not occur)
	 */
	public final LSA parseLSA(final Resource lsa, final Model model)
			throws Exception {
		final Statement owner = lsa.getProperty(model
				.createProperty(IS_OWNED_PROP));
		LSA res;
		if (owner == null) {
			res = factory.createLSA(factory.createLSAid(new URI(lsa.getURI())));
		} else {
			res = factory.createLSA(factory.createLSAid(new URI(owner
					.getString())));
		}

		StmtIterator props = lsa.listProperties();
		while (props.hasNext()) {
			res.getSemanticDescription().addProperty(
					parseProperty(props.next().getPredicate()));
		}

		return res;
	}

	/**
	 * <p>
	 * Parses an LSA's property.
	 * </p>
	 * 
	 * @param prop
	 *            The property in RDF
	 * @return The LSA's property
	 * @throws Exception
	 *             Invalid information provided
	 */
	private it.apice.sapere.api.lsas.Property parseProperty(
			final com.hp.hpl.jena.rdf.model.Property prop) throws Exception {
		return factory
				.createProperty(new URI(prop.getURI()), parseValues(prop));
	}

	/**
	 * <p>
	 * Parses all LSA's property values.
	 * </p>
	 * 
	 * @param prop
	 *            The property in RDF
	 * @return The list of property values
	 * @throws Exception
	 *             Cannot parse values
	 */
	private PropertyValue<?>[] parseValues(
			final com.hp.hpl.jena.rdf.model.Property prop) throws Exception {
		final Set<PropertyValue<?>> res = new HashSet<PropertyValue<?>>();
		final StmtIterator iter = prop.listProperties(prop);
		while (iter.hasNext()) {
			final Statement stmt = iter.next();

			res.add(parseValue(stmt.getLiteral()));
		}

		return res.toArray(new PropertyValue<?>[res.size()]);
	}

	/**
	 * <p>
	 * Parses a value, determining its type.
	 * </p>
	 * 
	 * @param literal
	 *            The literal to be parsed
	 * @return The property value
	 * @throws Exception
	 *             Cannot parse value
	 */
	private PropertyValue<?> parseValue(final Literal literal) 
			throws Exception {
		final Object value = literal.getValue();
		final String langCode = literal.getLanguage();

		if (value instanceof Boolean) {
			return factory.createPropertyValue((Boolean) value);
		} else if (value instanceof Double) {
			return factory.createPropertyValue((Double) value);
		} else if (value instanceof Float) {
			return factory.createPropertyValue((Float) value);
		} else if (value instanceof Long) {
			return factory.createPropertyValue((Long) value);
		} else if (value instanceof Integer) {
			return factory.createPropertyValue((Integer) value);
		} else if (literal.isURIResource()) {
			return factory.createPropertyValue(new URI(value.toString()));
		} else if (value instanceof XSDDateTime) {
			return factory.createPropertyValue(((XSDDateTime) value)
					.asCalendar().getTime());
		} else if (value instanceof Date) {
			return factory.createPropertyValue((Date) value);
		}

		// Using String representation..
		if (langCode != null) {
			return factory.createPropertyValue(literal.getString(), langCode);
		}

		return factory.createPropertyValue(literal.getString());
	}

	/**
	 * <p>
	 * Serializes an LSA into a Jena Resource.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be serialized
	 * @param model
	 *            Jena model
	 * @return The serialized Resource
	 */
	public final Resource serializeLSA(final LSA lsa, final Model model) {
		final Resource res = model.createResource(lsa.getLSAId().getId()
				.toString());

		for (Property prop : lsa.getSemanticDescription().properties()) {
			final com.hp.hpl.jena.rdf.model.Property sProp = model
					.createProperty(prop.getName().getValue().toString());

			for (PropertyValue<?> val : prop.values()) {
				if (val.hasLocale() && val.isLiteral()) {
					res.addProperty(
							sProp,
							model.createLiteral(val.getValue().toString(),
									val.getLanguageCode()));
				} else if (val.getValue() instanceof Date) {
					final Calendar cal = Calendar.getInstance();
					cal.setTime((Date) val.getValue());
					res.addProperty(sProp, model.createTypedLiteral(cal));
				} else {
					res.addProperty(sProp,
							model.createTypedLiteral(val.getValue()));
				}
			}
		}

		return res;
	}
}
