package com.jbpi.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;

public class ConversionUtils {

	public static float dpToPixel(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return dp * (metrics.densityDpi / 160f);
	}

	public static float pixelToDp(Context context, float pixel) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return pixel * 160f / metrics.densityDpi;
	}

	/**
	 * Taken from:
	 * http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable
	 * -format-in-java
	 * 
	 * @param bytes
	 * @param si
	 *            If true then result in SI, false for binary.
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getHumanReadableByteCount(long bytes, boolean si) {

		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/**
	 * Similar to the method above but always uses the binary units and uses the improper (albeit
	 * more common) abbreviations of these units, e.g. "KB" instead of "KiB".
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getHumanReadableByteCountBinary(long bytes) {

		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		char pre = "KMGTPE".charAt(exp - 1);

		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/**
	 * Taken from: http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex
	 * -string-in-java
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {

			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Taken from: http://stackoverflow.com/questions/12139755/how-to-convert-byte-array-to-integer
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {

		int ub1 = bytes[0] & 0xFF;
		int ub2 = bytes[1] & 0xFF;

		return (ub1 << 8) + ub2;
	}
}
