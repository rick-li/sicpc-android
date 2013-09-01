package com.sicpc.android.activities;

import java.io.File;

import roboguice.activity.RoboFragmentActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.actions.BookFragment;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.nav.SecondNavController;

public class SubMainActivity extends RoboFragmentActivity  {

	@Inject
	AppConfig appConfig;
	private SecondNavController navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_main);
		
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		BookFragment bookFragment = new BookFragment();

		Uri defaultImageUri = Uri.fromFile(new File("/sdcard/sicpc/yunqijiaoyushiguanjian/zishizhengquebaoankang/default"));
		bookFragment.setImageDir(defaultImageUri);
		ft.add(R.id.leftContent, bookFragment);
		ft.commit();
		
		navController = new SecondNavController(this);
		navController.renderSecondNav(appConfig.getNavNodes().get(0)
				.getChildren());
		
		

	}
}
