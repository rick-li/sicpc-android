package com.sicpc.android.actions;

import java.io.File;
import java.io.FilenameFilter;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.sicpc.android.R;
import com.sicpc.android.actions.BookFragment.PageProvider;
import com.sicpc.android.nav.NavNode;
import com.viewpagerindicator.CirclePageIndicator;

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
		Uri imageDir = node.getActionUri();
		File imageDirF = new File(imageDir.getPath());
		Log.i(TAG, "Page Provider dir is " + imageDirF.getAbsolutePath()
				+ " exists " + imageDirF.exists());
		final File[] imageList = imageDirF.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {

				return (filename.endsWith("png") || filename
						.endsWith("jpg"));
			}
		});
		
		
		if (bookContainer.getTranslationX() < 0) {
			Log.d(TAG, "Start animation.");

			TranslateAnimation animation = new TranslateAnimation(-600, 0, 0, 0);
			bookContainer.startAnimation(animation);

			bookContainer.setTranslationX(0);
		}

		CirclePageIndicator indicator = (CirclePageIndicator) ctx
				.findViewById(R.id.image_indicator);
		ViewPager mockPager = new ViewPager(ctx);
		mockPager.setAdapter(new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return false;
			}
			
			@Override
			public int getCount() {
				return imageList.length;
			}
		});
		indicator.setViewPager(mockPager);

		View root = ctx.findViewById(R.id.sub_main_root);
		root.setBackgroundResource(R.drawable.bookaction_bg_red);
		Log.d(TAG, "After animation. " + bookContainer.getTranslationX());
	}

}
