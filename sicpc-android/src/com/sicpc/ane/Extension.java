package com.sicpc.ane;

import android.content.Context;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

public class Extension implements FREExtension {
	public static final String TAG = "Extension";

	public static FREContext extensionContext;
	public static Context appContext;

	@Override
	public FREContext createContext(String arg0) {
		Log.i(TAG, "Create Air Extension");
		return new ExtensionContext();
	}

	@Override
	public void dispose() {
		Log.i(TAG, "Dispose Extension");
		appContext = null;
		extensionContext = null;
	}

	@Override
	public void initialize() {
		Log.i(TAG, "Initialize");
	}

}
