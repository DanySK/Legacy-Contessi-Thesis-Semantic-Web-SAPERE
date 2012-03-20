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
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

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
		System.out.println("\nLSA " + lsa.getURI() + ":");
		final Statement owner = lsa.getProperty(model
				.createProperty(IS_OWNED_PROP));
		LSA res;
		if (owner == null) {
			res = factory.createLSA(factory.createLSAid(new URI(lsa.getURI())));
		} else {
			res = factory.createLSA(factory.createLSAid(new URI(owner
					.getString())));
		}

		populateLSA(res, lsa, model);

		return res;
	}

	/**
	 * <p>
	 * Inspects the model, to populate the LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            The lsa to be populated
	 * @param res
	 *            The resource representing the LSA in the model
	 * @param model
	 *            The model to inspected
	 * @throws Exception
	 *             Something went wrong during model navigation
	 */
	private void populateLSA(final LSA lsa, final Resource res,
			final Model model) throws Exception {
		final ResultSet iter = execQuery(model, lsaPropsQuery(res));
		while (iter.hasNext()) {
			final Resource curr = iter.next().getResource(propVar());
			lsa.getSemanticDescription().addProperty(
					parseProperty(model, res, curr.getURI()));
		}
	}

	/**
	 * <p>
	 * Retrieves the name of the query's prop variable.
	 * </p>
	 * 
	 * @return Name of the prop var
	 */
	private String propVar() {
		return "?prop";
	}

	/**
	 * <p>
	 * Retrieves the name of the query's obj variable.
	 * </p>
	 * 
	 * @return Name of the obj var
	 */
	private String objVar() {
		return "?obj";
	}

	/**
	 * <p>
	 * Retrieves the text of the SPARQL query which will retrieve LSA's
	 * properties.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be inspected
	 * @return The query string
	 */
	private String lsaPropsQueryText(final Resource lsa) {
		return "SELECT DISTINCT " + propVar() + " WHERE { <" + lsa.getURI()
				+ "> " + propVar() + " " + objVar() + " }";
	}

	/**
	 * <p>
	 * Retrieves the text of the SPARQL query which will retrieve values of
	 * LSA's properties.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be inspected
	 * @param propURI
	 *            The property whose values should be retrieved
	 * @return The query string
	 */
	private String lsaPropsValuesQueryText(final Resource lsa,
			final String propURI) {
		return "SELECT DISTINCT " + objVar() + " WHERE { <" + lsa.getURI()
				+ "> <" + propURI + "> " + objVar() + " }";
	}

	/**
	 * <p>
	 * Creates a new query that retrieves LSA's properties.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA whose properties will be retrieved
	 * @return The query object
	 */
	private Query lsaPropsQuery(final Resource lsa) {
		return QueryFactory.create(lsaPropsQueryText(lsa));
	}

	/**
	 * <p>
	 * Creates a new query which will retrieve values of LSA's properties.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA to be inspected
	 * @param propURI
	 *            The property whose values should be retrieved
	 * @return The query object
	 */
	private Query lsaPropsValuesQuery(final Resource lsa, 
			final String propURI) {
		return QueryFactory.create(lsaPropsValuesQueryText(lsa, propURI));
	}

	/**
	 * <p>
	 * Executes a SPARQL query on provided model.
	 * </p>
	 * 
	 * @param model
	 *            The queried model
	 * @param query
	 *            The query to be executed
	 * @return Query's result set
	 */
	private ResultSet execQuery(final Model model, final Query query) {
		return QueryExecutionFactory.create(query, model).execSelect();
	}

	/**
	 * <p>
	 * Parses an LSA's property.
	 * </p>
	 * 
	 * @param res
	 *            The resource to be inspected
	 * @param model
	 *            The model on which work
	 * 
	 * @param propURI
	 *            The property in RDF
	 * @return The LSA's property
	 * @throws Exception
	 *             Invalid information provided
	 */
	private it.apice.sapere.api.lsas.Property parseProperty(final Model model,
			final Resource res, final String propURI) throws Exception {
		return factory.createProperty(new URI(propURI),
				parseValues(model, res, propURI));
	}

	/**
	 * <p>
	 * Parses all LSA's property values.
	 * </p>
	 * 
	 * @param lsa
	 *            The LSA on which work
	 * @param model
	 *            The model on which work
	 * @param propURI
	 *            The property in RDF
	 * @return The list of property values
	 * @throws Exception
	 *             Cannot parse values
	 */
	private PropertyValue<?>[] parseValues(final Model model,
			final Resource lsa, final String propURI) throws Exception {
		final Set<PropertyValue<?>> res = new HashSet<PropertyValue<?>>();
		final ResultSet iter = execQuery(model,
				lsaPropsValuesQuery(lsa, propURI));
		while (iter.hasNext()) {
			final QuerySolution curr = iter.next();
			final RDFNode rVal = curr.get(objVar());

			if (rVal.isURIResource()) {
				res.add(factory.createPropertyValue(new URI(rVal.asResource()
						.getURI())));
			} else if (rVal.isLiteral()) {
				res.add(parseValue(rVal.asLiteral()));
			}
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
