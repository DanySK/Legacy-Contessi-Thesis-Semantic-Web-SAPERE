package vlcj.tutorial1;

import java.util.concurrent.Semaphore;

import it.apice.sapere.testcase.utils.DisplayFrame;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * <p>
 * This tutorial is meant to test basic functionalities of VLCj.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class Tutorial1A {
	
	/**
	 * <p>
	 * Builds a new {@link Tutorial1A}.
	 * </p>
	 */
	private Tutorial1A() {
		
	}

	/**
	 * <p>
	 * Launches tutorial.
	 * </p>
	 *
	 * @param args CLI arguments
	 * @throws InterruptedException Cannot wait
	 */
	public static void main(final String[] args) throws InterruptedException {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				"/Applications/VLC (64-bit).app/Contents/MacOS/lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		final Semaphore ev = new Semaphore(0);
		final DisplayFrame frame = new DisplayFrame();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
				ev.release();
			}
		});

		ev.acquire();

		System.out.println("§§§ play!!!!");
		frame.displayMedia("/Users/conteit/trailer.mov");
	}
}