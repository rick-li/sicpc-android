package com.sicpc.android.actions.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sicpc.android.R;

/**
 * Video view activity.
 */

public class VideoViewActivity extends Activity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
	public static final String TAG = VideoViewActivity.class.getSimpleName();

	private VideoView mVideoView;
	private Uri mUri;
	private int mPositionWhenPaused = -1;
	private MediaController mMediaController;
	private Dialog dialog;
	private VideoViewActivity instance = this;


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoview);

		Intent intent = getIntent();
		mUri = intent.getData();

//		dialog = MobileUIUtils.showLoadingProgress(this, "", "Loading video...", false, true, new OnBackPressedListener()
//		{
//			@Override
//			public void onBackPressed(DialogInterface dialog)
//			{
//				instance.onBackPressed();
//			}
//		});

		mVideoView = (VideoView) findViewById(R.id.videoview);
		mMediaController = new MediaController(this);
		mVideoView.setMediaController(mMediaController);
		mVideoView.setVideoURI(mUri);
		addListener();
		onStart();

	}

	private void addListener()
	{
		mVideoView.setOnPreparedListener(new OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				// mVideoView.setBackgroundColor(Color.argb(0, 0, 255, 0));
				mVideoView.requestFocus();

			}
		});
		mVideoView.setOnErrorListener(new OnErrorListener()
		{
			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2)
			{
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				return false;
			}
		});
		mVideoView.setOnCompletionListener(new OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer arg0)
			{
				finish();
			}
		});
	}

	@Override
	public void onStart()
	{
		mVideoView.start();
		super.onStart();
	}

	@Override
	public void onPause()
	{
		// Stop video when the activity is pause.
		mPositionWhenPaused = mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();
		// CHECKSTYLE:OFF
		Log.d(TAG, "OnStop: mPositionWhenPaused = " + mPositionWhenPaused);
		Log.d(TAG, "OnStop: getDuration  = " + mVideoView.getDuration());
		// CHECKSTYLE:ON
		super.onPause();
	}

	@Override
	public void onStop()
	{

		super.onStop();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Log.d(TAG, "back pressed in video");
//		if (dialog.isShowing())
//		{
//			Log.d(TAG, "dialog is showing...");
//			dialog.dismiss();
//		}
		this.finish();
	}

	@Override
	public void onResume()
	{
		// Resume video player
		if (mPositionWhenPaused >= 0)
		{
			mVideoView.seekTo(mPositionWhenPaused);
			mPositionWhenPaused = -1;
		}
		super.onResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		this.finish();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		return false;
	}

}