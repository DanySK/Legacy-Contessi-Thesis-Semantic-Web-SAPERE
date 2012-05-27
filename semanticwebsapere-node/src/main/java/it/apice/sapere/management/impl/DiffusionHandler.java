package it.apice.sapere.management.impl;

import java.net.URI;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.node.internal.LoggerFactoryImpl;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.impl.NetworkManager;
import it.apice.sapere.node.networking.impl.NodeMessage;
import it.apice.sapere.node.networking.impl.NodeMessageType;
import it.apice.sapere.node.networking.utils.impl.SpaceOperation;

/**
 * <p>
 * This class models an entity responsible for {@link NetworkManager}
 * triggering, according to LSA diffusion logic.
 * </p>
 * <p>
 * This entity has been conceived as a {@link SpaceObserver} in order to ensure
 * that an LSA is diffused as soon as some agent modifies its location.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DiffusionHandler implements SpaceObserver {

	/** SAPERE namespace. */
	private static final transient String SAPERE_NS = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** LSA's location property URI. */
	private static final transient URI LOCATION_PROP = URI.create(SAPERE_NS
			+ "location");

	/** Local location property value URI. */
	private static final transient URI LOCAL_VAL = URI.create(SAPERE_NS
			+ "local");

	/** Reference to monitored LSA-space. */
	@SuppressWarnings("rawtypes")
	private final transient LSAspaceCore space;

	/**
	 * <p>
	 * Builds a new {@link DiffusionHandler}.
	 * </p>
	 * 
	 * @param lsaSpace
	 *            Reference to monitored LSA-space
	 */
	public DiffusionHandler(final LSAspaceCore<?> lsaSpace) {
		if (lsaSpace == null) {
			throw new IllegalArgumentException("Invalid LSA-space provided");
		}

		space = lsaSpace;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void eventOccurred(final SpaceEvent ev) {
		// Checks only events caused by an eco-law application (because it is
		// the only way to change a synthetic property
		if (!ev.getOperationType().equals(SpaceOperationType.SYSTEM_ACTION)) {
			return;
		}

		for (String rdfLsa : ev.getLSAContent(RDFFormat.RDF_XML)) {
			@SuppressWarnings("rawtypes")
			final CompiledLSA lsa = NodeServicesImpl.getInstance()
					.getLSACompiler().parse(rdfLsa, RDFFormat.RDF_XML);
			LoggerFactoryImpl.getInstance().getLogger(DiffusionHandler.class)
					.spy("CHECKING LSA for DIFFUSION:\n" + lsa.toString());

			try {
				// Enact diffusion (only if required)
				NetworkManager.getInstance().doDiffuse(
						getLSALocation(lsa),
						new NodeMessage(NodeMessageType.DIFFUSE, null,
								new SpaceOperation(
										SpaceOperationType.SYSTEM_DIFFUSE, lsa,
										"system"), 0, 0, new Float[0]));

				// Remove diffused LSA from the LSA-space
				space.remove(lsa);
				LoggerFactoryImpl.getInstance()
						.getLogger(DiffusionHandler.class)
						.log("DIFFUSE " + lsa.getLSAid());
			} catch (SAPEREException ex) {
				LoggerFactoryImpl
						.getInstance()
						.getLogger(DiffusionHandler.class)
						.spy(String.format(
								"LSA <%s> does not need to be diffused",
								lsa.getLSAid()));
			} catch (Exception ex) {
				LoggerFactoryImpl.getInstance()
						.getLogger(DiffusionHandler.class)
						.error("DIFFUSE failed", ex);
			}
		}
	}

	/**
	 * <p>
	 * Extracts the LSA location.
	 * </p>
	 * 
	 * @param lsa
	 *            Candidate LSA for diffusion
	 * @return The diffusion destination
	 * @throws SAPEREException
	 *             No diffusion required
	 */
	private String getLSALocation(final CompiledLSA<?> lsa)
			throws SAPEREException {
		final URI loc = lsa.readURIProperty(LOCATION_PROP)[0];
		if (loc.equals(LOCAL_VAL)) {
			throw new SAPEREException("Should not diffuse");
		}

		return loc.toString();
	}

}