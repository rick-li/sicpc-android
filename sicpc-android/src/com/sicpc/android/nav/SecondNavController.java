package com.sicpc.android.nav;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sicpc.android.R;
import com.sicpc.android.nav.view.CircularListAdapter;
import com.sicpc.android.nav.view.CurvedList;

public class SecondNavController {
	private static final String TAG = "SecondNavController";
	private Activity ctx;
	private LayoutInflater inflator;
	private int secondListPos = 0;
	private ListInOutAnimationtor thirdListAnimator;
	String[] secondNavStrList = { "舒适穿着勤护肤", "安全饮食重营养", "姿势正确保安康", "活动休息细安排",
			"孕期卫生多重视", "孕妇用药须谨慎", "自我监护防意外" };

	String[] thirdNavStrList1 = { "孕期可以参加体育活动吗", "孕期旅游须知", "孕妇怎么做保健操",
			"合理安排活动与休息", "产前操" };
	String[] thirdNavStrList2 = { "aaaaa", "bbbbb", "ccccc", "ddddd" };
	

	private ListView thirdList1;
	private ListView thirdList2;

	public SecondNavController(Activity ctx) {
		this.ctx = ctx;
		inflator = LayoutInflater.from(ctx);

		thirdList1 = (ListView) ctx.findViewById(R.id.thirdList1);
		thirdList2 = (ListView) ctx.findViewById(R.id.thirdList2);
		bindThirdNavEvent(thirdList1);
		bindThirdNavEvent(thirdList2);
		
		thirdListAnimator = new ListInOutAnimationtor(ctx, thirdList1,
				thirdList2, thirdNavStrList1);
	}
	
	private void bindThirdNavEvent(ListView thirdlist){
		thirdlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}

	public void renderSecondNav(List<NavNode> secondNavs) {

		final CurvedList secondNavList = (CurvedList) ctx
				.findViewById(R.id.secondNavList);

		secondNavList.setAdapter(new CircularListAdapter(new BaseAdapter() {
			int mCount = secondNavStrList.length;
			LayoutInflater mInflater = ctx.getLayoutInflater();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				Log.i(TAG, "get view position " + position);

				if (convertView == null) {
					Log.i(TAG, "convertView is null");
					convertView = mInflater.inflate(R.layout.listitem, null);
				}
				((TextView) convertView).setText(secondNavStrList[position]);

				return convertView;
			}

			@Override
			public long getItemId(int position) {
				Log.d(TAG, "get itemid " + position);
				return position;
			}

			@Override
			public Object getItem(int position) {
				Log.d(TAG, "get item " + position);
				return position;
			}

			@Override
			public int getCount() {
				return mCount;
			}
		}));
		
		secondNavList.smoothScrollToPositionFromTop(secondNavStrList.length * 2, 0);
		
		secondNavList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG, "Scrolling to position " + position);
				int secondNavCount = secondNavStrList.length;
				if (position / secondNavCount == 0) {
					position += secondNavCount;
				}
				int midPos = secondNavList.getHeight() / 2 - view.getHeight() / 2;
				secondNavList.smoothScrollToPositionFromTop(position, midPos);

				thirdListAnimator.updateListItems(thirdNavStrList2);
			}
		});

	}

	private void addThridNav(LinearLayout thirdListRoot, List<NavNode> nodes) {
//		for (NavNode node : nodes) {
//			RelativeLayout root = (RelativeLayout) this.inflator.inflate(
//					R.layout.third_nav, null);
//			TextView item = (TextView) root
//					.findViewById(R.id.third_nav_list_item);
//			item.setText(node.getTitle());
//			thirdListRoot.addView(root);
//		}
		thirdListAnimator.updateListItems(thirdNavStrList2);
	}

}
