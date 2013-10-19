package com.sicpc.android.activities;

import java.io.File;

import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.sicpc.android.R;
import com.sicpc.android.actions.ImageFragment;
import com.viewpagerindicator.CircleCurlIndicator;

import fi.harism.curl.CurlView;

public class ImageActivity extends RoboFragmentActivity {

	protected static final String TAG = ImageActivity.class.getSimpleName();

	public static final String DEFAULT_NUM_KEY = "defaultNumKey";

	private boolean initialized = false;

	private int defaultNum = 0;
	private Uri dirUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		final Uri imageUri = this.getIntent().getData();

		this.setContentView(R.layout.image);
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ImageFragment bookFragment = new ImageFragment();

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

		bookFragment.setImageDir(dirUri, defaultNum);
		ft.add(R.id.leftContent, bookFragment);
		ft.commit();

		LinearLayout leftContent = (LinearLayout) this
				.findViewById(R.id.leftContent);
		leftContent.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (initialized) {
							return;
						}
						CurlView bookView = (CurlView) ImageActivity.this
								.findViewById(R.id.curlView);
						// bookView.setPageProvider(new PageProvider(dirUri));
						// bookView.setCurrentIndex(defaultNum);
						// indicator.
						CircleCurlIndicator indicator = (CircleCurlIndicator) ImageActivity.this
								.findViewById(R.id.image_indicator);

						indicator.setCurler(bookView);
						indicator.setCurrentPage(defaultNum);

					}
				});

	}
}
