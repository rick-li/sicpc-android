package com.sicpc.android.nav;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sicpc.android.R;
import com.sicpc.android.nav.view.CircularListAdapter;
import com.sicpc.android.nav.view.CurvedList;

public class SecondNavController {
	private static final String TAG = "SecondNavController";
	private Activity ctx;
	private LayoutInflater inflator;
	private int secondListPos = 0;
	public SecondNavController(Activity ctx) {
		this.ctx = ctx;
		inflator = LayoutInflater.from(ctx);
	}

	public void renderSecondNav(List<NavNode> secondNavs) {

		final CurvedList curvedList = (CurvedList) ctx
				.findViewById(R.id.secondNavList);

		curvedList.setAdapter(new CircularListAdapter(new BaseAdapter() {
			LayoutInflater mInflater = ctx.getLayoutInflater();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.i(TAG, "get view position " + position);
				if (convertView == null) {
					Log.i(TAG, "convertView is null");
					convertView = mInflater.inflate(R.layout.listitem, null);

				}
				((TextView) convertView).setText("Hellooooooooooooo "
						+ position);

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
				return 15;
			}
		}));
		curvedList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG, "Scrolling to position "+position);				
				int midPos = curvedList.getHeight()/2 - view.getHeight()/2; 
				curvedList.smoothScrollToPositionFromTop(position, midPos);
				view.setBackgroundColor(Color.BLUE);
			}
		});
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
