package it.apice.sapere.demo.objs.impl;

/**
 * <p>
 * Class that represents a location.
 * </p>
 *
 * @author Paolo Contessi
 *
 */
public class Location {

	/** X coordinate location. */
	private final double _x;

	/** Y coordinate location. */
	private final double _y;
	
	/**
	 * <p>
	 * Builds a new {@link Person}.
	 * </p>
	 * 
	 * @param x
	 *            X-coord location
	 * @param y
	 *            Y-coord location
	 */
	public Location(final double x, final double y) {
		_x = x;
		_y = y;
	}

	/**
	 * @return X-coord location
	 */
	public double getX() {
		return _x;
	}

	/**
	 * @return Y-coord location
	 */
	public double getY() {
		return _y;
	}
}
