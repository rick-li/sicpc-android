package com.sicpc.android.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sicpc.android.R;

public class CurveListActivity extends Activity {

	protected static final String TAG = "CurveListActivity";
	private int containerWidth = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curve_list);
		containerWidth = ((LinearLayout)this.findViewById(R.id.root)).getWidth();
		testList();
	}

//	ListView list1;
//	ListView list2;
	ArrayAdapter<String> adaptor1;
	ArrayAdapter<String> adaptor2;
	private void testList() {
		final Activity ctx = this;
		visiList = (ListView) this.findViewById(R.id.thirdList1);
		String[] str1 = { "Hello1", "Hello1", "Hello1", "Hello1", "Hello1",
				"Hello1" };
		invisiList = (ListView) this.findViewById(R.id.thirdList2);
		String[] str2 = { "Bye", "Bye", "Bye", "Bye", "Bye", "Bye" };
		adaptor1 = new ArrayAdapter<String>(ctx,
				R.layout.listitem, str1);
		
		adaptor2 = new ArrayAdapter<String>(ctx,
				R.layout.listitem, str2);
		visiList.setAdapter(adaptor1);
		invisiList.setAdapter(adaptor2);
		Button b = (Button) this.findViewById(R.id.animationBtn);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startSlideAnimation2(visiList, invisiList);

			}
		});
	}
	List<PointF> posList = new ArrayList<PointF>(200);
	final String VISIBLE_ITEM_POS = "visibleItemPos";
	final String ACTION = "action";
	final String REMOVE_LIST = "remove_list";
	final String RESTORE_LIST = "restore_list";
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
				aniView.startAnimation(AnimationUtils.loadAnimation(
						CurveListActivity.this, R.anim.push_left_out));
				aniView.setVisibility(View.INVISIBLE);
			}
		}
	};
	
	Handler pushInHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (RESTORE_LIST.equals(msg.getData().getString(ACTION))) {
				for(int i=0;i<invisiList.getChildCount();i++){
					invisiList.getChildAt(i).setX(2000);
				}
				invisiList.setVisibility(View.VISIBLE);
				invisiList.setX(0);
			} else {
				if(invisiList.getVisibility() == View.INVISIBLE){

					//invisiList.setVisibility(View.VISIBLE);
				}
				int idx = msg.getData().getInt(VISIBLE_ITEM_POS);
				View aniView = invisiList.getChildAt(idx);
				if(aniView == null){
					Log.w(TAG, "Index: "+idx  +" does not exists.");
					return;
				}
				aniView.setVisibility(View.VISIBLE);
				aniView.setX(posList.get(idx).x);
				aniView.setY(posList.get(idx).y);
				aniView.startAnimation(AnimationUtils.loadAnimation(
						CurveListActivity.this, R.anim.push_left_in));
			}
		}
	};
	ListView visiList;
	ListView invisiList;

	private void startSlideAnimation2(final ListView list1, final ListView list2) {
		if(list1.getVisibility() == View.INVISIBLE){
			visiList = list2;
			invisiList = list1;
		}else{
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

//	private void slideOut(View view, int duration,
//			AnimatorListenerAdapter listener) {
//		Interpolator accelerator = new AccelerateInterpolator();
//		float startPos = view.getLeft();
//		float endPos = -view.getWidth();
//		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(view, "x", startPos,
//				endPos);
//		visToInvis.setDuration(duration);
//		visToInvis.setInterpolator(accelerator);
//		if (listener != null)
//			visToInvis.addListener(listener);
//		visToInvis.start();
//	}
//
//	private void slideIn(View view, int startPos, int endPos, int duration,
//			AnimatorListenerAdapter listener) {
//		Interpolator decelerator = new DecelerateInterpolator();
//		ObjectAnimator invisToVis = ObjectAnimator.ofFloat(view, "x", startPos,
//				endPos);
//		invisToVis.setDuration(duration);
//		invisToVis.setInterpolator(decelerator);
//		if (listener != null)
//			invisToVis.addListener(listener);
//		invisToVis.start();
//	}

//	private void startSlideAnimation1() {
//
//		final ListView visibleList = visiList;
//		final ListView invisibleList = invisiList;
//
////		if (list1.getVisibility() == View.GONE) {
////			invisibleList = list1;
////			visibleList = list2;
////		} else {
////			invisibleList = list2;
////			visibleList = list1;
////		}
//		final int duration = 500;
//		final int visibleLeft = visibleList.getLeft();
//		slideOut(visibleList, duration, new AnimatorListenerAdapter() {
//			@Override
//			public void onAnimationEnd(Animator anim) {
//				visibleList.setVisibility(View.GONE);
//				invisibleList.setVisibility(View.VISIBLE);
//				Log.i(TAG, "invisible pos: " + invisibleList.getX() + " , "
//						+ invisibleList.getY());
//				// invisToVis.start();
////				slideIn(invisibleList, visibleList.getWidth(), visibleLeft,
////						duration, null);
//			}
//		});
//
//		// new AnimatorListenerAdapter() {
//		// @Override
//		// public void onAnimationEnd(Animator anim) {
//		// Log.i(TAG, "animation completed.");
//		// }
//	}

	// private void testCurvedList(){
	// final Activity ctx = this;
	// CurvedList curvedList = (CurvedList) this.findViewById(R.id.curvedList);
	// curvedList.setAdapter(new BaseAdapter() {
	// LayoutInflater mInflater = ctx.getLayoutInflater();
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.listitem, null);
	// }
	// ((TextView) convertView).setText("Hellooooooooooooo "
	// + position);
	// return convertView;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return "Hellooooooooooooo " + position;
	// }
	//
	// @Override
	// public int getCount() {
	// return 100;
	// }
	// });
	//
	// }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.curve_list, menu);
		return true;
	}

}
