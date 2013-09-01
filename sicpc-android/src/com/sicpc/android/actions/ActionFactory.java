package com.sicpc.android.actions;

import android.support.v4.app.FragmentActivity;

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

	public Action getAction(FragmentActivity ctx, NavNode node) {
		ActionType actionType = node.getActionType();
		if (actionType != null) {
			if (actionType.equals(ActionType.BOOK)) {
				return new BookAction(ctx, node);
			} else if (actionType.equals(ActionType.VIDEO)) {
				return new VideoAction(ctx, node);
			} else if (actionType.equals(ActionType.FLASH)) {
				return new FlashAction(ctx, node);
			}
		}
		return new ImageAction(null);
	}
}
