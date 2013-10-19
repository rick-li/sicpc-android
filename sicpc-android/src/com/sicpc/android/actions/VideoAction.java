package com.sicpc.android.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.sicpc.android.actions.video.VideoViewActivity;
import com.sicpc.android.nav.NavNode;

/**
 * 
 * Start a video activity to play video.
 * 
 */
public class VideoAction implements Action {
	private static final String TAG = VideoAction.class.getSimpleName();
	private NavNode node;

	public VideoAction(NavNode node) {
		this.node = node;
	}

	@Override
	public void doAction(Context activity) {
		Intent i = new Intent();
		Uri uri = node.getActionUri();
		if (uri == null) {
			Log.e(TAG, "Video uri not set.");
			Toast.makeText(activity, "视频不存在！", 1000*5);
			return;
		}
		Log.i(TAG, "action uri is " + uri.getPath());
		i.setData(node.getActionUri());
		i.setClass(activity, VideoViewActivity.class);
		activity.startActivity(i);
	}

}
