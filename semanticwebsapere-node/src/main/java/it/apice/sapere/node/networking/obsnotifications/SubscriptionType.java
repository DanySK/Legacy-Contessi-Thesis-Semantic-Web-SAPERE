package it.apice.sapere.node.networking.obsnotifications;

/**
 * <p>
 * This enumeration represents all possible subscription type.
 * </p>
 * 
 */
public enum SubscriptionType {
	
	/** READ subscription type. */
	READ, 
	
	/** One subscription type. */
	ONE_TIME_SUBSCRIPTION, 
	
	/** Many subscription type. */
	PERMANENT_SUBSCRIPTION, 
	
	/** Cancel subscription type. */
	CANCEL_SUBSCRIPTION;
}
