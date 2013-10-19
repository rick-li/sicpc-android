package com.sicpc.android.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.inject.Provider;
import com.sicpc.android.nav.NavNode;
import com.sicpc.android.nav.NavNode.ActionType;
import com.sicpc.ane.functions.PlayVideoFunction;

/**
 * 
 * Parse app_config.xml.
 * 
 */
public class AppConfigProvider implements Provider<AppConfig> {
	private static String TAG = "AppConfigParser";
	private AppConfig cfg;
	private InputStream configXmlIs;

	@Inject
	private Context context;

	private List<String> errorList = Lists.newArrayList();

	public AppConfig parseConfigXml() throws Exception {
		cfg = new AppConfig();
		if (configXmlIs == null) {

			File storageDir = Environment.getExternalStorageDirectory();
			String appConfigXml = storageDir + "/sicpc/app_config.xml";
			File file = new File(appConfigXml);
			if (!file.exists()) {
				// throw new IllegalStateException(
				// "app_config.xml doesn't exist in " + directory
				// + "/sicpc");
				this.errorList.add("严重错误: " + appConfigXml + " 不存在！");
			}
			configXmlIs = new FileInputStream(file);
		}

		DocumentBuilder domBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = domBuilder.parse(configXmlIs);
		Node openVideoNode = doc.getElementsByTagName("openVideo").item(0);
		if (openVideoNode == null) {
			Log.w(TAG,
					"openVideo is not set in app_config.xml, default to open.mp4");
			cfg.setOpenVideo("open.mp4");
		} else {
			cfg.setOpenVideo(openVideoNode.getTextContent());
			Log.i(TAG, "openVideo -> " + cfg.getOpenVideo());
		}
		Node dataFolderNode = doc.getElementsByTagName("dataFolder").item(0);
		if (dataFolderNode == null) {
			Log.w(TAG,
					"DataFolder is not set in app_config.xml, default to /sdcard/sicpc");
			cfg.setDataFolder("/sdcard/sicpc");

		} else {
			cfg.setDataFolder(dataFolderNode.getTextContent());
			Log.i(TAG, "dataFolder -> " + cfg.getDataFolder());
		}
		Node navRoot = doc.getElementsByTagName("nav").item(0);
		if (navRoot == null) {
			Log.w(TAG, "navRoot is not set in app_config.xml.");
		}
		List<NavNode> navNodes = new ArrayList<NavNode>();
		for (int i = 0; i < navRoot.getChildNodes().getLength(); i++) {
			Node node = navRoot.getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				navNodes.add(parseNavNode(node));
			}

		}
		cfg.setNavNodes(navNodes);
		cfg.setErrorList(errorList);
		return cfg;
	}

	private boolean isFileExists(String filename) {
		return new File(filename).exists();
	}

	private NavNode parseNavNode(Node navRoot) {
		NavNode navNode = new NavNode();
		try {

			Node idNode = navRoot.getAttributes().getNamedItem("id");
			if (idNode != null) {
				navNode.setId(idNode.getNodeValue());
				Log.d(TAG, "Set id for " + navNode.getId());
			}

			Node titleNode = navRoot.getAttributes().getNamedItem("title");
			if (titleNode != null) {
				navNode.setTitle(titleNode.getNodeValue());
			}

			Node imageNode = navRoot.getAttributes().getNamedItem("image");
			if (imageNode != null) {
				File imgFile = new File(cfg.getDataFolder() + "/"
						+ imageNode.getNodeValue());
				if (!imgFile.exists()) {
					Log.e(TAG, "Img file " + imgFile.getAbsolutePath()
							+ " doesn't exist.");
					errorList
							.add("图片标题: " + imgFile.getAbsolutePath() + "不存在！");
				}
				navNode.setImage(Uri.fromFile(imgFile));
			}
			Node actionTypeNode = navRoot.getAttributes().getNamedItem(
					"actionType");
			if (actionTypeNode != null) {
				ActionType actionType = ActionType.valueOf(actionTypeNode
						.getNodeValue().toString().toUpperCase());
				navNode.setActionType(actionType);
				Log.i(TAG, "Node " + navNode.getTitle() + " action type is "
						+ actionType);
			}

			Node actionNode = navRoot.getAttributes()
					.getNamedItem("actionFile");
			if (actionNode != null) {
				File actionFile = new File(cfg.getDataFolder() + "/"
						+ actionNode.getNodeValue());
				if (actionFile.exists()) {
					navNode.setActionUri(Uri.fromFile(actionFile));
				} else {
					errorList.add("目标文件不存在: " + actionFile.getAbsolutePath()
							+ "不存在！");
					Log.w(TAG,
							"actionFile doesn't exist "
									+ actionFile.getAbsolutePath());
				}
			}

			// navNode.setLevel(NavNode.Level.valueOf(navRoot.getAttributes().getNamedItem("title").getNodeValue()));
			NodeList childNodes = navRoot.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					navNode.getChildren().add(parseNavNode(node));
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Fail to parse " + navRoot.toString(), e);
		}
		return navNode;
	}

	public InputStream getConfigXml() {
		return configXmlIs;
	}

	public void setConfigXml(InputStream configXml) {
		this.configXmlIs = configXml;
	}

	public AppConfig getCfg() {
		return cfg;
	}

	public void setCfg(AppConfig cfg) {
		this.cfg = cfg;
	}

	@Override
	public AppConfig get() {
		Log.i(TAG, "AppConfigProvider get.");
		if (this.cfg == null) {
			try {
				this.cfg = this.parseConfigXml();
			} catch (Exception e) {
				Log.e(TAG, "Failed to parse config xml.", e);
			}
		}
		PlayVideoFunction.appCfg = cfg;
		return this.cfg;
	}

	// public Resources getResources() {
	// return resources;
	// }
	//
	// public void setResources(Resources resources) {
	// this.resources = resources;
	// }

}
