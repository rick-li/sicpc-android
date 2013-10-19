package com.sicpc.android.nav;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.common.collect.Lists;
import com.sicpc.android.R;

public class ListInOutAnimationtor {
	protected final String TAG = this.getClass().getSimpleName();
	
	final String VISIBLE_ITEM_POS = "visibleItemPos";
	final String ACTION = "action";
	final String REMOVE_LIST = "remove_list";
	final String RESTORE_LIST = "restore_list";
	private ListView visiList;
	private ListView invisiList;

	private Activity ctx;
	private ImageAdaptor adaptor1;
	private ImageAdaptor adaptor2;
	
	private AnimationStateListener animationStateListener;
	
	
	public ListInOutAnimationtor(Activity ctx, ListView thirdlist1, ListView thirdlist2, List<NavNode> defaultListItems) {
		this.ctx = ctx;
		visiList = thirdlist1;
		invisiList = thirdlist2;
		
		List<NavNode> list1 = defaultListItems;
		List<NavNode> list2 = Lists.newArrayList();
		
		
		adaptor1 = new ImageAdaptor(list1);
		adaptor2 = new ImageAdaptor(list2);

		visiList.setAdapter(adaptor1);
		invisiList.setAdapter(adaptor2);
		
//		visiList.setVisibility(View.VISIBLE);
//		invisiList.setVisibility(View.VISIBLE);
		
//		startSlideAnimation2(visiList, invisiList);
	}

	public void updateListItems(List<NavNode> newListItems, AnimationStateListener animationStateListener) {
		this.animationStateListener = animationStateListener;
		
		if(invisiList.getVisibility() == View.VISIBLE){
			//swap visible/invisible;
			ListView tmplist = invisiList;
			invisiList = visiList;
			visiList = tmplist;
		}
		invisiList.setAdapter(new ImageAdaptor(newListItems));
		((ImageAdaptor)invisiList.getAdapter()).notifyDataSetChanged();
		invisiList.invalidateViews();
//		Log.d(TAG, "invisible list pos "+invisiList.getLeft());
//		Log.d(TAG, "visible list pos "+visiList.getLeft());
//		invisiList.invalidateViews();
//		invisiList.setVisibility(View.VISIBLE);
//		invisiList.setX(0);
//		visiList.setVisibility(View.VISIBLE);
		startSlideAnimation2(visiList, invisiList);
	}

	private void startSlideAnimation2(final ListView visiList, final ListView invisiList) {
		animationStateListener.setState(true);
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
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//remove list itself
				Message removeMsg = Message.obtain();
				Bundle removeListData = new Bundle();
				removeListData.putString(ACTION, REMOVE_LIST);
				removeMsg.setData(removeListData);
				pushOutHandler.sendMessage(removeMsg);

				//restore list itself
				Message restoreMsg = Message.obtain();
				Bundle restoreListData = new Bundle();
				restoreListData.putString(ACTION, RESTORE_LIST);
				restoreMsg.setData(restoreListData);
				pushInHandler.sendMessage(restoreMsg);
//
				int numerOfinvisibleItems = invisiList.getCount();
				for (int i = 0; i < numerOfinvisibleItems; i++) {
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
				
				new Timer().schedule(new TimerTask() {
					
					@Override
					public void run() {
						
						Bundle aniBdl = new Bundle();
						aniBdl.putBoolean("animation", false);
						Message aniMsg = Message.obtain();
						aniMsg.setData(aniBdl);
						finishedHandler.sendMessage(aniMsg);
						
					}
				}, 1000);
				
			}

		}).start();
	}
	private List<PointF> posList = new ArrayList<PointF>(200);
	Handler pushOutHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (REMOVE_LIST.equals(msg.getData().getString(ACTION))) {
//				visiList.setVisibility(View.INVISIBLE);
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
				int idx = msg.getData().getInt(VISIBLE_ITEM_POS);
				View aniView = invisiList.getChildAt(idx);
				if (aniView == null) {
					Log.w(TAG, "Index: " + idx + " does not exists.");
					return;
				}
				aniView.setVisibility(View.VISIBLE);
				aniView.setX(0);
//				aniView.setY(posList.get(idx).y);
				aniView.startAnimation(AnimationUtils.loadAnimation(ctx,
						R.anim.push_left_in));
			}
		}
	};
	
	Handler finishedHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "Animation finished, set animation state.");
			animationStateListener.setState(false);
		}
		
	};
	
	class ImageAdaptor extends BaseAdapter{
		List<NavNode> navNodes;
		public ImageAdaptor(List<NavNode> navNodes){
			this.navNodes = navNodes;
		}
		LayoutInflater mInflater = ctx.getLayoutInflater();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Log.i(TAG, "get view position " + position + " image is " + this.navNodes.get(position).getImage());

			if (convertView == null) {
				
				convertView = mInflater.inflate(R.layout.listimageitemleft, null);
			}
			((ImageView)((RelativeLayout) convertView).getChildAt(0)).setImageURI(navNodes.get(
					position).getImage());

			return convertView;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public int getCount() {
			return this.navNodes.size();
		}
	} 
}
