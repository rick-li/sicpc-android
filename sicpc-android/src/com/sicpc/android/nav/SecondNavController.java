package com.sicpc.android.nav;

import java.io.File;
import java.util.List;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.sicpc.android.R;
import com.sicpc.android.actions.Action;
import com.sicpc.android.actions.ActionFactory;
import com.sicpc.android.nav.view.CircularListAdapter;
import com.sicpc.android.nav.view.CurvedList;

public class SecondNavController implements AnimationStateListener {
	private static final String TAG = "SecondNavController";
	private FragmentActivity ctx;
	private LayoutInflater inflator;
	private int secondListPos = 0;
	private boolean initiailized = false;
	private ListInOutAnimationtor thirdListAnimator;
	private List<NavNode> secondNavNodes = Lists.newArrayList();
	private List<NavNode> thirdNavNodes = Lists.newArrayList();

	private ListView thirdList1;
	private ListView thirdList2;

	public SecondNavController(FragmentActivity ctx) {
		this.ctx = ctx;
		inflator = LayoutInflater.from(ctx);

		thirdList1 = (ListView) ctx.findViewById(R.id.thirdList1);
		thirdList2 = (ListView) ctx.findViewById(R.id.thirdList2);
		bindThirdNavEvent(thirdList1);
		bindThirdNavEvent(thirdList2);

	}

	private void bindThirdNavEvent(ListView thirdlist) {
		thirdlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NavNode node = thirdNavNodes.get(position);
				if (node != null) {
					startAction(node);
				}
			}
		});
	}

	public void renderSecondNav(final List<NavNode> secondNavNodes) {
		this.secondNavNodes = secondNavNodes;
		final CurvedList secondNavList = (CurvedList) ctx
				.findViewById(R.id.secondNavList);

		final int mCount = secondNavNodes.size();
		secondNavList.setAdapter(new CircularListAdapter(new BaseAdapter() {
			LayoutInflater mInflater = ctx.getLayoutInflater();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.listimageitemmid,
							null);
				}
				Uri imageUri = secondNavNodes.get(position).getImage();

				Log.i(TAG, "Curve List image is " + imageUri.getPath());

				File imageFile = new File(imageUri.getPath());
				Log.i(TAG, "Curve List image exisits - " + imageFile.exists());
				((ImageView) convertView).setImageURI(secondNavNodes.get(
						position).getImage());

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

		secondNavList.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				if (!initiailized) {
					secondNavList.smoothScrollToPositionFromTop(mCount * 2, 0);
					initiailized = true;
				}
			}
		});

		secondNavList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (animationState) {
					Log.d(TAG, "Still in the animation state, return.");
					return;
				}
				Log.d(TAG, "Scrolling to position " + position);
				int secondNavCount = secondNavNodes.size();
				int realPos = position % mCount;
				// secondNavs.get(location);
				if (position / secondNavCount == 0) {
					position += secondNavCount;
				}
				int midPos = secondNavList.getHeight() / 2 - view.getHeight()
						/ 2;
				secondNavList.smoothScrollToPositionFromTop(position, midPos);
				NavNode clickedNode = secondNavNodes.get(realPos);
				thirdNavNodes = clickedNode.getChildren();
				thirdListAnimator.updateListItems(thirdNavNodes, SecondNavController.this);
			}
		});

		List<NavNode> defaultList = secondNavNodes.get(0).getChildren();
		thirdListAnimator = new ListInOutAnimationtor(ctx, thirdList1,
				thirdList2, defaultList);
		
	}

	private void startAction(NavNode navNode) {
		Action action = ActionFactory.getInstance().getAction(ctx, navNode);
		action.doAction();
	}

	private boolean animationState = false;

	@Override
	public void setState(boolean state) {
		this.animationState = state;
	}

}
