package com.bronzelabs.androidutils;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;

public class Utilities {

	public static float convertDpToPixel(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return dp * (metrics.densityDpi / 160f);
	}

	public static float convertPixelToDp(Context context, float pixel) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return pixel * 160f / metrics.densityDpi;
	}

	public static void openLink(Context context, int linkId) {

		openLink(context, context.getString(linkId));
	}

	public static void openLink(Context context, String link) {

		Uri uri = Uri.parse(link);
		Intent intentCall = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intentCall);
	}

	/**
	 * Generate a string of random alphanumeric characters and given length. Useful for things which
	 * require unique strings.
	 * 
	 * Note: CASE SENSITIVE.
	 * 
	 * Note: length has a high influence on how likely it'll be to generate the same string twice.
	 * The probability factor formula is: 1 / (number_of_possible_chars ^ stringLength).
	 * 
	 * @return String of given length with random alphanumeric chars from the English alphabet.
	 */
	public static String generateOrderId(int stringLength) {
		// Order Id has chars from the following range: [A-Za-z0-9]
		// This means there's 62 possible chars for each char spot in the string
		Random random = new Random();
		StringBuilder orderIdBuilder = new StringBuilder();

		for (int i = 0; i < stringLength; i++) {

			int randomCharIndex = random.nextInt(ALPHANUMERIC_CHARS_ENG.length);
			orderIdBuilder.append(ALPHANUMERIC_CHARS_ENG[randomCharIndex]);
		}

		return orderIdBuilder.toString();
	}

	private static final char[] ALPHANUMERIC_CHARS_ENG = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };
}
