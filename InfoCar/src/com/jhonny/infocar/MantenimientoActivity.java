package com.jhonny.infocar;

import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.LinearLayout;


public class MantenimientoActivity extends Activity {
	
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mantenimiento);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mantenimiento, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		cargaPublicidad();
	}
	
	private void cargaPublicidad(){
		int placementWidth = BANNER_AD_WIDTH;
		
		//Finds an ad that best fits a users device.
		if(canFit(IAB_LEADERBOARD_WIDTH)) {
		    placementWidth = IAB_LEADERBOARD_WIDTH;
		}else if(canFit(MED_BANNER_WIDTH)) {
		    placementWidth = MED_BANNER_WIDTH;
		}
		
		MMAdView adView = new MMAdView(this);
		adView.setApid("158789");
		MMRequest request = new MMRequest();
		adView.setMMRequest(request);
		adView.setId(MMSDK.getDefaultAdId());
		adView.setWidth(placementWidth);
		adView.setHeight(BANNER_AD_HEIGHT);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout2);
		layout.removeAllViews();
		layout.addView(adView);
		adView.getAd();
	}
	
	protected boolean canFit(int adWidth) {
		int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, getResources().getDisplayMetrics());
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		return metrics.widthPixels >= adWidthPx;
	}
}
