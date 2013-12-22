package com.sicpc.android.nav;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
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
					
					WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
					Display display = wm.getDefaultDisplay();
					Point p = new Point();
					display.getSize(p);
					Log.i(TAG, "width "+p.x+ " height "+ p.y);
					if(p.y == 752){
						//10 inch asus
						secondNavList.smoothScrollToPositionFromTop(mCount * 2, 0);
					}else if (p.y==736){
						//7 inch
						secondNavList.smoothScrollToPositionFromTop(mCount *2, 50);
					}else{
						secondNavList.smoothScrollToPositionFromTop(mCount * 2, 67);
					}
					
					
					initiailized = true;
				}
			}
		});

		secondNavList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG, "Animation state is " + animationState);
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
				thirdListAnimator.updateListItems(thirdNavNodes,
						SecondNavController.this);
			}
		});

		NavNode defaultNode = null;
		for (NavNode node : secondNavNodes) {
			if (node.isDefault()) {
				defaultNode = node;
				break;
			}
		}
		if (defaultNode == null) {
			defaultNode = secondNavNodes.get(0);
		}
		List<NavNode> defaultThirdList = defaultNode.getChildren();
		this.thirdNavNodes = defaultThirdList;
		thirdListAnimator = new ListInOutAnimationtor(ctx, thirdList1,
				thirdList2, defaultThirdList);

	}

	private void startAction(NavNode navNode) {
		Action action = ActionFactory.getInstance().getAction(ctx, navNode);
		action.doAction(ctx);
	}

	private boolean animationState = false;

	@Override
	public void setState(boolean state) {
		Log.d(TAG, "Anumation state change: " + state);
		this.animationState = state;
	}

}
