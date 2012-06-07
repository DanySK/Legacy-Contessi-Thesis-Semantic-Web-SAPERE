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
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(clazz = "it.apice.sapere.api.space.core.impl.LSACompilerImpl", 
			method = "compile", location = @Location(Kind.RETURN))
	public static void onCompile(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations
				.addToAggregation(minOpDur, compileKey, durInMS);
		BTraceUtils.Aggregations
				.addToAggregation(avgOpDur, compileKey, durInMS);
		BTraceUtils.Aggregations
				.addToAggregation(maxOpDur, compileKey, durInMS);

		// Update Beans
		cDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(compileKey)), " ms");
		cDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(compileKey)), " ms");
		cDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(compileKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"COMPILE duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(cDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(cDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(cDurMax,
												" ms")))))));
	}

	/**
	 * <p>
	 * Instruments <code>parse</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param args
	 *            Methods parameters
	 */
	@OnMethod(clazz = "it.apice.sapere.api.space.core.impl.LSACompilerImpl", 
			method = "parse", location = @Location(Kind.RETURN))
	public static void onParse(@Duration final long duration,
			final AnyType[] args) {
		// Convert nanos to milliseconds
		final long durInMS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, parseKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, parseKey, durInMS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, parseKey, durInMS);

		// Update Beans
		pDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(parseKey)), " ms");
		pDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(parseKey)), " ms");
		pDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(parseKey)), " ms");

		// Print statistics on console
		BTraceUtils.println(BTraceUtils.Strings.concat(
				"PARSE duration statistics (Min/Avg/Max): ",
				BTraceUtils.Strings.concat(pDurMin, BTraceUtils.Strings.concat(
						"ms/", BTraceUtils.Strings.concat(pDurAvg,
								BTraceUtils.Strings.concat("ms/",
										BTraceUtils.Strings.concat(pDurMax,
												" ms")))))));
	}
}
