package it.apice.sapere.demo.objs.impl;

import it.apice.sapere.demo.rendering.impl.MainFrame.RenderablePanel;
import it.apice.sapere.demo.rendering.impl.Renderable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Class that represents a Display in the demo scenario.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class Display implements Renderable {

	/** Range of the display. */
	private static final transient double RANGE = 14.0D;

	/** Display width. */
	private static final transient double WIDTH = 7.0D;

	/** Display height. */
	private static final transient double HEIGHT = 5.5D;

	/** X coordinate location. */
	private double _x;

	/** Y coordinate location. */
	private double _y;

	/** Mutual exclusion access. */
	private final transient Lock mutex = new ReentrantLock();

	/** On/Off status flag. */
	private boolean isOn;

	/** Names which greetings are directed to. */
	private String names = "Alfa, Beta, Gamma, Delta, ...";

	/** The surface on which this will be displayed. */
	private transient RenderablePanel _surface;

	/**
	 * <p>
	 * Builds a new {@link Display}.
	 * </p>
	 * 
	 * @param x
	 *            X-coord location
	 * @param y
	 *            Y-coord location
	 */
	public Display(final double x, final double y) {
		setLocation(x, y);
		turnOff();
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
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * <p>
	 * Turns the display off.
	 * </p>
	 */
	public void turnOff() {
		isOn = false;
		refreshDisplay();
	}

	/**
	 * <p>
	 * Turns the display on and show a greeting message.
	 * </p>
	 * 
	 * @param name
	 *            The name of the person to welcome
	 */
	public void showGreetings(final String name) {
		isOn = name != null && name.length() > 0;
		names = name;
		refreshDisplay();
	}

	@Override
	public void render(final Graphics2D g) {
		final Color defColor = g.getColor();
		final Stroke defStroke = g.getStroke();

		final Ellipse2D p = new Ellipse2D.Double(_x - RANGE / 2,
				_y - RANGE / 2, RANGE, RANGE);

		g.setColor(Color.DARK_GRAY);
		// g.setStroke(DOTTED LINE);
		g.fill(p);

		if (isOn) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.BLACK);
		}

		final Rectangle2D disp = new Rectangle2D.Double(_x - WIDTH / 2, _y
				- HEIGHT / 2, WIDTH, HEIGHT);
		g.fill(disp);
		g.setColor(Color.WHITE);
		g.draw(disp);

		if (isOn) {
			final Rectangle defClip = g.getClipBounds();
			g.setClip(disp);
			g.drawString("Welcome", (float) _x - 2.75F, (float) _y - 1.25F);
			float dy = 0.0F;
			for (String line : split(names, g.getFontMetrics(),
					5)) {
				g.drawString(line, (float) _x - 2.75F, (float) _y + dy);
				dy += 1.0F;
			}
			g.setClip(defClip);
		}

		g.setColor(defColor);
		g.setStroke(defStroke);
	}

	/**
	 * <p>
	 * Splits all names into lines.
	 * </p>
	 * 
	 * @param allNames
	 *            Names to be greated
	 * @param fm
	 *            {@link FontMetrics}
	 * @param width
	 *            Line width
	 * @return Text lines
	 */
	private String[] split(final String allNames, final FontMetrics fm,
			final int width) {
		final String[] parts = allNames.split(",");
		final List<String> lines = new LinkedList<String>();

		for (int idx = 0; idx < parts.length; idx++) {
			final StringBuilder builder = new StringBuilder(parts[idx].trim());
			if (idx < parts.length - 1) {
				builder.append(",");
			}
			
			for (int i = idx + 1; i < parts.length
					&& fm.stringWidth(builder.toString()) < width; i++) {
				builder.append(" ").append(parts[i].trim());
				if (i < parts.length - 1) {
					builder.append(",");
				}

				idx = i;
			}
			
			lines.add(builder.toString());
		}

		return lines.toArray(new String[lines.size()]);
	}
	
	/**
	 * <p>
	 * Causes display surface refresh.
	 * </p>
	 */
	private void refreshDisplay() {
		if (_surface != null) {
			_surface.repaint();
		}
	}

	@Override
	public void moveTo(final double x, final double y) {

	}

	@Override
	public boolean hits(final double x, final double y) {
		return false;
	}

	@Override
	public void setSurface(final RenderablePanel surface) {
		_surface = surface;
		surface.repaint();
	}
}
