package com.sicpc.android.activities;

import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.actions.BookFragment;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.nav.SecondNavController;
import com.sicpc.android.nav.view.ScrollContainer;

public class MainActivity extends RoboFragmentActivity  {

	@Inject
	AppConfig appConfig;
	private SecondNavController navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fixClassLoaderIssue();
		setContentView(R.layout.activity_main);
		navController = new SecondNavController(this);
		navController.renderSecondNav(appConfig.getNavNodes().get(0)
				.getChildren());

		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		Fragment bookFragment = new BookFragment();
		ft.add(R.id.leftContent, bookFragment);
		ft.commit();

		
	}
	
	private static void fixClassLoaderIssue()
	{
	ClassLoader myClassLoader = MainActivity.class.getClassLoader();
	Thread.currentThread().setContextClassLoader(myClassLoader);
	}  
}
