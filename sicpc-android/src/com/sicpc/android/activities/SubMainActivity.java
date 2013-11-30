package com.sicpc.android.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.actions.ImageFragment;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.nav.SecondNavController;

public class SubMainActivity extends RoboFragmentActivity {

	private static final String TAG = "SubMainActivity";
	
	@Inject
	AppConfig appConfig;
	private SecondNavController navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "on Create is called.");
//		Bundle extras = this.getIntent().getExtras();
//		if (extras != null) {
////			runBookAction = extras.getBoolean(ImageAction.BOOK_ACTION_KEY);
//			Log.d(TAG, "Book action found");
//		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_main);

		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ImageFragment bookFragment = new ImageFragment();

		copyDefaultBookBg();
		Uri defaultImageUri = Uri.fromFile(new File(
				"/sdcard/sicpc/defaultBookBg"));
		bookFragment.setImageDir(defaultImageUri, 0);
		ft.add(R.id.leftContent, bookFragment);
		ft.commit();

		navController = new SecondNavController(this);
		navController.renderSecondNav(appConfig.getNavNodes().get(0)
				.getChildren());
		
	}

	private void copyDefaultBookBg() {
		try {
			File destDir = new File("/sdcard/sicpc/defaultBookBg");
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			File dest = new File("/sdcard/sicpc/defaultBookBg/dummy.png");
			if (!dest.exists()) {
				dest.createNewFile();
			}
			InputSupplier<InputStream> srcFileSupplier = new InputSupplier<InputStream>() {

				@Override
				public InputStream getInput() throws IOException {
					return SubMainActivity.this.getAssets().open("dummy.png");
				}

			};

			Files.copy(srcFileSupplier, dest);
		} catch (IOException e) {
			Log.e(TAG, "Unable to copy.", e);
		}
	}
}
