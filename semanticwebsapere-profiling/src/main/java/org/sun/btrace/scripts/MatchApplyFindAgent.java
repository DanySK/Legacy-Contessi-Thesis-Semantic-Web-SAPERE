package org.sun.btrace.scripts;

import com.sun.btrace.AnyType;
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
@BTrace
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
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "match", location = @Location(Kind.RETURN))
	public static void onMatch(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, matchKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, matchKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, matchKey, durInMS);

		// Update Beans
		mDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(matchKey)), " ms");
		mDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(matchKey)), " ms");
		mDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(matchKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"MATCH duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(mDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(mDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(mDurMax,
												" ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>apply</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "apply", location = @Location(Kind.RETURN))
	public static void onApply(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, applyKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, applyKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, applyKey, durInMS);

		// Update Beans
		aDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(applyKey)), " ms");
		aDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(applyKey)), " ms");
		aDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(applyKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"APPLY duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(aDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(aDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(aDurMax,
												" ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>compile</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.management.impl.ReactionManagerImpl", 
		method = "findNextScheduling", location = @Location(Kind.RETURN))
	public static void onFind(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, findKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, findKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, findKey, durInMS);

		// Update Beans
		fnDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(findKey)), " ms");
		fnDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(findKey)), " ms");
		fnDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(findKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"FINDNEXTSCHEDULING duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(fnDurMin, BTraceUtils.Strings
						.concat("ms/", BTraceUtils.Strings.concat(fnDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(fnDurMax,
												" ms")))))));
	}

}
