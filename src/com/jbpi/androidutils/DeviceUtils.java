package com.jbpi.androidutils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.WindowManager;

import com.jrummyapps.android.os.Devices;

public class DeviceUtils {

	public static int getScreenHeight(Context context) {

		WindowManager windowManager =
				(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point pointSize = new Point();
		display.getSize(pointSize);

		return pointSize.y;
	}

	public static int getScreenHeightWithoutActionBar(Context context) {

		return getScreenHeight(context) - getActionBarHeight(context);
	}

	public static int getScreenWidth(Context context) {

		WindowManager windowManager =
				(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point pointSize = new Point();
		display.getSize(pointSize);

		return pointSize.x;
	}

	public static int getActionBarHeight(Context context) {

		final TypedArray styledAttributes =
				context.getTheme().obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		return actionBarHeight;
	}

	/**
	 * Only works properly if the status bar is there.
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {

		int resourceId =
				context.getResources().getIdentifier("status_bar_height", "dimen", "android");

		return context.getResources().getDimensionPixelSize(resourceId);
	}

	public static String getUserFriendlyDeviceName() {

		return Devices.getDeviceName();
	}

	/**
	 * Checks if the device is online.
	 * 
	 * @return True if the device is connected to the Internet.
	 */
	public static boolean isInternetEnabled(Context context) {

		ConnectivityManager connectivityManager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {

			return false;
		}

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

			return true;
		}

		return false;
	}

	/**
	 * Checks if the Location Services are available by checking if the GPS and Networking are ON.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isLocationServicesEnabled(Context context) {

		LocationManager locationManager =
				(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean isLocationGpsEnabled = false;
		boolean isLocationWifiEnabled = false;

		try {

			isLocationGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isLocationWifiEnabled =
					locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		catch (Exception e) {
		}

		if (!isLocationGpsEnabled && !isLocationWifiEnabled) {

			return false;
		}

		return true;
	}

	/**
	 * Checks if the device's bluetooth is on.
	 * 
	 * @return True if bluetooth turned on.
	 */
	public static boolean isBluetoothEnabled() {

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return bluetoothAdapter.isEnabled();
	}

	public static boolean isAppInstalled(Context context, String packagename) {

		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packagename);

		if (intent != null) {

			return true;
		}
		else {

			return false;
		}
	}

	public static void sendNotification(Context context, int resSound, int resStatIcon,
			Intent intent, int notificationId, String title, String text) {

		// Get the Uri to the sound file
		Uri uriSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + resSound);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(context).setSmallIcon(resStatIcon)
						.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
						.setSound(uriSound).setContentTitle(title).setContentText(text)
						.setAutoCancel(true);

		// Pass in a random id since there's stupid results when using the same number every time
		int pendingIntentId = (int) System.currentTimeMillis();
		PendingIntent pendingIntent =
				PendingIntent.getActivity(context, pendingIntentId, intent, 0);
		notificationBuilder.setContentIntent(pendingIntent);
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// An ID allows you to update the notification later on.
		notificationManager.notify(notificationId, notificationBuilder.build());
	}

	public static void cancelNotification(Context context, int notificationId) {

		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}
}
