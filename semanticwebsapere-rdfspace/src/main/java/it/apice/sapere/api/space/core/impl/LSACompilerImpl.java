package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.lsas.visitors.impl.ToJenaVisitorImpl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * This class provides an implementation of the {@link LSACompiler} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class LSACompilerImpl implements LSACompiler<StmtIterator> {

	@Override
	public final CompiledLSA<StmtIterator> compile(final LSA lsa) {
		final Model tmp = ModelFactory.createDefaultModel();
		new ToJenaVisitorImpl(tmp).visit(lsa);

		return new CompiledLSAImpl(lsa.getLSAId(), tmp.listStatements());
	}

}
