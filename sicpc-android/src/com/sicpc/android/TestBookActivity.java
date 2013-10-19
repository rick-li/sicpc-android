package com.sicpc.android;

import java.io.File;

import org.coolreader.CoolReader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TestBookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String f = "/sdcard/sytl1.txt";
		Intent i = new Intent();
		i.setClass(this, org.coolreader.CoolReader.class);
		i.setData(Uri.fromFile(new File(f)));
		i.setAction(Intent.ACTION_VIEW);
		i.putExtra(CoolReader.OPEN_FILE_PARAM, f);
		this.startActivity(i);
	}

}
