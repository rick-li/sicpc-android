package com.sicpc.android.actions;

import android.content.Context;

import com.sicpc.android.activities.SubMainActivity;
import com.sicpc.android.nav.NavNode;
import com.sicpc.android.nav.NavNode.ActionType;

public class ActionFactory {

	private static ActionFactory instance;
	static {
		instance = new ActionFactory();
	}

	public static ActionFactory getInstance() {
		return instance;
	}
	

	public Action getAction(Context ctx, NavNode node, String bgPath) {
		ActionType actionType = node.getActionType();
		if (actionType != null) {
			if (actionType.equals(ActionType.IMAGE)) {
				if (ctx instanceof SubMainActivity) {
					return new PartImageAction(node, bgPath);
				} else {
					return new ImageAction(node, bgPath);
				}
			} else if (actionType.equals(ActionType.VIDEO)) {
				return new VideoAction(node);
			} else if (actionType.equals(ActionType.BOOK)) {
				return new BookAction(node);
			}
		}
		return null;
	}
}
