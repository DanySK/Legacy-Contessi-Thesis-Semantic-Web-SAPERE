package com.sun.btrace.scripts;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
import com.sun.btrace.aggregation.AggregationKey;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.Property;

/**
 * <p>
 * BTrace script which collect the duration of the execution of
 * <code>parse</code> and <code>compile</code> operations (relative to LSAs).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
@BTrace(unsafe = true)
public final class DemoThroughput {

	/** Time at which last DIFFUSE receival occurred. */
	private static long lastReceive = BTraceUtils.timeNanos();

	/** Time at which last AGGREGATION application occurred. */
	private static long lastApply = BTraceUtils.timeNanos();

	/** Actual period between two <code>handleDiffusion</code> calls. */
	private static long receivePeriod;

	/** Counter which verifies calls balancing. */
	private static int balancer = 0;
	
	/**
	 * <p>
	 * Constructor obfuscator.
	 * </p>
	 */
	private DemoThroughput() {

	}

	/**
	 * <p>
	 * Instruments DIFFUSE receival frequency.
	 * </p>
	 */
	@OnMethod(
	clazz = "it.apice.sapere.node.networking.manager.impl.NetworkManagerImpl", 
	method = "handleDiffusion")
	public static void onReceive() {
		balancer++;
		final long now = BTraceUtils.timeNanos();

		receivePeriod = (now - lastReceive);
		lastReceive = now;
	}

	/**
	 * <p>
	 * Instruments AGGREGATION application frequency.
	 * </p>
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "apply")
	public static void onApply() {
		balancer--;
		final long now = BTraceUtils.timeNanos();

		final long applyPeriod = (now - lastApply);
		final long waitPeriod = (now - lastReceive);
		lastApply = now;

		BTraceUtils.println(
				BTraceUtils.concat(
						BTraceUtils.concat(BTraceUtils.concat(
								BTraceUtils.concat(
										BTraceUtils.str(receivePeriod / 1e6), ":"),
								BTraceUtils.str(applyPeriod / 1e6)), BTraceUtils
								.concat(":", BTraceUtils.str(waitPeriod / 1e6))),
				BTraceUtils.concat(":", BTraceUtils.str(balancer * 1000))));
	}
}
