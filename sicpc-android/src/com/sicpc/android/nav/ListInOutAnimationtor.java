package com.sicpc.android.nav;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sicpc.android.R;

public class ListInOutAnimationtor {
	protected final String TAG = this.getClass().getSimpleName();
	private List<PointF> posList = new ArrayList<PointF>(200);
	final String VISIBLE_ITEM_POS = "visibleItemPos";
	final String ACTION = "action";
	final String REMOVE_LIST = "remove_list";
	final String RESTORE_LIST = "restore_list";
	private ListView visiList;
	private ListView invisiList;

	private Activity ctx;
	ArrayAdapter<String> adaptor1;
	ArrayAdapter<String> adaptor2;

	public ListInOutAnimationtor(Activity ctx, String[] defaultListItems) {
		this.ctx = ctx;
		visiList = (ListView) ctx.findViewById(R.id.thirdList1);
		String[] str1 = {};
		invisiList = (ListView) ctx.findViewById(R.id.thirdList2);
		String[] str2 = defaultListItems;
		ArrayList<String> ls1 = new ArrayList<String>();
		ls1.addAll(Arrays.asList(str1));
		
		ArrayList<String> ls2 = new ArrayList<String>();
		ls2.addAll(Arrays.asList(str2));
		adaptor1 = new ArrayAdapter<String>(ctx, R.layout.listitem, ls1);
		adaptor2 = new ArrayAdapter<String>(ctx, R.layout.listitem, ls2);

		visiList.setAdapter(adaptor1);
		invisiList.setAdapter(adaptor2);
		
		startSlideAnimation2(visiList, invisiList);
	}

	public void updateListItems(String[] newListItems) {
		// invisiList
		ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)invisiList.getAdapter();
		arrayAdapter.clear();
		ArrayList<String> newLs = new ArrayList<String>();
		newLs.addAll(Arrays.asList(newListItems));
		arrayAdapter.addAll(newLs);
		
		arrayAdapter.notifyDataSetChanged();
		invisiList.invalidateViews();
		startSlideAnimation2(visiList, invisiList);
	}

	private void startSlideAnimation2(final ListView list1, final ListView list2) {
		if (list1.getVisibility() == View.INVISIBLE) {
			visiList = list2;
			invisiList = list1;
		} else {
			visiList = list1;
			invisiList = list2;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// push left visible list
				int numerOfVisibleItems = visiList.getLastVisiblePosition()
						- visiList.getFirstVisiblePosition() + 1;
				for (int i = visiList.getFirstVisiblePosition(); i <= visiList
						.getLastVisiblePosition(); i++) {
					Message msg = Message.obtain();
					Bundle data = new Bundle();
					data.putInt(VISIBLE_ITEM_POS, i);
					// Message msg = Message.obtain();
					msg.setData(data);
					pushOutHandler.sendMessage(msg);
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Message removeMsg = Message.obtain();
				Bundle removeListData = new Bundle();
				removeListData.putString(ACTION, REMOVE_LIST);
				removeMsg.setData(removeListData);
				pushOutHandler.sendMessage(removeMsg);

				Message restoreMsg = Message.obtain();
				Bundle restoreListData = new Bundle();
				restoreListData.putString(ACTION, RESTORE_LIST);
				restoreMsg.setData(restoreListData);
				pushInHandler.sendMessage(restoreMsg);
				for (int i = 0; i < numerOfVisibleItems; i++) {
					Bundle data = new Bundle();
					data.putInt(VISIBLE_ITEM_POS, i);
					Message msg = Message.obtain();
					msg.setData(data);
					pushInHandler.sendMessage(msg);
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		}).start();
	}

	Handler pushOutHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (REMOVE_LIST.equals(msg.getData().getString(ACTION))) {
				visiList.setVisibility(View.INVISIBLE);
				visiList.setX(2000);
			} else {
				int idx = msg.getData().getInt(VISIBLE_ITEM_POS);
				View aniView = visiList.getChildAt(idx);
				posList.add(new PointF(aniView.getX(), aniView.getY()));
				aniView.startAnimation(AnimationUtils.loadAnimation(ctx,
						R.anim.push_left_out));
				aniView.setVisibility(View.INVISIBLE);
			}
		}
	};

	Handler pushInHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (RESTORE_LIST.equals(msg.getData().getString(ACTION))) {
				for (int i = 0; i < invisiList.getChildCount(); i++) {
					invisiList.getChildAt(i).setX(2000);
				}
				invisiList.setVisibility(View.VISIBLE);
				invisiList.setX(0);
			} else {
				if (invisiList.getVisibility() == View.INVISIBLE) {

					// invisiList.setVisibility(View.VISIBLE);
				}
				int idx = msg.getData().getInt(VISIBLE_ITEM_POS);
				View aniView = invisiList.getChildAt(idx);
				if (aniView == null) {
					Log.w(TAG, "Index: " + idx + " does not exists.");
					return;
				}
				aniView.setVisibility(View.VISIBLE);
				aniView.setX(posList.get(idx).x);
				aniView.setY(posList.get(idx).y);
				aniView.startAnimation(AnimationUtils.loadAnimation(ctx,
						R.anim.push_left_in));
			}
		}
	};

}
