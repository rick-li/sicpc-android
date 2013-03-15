package com.sicpc.android.test;

import org.junit.runners.model.InitializationError;

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class RoboInjectedTestRunner extends RobolectricTestRunner {
	public RoboInjectedTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	public void prepareTest(Object test) {
		Application app = Robolectric.application;
		RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, 
	            RoboGuice.newDefaultRoboModule(app), new TestModule());
		Injector injector = RoboGuice.getInjector(app);
		injector.injectMembers(test);
	}
}

class TestModule extends AbstractModule {
    @Override
    protected void configure() {
//        bind(Vibrator.class).toInstance(vibratorMock);
    }
}