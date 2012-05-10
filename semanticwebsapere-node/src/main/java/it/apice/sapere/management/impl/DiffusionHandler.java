package it.apice.sapere.management.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.core.CompiledLSA;
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

	@Override
	public final void eventOccurred(final SpaceEvent ev) {
		for (String rdfLsa : ev.getLSAContent(RDFFormat.RDF_XML)) {
			final CompiledLSA<?> lsa = NodeServicesImpl.getInstance()
					.getLSACompiler().parse(rdfLsa, RDFFormat.RDF_XML);

			try {
				NetworkManager.getInstance().doDiffuse(
						getLSALocation(lsa),
						new NodeMessage(NodeMessageType.DIFFUSE, null,
								new SpaceOperation(
										SpaceOperationType.SYSTEM_DIFFUSE, lsa,
										"system"), 0, 0, new float[0]));
				// TODO find if latitude, longitude and orientation can be
				// provided (not 0, 0, new float[0])
				LoggerFactoryImpl.getInstance()
						.getLogger(DiffusionHandler.class)
						.info("DIFFUSE " + lsa.getLSAid());
			} catch (SAPEREException ex) {
				LoggerFactoryImpl
						.getInstance()
						.getLogger(DiffusionHandler.class)
						.debug(String.format(
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
		assert lsa != null;
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
