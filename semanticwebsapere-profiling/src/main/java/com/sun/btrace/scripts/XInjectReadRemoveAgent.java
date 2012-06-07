package com.sun.btrace.scripts;

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
 * BTrace script which collect the duration of the execution of Agents
 * primitives on the LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
@BTrace
public final class XInjectReadRemoveAgent {

	/** Average (inject) duration value provided as a Bean. */
	@Property
	private static String iDurAvg;

	/** Minimum (inject) duration value provided as a Bean. */
	@Property
	private static String iDurMin;

	/** Maximum (inject) duration value provided as a Bean. */
	@Property
	private static String iDurMax;

	/** Average (read) duration value provided as a Bean. */
	@Property
	private static String rdDurAvg;

	/** Minimum (read) duration value provided as a Bean. */
	@Property
	private static String rdDurMin;

	/** Maximum (read) duration value provided as a Bean. */
	@Property
	private static String rdDurMax;

	/** Average (update) duration value provided as a Bean. */
	@Property
	private static String uDurAvg;

	/** Minimum (update) duration value provided as a Bean. */
	@Property
	private static String uDurMin;

	/** Maximum (update) duration value provided as a Bean. */
	@Property
	private static String uDurMax;

	/** Average (remove) duration value provided as a Bean. */
	@Property
	private static String rmDurAvg;

	/** Minimum (remove) duration value provided as a Bean. */
	@Property
	private static String rmDurMin;

	/** Maximum (remove) duration value provided as a Bean. */
	@Property
	private static String rmDurMax;

	/** Aggregator Key for <code>inject</code> operation. */
	private static AggregationKey injectKey = BTraceUtils.Aggregations
			.newAggregationKey("inject");

	/** Aggregator Key for <code>read</code> operation. */
	private static AggregationKey readKey = BTraceUtils.Aggregations
			.newAggregationKey("read");

	/** Aggregator Key for <code>update</code> operation. */
	private static AggregationKey updateKey = BTraceUtils.Aggregations
			.newAggregationKey("update");

	/** Aggregator Key for <code>remove</code> operation. */
	private static AggregationKey removeKey = BTraceUtils.Aggregations
			.newAggregationKey("remove");

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
	private XInjectReadRemoveAgent() {

	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "inject", location = @Location(Kind.RETURN))
	public static void onInject(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, injectKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, injectKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, injectKey, durInMS);

		// Update Beans
		iDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(injectKey)), " ms");
		iDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(injectKey)), " ms");
		iDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(injectKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"INJECT duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(iDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(iDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(iDurMax,
												" ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "read", location = @Location(Kind.RETURN))
	public static void onRead(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, readKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, readKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, readKey, durInMS);

		// Update Beans
		rdDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(readKey)), " ms");
		rdDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(readKey)), " ms");
		rdDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(readKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"READ duration statistics (Min/Avg/Max): ", BTraceUtils.Strings
						.concat(rdDurMin, BTraceUtils.Strings.concat("ms/",
								BTraceUtils.Strings.concat(rdDurAvg,
										BTraceUtils.Strings.concat("ms/",
												BTraceUtils.Strings.concat(
														rdDurMax, " ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "update", location = @Location(Kind.RETURN))
	public static void onUpdate(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, updateKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, updateKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, updateKey, durInMS);

		// Update Beans
		uDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(updateKey)), " ms");
		uDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(updateKey)), " ms");
		uDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(updateKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"UPDATE duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(uDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(uDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(uDurMax,
												" ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "remove", location = @Location(Kind.RETURN))
	public static void onRemove(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, removeKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, removeKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, removeKey, durInMS);

		// Update Beans
		rmDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(removeKey)), " ms");
		rmDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(removeKey)), " ms");
		rmDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(removeKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"REMOVE duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(rmDurMin, BTraceUtils.Strings
						.concat("ms/", BTraceUtils.Strings.concat(rmDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(rmDurMax,
												" ms")))))));
	}
}
