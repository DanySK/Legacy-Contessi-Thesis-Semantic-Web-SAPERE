package it.apice.sapere.api.node;

import it.apice.sapere.api.EcolawFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.ecolaws.formulas.FormulaFactory;
import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;

/**
 * <p>
 * Basic interface for the entity capable of retrieving a reference to each node
 * service.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface NodeServices {

	/**
	 * <p>
	 * Retrieves a reference to the LSA Factory.
	 * </p>
	 * 
	 * @return Reference to the LSA Factory
	 */
	PrivilegedLSAFactory getLSAFactory();

	/**
	 * <p>
	 * Retrieves a reference to the LSA Compiler.
	 * </p>
	 * 
	 * @return Reference to the LSA Compiler
	 */
	LSACompiler<?> getLSACompiler();

	/**
	 * <p>
	 * Retrieves a reference to the LSA Parser.
	 * </p>
	 * 
	 * @return Reference to the LSA Parser
	 */
	LSAParser getLSAParser();

	/**
	 * <p>
	 * Retrieves a reference to the Eco-law Factory.
	 * </p>
	 * 
	 * @return Reference to the Eco-law Factory
	 */
	EcolawFactory getEcolawFactory();

	/**
	 * <p>
	 * Retrieves a reference to the Eco-law Compiler.
	 * </p>
	 * 
	 * @return Reference to the Eco-law Compiler
	 */
	EcolawCompiler getEcolawCompiler();

	/**
	 * <p>
	 * Retrieves a reference to the Formula Factory.
	 * </p>
	 * 
	 * @return Reference to the Formula Factory
	 */
	FormulaFactory getFormulaFactory();

	/**
	 * <p>
	 * Retrieves a reference to the LSA-space.
	 * </p>
	 * 
	 * @return Reference to the LSA-space
	 */
	LSAspaceCore<?> getLSAspace();

	/**
	 * <p>
	 * Retrieves a reference to the Reaction Manager.
	 * </p>
	 * 
	 * @return Reference to the Reaction Manager
	 */
	ReactionManager getReactionManager();

	/**
	 * <p>
	 * Retrieves a reference to the registry of custom function that should be
	 * used during match (and apply) phases.
	 * 
	 * @return Reference to Match Functions Registry Service
	 */
	MatchFunctRegistry<?> getCustomFunctionRegistry();
}
