package it.apice.sapere.node.networking.utils;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.io.Serializable;

/**
 * <p>
 * Class that represent an Operation that should be executed on a space.
 * </p>
 * 
 * @author Marco Santarelli
 * @author Matteo Desanti
 * 
 */
public class SpaceOperation implements Serializable {

	/** Serialization ID. */
	private static final long serialVersionUID = -199312946939588667L;

	/** Serialized LSA (RDF/XML format). */
	private final String lsa;

	/** The involved LSA-id. */
	private final LSAid lsaId;

	/** Operation type. */
	private final SpaceOperationType type;

	/** Destination IP. */
	private final String destLoc;

	/** ID of the agent that requests the operation. */
	private final String requestingId;

	/**
	 * <p>
	 * Creates a new SpaceOperation with the given parameters.
	 * </p>
	 * 
	 * @param aType
	 *            type of the operation
	 * @param aLsa
	 *            A {@link CompiledLSA} interested by the operation
	 * @param aReqId
	 *            identifier of the process that request the operation
	 */
	public SpaceOperation(final SpaceOperationType aType,
			final CompiledLSA<?> aLsa, final String aReqId) {
		this(aType, aLsa.getLSAid(), aLsa.toString(RDFFormat.RDF_XML), aReqId,
				null);
	}

	/**
	 * <p>
	 * Creates a new SpaceOperation with the given parameters.
	 * </p>
	 * 
	 * @param aType
	 *            type of the operation
	 * @param aLsa
	 *            A {@link CompiledLSA} interested by the operation
	 * @param aReqId
	 *            identifier of the process that request the operation
	 * @param ipDest
	 *            IP address for a diffuse operation
	 */
	public SpaceOperation(final SpaceOperationType aType,
			final CompiledLSA<?> aLsa, final String aReqId, 
			final String ipDest) {
		this(aType, aLsa.getLSAid(), aLsa.toString(RDFFormat.RDF_XML), aReqId,
				ipDest);
	}

	/**
	 * <p>
	 * Creates a new SpaceOperation with the given parameters.
	 * </p>
	 * 
	 * @param aType
	 *            type of the operation
	 * @param aLsaId
	 *            identifier of the LSA that the operation affect
	 * @param aLsa
	 *            the LSA that the operation affect (serialized as RDF/XML)
	 * @param aReqId
	 *            identifier of the process that request the operation
	 */
	public SpaceOperation(final SpaceOperationType aType, final LSAid aLsaId,
			final String aLsa, final String aReqId) {
		this(aType, aLsaId, aLsa, aReqId, null);
	}

	/**
	 * <p>
	 * Creates a new SpaceOperation with the given parameters.
	 * </p>
	 * 
	 * @param aType
	 *            type of the operation
	 * @param aLsaId
	 *            identifier of the LSA that the operation affect
	 * @param aLsa
	 *            the LSA that the operation affect (serialized as RDF/XML)
	 * @param aReqId
	 *            identifier of the process that request the operation
	 * @param ipDest
	 *            IP address for a diffuse operation
	 */
	public SpaceOperation(final SpaceOperationType aType, final LSAid aLsaId,
			final String aLsa, final String aReqId, final String ipDest) {
		type = aType;
		lsaId = aLsaId;
		lsa = aLsa;
		requestingId = aReqId;
		destLoc = ipDest;
	}

	/**
	 * <p>
	 * Getter for the LSA of the operation.
	 * </p>
	 * 
	 * @return The LSA (in RDF/XML Format)
	 */
	public final String getLSA() {
		return lsa;
	}

	/**
	 * <p>
	 * Getter for the LSA identifier of the operation.
	 * </p>
	 * 
	 * @return the LSA defined in this operation
	 */
	public final LSAid getLSAid() {
		return lsaId;
	}

	/**
	 * <p>
	 * Getter for the type of the operation.
	 * </p>
	 * 
	 * @return the type of the operation
	 */
	public final SpaceOperationType getType() {
		return type;
	}

	/**
	 * <p>
	 * Getter for the identifier for the diffuse location for this operation.
	 * </p>
	 * 
	 * @return the identifier of the location
	 */
	public final String getDiffuseLocation() {
		return destLoc;
	}

	/**
	 * <p>
	 * Getter for the requesting identifier for this operation.
	 * </p>
	 * 
	 * @return the requestor identifier
	 */
	public final String getRequestorId() {
		return requestingId;
	}

	@Override
	public final String toString() {
		String res = "operation[type=" + type;
		if (lsaId != null) {
			res += ",lsaId=" + lsaId;
		}
		if (lsa != null) {
			res += ",lsa=" + lsa;
		}
		if (requestingId != null) {
			res += ",requestedBy=" + requestingId;
		}

		res += "]";

		return res;
	}

}
