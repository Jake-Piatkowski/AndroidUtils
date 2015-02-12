package com.jbpi.androidutils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

public class FileUtils {

	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) throws IOException {

		if (!sourceFile.exists()) {
			return;
		}

		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();

		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}

		if (source != null) {
			source.close();
		}

		if (destination != null) {
			destination.close();
		}
	}

	public static void moveFile(String oldPath, String newPath, String newFilename) {

		// Get the old filename
		String oldFilename =
				oldPath.substring(oldPath.lastIndexOf(File.separatorChar) + 1, oldPath.length());

		// If a new filename is specified, use it for the new file
		String usedFilename = oldFilename;

		if (newFilename != null && !newFilename.equals("")) {

			usedFilename = newFilename;
		}

		// Get the Files
		File fileInDownloads = new File(oldPath);
		File fileInPictures = new File(newPath + File.separatorChar + usedFilename);

		// Remove the existing file
		if (fileInPictures.exists()) {

			fileInPictures.delete();
		}

		// Move it to the new location
		fileInDownloads.renameTo(fileInPictures);
	}

	/**
	 * Reads a text file from the given path into a string. Taken from:
	 * http://stackoverflow.com/questions/6349759/using-json-file-in-android-app-resources
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readStringFromFile(String filePath) throws IOException {

		File file = new File(filePath);
		InputStream inputStream = new FileInputStream(file);
		Writer stringWriter = new StringWriter();
		char[] buffer = new char[1024];

		try {

			Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			int n;

			while ((n = reader.read(buffer)) != -1) {

				stringWriter.write(buffer, 0, n);
			}

			reader.close();

		}
		finally {

			inputStream.close();
		}

		return stringWriter.toString();
	}

	/**
	 * Requires WRITE_EXTERNAL_STORAGE permission.
	 * 
	 * Taken from:
	 * http://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
	 * 
	 * @param path
	 * @param zipname
	 * @return
	 * @throws IOException
	 */
	public static boolean unpackZip(String path, String zipname, String outputPath)
			throws IOException {

		InputStream inputStream;
		ZipInputStream zipInputStream;

		String filename;
		inputStream = new FileInputStream(path + zipname);
		zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
		ZipEntry ze;
		byte[] buffer = new byte[1024];
		int count;

		while ((ze = zipInputStream.getNextEntry()) != null) {

			filename = ze.getName();

			// Need to create directories if not exists, or
			// it will generate an Exception...
			if (ze.isDirectory()) {

				File fmd = new File(outputPath + filename);
				fmd.mkdirs();

				continue;
			}

			FileOutputStream fileOutputStream = new FileOutputStream(outputPath + filename);

			while ((count = zipInputStream.read(buffer)) != -1) {

				fileOutputStream.write(buffer, 0, count);
			}

			fileOutputStream.close();
			zipInputStream.closeEntry();
		}

		zipInputStream.close();

		return true;
	}

	/**
	 * Returns the resource id of the raw if it exists, otherwise 0.
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int resIdFromRaw(Context context, String name) {

		return context.getResources().getIdentifier(name, "raw", context.getPackageName());
	}

	/**
	 * Returns the resource id of the drawable if it exists, otherwise 0.
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int resIdFromDrawable(Context context, String name) {

		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
}
