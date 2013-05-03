package com.sicpc.android.nav;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sicpc.android.R;

public class SecondNavController {
	private static final String TAG = "SecondNavController";
	private Activity ctx;
	private LayoutInflater inflator;

	public SecondNavController(Activity ctx) {
		this.ctx = ctx;
		inflator = LayoutInflater.from(ctx);
	}

	public void renderSecondNav(List<NavNode> secondNavs) {
		LinearLayout secondNavRoot = (LinearLayout) ctx
				.findViewById(R.id.secondNavRoot);
		for (NavNode secondNode : secondNavs) {
			secondNavRoot.addView(createSecondNav(secondNode));
		}
	}

	private LinearLayout createSecondNav(NavNode navNode) {
		LinearLayout secondNavView = (LinearLayout) this.inflator.inflate(
				R.layout.second_nav_header, null);
		TextView secondTitle = (TextView) secondNavView
				.findViewById(R.id.second_nav_header_text);
		secondTitle.setText(navNode.getTitle());
		LinearLayout thirdListRoot = (LinearLayout) secondNavView
				.findViewById(R.id.third_nav_list);
		Log.d(TAG, "Third nav list is " + navNode.getChildren());
		addThridNav(thirdListRoot, navNode.getChildren());
		return secondNavView;
	}

	private void addThridNav(LinearLayout thirdListRoot, List<NavNode> nodes) {
		for (NavNode node : nodes) {
			RelativeLayout root = (RelativeLayout) this.inflator.inflate(
					R.layout.third_nav, null);
			TextView item = (TextView) root
					.findViewById(R.id.third_nav_list_item);
			item.setText(node.getTitle());
			thirdListRoot.addView(root);
		}
	}

}
