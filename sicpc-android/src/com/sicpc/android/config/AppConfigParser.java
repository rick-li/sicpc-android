package com.sicpc.android.config;

import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/**
 * 
 * Parse app_config.xml.
 * 
 */
public class AppConfigParser {
	private static String TAG = "AppConfigParser";
	private AppConfig cfg;
	private URL configXml;
//	private Resources resources;

	public AppConfig parseConfigXml() throws Exception{
		cfg = new AppConfig();
		XmlPullParser xml = XmlPullParserFactory.newInstance().newPullParser();
		xml.setInput(configXml.openStream(), "UTF-8");
		int eventType = -1;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			
			if (eventType == XmlPullParser.START_TAG) {
				String strNode = xml.getName();
				if (strNode.equals("dataFolder"))
				{
					cfg.setDataFolder(xml.nextText());
				}
			}
			eventType = xml.next();
		}
		return cfg;
	}

	public URL getConfigXml() {
		return configXml;
	}

	public void setConfigXml(URL configXml) {
		this.configXml = configXml;
	}

	public AppConfig getCfg() {
		return cfg;
	}

	public void setCfg(AppConfig cfg) {
		this.cfg = cfg;
	}

//	public Resources getResources() {
//		return resources;
//	}
//
//	public void setResources(Resources resources) {
//		this.resources = resources;
//	}

}
