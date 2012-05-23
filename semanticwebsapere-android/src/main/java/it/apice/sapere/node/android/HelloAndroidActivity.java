package it.apice.sapere.node.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {

	private static String TAG = "semanticwebsapere-android";

	/** START button. */
	private transient Button btnStart;

	/** STOP button. */
	private transient Button btnStop;

	/** Console view. */
	private transient TextView logger;

	/** Activity Handler. */
	private transient Handler hdl;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main);

		hdl = new Handler(getMainLooper());
		logger = ((TextView) findViewById(R.id.console));

		btnStart = ((Button) findViewById(R.id.btnStart));
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startNode();
					switchButtons();
				} catch (Exception ex) {
					log("START: " + ex.getMessage());
				}
			}
		});

		btnStop = ((Button) findViewById(R.id.btnStop));
		btnStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					stopNode();
					switchButtons();
				} catch (Exception ex) {
					log("STOP: " + ex.getMessage());
				}
			}
		});
	}

	/**
	 * <p>
	 * Switches buttons enable.
	 * </p>
	 */
	private void switchButtons() {
		btnStart.setEnabled(!btnStart.isEnabled());
		btnStop.setEnabled(!btnStop.isEnabled());
	}

	/**
	 * <p>
	 * Starts the SAPERE-node.
	 * </p>
	 */
	private void startNode() throws Exception {
		log("Starting SAPERE-node..");
		
//		try {
//			RDFModelServices.getInstance();
//		} catch (Exception ex) {
//			warn("Cannot init node", ex);
//		}
	}

	/**
	 * <p>
	 * Stop the SAPERE-node.
	 * </p>
	 */
	private void stopNode() throws Exception {
		log("Stopping SAPERE-node..");
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void error(final String msg) {
		Log.e(TAG, msg);
		printLog("ERROR " + msg);
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void error(final String msg, final Throwable cause) {
		Log.e(TAG, msg, cause);
		printLog("ERROR " + msg + " (" + cause.getMessage() + ")");
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void warn(final String msg) {
		Log.d(TAG, msg);
		printLog("WARNING " + msg);
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void warn(final String msg, final Throwable cause) {
		Log.d(TAG, msg, cause);
		printLog("WARNING " + msg + " (" + cause.getMessage() + ")");
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void spy(final String msg) {
		Log.d(TAG, msg);
		printLog("DEBUG " + msg);
	}

	/**
	 * <p>
	 * Prints a log message.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be logged
	 */
	public void log(final String msg) {
		Log.i(TAG, msg);
		printLog("INFO " + msg);
	}

	/**
	 * <p>
	 * Displays the log message onto the GUI.
	 * </p>
	 * 
	 * @param msg
	 *            The message to be displayed
	 */
	private void printLog(final String msg) {
		hdl.post(new Runnable() {

			@Override
			public void run() {
				logger.append(msg + "\n");
				logger.setLines(logger.getLineCount());
			}
		});
	}
}
