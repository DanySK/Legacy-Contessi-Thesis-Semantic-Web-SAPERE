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
import com.sun.btrace.annotations.Return;
import com.sun.btrace.annotations.OnEvent;
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
@BTrace(unsafe = true)
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

	/** Invocations counter (<code>inject</code>). */
	private static int iCounter = 1;

	/** Invocations counter (<code>read</code>). */
	private static int rdCounter = 1;

	/** Invocations counter (<code>remove</code>). */
	private static int rmCounter = 1;

	/** Invocations counter (<code>update</code>). */
	private static int uCounter = 1;

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
	 * @param clsa
	 * 			  Input Compiled LSA
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "inject", location = @Location(Kind.RETURN))
	public static void onInject(@Duration final long duration, 
			final AnyType clsa) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, injectKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, injectKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, injectKey, durInNS);

		// Update Beans
		iDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(injectKey)), " us");
		iDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(injectKey)), " us");
		iDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(injectKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.concat(
				BTraceUtils.Strings.concat(BTraceUtils.Strings
				.concat(BTraceUtils.Strings.concat(
						BTraceUtils.Strings.concat("inject #",
								BTraceUtils.str(iCounter++)), ":\t"),
						BTraceUtils.str(durInNS)), " us:"), 
						BTraceUtils.str(BTraceUtils.strlen(clsa.toString()))));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "read", location = @Location(Kind.RETURN))
	public static void onRead(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, readKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, readKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, readKey, durInNS);

		// Update Beans
		rdDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(readKey)), " us");
		rdDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(readKey)), " us");
		rdDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(readKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("read #",
										BTraceUtils.str(rdCounter++)), ":\t"),
								BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "update", location = @Location(Kind.RETURN))
	public static void onUpdate(@Duration final long duration) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, updateKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, updateKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, updateKey, durInNS);

		// Update Beans
		uDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(updateKey)), " us");
		uDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(updateKey)), " us");
		uDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(updateKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("update #",
										BTraceUtils.str(uCounter++)), ":\t"),
								BTraceUtils.str(durInNS)), " us"));
	}

	/**
	 * <p>
	 * Instruments <code>inject</code> method.
	 * </p>
	 * 
	 * @param duration
	 *            Duration of method execution
	 * @param clsa 
	 * 			  Input Compiled LSA
	 */
	@OnMethod(
		clazz = "it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl", 
		method = "remove", location = @Location(Kind.RETURN))
	public static void onRemove(@Duration final long duration, 
			final AnyType clsa) {
		// Convert nanos to milliseconds
		final long durInNS = duration / 1000;

		// Update aggregations
		BTraceUtils.Aggregations.addToAggregation(minOpDur, removeKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(avgOpDur, removeKey, durInNS);
		BTraceUtils.Aggregations.addToAggregation(maxOpDur, removeKey, durInNS);

		// Update Beans
		rmDurMin = BTraceUtils.Strings.concat(
				BTraceUtils.str(minOpDur.getValueForKey(removeKey)), " us");
		rmDurAvg = BTraceUtils.Strings.concat(
				BTraceUtils.str(avgOpDur.getValueForKey(removeKey)), " us");
		rmDurMax = BTraceUtils.Strings.concat(
				BTraceUtils.str(maxOpDur.getValueForKey(removeKey)), " us");

		// Print duration on console
		BTraceUtils.println(BTraceUtils.concat(
				BTraceUtils.Strings.concat(BTraceUtils.Strings
						.concat(BTraceUtils.Strings.concat(
								BTraceUtils.Strings.concat("remove #",
										BTraceUtils.str(rmCounter++)), ":\t"),
						BTraceUtils.str(durInNS)), " us:"), BTraceUtils.str(
								BTraceUtils.strlen(clsa.toString()))));
	}
	
	/**
	 * <p>
	 * Tries to get LSA length.
	 * </p>
	 * 
	 * @param retVal
	 *            Return value of the method
	 */
	//@OnMethod(
	//		clazz = "it.apice.sapere.api.space.core.impl.CompiledLSAImpl", 
	//		method = "toString", location = @Location(Kind.RETURN))
	public static void onToString(@Return final AnyType retVal) {
        BTraceUtils.println(
            BTraceUtils.concat(
                BTraceUtils.concat(BTraceUtils.str(iCounter), ":"),
                BTraceUtils.str(BTraceUtils.strlen(retVal.toString()))));
	}
	
	/**
	 * <p>
	 * Prints properties.
	 * </p>
	 */
	@OnEvent
	public static void onEvent() {
		BTraceUtils.println(iDurMin);
		BTraceUtils.println(iDurAvg);
		BTraceUtils.println(iDurMax);
		BTraceUtils.println(rdDurMin);
		BTraceUtils.println(rdDurAvg);
		BTraceUtils.println(rdDurMax);
		BTraceUtils.println(rmDurMin);
		BTraceUtils.println(rmDurAvg);
		BTraceUtils.println(rmDurMax);
		BTraceUtils.println(uDurMin);
		BTraceUtils.println(uDurAvg);
		BTraceUtils.println(uDurMax);
	}
}
