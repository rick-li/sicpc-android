package com.sicpc.android.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.sicpc.android.actions.video.VideoViewActivity;
import com.sicpc.android.nav.NavNode;

/**
 * 
 * Start a video activity to play video.
 * 
 */
public class VideoAction implements Action {
	private static final String TAG = VideoAction.class.getSimpleName();
	private Context ctx;
	private NavNode node;

	public VideoAction(Context ctx, NavNode node) {
		this.ctx = ctx;
		this.node = node;
	}

	@Override
	public void doAction() {
		Intent i = new Intent();
		Uri uri = node.getActionUri();
		if (uri == null) {
			Log.e(TAG, "Video uri not set.");
		}
		Log.i(TAG, "action uri is " + uri.getPath());
		i.setData(node.getActionUri());
		i.setClass(ctx, VideoViewActivity.class);
		ctx.startActivity(i);
	}

}
