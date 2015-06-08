package com.sicpc.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Strings;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.sicpc.android.R;
import com.sicpc.android.config.AppConfig;
import com.sicpc.android.config.AppConfigProvider;
import com.sicpc.android.utils.UnZipper;

public class MainActivity extends RoboActivity implements Observer{

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
	
	
//	@Inject
	AppConfig appConfig;
	
	@Inject
	AppConfigProvider configProvider;

	SharedPreferences prefs;
	
	AlertDialog upgradeDialog;
	static final String DATA_COPIED_KEY = "data_copied";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		prefs = this.getPreferences(MODE_PRIVATE);
		Boolean isDataCopied = prefs.getBoolean(DATA_COPIED_KEY, false);
		if(isDataCopied){
			ready();
		}else{
			upgradeDialog = new AlertDialog.Builder(this).setTitle("點擊開始更新內容")
					.setMessage("").setNeutralButton("開始", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).create();
			upgradeDialog.setOnShowListener(new DialogInterface.OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {

					final Button b = upgradeDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
					b.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view) {
							
							unzipFiles();
							b.setActivated(false);
							
						}
					});
				}
			});
			upgradeDialog.show();
	
		}
		
	}
	
	private void ready(){
		appConfig = configProvider.get();
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
	public void unzipFiles() {
		String unzipLocation = Environment.getExternalStorageDirectory()
				.toString() + "/sicpc";
		
		InputStream is = null;
		try {
			is = this.getAssets().open("data.zip");
		} catch (IOException e) {
			e.printStackTrace();
		}

		UnZipper unzipper = new UnZipper(is, unzipLocation);
		unzipper.addObserver(this);
		unzipper.unzip();
	}
	@Override
	public void update(Observable paramObservable, Object arg) {

		if ("finished".equals(arg)) {
			prefs.edit().putBoolean(DATA_COPIED_KEY, true).commit();
			upgradeDialog.dismiss();
			ready();
		} else {
			TextView messageView = (TextView) upgradeDialog
					.findViewById(android.R.id.message);
			messageView.setText("正在複製 " + arg);
		}

	}

}
