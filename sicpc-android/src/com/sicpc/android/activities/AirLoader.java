package com.sicpc.android.activities;

import air.tt.AppEntry;
import android.os.Bundle;
import android.view.KeyEvent;

public class AirLoader extends AppEntry {

	@Override
	public void onCreate(Bundle test2233) {
		super.onCreate(test2233);
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event){
		if(event.getKeyCode() == 4){
			AirLoader.this.finish();
			return false;
		}
		return super.onKeyDown(keycode, event);
	} 
	
	@Override
	public void onBackPressed() {
		this.finish();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
}