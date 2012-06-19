package it.apice.sapere.demo.rendering.impl;

import it.apice.sapere.demo.rendering.impl.MainFrame.RenderablePanel;

import java.awt.Graphics2D;

/**
 * <p>
 * Basic interface for renderable objects.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public interface Renderable {

	/**
	 * <p>
	 * Draws the object onto the canvas.
	 * </p>
	 * 
	 * @param g
	 *            The canvas
	 */
	void render(Graphics2D g);

	/**
	 * <p>
	 * Moves the object in the specified location.
	 * </p>
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 */
	void moveTo(double x, double y);

	/**
	 * <p>
	 * Checks if the point is on to the object.
	 * </p>
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 * @return True if hits the object, false otherwise
	 */
	boolean hits(double x, double y);

	/**
	 * <p>
	 * Let the {@link Renderable} know the surface on which it will be
	 * displayed.
	 * </p>
	 * 
	 * @param surface
	 *            The surface
	 */
	void setSurface(RenderablePanel surface);
}
