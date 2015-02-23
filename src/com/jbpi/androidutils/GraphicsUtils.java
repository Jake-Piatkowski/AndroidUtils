package com.jbpi.androidutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class GraphicsUtils {

	/**
	 * Returns the height of the bitmap for current screen density.
	 * 
	 * NOTE: this method should be used once the layout is established - e.g. in
	 * #onWindowFocusChanged.
	 * 
	 * @param context
	 * @param resourceId
	 *            of the bitmap drawable.
	 * @return Height in pixels.
	 */
	public static int getBitmapHeight(Context context, int resourceId) {

		BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
		bitmapFactoryOptions.inTargetDensity =
				context.getResources().getDisplayMetrics().densityDpi;
		Bitmap bitmap =
				BitmapFactory.decodeResource(context.getResources(), resourceId,
						bitmapFactoryOptions);

		return bitmap.getHeight();
	}

	/**
	 * Converts a given View into a Bitmap drawable.
	 * 
	 * Taken from:
	 * http://www.nasc.fr/android/android-using-layout-as-custom-marker-on-google-map-api/
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(Context context, View view) {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();

		Bitmap bitmap =
				Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
						Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	/**
	 * Recycles Bitmap used in ImageView's Drawable. If the Drawable is of type AnimationDrawable,
	 * then the Bitmap of every one of its frames will be recycled.
	 * 
	 * @param imageView
	 */
	public static void recycleBitmap(ImageView imageView) {

		if (imageView == null || imageView.getDrawable() == null) {

			return;
		}

		if (imageView.getDrawable() instanceof BitmapDrawable) {

			BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			GraphicsUtils.recycleBitmapDrawable(bitmapDrawable);
		}
		else if (imageView.getDrawable() instanceof AnimationDrawable) {

			AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();

			for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {

				BitmapDrawable bitmapDrawableFrame = (BitmapDrawable) animationDrawable.getFrame(i);
				GraphicsUtils.recycleBitmapDrawable(bitmapDrawableFrame);
			}
		}
		else {

			Log.e("AndroidUtils",
					"GraphicsUtils.recycleBitmap could NOT recycle Bitmap - unknown type of drawable");
		}
	}

	private static void recycleBitmapDrawable(BitmapDrawable bitmapDrawable) {

		if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null) {

			bitmapDrawable.getBitmap().recycle();
			bitmapDrawable = null;
		}
	}

	public static LayoutInflater getLayoutInflater(Context context) {

		return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
}
