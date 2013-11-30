package com.sicpc.android.activities;

import java.io.File;
import java.util.Arrays;

import roboguice.activity.RoboFragmentActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sicpc.android.R;
import com.viewpagerindicator.CirclePageIndicator;

public class ImageActivity extends RoboFragmentActivity {

	protected static final String TAG = ImageActivity.class.getSimpleName();

	public static final String DEFAULT_NUM_KEY = "defaultNumKey";

	private int defaultNum = 0;
	private Uri dirUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		final Uri imageUri = this.getIntent().getData();

		this.setContentView(R.layout.image);

		String path = imageUri.getPath();
		File f = new File(path);
		if (f.isDirectory()) {
			dirUri = imageUri;
		} else {
			File p = new File(f.getParent());
			dirUri = Uri.fromFile(p);
		}

		File dirF = new File(dirUri.getPath());
		String destImg = new File(imageUri.getPath()).getName();
		File[] dirFiles = dirF.listFiles();
		Arrays.sort(dirFiles);
		Log.d(TAG, "destImg " + destImg);
		for (int i = 0; i < dirFiles.length; i++) {
			File file = dirFiles[i];
			String filename = file.getName();
			if (filename.endsWith(".png") || filename.endsWith(".jpg")
					|| filename.endsWith(".gif")) {
				Log.d(TAG, "filename " + filename);
				if (destImg.equals(filename)) {
					defaultNum = i;
					break;
				}
			}
		}

		ViewPager mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new ImagePageAdapter(this, dirFiles));
		mPager.setCurrentItem(defaultNum);
		CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.image_indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setCurrentItem(defaultNum);

	}

	private class ImagePageAdapter extends PagerAdapter {
		Context context;
		File[] mImages;

		public ImagePageAdapter(Context learn, File[] images) {
			context = learn;
			mImages = images;
		}

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(context);
			image.setImageURI(Uri.fromFile(mImages[position]));
			((ViewPager) container).addView(image);
			if (position >= 1) {
				image.setImageURI(Uri.fromFile(mImages[position]));
			}
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

	}
}
