package com.sicpc.android.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sicpc.android.SicpcApp;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AppConfigParserTest {

	AppConfigParser parser;

	@Before
	public void init() {
		parser = new AppConfigParser();
	}

	@Test
	public void parseConfigXml() {
		URL configXml = AppConfigParserTest.class
				.getResource("test_config.xml");
		parser.setConfigXml(configXml);
//		parser.setResources(new SicpcApp().getResources());
		AppConfig cfg = null;
		try {
			cfg = parser.parseConfigXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat("/sdcard/test", equalTo(cfg.getDataFolder()));

	}
}
