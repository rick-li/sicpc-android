package com.sicpc.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.os.AsyncTask;
import android.util.Log;

import com.google.common.io.ByteStreams;

public class UnZipper extends Observable {

	private static final String TAG = "UnZip";
	private String mDestinationPath;
	InputStream sourceIS;

	public UnZipper(InputStream sourceIS, String destinationPath) {
		this.sourceIS = sourceIS;
		mDestinationPath = destinationPath;
	}

	public String getDestinationPath() {
		return mDestinationPath;
	}

	public void unzip() {

		Log.d(TAG, "unzipping to " + mDestinationPath);
		new UnZipTask().execute(mDestinationPath);
	}

	private class UnZipTask extends AsyncTask<String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			final String destinationPath = params[0];
			final ZipInputStream zis;
			try {
				zis = new ZipInputStream(sourceIS);
				for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis
						.getNextEntry()) {
					publishProgress(entry.toString());
					unzipEntry(zis, entry, destinationPath);

				}
				zis.close();
			} catch (Exception e) {
				Log.e(TAG, "Error while extracting zip stream", e);
				return false;
			}

			return true;
		}

		@Override
		protected void onProgressUpdate(String... extractingFile) {
			setChanged();
			notifyObservers(extractingFile[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			setChanged();
			notifyObservers("finished");
		}

		private void unzipEntry(ZipInputStream zis, ZipEntry entry,
				String outputDir) throws IOException {

			if (entry.isDirectory()) {
				createDir(new File(outputDir, entry.getName()));
				return;
			}

			File outputFile = new File(outputDir, entry.getName());
			if (!outputFile.getParentFile().exists()) {
				createDir(outputFile.getParentFile());
			}

			Log.v(TAG, "Extracting: " + entry);

			BufferedInputStream inputStream = new BufferedInputStream(zis);
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(outputFile));

			try {
				ByteStreams.copy(inputStream, outputStream);
			} finally {
				outputStream.close();
				// inputStream.close();
			}
		}

		private void createDir(File dir) {
			if (dir.exists()) {
				return;
			}
			Log.v(TAG, "Creating dir " + dir.getName());
			if (!dir.mkdirs()) {
				throw new RuntimeException("Can not create dir " + dir);
			}
		}
	}
}