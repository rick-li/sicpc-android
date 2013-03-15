package com.sicpc.android.activities;

import java.io.File;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.sicpc.android.R;
import com.sicpc.android.config.AppConfig;

public class OpenActivity extends RoboActivity {
	private static final String TAG = "OpenActivity";
	
	@Inject AppConfig appConfig;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open);
		
		
		Button skipBtn = (Button)this.findViewById(R.id.skip_btn);
		skipBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				gotoMainScreen();
			}
			
		});
		
		String openFile = appConfig.getDataFolder()+"/"+appConfig.getOpenVideo();
		Log.i(TAG, "Open file is "+openFile);
		VideoView openVideo = (VideoView)this.findViewById(R.id.open_video);
		openVideo.setVideoURI(Uri.fromFile(new File(openFile)));
		openVideo.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				gotoMainScreen();
			}
			
		});
		openVideo.start();
	}
	
	private void gotoMainScreen(){
		Log.i(TAG, "To Main Screen.");
		Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(i);
	}
}
