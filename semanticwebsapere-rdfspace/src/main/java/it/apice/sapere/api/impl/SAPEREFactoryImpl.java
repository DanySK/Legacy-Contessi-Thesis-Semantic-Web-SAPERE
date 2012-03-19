package it.apice.sapere.api.impl;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.impl.LSAImpl;
import it.apice.sapere.api.lsas.impl.LSAidImpl;
import it.apice.sapere.api.lsas.impl.PropertyImpl;
import it.apice.sapere.api.lsas.impl.PropertyNameImpl;
import it.apice.sapere.api.lsas.values.BooleanValue;
import it.apice.sapere.api.lsas.values.DateTimeValue;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.FloatValue;
import it.apice.sapere.api.lsas.values.IntegerValue;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.LiteralValue;
import it.apice.sapere.api.lsas.values.LongValue;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.URIValue;
import it.apice.sapere.api.lsas.values.impl.BooleanValueImpl;
import it.apice.sapere.api.lsas.values.impl.DateTimeValueImpl;
import it.apice.sapere.api.lsas.values.impl.DoubleValueImpl;
import it.apice.sapere.api.lsas.values.impl.FloatValueImpl;
import it.apice.sapere.api.lsas.values.impl.IntegerValueImpl;
import it.apice.sapere.api.lsas.values.impl.LSAidValueImpl;
import it.apice.sapere.api.lsas.values.impl.LiteralValueImpl;
import it.apice.sapere.api.lsas.values.impl.LongValueImpl;
import it.apice.sapere.api.lsas.values.impl.URIValueImpl;
import it.apice.sapere.api.nodes.SAPEREAgent;
import it.apice.sapere.api.nodes.SAPEREAgentBehaviour;
import it.apice.sapere.api.nodes.SAPERENode;
import it.apice.sapere.nodes.impl.SAPEREAgentImpl;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * <p>
 * Implementation of ExtSAPEREFactory.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see ExtSAPEREFactory
 * 
 */
public class SAPEREFactoryImpl implements ExtSAPEREFactory {

	/** Prefix for automatically generated URIs. */
	private static final transient String URI_PREFIX = "http://www.sapere"
			+ "-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Identifier of this factory (one per node). */
	private final transient String factoryId;

	/** Where the factory is running. */
	private final transient SAPERENode thisNode;

	/**
	 * <p>
	 * Builds a new SAPEREFactoryImpl.
	 * </p>
	 * 
	 * @param node
	 *            The node on which the factory will run (unique id)
	 */
	public SAPEREFactoryImpl(final SAPERENode node) {
		if (node == null) {
			throw new IllegalArgumentException("Invalid node");
		}

		try {
			factoryId = hash(node.getNodeId().toString());
			thisNode = node;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Error("Unforseen situation occurred", e);
		}
	}

	@Override
	public final LSA createLSA() {
		return createLSA(generateLSAid(), null);
	}

	@Override
	public final LSA createLSA(final SAPEREAgent owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Invalid owner");
		}
		return createLSA(generateLSAid(), owner);
	}

	@Override
	public final Property createProperty(final URI name) {
		return new PropertyImpl(createPropertyName(name));
	}

	@Override
	public final Property createProperty(final URI name,
			final PropertyValue<?>... initialValues) {
		final Property prop = createProperty(name);

		for (PropertyValue<?> val : initialValues) {
			prop.addValue(val);
		}

		return prop;
	}

	@Override
	public final URIValue createPropertyValue(final URI value) {
		return new URIValueImpl(value);
	}

	@Override
	public final LSAidValue createPropertyValue(final LSAid value) {
		return new LSAidValueImpl(value);
	}

	@Override
	public final LiteralValue createPropertyValue(final String value) {
		return new LiteralValueImpl(value);
	}

	@Override
	public final LiteralValue createPropertyValue(final String value,
			final String languageCode) {
		return new LiteralValueImpl(value, languageCode);
	}

	@Override
	public final IntegerValue createPropertyValue(final int value) {
		return new IntegerValueImpl(value);
	}

	@Override
	public final LongValue createPropertyValue(final long value) {
		return new LongValueImpl(value);
	}

	@Override
	public final BooleanValue createPropertyValue(final boolean value) {
		return new BooleanValueImpl(value);
	}

	@Override
	public final FloatValue createPropertyValue(final float value) {
		return new FloatValueImpl(value);
	}

	@Override
	public final DoubleValue createPropertyValue(final double value) {
		return new DoubleValueImpl(value);
	}

	@Override
	public final DateTimeValue createPropertyValue(final Date value) {
		return new DateTimeValueImpl(value);
	}

	@Override
	public final LSAid createLSAid() {
		return generateLSAid();
	}

	@Override
	public final PropertyName createPropertyName(final URI value) {
		return new PropertyNameImpl(value);
	}

	@Override
	public final SAPEREAgent createAgent(final URI id,
			final SAPEREAgentBehaviour behav) {
		return new SAPEREAgentImpl(id, thisNode, behav);
	}

	@Override
	public final LSA createLSA(final LSAid lsaId) {
		return new LSAImpl(lsaId, null);
	}

	@Override
	public final LSA createLSA(final LSAid lsaId, final SAPEREAgent owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Invalid owner");
		}

		return new LSAImpl(lsaId, owner);
	}

	@Override
	public final LSAid createLSAid(final URI id) {
		return new LSAidImpl(id);
	}

	/**
	 * <p>
	 * Generates a new unique LSA-id.
	 * </p>
	 * 
	 * @return A fresh LSA-id.
	 */
	private LSAid generateLSAid() {
		try {
			return new LSAidImpl(new URI(URI_PREFIX + "lsa" + factoryId + "-"
					+ System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Unforseen situation occurred", e);
		}
	}

	/**
	 * <p>
	 * Determines an hash string from a content.
	 * </p>
	 * 
	 * @param content
	 *            The string whose hash should be calculated
	 * @return An hash built upon content
	 * @throws NoSuchAlgorithmException
	 *             Cannot calculate the hash
	 */
	private String hash(final String content) throws NoSuchAlgorithmException {
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		digest.digest(content.getBytes());
		byte[] hash = digest.digest();

		return new String(hash);
	}
}
