package com.sicpc.android.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.actions.ImageFragment;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.nav.NavNode;
import com.sicpc.android.nav.SecondNavController;

public class SubMainActivity extends RoboFragmentActivity {
	public static final String CONTENT_ID = "content_id";
	private static final String TAG = "SubMainActivity";

	@Inject
	AppConfig appConfig;
	private SecondNavController navController;

	@InjectView(R.id.sub_main_root)
	private RelativeLayout layoutRoot;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "on Create is called.");

		super.onCreate(savedInstanceState);
		String contentId = this.getIntent().getExtras().getString(CONTENT_ID);
		NavNode contentNode = null;
		for(int i=0; i< appConfig.getNavNodes().size(); i++){
			NavNode n = appConfig.getNavNodes().get(i);
			if(contentId.equals(n.getId())){
				contentNode = n;
			}
			
		}
		
		setContentView(R.layout.sub_main);
		String bgImagePath = appConfig.getDataFolder() + "/" + contentNode.getNode().getAttributes().getNamedItem("bg").getNodeValue();
		String bookBgPath = appConfig.getDataFolder() + "/" + contentNode.getNode().getAttributes().getNamedItem("bookbg").getNodeValue();
		layoutRoot.setBackgroundDrawable(Drawable.createFromPath(bgImagePath));
		
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ImageFragment bookFragment = new ImageFragment();

		copyDefaultBookBg();
		Uri defaultImageUri = Uri.fromFile(new File(
				"/sdcard/sicpc/defaultBookBg"));
		bookFragment.setImageDir(defaultImageUri, 0);
		ft.add(R.id.leftContent, bookFragment);
		ft.commit();

		navController = new SecondNavController(this, bookBgPath);
		navController.renderSecondNav(contentNode.getChildren());

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
