package com.sicpc.android.actions;

import android.content.Context;

/**
 * 
 * Generic action, can be book/image/video/falsh..
 * 
 */
public interface Action {
	public final static String ACTION_ID = "action_Id";

	void doAction(Context activity);
}
