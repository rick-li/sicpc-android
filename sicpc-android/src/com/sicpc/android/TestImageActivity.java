package com.sicpc.android;

import roboguice.activity.RoboActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.inject.Inject;
import com.sicpc.android.activities.ImageActivity;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.nav.NavNode;

public class TestImageActivity extends RoboActivity {

	@Inject
	private AppConfig appCfg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		NavNode node = appCfg.searchNodeById(this, "test-image");
		Intent i = new Intent(this, ImageActivity.class);
		
		i.setData(node.getActionUri());
		i.putExtra(ImageActivity.DEFAULT_NUM_KEY, 5);
		
		this.startActivity(i);
		
	}

	

}
