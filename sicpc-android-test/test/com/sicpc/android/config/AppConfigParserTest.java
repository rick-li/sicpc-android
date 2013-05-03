package com.sicpc.android.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.sicpc.android.nav.NavNode;
import com.sicpc.android.test.RoboInjectedTestRunner;

@RunWith(RoboInjectedTestRunner.class)
public class AppConfigParserTest {

	@Inject AppConfigProvider parser;
	
	public void parseConfigXml() {
		URL configXml = AppConfigParserTest.class
				.getResource("test_config.xml");
		try {
			parser.setConfigXml(configXml.openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		parser.setResources(new SicpcApp().getResources());
		
		AppConfig cfg = null;
		try {
			cfg = parser.parseConfigXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThat("/sdcard/test", equalTo(cfg.getDataFolder()));
		
		List<NavNode> firstNodes = cfg.getNavNodes();
		Assert.assertEquals(1, firstNodes.size());
		
		NavNode firstNode = firstNodes.get(0);
		Assert.assertEquals("level1", firstNode.getTitle());
		List<NavNode> secondNodes = firstNode.getChildren();
		Assert.assertEquals(1, secondNodes.size());
		Assert.assertEquals("level2", secondNodes.get(0).getTitle());
		
		Assert.assertEquals(3, secondNodes.get(0).getChildren().size());
		
	}
}
