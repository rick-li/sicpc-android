package com.sicpc.android.actions;

import java.io.File;

import org.coolreader.CoolReader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.sicpc.android.nav.NavNode;

public class BookAction implements Action {

	private NavNode node;

	public BookAction(NavNode node) {
		this.node = node;
	}

	@Override
	public void doAction(Context activity) {
		Uri uri = node.getActionUri();
		String p = uri.getPath();
		File f = new File(p);
		if (!f.exists()) {
			Toast.makeText(activity, "该电子书不存在 " + p, 1000 * 2);
		}
		
		Intent i = new Intent();
		i.setClass(activity, org.coolreader.CoolReader.class);
		i.setData(Uri.fromFile(f));
		i.setAction(Intent.ACTION_VIEW);
		i.putExtra(CoolReader.OPEN_FILE_PARAM, f);
		activity.startActivity(i);
	}

}
