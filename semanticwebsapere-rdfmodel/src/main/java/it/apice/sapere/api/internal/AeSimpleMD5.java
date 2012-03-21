package it.apice.sapere.api.internal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Utility class which provides an MD5 hash hex string.
 * </p>
 * <p>
 * Taken from http://www.anyexample.com/programming/java/
 * java_simple_class_to_compute_md5_hash.xml.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class AeSimpleMD5 {

	/** Length of hash code. */
	private static final transient int HASH_LENGTH = 32;

	/** Magic number 1. */
	private static final transient int NUM_0X0F = 0x0F;

	/** Magic number 2. */
	private static final transient int NUM_4 = 4;

	/** Magic number 3. */
	private static final transient int NUM_9 = 9;

	/** Magic number 4. */
	private static final transient int NUM_10 = 10;

	/**
	 * <p>
	 * Dummy constructor.
	 * </p>
	 */
	private AeSimpleMD5() {

	}

	/**
	 * <p>
	 * Converts provided bytes in a hex string.
	 * </p>
	 * 
	 * @param data
	 *            Data to be converted
	 * @return Hex string
	 */
	private static String convertToHex(final byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> NUM_4) & NUM_0X0F;
			int twoHalfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= NUM_9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - NUM_10)));
				}
				halfbyte = data[i] & NUM_0X0F;
			} while (twoHalfs++ < 1);
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * <p>
	 * Calculates MD5 hash of the provided string.
	 * </p>
	 * 
	 * @param text
	 *            The text to be hashed
	 * @return The hex string representing the hash
	 * @throws NoSuchAlgorithmException
	 *             No MD5 hashing algorithm implementation is available
	 * @throws UnsupportedEncodingException
	 *             Cannot use UTF-8 (strange)
	 */
	public static String calculateMD5(final String text)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[HASH_LENGTH];
		md.update(text.getBytes("UTF-8"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}
}