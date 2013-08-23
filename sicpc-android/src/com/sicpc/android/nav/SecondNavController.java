package com.sicpc.android.nav;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

	public SecondNavController(Activity ctx) {
		this.ctx = ctx;
		inflator = LayoutInflater.from(ctx);
	}

	public void renderSecondNav(List<NavNode> secondNavs) {
		// LinearLayout secondNavRoot = (LinearLayout) ctx
		// .findViewById(R.id.secondNavRoot);
		// for (NavNode secondNode : secondNavs) {
		// secondNavRoot.addView(createSecondNav(secondNode));
		// }
		// ListView listest = (ListView)ctx.findViewById(R.id.listtest);
		String[] circularData = new String[] { "Flinging", "This", "List,",
				"Makes", "It", "Appear", "To", "Scroll", "On", "And", "On",
				"And", "On" };

		// 2. Create your adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
				android.R.layout.simple_list_item_1, circularData);

		// 3. Wrap your adapter within the CircularListAdapter
		final CircularListAdapter circularListAdapter = new CircularListAdapter(
				adapter);

		// 4. Set the adapter to your ListView
		// ListView listtest = (ListView)ctx.findViewById(R.id.listtest);
		// listtest.setAdapter(circularListAdapter);

		CurvedList curvedList = (CurvedList) ctx.findViewById(R.id.curvedList);
		curvedList.setAdapter(new CircularListAdapter(new BaseAdapter() {
			LayoutInflater mInflater = ctx.getLayoutInflater();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.listitem, null);
				}
				((TextView) convertView).setText("Hellooooooooooooo "
						+ position);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return "Hellooooooooooooo " + position;
			}

			@Override
			public int getCount() {
				return 100;
			}
		}));

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
