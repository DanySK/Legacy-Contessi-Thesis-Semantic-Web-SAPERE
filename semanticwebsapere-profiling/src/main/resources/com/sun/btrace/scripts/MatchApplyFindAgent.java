package com.sun.btrace.scripts;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
import com.sun.btrace.aggregation.AggregationKey;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
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
public final class MatchApplyFindAgent {

	/** Average (match) duration value provided as a Bean. */
	@Property
	private static String mDurAvg;

	/** Minimum (match) duration value provided as a Bean. */
	@Property
	private static String mDurMin;

	/** Maximum (match) duration value provided as a Bean. */
	@Property
	private static String mDurMax;

	/** Average (apply) duration value provided as a Bean. */
	@Property
	private static String aDurAvg;

	/** Minimum (apply) duration value provided as a Bean. */
	@Property
	private static String aDurMin;

	/** Maximum (apply) duration value provided as a Bean. */
	@Property
	private static String aDurMax;

	/** Average (findNext) duration value provided as a Bean. */
	@Property
	private static String fnDurAvg;

	/** Minimum (findNext) duration value provided as a Bean. */
	@Property
	private static String fnDurMin;

	/** Maximum (findNext) duration value provided as a Bean. */
	@Property
	private static String fnDurMax;

	/** Aggregator Key for <code>match</code> operation. */
	private static AggregationKey matchKey = BTraceUtils.Aggregations
			.newAggregationKey("match");

	/** Aggregator Key for <code>apply</code> operation. */
	private static AggregationKey applyKey = BTraceUtils.Aggregations
			.newAggregationKey("apply");

	/** Aggregator Key for <code>findNext</code> operation. */
	private static AggregationKey findKey = BTraceUtils.Aggregations
			.newAggregationKey("findNext");

	/** Aggregator for minimum duration of operations. */
	private static Aggregation minOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MINIMUM);

	/** Aggregator for average duration of operations. */
	private static Aggregation avgOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.AVERAGE);

	/** Aggregator for maximum duration of operations. */
	private static Aggregation maxOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MAXIMUM);

	/** Invocations counter (<code>match</code>). */
	private static int mCounter = 1;
	
	/** Invocations counter (<code>apply</code>). */
	private static int aCounter = 1;
	
	/** Invocations counter (<code>findNext</code>). */
	private static int fnCounter = 1;
	
	/**
	 * <p>
	 * Constructor obfuscator.
	 * </p>
	 */
	private MatchApplyFindAgent() {

	}

	/**
	 * <p>
	 * Instruments <code>match</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "match", location = @Location(Kind.RETURN))
	public static void onMatch(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, matchKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, matchKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, matchKey, durInNS);

		// Update Beans
		mDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(matchKey)), "us");
		mDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(matchKey)), "us");
		mDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(matchKey)), "us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("match #",
										BTraceUtils.str(mCounter++)), ":\t"),
								BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Instruments <code>apply</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "apply", location = @Location(Kind.RETURN))
	public static void onApply(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, applyKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, applyKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, applyKey, durInNS);

		// Update Beans
		aDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(applyKey)), "us");
		aDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(applyKey)), "us");
		aDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(applyKey)), "us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("apply #",
										BTraceUtils.str(aCounter++)), ":\t"),
								BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Instruments <code>compile</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
		clazz = "it.apice.sapere.management.impl.ReactionManagerImpl", 
		method = "findNextScheduling", location = @Location(Kind.RETURN))
	public static void onFind(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, findKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, findKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, findKey, durInNS);

		// Update Beans
		fnDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(findKey)), "us");
		fnDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(findKey)), "us");
		fnDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(findKey)), "us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("findNext #",
										BTraceUtils.str(fnCounter++)), ":\t"),
								BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Prints properties.
	 * </p>
	 */
	@OnEvent
	public static void onEvent() {
		BTraceUtils.println(mDurMin);
		BTraceUtils.println(mDurAvg);
		BTraceUtils.println(mDurMax);
		BTraceUtils.println(aDurMin);
		BTraceUtils.println(aDurAvg);
		BTraceUtils.println(aDurMax);
		BTraceUtils.println(fnDurMin);
		BTraceUtils.println(fnDurAvg);
		BTraceUtils.println(fnDurMax);
	}
}
