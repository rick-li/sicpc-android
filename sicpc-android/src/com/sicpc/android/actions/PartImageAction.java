package com.sicpc.android.actions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.sicpc.android.R;
import com.sicpc.android.actions.ImageFragment.PageProvider;
import com.sicpc.android.activities.SubMainActivity;
import com.sicpc.android.nav.NavNode;
import com.viewpagerindicator.CircleCurlIndicator;

import fi.harism.curl.CurlView;

public class PartImageAction implements Action {

	private static final String TAG = "PartImageAction";
	private NavNode node;
	private String bgPath;
	
	public PartImageAction(NavNode node, String bgPath) {
		this.node = node;
		this.bgPath = bgPath;
	}

	@Override
	public void doAction(Context activity) {
		SubMainActivity subActivity = (SubMainActivity) activity;
		LinearLayout bookContainer = (LinearLayout) subActivity
				.findViewById(R.id.leftContent);
		CurlView bookView = (CurlView) subActivity.findViewById(R.id.curlView);
		// already in SubMain, update current activity.
		bookView.setPageProvider(new PageProvider(node.getActionUri()));

		if (bookContainer.getTranslationX() < 0) {
			Log.d(TAG, "Start animation.");

			TranslateAnimation animation = new TranslateAnimation(-600, 0, 0, 0);
			bookContainer.startAnimation(animation);

			bookContainer.setTranslationX(0);
		}

		// indicator.
		CircleCurlIndicator indicator = (CircleCurlIndicator) subActivity
				.findViewById(R.id.image_indicator);

		indicator.setCurler(bookView);
		indicator.setCurrentPage(0);
		View root = subActivity.findViewById(R.id.sub_main_root);
		root.setBackgroundDrawable(Drawable.createFromPath(bgPath));

	}

}
