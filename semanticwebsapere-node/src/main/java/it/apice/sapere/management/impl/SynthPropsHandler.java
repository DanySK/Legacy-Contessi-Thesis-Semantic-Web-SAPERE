package it.apice.sapere.management.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.strategy.CustomStrategyPipelineStep;

import java.net.URI;
import java.util.Calendar;

/**
 * <p>
 * This class adds Synthetic Properties management to the SAPERE-node.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
@SuppressWarnings("rawtypes")
public class SynthPropsHandler implements CustomStrategyPipelineStep {

	/** SAPERE namespace. */
	private static final transient String SAPERE_NS = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Creation Time property URI. */
	private static final transient URI CREATION_TIME_PROP = URI
			.create(SAPERE_NS + "creationTime");

	/** Last Modified property URI. */
	private static final transient URI LAST_MOD_PROP = URI.create(SAPERE_NS
			+ "lastModified");

	/** LSA's location property URI. */
	private static final transient URI LOCATION_PROP = URI.create(SAPERE_NS
			+ "location");

	/** Local location property value URI. */
	private static final transient URI LOCAL_VAL = URI.create(SAPERE_NS
			+ "local");

	@Override
	public final CompiledLSA handleInject(final CompiledLSA lsa)
			throws SAPEREException {
		lsa.clearProperty(LOCATION_PROP);
		lsa.assertProperty(LOCATION_PROP, LOCAL_VAL);

		final Calendar now = Calendar.getInstance();
		lsa.clearProperty(CREATION_TIME_PROP);
		lsa.assertProperty(CREATION_TIME_PROP, now.toString());
		lsa.clearProperty(LAST_MOD_PROP);
		lsa.assertProperty(LAST_MOD_PROP, now.toString());

		return lsa;
	}

	@Override
	public final LSAid handleRead(final LSAid lsaId) throws SAPEREException {
		return lsaId;
	}

	@Override
	public final CompiledLSA handleRemove(final CompiledLSA lsa)
			throws SAPEREException {
		return lsa;
	}

	@Override
	public final CompiledLSA handleUpdate(final CompiledLSA lsa)
			throws SAPEREException {
		lsa.clearProperty(LAST_MOD_PROP);
		lsa.assertProperty(LAST_MOD_PROP, Calendar.getInstance().toString());

		return lsa;
	}

}
