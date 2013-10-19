package com.sicpc.android.activities;

import air.ceshi0922.AppEntry;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 
 * Load adobe air flash swf, handle back and finish action.
 *
 */
public class AirLoader extends AppEntry {

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
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