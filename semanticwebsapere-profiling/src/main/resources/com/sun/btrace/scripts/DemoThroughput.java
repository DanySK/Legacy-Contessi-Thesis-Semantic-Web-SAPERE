package com.sun.btrace.scripts;

import java.util.concurrent.atomic.AtomicInteger;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
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
	/** Time at which last AGGREGATION application occurred. */
	private static long lastApply = BTraceUtils.timeNanos();
	//
	// /** Actual period between two <code>handleDiffusion</code> calls. */
	// private static long receivePeriod;
	//
	// /** Counter which verifies calls balancing. */
	// private static int balancer = 0;

	/** Counts DIFFUSE receival events. */
	private static AtomicInteger receivedCounter = BTraceUtils
			.newAtomicInteger(0);

	/** Counts AGGR apply events. */
	private static AtomicInteger applyCounter = BTraceUtils.newAtomicInteger(0);

	/** Flag which enable/disable the test run. */
	private static boolean testOn = true;

	/** Flag which marks last iteration. */
	private static boolean printStats = true;

	/** Aggregator for minimum period (receive). */
	private static Aggregation minRPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MINIMUM);

	/** Aggregator for average period (receive). */
	private static Aggregation avgRPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.AVERAGE);

	/** Aggregator for maximum period (receive). */
	private static Aggregation maxRPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MAXIMUM);

	/** Aggregator for minimum period (apply). */
	private static Aggregation minAPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MINIMUM);

	/** Aggregator for average period (apply). */
	private static Aggregation avgAPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.AVERAGE);

	/** Aggregator for maximum period (apply). */
	private static Aggregation maxAPeriod = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MAXIMUM);

	/** Number of expected diffusions. */
	private static int limit = 300;

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
		testOn = BTraceUtils.incrementAndGet(receivedCounter) < limit;
		if (testOn) {
			final long now = BTraceUtils.timeNanos();
			final long receivePeriod = (now - lastReceive);
			lastReceive = now;

			if (BTraceUtils.get(receivedCounter) > 1) { 
				BTraceUtils.Aggregations
					.addToAggregation(minRPeriod, receivePeriod);
				BTraceUtils.Aggregations
					.addToAggregation(avgRPeriod, receivePeriod);
				BTraceUtils.Aggregations
					.addToAggregation(maxRPeriod, receivePeriod);
			}
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
			final int cnt = BTraceUtils.incrementAndGet(applyCounter);
            BTraceUtils.println(BTraceUtils.str(cnt));

			final long now = BTraceUtils.timeNanos();
			final long applyPeriod = (now - lastApply);
			lastApply = now;

			if (BTraceUtils.get(applyCounter) > 1) {
				BTraceUtils.Aggregations.addToAggregation(minAPeriod, 
						applyPeriod);
				BTraceUtils.Aggregations.addToAggregation(avgAPeriod, 
						applyPeriod);
				BTraceUtils.Aggregations.addToAggregation(maxAPeriod, 
						applyPeriod);
			}

		} else if (printStats) {
			printStats = false;
			BTraceUtils
					.println(BTraceUtils.concat(
							"Success: ",
							BTraceUtils.str(((double) BTraceUtils
									.get(applyCounter) + 1) / limit)));
			BTraceUtils.printAggregation("MIN-receive", minRPeriod);
			BTraceUtils.printAggregation("AVG-receive", avgRPeriod);
			BTraceUtils.printAggregation("MAX-receive", maxRPeriod);

			BTraceUtils.printAggregation("MIN-apply", minAPeriod);
			BTraceUtils.printAggregation("AVG-apply", avgAPeriod);
			BTraceUtils.printAggregation("MAX-apply", maxAPeriod);
		}
	}
}
