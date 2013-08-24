package com.sicpc.ane.functions;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

import com.sicpc.android.actions.video.VideoViewActivity;
import com.sicpc.ane.Extension;

public class PlayVideoFunction implements FREFunction {
	public static final String TAG = "VideoFunction";

	@Override
	public FREObject call(FREContext context, FREObject[] args) {
		Log.i(TAG, "HELLO Call Play video function.");

		Extension.extensionContext = context;
		Extension.appContext = context.getActivity().getApplicationContext();
		String fileName = null;
		try {
			fileName = args[0].getAsString();

		} catch (Exception e) {
			Log.e(TAG, "Error to parse the arg: " + args[0], e);
		}
		Log.i(TAG, "Calling fileName: " + fileName);
		Intent intent = new Intent();
		intent.setData(Uri.parse(""));
		intent.setClass(context.getActivity(), VideoViewActivity.class);
		Log.i(TAG, "Setting intent.");
		context.getActivity().startActivity(intent);
		Log.i(TAG, "Starting activity.");
		return null;
	}

}
