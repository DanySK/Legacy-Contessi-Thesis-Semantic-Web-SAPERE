package it.apice.sapere.api.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.internal.AeSimpleMD5;
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

import java.net.URI;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Implementation of ExtSAPEREFactory.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see PrivilegedLSAFactory
 * 
 */
public class LSAFactoryImpl implements PrivilegedLSAFactory {

	/** Prefix for automatically generated URIs. */
	private static final transient String URI_PREFIX = "http://www.sapere"
			+ "-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Identifier of this factory (one per node). */
	private final transient String factoryId;

	/** Identifier of the node on which the factory is running. */
	private final transient String nodeId;

	/** Counts generated ids. */
	private final transient AtomicInteger idCounter = new AtomicInteger();

	/**
	 * Factory default constructor.
	 */
	public LSAFactoryImpl() {
		this(String.format("%snode%s", URI_PREFIX, UUID.randomUUID()));
	}

	/**
	 * <p>
	 * Builds a new SAPEREFactoryImpl.
	 * </p>
	 * 
	 * @param uniqueId
	 *            A system-wide unique id (this will identify also the node)
	 */
	public LSAFactoryImpl(final String uniqueId) {
		if (uniqueId == null) {
			throw new IllegalArgumentException("Invalid node");
		}

		try {
			nodeId = uniqueId;
			factoryId = hash(uniqueId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Unforseen situation occurred", e);
		}
	}

	@Override
	public final String getNodeID() {
		return nodeId;
	}

	@Override
	public final LSA createLSA() {
		return new LSAImpl(generateLSAid());
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
	public final LSAid createLSAid(final URI id) {
		return new LSAidImpl(id);
	}

	@Override
	public final LSA createLSA(final LSAid lsaId) {
		if (lsaId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		return new LSAImpl(lsaId);
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
			// final LSAid id =
			return new LSAidImpl(new URI(URI_PREFIX + "lsa"
					+ String.format("%X", idCounter.getAndIncrement()) + "-"
					+ factoryId));
			// System.out.println(id);
			// return id;
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
	 * @throws Exception
	 *             Cannot calculate the hash (no algorithm or invalid encoding)
	 */
	private String hash(final String content) throws Exception {
		return AeSimpleMD5.calculateMD5(content);
	}

}
