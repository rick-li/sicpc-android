package com.sicpc.android.actions;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.sicpc.android.R;
import com.sicpc.android.actions.BookFragment.PageProvider;
import com.sicpc.android.nav.NavNode;

import fi.harism.curl.CurlView;

public class BookAction implements Action {

	private static final String TAG = BookAction.class.getSimpleName();
	private FragmentActivity ctx;
	private NavNode node;

	public BookAction(FragmentActivity ctx, NavNode node) {
		this.ctx = ctx;
		this.node = node;
	}

	@Override
	public void doAction() {

		LinearLayout bookContainer = (LinearLayout) ctx
				.findViewById(R.id.leftContent);

		CurlView curlView = (CurlView) ctx.findViewById(R.id.curlView);
		curlView.setPageProvider(new PageProvider(node.getActionUri()));
		
		if (bookContainer.getTranslationX() < 0) {
			Log.d(TAG, "Start animation.");
			
			TranslateAnimation animation = new TranslateAnimation(-600, 0, 0, 0);
			bookContainer.startAnimation(animation);
			
			bookContainer.setTranslationX(0);
		}

		View root = ctx.findViewById(R.id.sub_main_root);
		root.setBackgroundResource(R.drawable.bookaction_bg_red);
		Log.d(TAG, "After animation. "+bookContainer.getTranslationX());
	}

}
