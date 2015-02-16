package com.jbpi.androidutils.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

/**
 * AsyncTask which saves the contents of a View into a file at a given path.
 * 
 * @author jake
 * 
 */
public class SaveViewToFileTask extends AsyncTask<Void, Void, Boolean> {

	private View view;
	private String fullFilepath;

	private SaveViewToFileTaskListener saveViewToFileTaskListener;

	private boolean wasSaveSuccessful = true;
	private String message = "";

	@Override
	protected Boolean doInBackground(Void... params) {

		File pictureFile = new File(this.fullFilepath);

		try {

			// Save the picture
			FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
			Bitmap bitmap;

			this.view.setDrawingCacheEnabled(true);
			bitmap = Bitmap.createBitmap(this.view.getDrawingCache());
			this.view.setDrawingCacheEnabled(false);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

			fileOutputStream.close();
			bitmap.recycle();
		}
		catch (FileNotFoundException e) {
			this.wasSaveSuccessful = false;
			this.message = "Error in file path.";
			e.printStackTrace();
		}
		catch (IOException e) {
			this.wasSaveSuccessful = false;
			this.message = "Error when attempting to save file.";
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		if (this.saveViewToFileTaskListener != null) {

			this.saveViewToFileTaskListener.finishedSavingViewToFile(this.wasSaveSuccessful,
					this.message);
		}
	}

	public SaveViewToFileTask(View view, String fullFilepath,
			SaveViewToFileTaskListener saveViewToFileTaskListener) {

		super();

		this.view = view;
		this.fullFilepath = fullFilepath;
		this.saveViewToFileTaskListener = saveViewToFileTaskListener;
	}

	public interface SaveViewToFileTaskListener {

		public void finishedSavingViewToFile(boolean wasSaveSuccessful, String message);
	}
}
