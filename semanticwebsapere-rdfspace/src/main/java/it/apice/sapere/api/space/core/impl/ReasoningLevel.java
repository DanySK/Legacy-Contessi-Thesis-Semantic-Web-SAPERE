package it.apice.sapere.api.space.core.impl;

/**
 * <p>
 * This enumeration lists all possible level of reasoning that the space can
 * handle.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public enum ReasoningLevel {
	/** No reasoning. */
	NONE,

	/** Simple reasoning on RDFS. */
	RDFS_INF,

	/** Maximum reasoning capabilities: OWL DL. */
	OWL_DL;
}
