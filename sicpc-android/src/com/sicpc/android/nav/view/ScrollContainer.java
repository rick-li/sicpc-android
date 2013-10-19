package com.sicpc.android.nav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.sicpc.android.activities.SubMainActivity;

public class ScrollContainer extends ScrollView {

	private static final String TAG = "ScrollContainer";
	private SubMainActivity activity;
	public ScrollContainer(Context context) {
		super(context);
	}

	public ScrollContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SubMainActivity getActivity() {
		return activity;
	}

	public void setActivity(SubMainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		super.onScrollChanged(l, t, oldl, oldt);
//		Log.d(TAG, "Can scroll up " + this.canScrollVertically(-1));
//		Log.d(TAG, "Can scroll down " + this.canScrollVertically(1));
//
//		TextView sup = (TextView) 
//				activity.findViewById(R.id.canScrollUp);
//		TextView sdown = (TextView)
//				activity.findViewById(R.id.canScrollDown);
//		if(canScrollVertically(1)){
//			//down
//			sdown.setVisibility(View.VISIBLE);
//		}else{
//			sdown.setVisibility(View.INVISIBLE);
//		}
//		
//		if(canScrollVertically(-1)){
//			//up
//			sup.setVisibility(View.VISIBLE);
//		}else{
//			sup.setVisibility(View.INVISIBLE);
//		}
	}

}
