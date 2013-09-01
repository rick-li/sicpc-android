package com.sicpc.android.nav;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

/**
 * 
 * Navigation Node, can be level 1-3.
 * 
 *
 */
public class NavNode {
	public enum Level{FIRST, SECOND, THIRD};
	
	public enum ActionType {BOOK, IMAGE, VIDEO, FLASH};
	
	private String id;
	
	private Level level;
	
	private String title;
	
	private Uri image;
	
	private ActionType actionType;
	
	private Uri actionUri;

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



	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}


	public Uri getImage() {
		return image;
	}

	public void setImage(Uri image) {
		this.image = image;
	}

	public Uri getActionUri() {
		return actionUri;
	}

	public void setActionUri(Uri actionUrl) {
		this.actionUri = actionUrl;
	}

	public List<NavNode> getChildren() {
		return children;
	}

	public void setChildren(List<NavNode> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "NavNode [id=" + id + ", level=" + level + ", title=" + title
				+ ", image=" + image + ", actionType=" + actionType
				+ ", actionUrl=" + actionUri + ", children=" + children + "]";
	}

}
