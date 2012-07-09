package it.apice.sapere.management.impl;

import it.apice.api.node.logging.impl.LoggerFactoryImpl;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.observation.SpaceEvent;
import it.apice.sapere.api.space.observation.SpaceObserver;
import it.apice.sapere.api.space.observation.SpaceOperationType;
import it.apice.sapere.node.internal.NodeServicesImpl;
import it.apice.sapere.node.networking.impl.NodeMessage;
import it.apice.sapere.node.networking.impl.NodeMessageType;
import it.apice.sapere.node.networking.manager.NetworkManager;
import it.apice.sapere.node.networking.manager.impl.NetworkManagerImpl;
import it.apice.sapere.node.networking.utils.impl.SpaceOperation;

import java.net.URI;

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

	/** Id of the node (used as sender id). */
	private final transient String senderId;

	/** Reference to monitored LSA-space. */
	@SuppressWarnings("rawtypes")
	private final transient LSAspaceCore space;

	/** Reference to {@link NetworkManager}. */
	private final transient NetworkManager manager;

	/** Flag which enables diffused LSA removal. */
	private final transient boolean _shouldRemove;

	/**
	 * <p>
	 * Builds a new {@link DiffusionHandler}.
	 * </p>
	 * 
	 * @param lsaSpace
	 *            Reference to monitored LSA-space
	 * @param nodeId
	 *            ID of this node
	 * @param shouldRemove
	 *            True in order to remove the diffused LSA, false otherwise
	 */
	public DiffusionHandler(final LSAspaceCore<?> lsaSpace,
			final String nodeId, final boolean shouldRemove) {
		if (lsaSpace == null) {
			throw new IllegalArgumentException("Invalid LSA-space provided");
		}

		space = lsaSpace;
		senderId = nodeId;
		manager = NetworkManagerImpl.getInstance();
		_shouldRemove = shouldRemove;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void eventOccurred(final SpaceEvent ev) {
		// Checks only events caused by an eco-law application (because it is
		// the only way to change a synthetic property) or LSA update
		if (!(ev.getOperationType().equals(SpaceOperationType.SYSTEM_APPLY) 
				|| ev.getOperationType().equals(
						SpaceOperationType.AGENT_UPDATE))) {
			return;
		}

		for (String rdfLsa : ev.getLSAContent(RDFFormat.RDF_XML)) {
			try {
				@SuppressWarnings("rawtypes")
				final CompiledLSA lsa = NodeServicesImpl.getInstance()
						.getLSACompiler().parse(rdfLsa, RDFFormat.RDF_XML);
				LoggerFactoryImpl.getInstance()
						.getLogger(DiffusionHandler.class)
						.spy("CHECKING LSA for DIFFUSION:\n" + lsa.toString());
				boolean done = false;
				for (URI loc : getLSALocation(lsa)) {
					try {
						if (loc.equals(LOCAL_VAL)) {
							throw new SAPEREException("Should not diffuse");
						}

						// Enact diffusion
						manager.diffuse(loc, new NodeMessage(
								NodeMessageType.DIFFUSE, senderId,
								new SpaceOperation(
										SpaceOperationType.SYSTEM_DIFFUSE, lsa,
										"system"), 0, 0, new Float[0]));
						
						done = true;

						LoggerFactoryImpl
								.getInstance()
								.getLogger(DiffusionHandler.class)
								.log("DIFFUSEd " + lsa.getLSAid() + " to "
										+ loc);
					} catch (SAPEREException ex) {
//						LoggerFactoryImpl
//								.getInstance()
//								.getLogger(DiffusionHandler.class)
//								.spy(String
//								.format("LSA <%s> does not need to be diffused",
//												lsa.getLSAid()));
						assert ex != null;
					} catch (Exception ex) {
						LoggerFactoryImpl.getInstance()
								.getLogger(DiffusionHandler.class)
								.error("DIFFUSE failed", ex);
					}
				}
				
				if (_shouldRemove && done) {
					// Remove diffused LSA from the LSA-space
					space.remove(lsa);
				}
			} catch (Exception ex) {
				assert ex != null;
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
	private URI[] getLSALocation(final CompiledLSA<?> lsa)
			throws SAPEREException {
		try {
			return lsa.readURIProperty(LOCATION_PROP);
		} catch (Exception ex) {
			throw new SAPEREException(
					"This LSA DOES NOT CONTAIN LOCATION information! "
							+ "Supposed to be local");
		}
	}

}
