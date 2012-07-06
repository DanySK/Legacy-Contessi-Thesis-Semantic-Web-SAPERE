package com.sun.btrace.scripts;

import java.util.concurrent.atomic.AtomicInteger;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

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
	//
	// /** Time at which last AGGREGATION application occurred. */
	// private static long lastApply = BTraceUtils.timeNanos();
	//
	// /** Actual period between two <code>handleDiffusion</code> calls. */
	// private static long receivePeriod;
	//
	// /** Counter which verifies calls balancing. */
	// private static int balancer = 0;

	/** Counts DIFFUSE receival events. */
	private static AtomicInteger receivedCounter = BTraceUtils
			.newAtomicInteger();

	/** Counts AGGR apply events. */
	private static AtomicInteger applyCounter = BTraceUtils.newAtomicInteger();

	/** Flag which enable/disable the test run. */
	private static boolean testOn = true;

	/** Flag which marks last iteration. */
	private static boolean printStats = true;

	/** Aggregator for minimum period. */
	private static Aggregation minPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MINIMUM);

	/** Aggregator for average period. */
	private static Aggregation avgPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.AVERAGE);

	/** Aggregator for maximum period. */
	private static Aggregation maxPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MAXIMUM);

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
		testOn = BTraceUtils.incrementAndGet(receivedCounter) < 1000;
		if (testOn) {
			final long now = BTraceUtils.timeNanos();
			final long receivePeriod = (now - lastReceive);
			lastReceive = now;

			BTraceUtils.Aggregations.addToAggregation(minPeriod, receivePeriod);
			BTraceUtils.Aggregations.addToAggregation(avgPeriod, receivePeriod);
			BTraceUtils.Aggregations.addToAggregation(maxPeriod, receivePeriod);
		}
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
		if (testOn) {
			BTraceUtils.incrementAndGet(applyCounter);
		} else if (printStats) {
			printStats = false;
			BTraceUtils.println(BTraceUtils.concat(BTraceUtils.str(BTraceUtils
					.get(applyCounter))), " / 1000 elaborations");
			BTraceUtils.printAggregation(minPeriod);
			BTraceUtils.printAggregation(avgPeriod);
			BTraceUtils.printAggregation(maxPeriod);
		}
	}
}
