package com.sicpc.android.activities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.test.RoboInjectedTestRunner;

@RunWith(RoboInjectedTestRunner.class)
public class MainActivityTest {

	@Test
	public void shouldHaveHappySmiles() throws Exception {
		MainActivity mainActivity = new MainActivity();
		mainActivity.onCreate(null);
		String hello = mainActivity.getResources().getString(
				R.string.hello_world);
		assertThat(hello, equalTo("Hello world!"));
	}
}
