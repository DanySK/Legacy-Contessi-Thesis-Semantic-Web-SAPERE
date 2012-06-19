package it.apice.sapere.demo.rendering.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * <p>
 * Class that represents the main frame.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MainFrame extends JFrame {

	/** Serialization ID. */
	private static final long serialVersionUID = -5802220183891925492L;

	/** Main panel. */
	private RenderablePanel contentPane;

	/** List of objects to be rendered. */
	private final transient List<Renderable> objs = Collections
			.synchronizedList(new LinkedList<Renderable>());

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		super("SAPERE demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new RenderablePanel(objs);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	/**
	 * <p>
	 * Registers an object that can be rendered.
	 * </p>
	 * 
	 * @param obj
	 *            An object to be displayed
	 */
	public void register(final Renderable obj) {
		objs.add(obj);
		obj.setSurface(contentPane);
	}

	/**
	 * <p>
	 * Inner class that extends {@link JPanel} in order to provide custom
	 * rendering.
	 * </p>
	 * 
	 * @author Paolo Contessi
	 * 
	 */
	public static class RenderablePanel extends JPanel {

		/** Serialization ID. */
		private static final long serialVersionUID = -4824976996368327770L;

		/** List of objects to be rendered. */
		private final transient List<Renderable> objects;

		/** World/Window ratio (X). */
		private transient double ratioX;

		/** World/Window ratio (Y). */
		private transient double ratioY;

		/** Dragging flag. */
		private transient boolean isDragging;

		/** Dragged object. */
		private transient Renderable dragged;

		/**
		 * <p>
		 * Builds a new {@link RenderablePanel}.
		 * </p>
		 * 
		 * @param objs
		 *            Renderable objects
		 */
		public RenderablePanel(final List<Renderable> objs) {
			super(true);

			objects = objs;
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(final MouseEvent ev) {
					if (isDragging) {
						isDragging = false;
						dragged.moveTo(((double) ev.getX()) / ratioX,
								((double) ev.getY()) / ratioY);
						dragged = null;
						redisplay();
					}
				}

				@Override
				public void mousePressed(final MouseEvent ev) {
					isDragging = false;
					for (Renderable obj : objects) {
						if (obj.hits(((double) ev.getX()) / ratioX,
								((double) ev.getY()) / ratioY)) {
							isDragging = true;
							dragged = obj;
							return;
						}
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseDragged(final MouseEvent ev) {
					if (isDragging) {
						dragged.moveTo(((double) ev.getX()) / ratioX,
								((double) ev.getY()) / ratioY);
						redisplay();
					}
				}
			});
		}

		/**
		 * <p>
		 * Causes the Panel to be repainted.
		 * </p>
		 */
		private void redisplay() {
			repaint();
		}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);

			final Graphics2D g2d = (Graphics2D) g;

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setBackground(Color.GRAY);
			g2d.clearRect(0, 0, getWidth(), getHeight());
			g2d.setFont(new Font("Arial", Font.PLAIN, 1));

			final AffineTransform tx = g2d.getTransform();
			ratioX = ((double) getWidth()) / 30.0;
			ratioY = ((double) getHeight()) / 30.0;
			tx.scale(ratioX, ratioY);
			g2d.setTransform(tx);

			for (Renderable obj : objects) {
				obj.render(g2d);
			}
		}
	}
}
