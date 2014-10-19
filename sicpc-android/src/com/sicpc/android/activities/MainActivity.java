package com.sicpc.android.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Strings;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.config.AppConfig;

public class MainActivity extends RoboActivity {

	protected static final String TAG = MainActivity.class.getSimpleName();

	@InjectView(R.id.main_jisheng)
	ImageView jishengBtn;

	@InjectView(R.id.main_yunqi)
	ImageView yunqiBtn;
	
	
	@InjectView(R.id.main_qingchun)
	ImageView qingchunBtn;
	
	
	@InjectView(R.id.main_xinhun)
	ImageView xinhunBtn;

	@InjectView(R.id.main_laonian)
	ImageView laonianBtn;
	
	@InjectView(R.id.main_shengyuhou)
	ImageView shengyuhouBtn;
	
	
	@Inject
	AppConfig appConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (appConfig.getErrorList().size() > 0) {
			AlertDialog dialog = new AlertDialog.Builder(this).setTitle("错误！")
					.setMessage(Strings.join("\n\r", appConfig.getErrorList()))
					.setPositiveButton("确定", null).create();
			dialog.show();
		}
		
		
		jishengBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, AirLoader.class);
				MainActivity.this.startActivity(i);
			}
		});

		yunqiBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(SubMainActivity.CONTENT_ID, "yunqijiaoyushiguanjian");
				i.setClass(MainActivity.this, SubMainActivity.class);
				MainActivity.this.startActivity(i);
			}
		});
		
		xinhunBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(SubMainActivity.CONTENT_ID, "xinhunqi");
				i.setClass(MainActivity.this, SubMainActivity.class);
				MainActivity.this.startActivity(i);
			}
		});
		
		qingchunBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(SubMainActivity.CONTENT_ID, "qingchunqi");
				i.setClass(MainActivity.this, SubMainActivity.class);
				MainActivity.this.startActivity(i);
			}
		});
		
		laonianBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(SubMainActivity.CONTENT_ID, "zhonglaonian");
				i.setClass(MainActivity.this, SubMainActivity.class);
				MainActivity.this.startActivity(i);
			}
		});
		
		shengyuhouBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(SubMainActivity.CONTENT_ID, "shengyuhoushiqi");
				i.setClass(MainActivity.this, SubMainActivity.class);
				MainActivity.this.startActivity(i);
			}
		});

	}
	

}
