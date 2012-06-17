package it.apice.sapere.demo.functions.impl;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase4;

/**
 * <p>
 * Function that computes the distance between two points.
 * </p>
 * 
 * @author Paolo Contessi
 *
 */
public class DistanceFunction extends FunctionBase4 {

	@Override
	public NodeValue exec(final NodeValue x1, final NodeValue y1,
			final NodeValue x2, final NodeValue y2) {
		return NodeValue.makeDecimal(Math.sqrt(Math.pow(
				x2.getDouble() - x1.getDouble(), 2)
				+ Math.pow(y2.getDouble() - y1.getDouble(), 2)));
	}

}
