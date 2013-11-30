package com.sicpc.android.actions;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sicpc.android.R;

import fi.harism.curl.CurlPage;
import fi.harism.curl.CurlView;

public class ImageFragment extends Fragment {
	protected static final String TAG = "ImageFragment";
	private CurlView mCurlView;
	private Uri imageDir;
	private int index;

	public void setImageDir(Uri imageDir, int index) {
		this.imageDir = imageDir;
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Activity activity = this.getActivity();
		// int index = 0;
		// if (activity.getLastNonConfigurationInstance() != null) {
		// index = (Integer) activity.getLastNonConfigurationInstance();
		// }

		// this.getActivity().getIntent().getE

		FrameLayout root = (FrameLayout) inflater.inflate(
				R.layout.image_content, null);
		mCurlView = (CurlView) root.findViewById(R.id.curlView);
		mCurlView.setPageProvider(new PageProvider(imageDir));
		mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		mCurlView.setCurrentIndex(index);
		mCurlView.setBackgroundColor(Color.TRANSPARENT);

		GestureDetector doubleTapDetector = new GestureDetector(activity,
				new SimpleOnGestureListener() {
					public boolean onDoubleTap(MotionEvent e) {
						mCurlView.getmRenderer().exitPinch();
						mCurlView.requestRender();
						mCurlView.setScaling(false);
						mCurlView.setScaled(false);
						mCurlView.setScaleFactor(1f);
						return true;
					}
				});

		ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(
				activity, new OnScaleGestureListener() {

					@Override
					public boolean onScale(ScaleGestureDetector detector) {

						mCurlView.onScale(detector.getScaleFactor());
						return true;
					}

					@Override
					public boolean onScaleBegin(ScaleGestureDetector detector) {
						mCurlView.setScaling(true);
						return true;
					}

					@Override
					public void onScaleEnd(ScaleGestureDetector detector) {
						mCurlView.setScaling(false);
					}

				});
		mCurlView.setScaleDetector(mScaleDetector);
		mCurlView.setDoubleTapDetector(doubleTapDetector);

		return root;
	}

	/**
	 * Bitmap provider.
	 */
	public static class PageProvider implements CurlView.PageProvider {
		File[] imageList;

		public PageProvider(Uri imageDir) {
			File imageDirF = new File(imageDir.getPath());
			Log.i(TAG, "Page Provider dir is " + imageDirF.getAbsolutePath()
					+ " exists " + imageDirF.exists());
			imageList = imageDirF.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {

					if (filename.endsWith("png") || filename.endsWith("jpg")) {
						Log.i(TAG, "Found file " + filename);
						return true;
					}
					return false;
				}
			});
			Arrays.sort(imageList);
		}

		@Override
		public int getPageCount() {
			return imageList.length;
		}

		private Bitmap loadBitmap(int width, int height, int index) {
			Bitmap b = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			b.eraseColor(0xFFFFFFFF);
			Canvas c = new Canvas(b);

			Drawable d = Drawable.createFromPath(imageList[index]
					.getAbsolutePath());

			int margin = 1;
			int border = 0;
			Rect r = new Rect(margin, margin, width - margin, height - margin);

			int imageWidth = r.width() - (border * 2);
			int imageHeight = imageWidth * d.getIntrinsicHeight()
					/ d.getIntrinsicWidth();
			if (imageHeight > r.height() - (border * 2)) {
				imageHeight = r.height() - (border * 2);
				imageWidth = imageHeight * d.getIntrinsicWidth()
						/ d.getIntrinsicHeight();
			}

			r.left += ((r.width() - imageWidth) / 2) - border;
			r.right = r.left + imageWidth + border + border;
			r.top += ((r.height() - imageHeight) / 2) - border;
			r.bottom = r.top + imageHeight + border + border;

			Paint p = new Paint();
			p.setColor(0xFFC0C0C0);
			c.drawRect(r, p);
			r.left += border;
			r.right -= border;
			r.top += border;
			r.bottom -= border;

			d.setBounds(r);
			d.draw(c);

			return b;
		}

		@Override
		public void updatePage(CurlPage page, int width, int height, int index) {
			Bitmap front = loadBitmap(width, height, index);
			page.setTexture(front, CurlPage.SIDE_FRONT);
			page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK);

		}

	}

	/**
	 * CurlView size changed observer.
	 */
	private class SizeChangedObserver implements CurlView.SizeChangedObserver {
		@Override
		public void onSizeChanged(int w, int h) {

			mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
			// mCurlView.setMargins(.1f, .1f, .1f, .1f);
		}
	}
}
