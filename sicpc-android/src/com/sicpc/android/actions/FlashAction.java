package com.sicpc.android.actions;

import android.content.Context;

import com.sicpc.android.nav.NavNode;

/**
 * 
 * Start adobe air wrapper actvitiy for flash.
 * 
 */
public class FlashAction implements Action {

	private Context ctx;
	private NavNode node;

	public FlashAction(Context ctx, NavNode node) {
		this.ctx = ctx;
		this.node = node;
	}

	@Override
	public void doAction() {

	}

}
