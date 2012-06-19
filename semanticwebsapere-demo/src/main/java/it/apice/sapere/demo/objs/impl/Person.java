package it.apice.sapere.demo.objs.impl;

import it.apice.sapere.demo.rendering.impl.Renderable;
import it.apice.sapere.demo.rendering.impl.MainFrame.RenderablePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Class that represents a Person in the demo scenario.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class Person implements Renderable {

	/** Size of a person. */
	private static final transient double SIZE = 1.0D;

	/** Person's name. */
	private final String _name;

	/** X coordinate location. */
	private double _x;

	/** Y coordinate location. */
	private double _y;

	/** Synchronized queue of movements: stores occupied locations. */
	private final transient BlockingQueue<Location> evQueue = 
			new LinkedBlockingQueue<Location>();

	/** Mutual exclusion access. */
	private final transient Lock mutex = new ReentrantLock();

	/**
	 * <p>
	 * Builds a new {@link Person}.
	 * </p>
	 * 
	 * @param name
	 *            The name of the person
	 * @param x
	 *            X-coord location
	 * @param y
	 *            Y-coord location
	 */
	public Person(final String name, final double x, final double y) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name provided");
		}

		_name = name;
		setLocation(x, y);
	}

	/**
	 * @return The name of the person
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return X-coord location
	 */
	public double getX() {
		mutex.lock();
		try {
			return _x;
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * @return Y-coord location
	 */
	public double getY() {
		mutex.lock();
		try {
			return _y;
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Sets the new location.
	 * </p>
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 */
	public void setLocation(final double x, final double y) {
		mutex.lock();
		try {
			_x = x;
			_y = y;
			notify(_x, _y);
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Sets the X-coord location.
	 * </p>
	 * 
	 * @param x
	 *            x-coord
	 */
	public void setX(final double x) {
		mutex.lock();
		try {
			_x = x;
			notify(_x, _y);
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Sets the Y-coord location.
	 * </p>
	 * 
	 * @param y
	 *            y-coord
	 */
	public void setY(final double y) {
		mutex.lock();
		try {
			_y = y;
			notify(_x, _y);
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Waits for next person movement in order to retrieve it.
	 * </p>
	 * 
	 * @return The new location
	 * @throws InterruptedException
	 *             Interrupted while waiting
	 */
	public Location getNextMovement() throws InterruptedException {
		return evQueue.take();
	}

	/**
	 * <p>
	 * Notifies movement.
	 * </p>
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 */
	private void notify(final double x, final double y) {
		evQueue.add(new Location(x, y));
	}

	@Override
	public void render(final Graphics2D g) {
		final Color defColor = g.getColor();
		final Ellipse2D p = new Ellipse2D.Double(_x - SIZE / 2, _y - SIZE / 2,
				SIZE, SIZE);

		g.setColor(Color.ORANGE);
		g.fill(p);
		g.setColor(Color.BLACK);
		g.drawString(getName(), (float) _x, (float) _y);

		g.setColor(defColor);
	}

	@Override
	public void moveTo(final double x, final double y) {
		setLocation(x, y);
	}

	@Override
	public boolean hits(final double x, final double y) {
		return Point2D.distance(getX(), getY(), x, y) <= SIZE / 2;
	}
	
	@Override
	public void setSurface(final RenderablePanel surface) {
		surface.repaint();
	}
}
