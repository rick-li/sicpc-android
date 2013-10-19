package com.sicpc.android.config;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.sicpc.android.nav.NavNode;

/**
 * 
 * App level configuration.
 * 
 */
@Singleton
public class AppConfig {
	private static final String TAG = "AppConfig";
	// All data file are relative to this data folder
	private String dataFolder = "";
	private String openVideo = "";
	private List<NavNode> navNodes;
	private Map<String, NavNode> idNodeMap = Maps.newHashMap();
	private List<String> errorList = Lists.newArrayList();

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
		createIndex(navNodes);
	}

	private void createIndex(List<NavNode> navNodes) {
		for (NavNode node : navNodes) {
			List<NavNode> child = node.getChildren();
			if (child != null && child.size() > 0) {
				Log.d(TAG, "goto child level for " + node.getTitle());
				createIndex(child);
			} else {
				if (node != null) {
					Log.d(TAG, "put to map " + node.getId());
					idNodeMap.put(node.getId(), node);
				}
			}
		}
	}

	public NavNode searchNodeById(Context ctx, String actionId) {
		Log.i(TAG, "Search action by Id " + actionId);
		NavNode node = this.getIdNodeMap().get(actionId);
		if (node == null) {
			Log.w(TAG, "Node not found " + actionId);
			return null;
		} else {
			Log.i(TAG, "Node found " + node.getTitle());
			return node;
		}
	}
	
	public Map<String, NavNode> getIdNodeMap() {
		return idNodeMap;
	}

	public void setIdNodeMap(Map<String, NavNode> idNodeMap) {
		this.idNodeMap = idNodeMap;
	}

	public String getOpenVideo() {
		return openVideo;
	}

	public void setOpenVideo(String openVideo) {
		this.openVideo = openVideo;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	
	

}
