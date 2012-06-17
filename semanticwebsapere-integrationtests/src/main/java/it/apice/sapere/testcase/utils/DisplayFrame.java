package it.apice.sapere.testcase.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * <p>
 * Display frame for the DisplayVLCService.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DisplayFrame extends JFrame {

	/** Serialization ID. */
	private static final long serialVersionUID = 1122649792516740518L;

	/** Reference to VLC media player. */
	private final transient EmbeddedMediaPlayerComponent mediaPlayerComponent;

	/** Event semaphore for "display ready" condition. */
	private final transient Semaphore ready = new Semaphore(0);

	/**
	 * Create the frame.
	 */
	public DisplayFrame() {
		super("Display");

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		setContentPane(mediaPlayerComponent);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 240);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				super.windowClosing(e);

				if (mediaPlayerComponent != null) {
					mediaPlayerComponent.release();
				}
			}
		});
	}

	/**
	 * <p>
	 * Refresh the display, starting playing a new media.
	 * </p>
	 * 
	 * @param mrl
	 *            Media Resource Locator
	 * @throws InterruptedException
	 *             Interrupted while waiting for display to show media
	 */
	public void displayMedia(final String mrl) throws InterruptedException {
		ready.acquire();
		try {
			mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
		} catch (Error ex) {
			System.err.println("Cannot exploit VLCj libraries: "
					+ ex.getMessage());
			setVisible(false);
			try {
				Runtime.getRuntime().exec(
						"/Applications/VLC.app/Contents/MacOS/VLC "
								+ mrl);
			} catch (IOException e) {
				throw new InterruptedException("Cannot play movie");
			}
		}
	}

	/**
	 * <p>
	 * Interrupts the media playback.
	 * </p>
	 */
	public void stopPlaying() {
		mediaPlayerComponent.getMediaPlayer().pause();
	}

	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);

		if (isVisible()) {
			ready.release();
		}
	}
}
