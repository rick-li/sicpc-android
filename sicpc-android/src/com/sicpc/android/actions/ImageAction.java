package com.sicpc.android.actions;

import android.net.Uri;

import com.sicpc.android.nav.NavNode;

/**
 * 
 * Images will be open in a carousel in the left side.
 * 
 */
public class ImageAction implements Action {

	private NavNode node;

	public ImageAction(NavNode node) {
		this.node = node;
	}

	@Override
	public void doAction() {
		
	}

}
