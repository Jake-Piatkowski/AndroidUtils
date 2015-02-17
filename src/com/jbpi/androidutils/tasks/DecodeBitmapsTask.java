package com.jbpi.androidutils.tasks;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DecodeBitmapsTask extends AsyncTask<Void, Void, Void> {

	private HashMap<ImageView, String> imageViewsContentFilepaths;
	private HashMap<ImageView, Bitmap> imageViewsBitmaps;

	@Override
	protected Void doInBackground(Void... params) {
		// For each ImageView decode its Bitmap
		for (ImageView imageView : this.imageViewsContentFilepaths.keySet()) {

			if (isCancelled()) {

				break;
			}

			String filepath = this.imageViewsContentFilepaths.get(imageView);
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);
			this.imageViewsBitmaps.put(imageView, bitmap);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// For each ImageView get its Bitmap and set it to said ImageView
		for (ImageView imageView : this.imageViewsBitmaps.keySet()) {

			Bitmap bitmap = this.imageViewsBitmaps.get(imageView);
			imageView.setImageBitmap(bitmap);
		}
		// Clear the collections just to make sure no unnecessary references
		// to any Bitmaps are kept
		this.imageViewsContentFilepaths.clear();
		this.imageViewsBitmaps.clear();
	}

	public DecodeBitmapsTask(HashMap<ImageView, String> viewsContentFilepaths) {

		super();

		this.imageViewsContentFilepaths = viewsContentFilepaths;
		this.imageViewsBitmaps = new HashMap<ImageView, Bitmap>();
	}
}
