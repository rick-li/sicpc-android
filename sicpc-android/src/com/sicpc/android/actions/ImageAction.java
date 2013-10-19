package com.sicpc.android.actions;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.sicpc.android.activities.ImageActivity;
import com.sicpc.android.nav.NavNode;

public class ImageAction implements Action {

	private static final String TAG = ImageAction.class.getSimpleName();

	public static final String BOOK_ACTION_KEY = "book_url_key";

	private NavNode node;

	public ImageAction(NavNode node) {
		this.node = node;
	}

	@Override
	public void doAction(Context activity) {

		// not in SubMainActivity, got to SubMain with book dir url.
		Intent i = new Intent();
		i.setClass(activity, ImageActivity.class);
		Uri uri = node.getActionUri();
		if (uri == null || !new File(uri.getPath()).exists()) {
			Log.e(TAG, "目标图片不存在！");
			Toast.makeText(activity, "目标图片不存在！", 1000 * 5);
			return;
		}
		i.setData(uri);
		activity.startActivity(i);

	}

}
