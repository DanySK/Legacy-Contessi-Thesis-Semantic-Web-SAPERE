package it.apice.sapere.node.networking.obsnotif.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.node.agents.networking.Message;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.observation.SpaceOperationType;

import java.io.Serializable;

/**
 * <p>
 * This message represents a notification of an event occurred in a (remote)
 * LSA-space.
 * </p>
 * 
 */
public class Notification extends Message implements Serializable {

	/** Serialization ID. */
	private static final long serialVersionUID = -6417756713784371365L;

	/** The type of occurred operation. */
	private SpaceOperationType type;

	/** LSA-id of the involved LSA. */
	private LSAid subject;

	/** The involved LSA in RDF/XML. */
	private String content;

	/**
	 * <p>
	 * Builds a new {@link Notification}.
	 * </p>
	 * 
	 * @param aType
	 *            Occurred operation type
	 * @param lsa
	 *            Involved LSA
	 */
	public Notification(final SpaceOperationType aType, 
			final CompiledLSA<?> lsa) {
		type = aType;
		subject = lsa.getLSAid();
		content = lsa.toString(RDFFormat.RDF_XML);
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The {@link Notification} to be cloned
	 */
	public Notification(final Notification src) {
		type = src.type;
		subject = src.subject;
		content = src.content;
	}

	/**
	 * <p>
	 * Retrieves the type of the operation occurred in the LSA-space.
	 * </p>
	 * 
	 * @return The type of the occurred operation
	 */
	public final SpaceOperationType getType() {
		return type;
	}

	/**
	 * <p>
	 * Clones the {@link Notification}.
	 * </p>
	 * 
	 * @return The cloned Notification
	 */
	public final Notification getCopy() {
		return new Notification(this);
	}

	/**
	 * <p>
	 * Retrieves the LSA as a RDF/XML String.
	 * </p>
	 * 
	 * @return A RDF/XML String representing the LSA
	 */
	public final String getLSA() {
		return content;
	}

	/**
	 * <p>
	 * Retrieves the LSA-id of the notification subject.
	 * </p>
	 * 
	 * @return The LSA-id of the subject
	 */
	public final LSAid getSubjectLSAid() {
		return subject;
	}

	@Override
	public final String toString() {
		if (content != null) {
			return "Notify[type=" + type + " subject=" + subject
					+ " newContent=" + content + "]";
		} else {
			return "Notify[type=" + type + " subject=" + subject + "]";
		}
	}
}
