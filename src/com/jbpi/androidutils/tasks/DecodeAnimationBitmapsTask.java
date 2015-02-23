package com.jbpi.androidutils.tasks;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DecodeAnimationBitmapsTask extends AsyncTask<Void, Void, Void> {

	private Resources resources;

	private ImageView imageView;

	private int resAnimDrawable;
	private Drawable drawableAnim;

	@Override
	protected Void doInBackground(Void... params) {

		if (isCancelled()) {

			return null;
		}

		this.drawableAnim = this.resources.getDrawable(this.resAnimDrawable);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		this.imageView.setImageDrawable(this.drawableAnim);

		this.resources = null;
	}

	public DecodeAnimationBitmapsTask(ImageView imageView, int resAnimDrawable, Resources resources) {

		super();

		this.resources = resources;
		this.imageView = imageView;
		this.resAnimDrawable = resAnimDrawable;
	}
}
