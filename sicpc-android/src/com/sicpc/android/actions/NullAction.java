package com.sicpc.android.actions;

import android.content.Context;
import android.widget.Toast;

public class NullAction implements Action {

	@Override
	public void doAction(Context activity) {
		Toast.makeText(activity, "无法找到相关类型", 1000 * 3);
	}

}
