package com.sicpc.ane.functions;

import android.content.Context;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.sicpc.android.actions.Action;
import com.sicpc.android.actions.ActionFactory;
import com.sicpc.android.config.AppConfig;
import com.sicpc.ane.Extension;

public class PlayVideoFunction implements FREFunction {
	public static final String TAG = "VideoFunction";
	public static AppConfig appCfg = null;
	private Context ctx;

	@Override
	public FREObject call(FREContext context, FREObject[] args) {

		Extension.extensionContext = context;
		Extension.appContext = context.getActivity().getApplicationContext();
		ctx = Extension.appContext;
		String actionId = null;
		try {
			actionId = args[0].getAsString();
		} catch (Exception e) {
			Log.e(TAG, "Error to parse the arg: " + args[0], e);
		}
		Log.i(TAG, "ID is : " + actionId);
		Action action = ActionFactory.getInstance().getAction(ctx,
				appCfg.searchNodeById(ctx, actionId));
		action.doAction(context.getActivity());
		return null;
	}

}
