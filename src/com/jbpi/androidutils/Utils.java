package com.jbpi.androidutils;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.Time;

public class Utils {

	public static boolean isEmpty(String string) {

		if (string == null || string.equals("")) {

			return true;
		}

		return false;
	}

	public static long getCurrentTime() {

		Time timeNow = new Time();
		timeNow.setToNow();

		return timeNow.toMillis(false);
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
	 * Shows a simple alert dialog with an OK button.
	 * 
	 * @param context
	 *            Current context.
	 * @param title
	 *            Title of the dialog.
	 * @param message
	 *            Message in the dialog.
	 */
	public static void showAlertDialog(Context context, String title, String message) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title).setMessage(message).setPositiveButton("OK", null);

		AlertDialog dialog = alertDialogBuilder.create();
		dialog.show();
	}

	/**
	 * Parses BluetoothLE device advertisement packets to return most basic profile data: UUID,
	 * major and minor.
	 * 
	 * @param adPacket
	 *            An array of bytes read straight from the BluetoothAdapter.
	 * 
	 * @return An array of strings where the 1st cell is the UUID, 2nd - major and 3rd - minor.
	 */
	public static String[] parseBeaconAdvertisementPacket(byte[] adPacket) {

		String[] parsedBeaconId = new String[3];
		int indexUuid = -1;

		if (adPacket.length < 10) {
			// TODO - jpiatkow - What happens when a packet shorter than 5 gets passed in here?
		}

		for (int i = 2; i <= 5; i++) {

			if (adPacket[i] == IBEACON_ID_BYTES[0] && adPacket[i + 1] == IBEACON_ID_BYTES[1]
					&& adPacket[i + 2] == IBEACON_ID_BYTES[2]
					&& adPacket[i + 3] == IBEACON_ID_BYTES[3]) {

				indexUuid = i + IBEACON_ID_BYTES.length;
				break;
			}
		}

		// Return abnormal values if not an iBeacon packet
		if (indexUuid == -1) {

			parsedBeaconId[0] = String.valueOf(-1);
			parsedBeaconId[1] = String.valueOf(-1);
			parsedBeaconId[2] = String.valueOf(-1);

			return parsedBeaconId;
		}

		// Get the beacon UUID as a string of hex digits
		byte[] byteUuid =
				Arrays.copyOfRange(adPacket, indexUuid, indexUuid + BEACON_UUID_BYTE_COUNT);
		parsedBeaconId[0] = ConversionUtils.bytesToHex(byteUuid);

		// Get the beacon major as a string from an int
		int indexMajor = indexUuid + BEACON_UUID_BYTE_COUNT;
		byte[] byteMajor =
				Arrays.copyOfRange(adPacket, indexMajor, indexMajor + BEACON_MAJOR_BYTE_COUNT);
		parsedBeaconId[1] = String.valueOf(ConversionUtils.bytesToInt(byteMajor));

		// Get the beacon minor as a string from an int
		int indexMinor = indexMajor + BEACON_MAJOR_BYTE_COUNT;
		byte[] byteMinor =
				Arrays.copyOfRange(adPacket, indexMinor, indexMinor + BEACON_MINOR_BYTE_COUNT);
		parsedBeaconId[2] = String.valueOf(ConversionUtils.bytesToInt(byteMinor));

		return parsedBeaconId;
	}

	private static final int BEACON_UUID_BYTE_COUNT = 16;
	private static final int BEACON_MAJOR_BYTE_COUNT = 2;
	private static final int BEACON_MINOR_BYTE_COUNT = 2;
	private static final byte[] IBEACON_ID_BYTES = { 0x4c, 0x00, 0x02, 0x15 };

	/**
	 * Converts milliseconds to time in format (m)m:ss.
	 * 
	 * Taken from:
	 * http://stackoverflow.com/questions/4802398/determining-the-duration-and-format-of-
	 * an-audio-file
	 * 
	 * @param time
	 *            In milliseconds.
	 * @return Minutes, colon, seconds (as 2 digits).
	 */
	@SuppressLint("DefaultLocale")
	public static String getFormattedAudioDuration(int time) {

		return String.format(
				"%01d:%02d",
				TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
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
