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
public final class ParseCompileAgent {

	/** Average (compilation) duration value provided as a Bean. */
	@Property
	private static String cDurAvg;

	/** Minimum (compilation) duration value provided as a Bean. */
	@Property
	private static String cDurMin;

	/** Maximum (compilation) duration value provided as a Bean. */
	@Property
	private static String cDurMax;

	/** Average (parsing) duration value provided as a Bean. */
	@Property
	private static String pDurAvg;

	/** Minimum (parsing) duration value provided as a Bean. */
	@Property
	private static String pDurMin;

	/** Maximum (parsing) duration value provided as a Bean. */
	@Property
	private static String pDurMax;

	/** Aggregator Key for <code>parse</code> operation. */
	private static AggregationKey parseKey = BTraceUtils.Aggregations
			.newAggregationKey("parse");

	/** Aggregator Key for <code>compile</code> operation. */
	private static AggregationKey compileKey = BTraceUtils.Aggregations
			.newAggregationKey("compile");

	/** Aggregator for minimum duration of operations. */
	private static Aggregation minOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MINIMUM);

	/** Aggregator for average duration of operations. */
	private static Aggregation avgOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.AVERAGE);

	/** Aggregator for maximum duration of operations. */
	private static Aggregation maxOpDur = BTraceUtils.Aggregations
			.newAggregation(AggregationFunction.MAXIMUM);

	/** Invocations counter (<code>compile</code>). */
	private static int cCounter = 1;
	
	/** Invocations counter (<code>parse</code>). */
	private static int pCounter = 1;

	/**
	 * <p>
	 * Constructor obfuscator.
	 * </p>
	 */
	private ParseCompileAgent() {

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
			clazz = "it.apice.sapere.api.space.core.impl.LSACompilerImpl", 
			method = "compile", location = @Location(Kind.RETURN))
	public static void onCompile(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations
				.addToAggregation(minOpDur, compileKey, durInNS);
		BTraceUtils.Aggregations
				.addToAggregation(avgOpDur, compileKey, durInNS);
		BTraceUtils.Aggregations
				.addToAggregation(maxOpDur, compileKey, durInNS);

		// Update Beans
		cDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(compileKey)), " us");
		cDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(compileKey)), " us");
		cDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(compileKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
				.concat(BTraceUtils.Strings.concat(
						BTraceUtils.Strings.concat("compile #",
								BTraceUtils.str(cCounter++)), ":\t"),
						BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Instruments <code>parse</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
			clazz = "it.apice.sapere.api.space.core.impl.LSACompilerImpl", 
			method = "parse", location = @Location(Kind.RETURN))
	public static void onParse(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, parseKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, parseKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, parseKey, durInNS);

		// Update Beans
		pDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(parseKey)), " us");
		pDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(parseKey)), " us");
		pDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(parseKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
				.concat(BTraceUtils.Strings.concat(
						BTraceUtils.Strings.concat("parse #",
								BTraceUtils.str(pCounter++)), ":\t"),
						BTraceUtils.str(durInNS)), " us"));
	}
	
	/**
	 * <p>
	 * Prints properties.
	 * </p>
	 */
	@OnEvent
	public static void onEvent() {
		BTraceUtils.println(cDurMin);
		BTraceUtils.println(cDurAvg);
		BTraceUtils.println(cDurMax);
		BTraceUtils.println(pDurMin);
		BTraceUtils.println(pDurAvg);
		BTraceUtils.println(pDurMax);
	}
}
