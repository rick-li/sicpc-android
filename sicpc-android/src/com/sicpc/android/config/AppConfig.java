package com.sicpc.android.config;

import java.util.List;

import com.sicpc.android.nav.NavNode;

/**
 * 
 * App level configuration.
 * 
 */
public class AppConfig {
	// All data file are relative to this data folder
	private String dataFolder = "";

	private List<NavNode> navNodes;

	public String getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}

	public List<NavNode> getNavNodes() {
		return navNodes;
	}

	public void setNavNodes(List<NavNode> navNodes) {
		this.navNodes = navNodes;
	}

}
