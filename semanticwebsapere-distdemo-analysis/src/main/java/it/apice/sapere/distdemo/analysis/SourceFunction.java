package it.apice.sapere.distdemo.analysis;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase4;

/**
 * <p>
 * This is a custom function capable of returning the provenance of given datum.
 * </p>
 * <p>
 * It checks if chosen (max) is the new observation datum and returns the
 * observation, otherwise the old one.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SourceFunction extends FunctionBase4 {

	@Override
	public NodeValue exec(final NodeValue max, final NodeValue newVal,
			final NodeValue newObs, final NodeValue oldObs) {
		if (newVal.equals(max)) {
			return newObs;
		}

		return oldObs;
	}

}
