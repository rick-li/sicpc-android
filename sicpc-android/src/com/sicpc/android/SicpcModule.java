package com.sicpc.android;

import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.config.AppConfigProvider;

public class SicpcModule extends AbstractModule {

	@Override
	protected void configure() {
		Log.i("MODULE", "Configuraring module.");
//		bind(AppConfig.class).toProvider(AppConfigProvider.class).in(
//				Singleton.class);
	}

}
