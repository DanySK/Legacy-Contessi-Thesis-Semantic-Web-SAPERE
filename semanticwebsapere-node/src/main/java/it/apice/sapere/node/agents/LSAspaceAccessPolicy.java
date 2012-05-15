package it.apice.sapere.node.agents;

import it.apice.sapere.api.space.LSAspace;

/**
 * <p>
 * This interface models a proxy capable of filtering requests to the LSA-space
 * and forward only the allowed ones.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface LSAspaceAccessPolicy extends LSAspace {

	/**
	 * <p>
	 * Sets the agent which will forward requests to the LSA-space that should
	 * be checked by this policy.
	 * </p>
	 * 
	 * @param aReq
	 *            The agent whose actions are under control
	 */
	void setRequestor(SAPEREAgent aReq);
}
