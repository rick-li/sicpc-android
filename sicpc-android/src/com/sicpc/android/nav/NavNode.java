package com.sicpc.android.nav;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Navigation Node, can be level 1-3.
 * 
 *
 */
public class NavNode {
	public enum Level{FIRST, SECOND, THIRD};
	
	public enum ActionType {BOOK, IMAGE, VIDEO, FLASH};
	
	private Level level;
	
	private String title;
	
	private URL image;
	
	private ActionType actionType;
	
	private URL actionUrl;

	private List<NavNode> children = new ArrayList<NavNode>();
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getImage() {
		return image;
	}

	public void setImage(URL image) {
		this.image = image;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public URL getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(URL actionUrl) {
		this.actionUrl = actionUrl;
	}

	public List<NavNode> getChildren() {
		return children;
	}

	public void setChildren(List<NavNode> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "NavNode [level=" + level + ", title=" + title + ", image="
				+ image + ", actionType=" + actionType + ", actionUrl="
				+ actionUrl + "]";
	}


	
	
}
