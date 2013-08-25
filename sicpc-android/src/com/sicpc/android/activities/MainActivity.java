package com.sicpc.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sicpc.android.R;

public class MainActivity extends Activity {

	protected static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		RelativeLayout root = (RelativeLayout) findViewById(R.id.main_root);
		for (int i = 0; i < root.getChildCount(); i++) {
			ImageView mainIcon = (ImageView) root.getChildAt(i);
			mainIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(TAG, "Main icon is clicked.");
					Intent i = new Intent();
					i.setClass(MainActivity.this, SubMainActivity.class);
					//TODO set data.
					startActivity(i);
				}
			});
		}

	}

}
