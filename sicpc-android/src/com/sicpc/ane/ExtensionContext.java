package com.sicpc.ane;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.sicpc.ane.functions.PlayVideoFunction;

public class ExtensionContext extends FREContext {

	public static final String TAG = "ExtentionContext";
	@Override
	public void dispose() {
		Log.i(TAG, "ExtentionContext is Disposed.");
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
		
		functions.put("playVideo", new PlayVideoFunction());
		
		return functions;
	}

}
