/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.deskclock;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.deskclock.Utils.ScreensaverMoveSaverRunnable;


public class ScreensaverActivity extends Activity {
    static final boolean DEBUG = false;
    static final String TAG = "DeskClock/ScreensaverActivity";

    // This value must match android:defaultValue of
    // android:key="screensaver_clock_style" in dream_settings.xml
    static final String DEFAULT_CLOCK_STYLE = "digital";

    private View mContentView, mSaverView;
    private View mAnalogClock, mDigitalClock;

    private final Handler mHandler = new Handler();
    private final ScreensaverMoveSaverRunnable mMoveSaverRunnable;
    private String mDateFormat;
    private String mDateFormatForAccessibility;
    private String mClockStyle;
    private boolean mPluggedIn = true;
    private final int mFlags = (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    
    //zzl
    
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams param;
	// private FloatView mLayout;

	private LinearLayout linearLayout = null;
	private TextView textView;
	private TextView textView2;
	
	private int y_start;
	private int x_start;
	private int y_move;
	private int x_move;
	
	private SeekBar_vertical seekBar_Down_Up;
	int current_light ;
	private int move_count;
    
	 private ImageView imageView_wifi;
	 private TextView textView_battery;
	 private ImageView imageView_battery;
	    
	 private int[] wifi_signal = {R.drawable.i_wifi_0,R.drawable.i_wifi_1,R.drawable.i_wifi_2,R.drawable.i_wifi_3};
	 private int[] battery_signal = {R.drawable.i_battery_25,R.drawable.i_barrery_50,R.drawable.i_battery_75,R.drawable.i_battery_100};
	   
	 private static final int BATTERY = 0x400 +1;
	 private static final int WIFI = 0x400 +2;
	 
	 private Handler handler;
	 private WifiBatteryReceiver wifiBatteryReceiver ;
	 
	 
	 private ActivityManager mActivityManager = null;  
	    // ProcessInfo Model类 用来保存所有进程信息  
	  

    ///////

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean changed = intent.getAction().equals(Intent.ACTION_TIME_CHANGED)
                    || intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                mPluggedIn = true;
                setWakeLock();
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                mPluggedIn = false;
                setWakeLock();
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                finish();
            }

            if (changed) {
                Utils.updateDateAddOld(ScreensaverActivity.this,mDateFormat, mDateFormatForAccessibility, mContentView);
                Utils.refreshAlarm(ScreensaverActivity.this, mContentView);
                Utils.setMidnightUpdater(mHandler, mMidnightUpdater);
            }

        }
    };

    private final ContentObserver mSettingsContentObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            Utils.refreshAlarm(ScreensaverActivity.this, mContentView);
        }
    };

    // Thread that runs every midnight and refreshes the date.
    private final Runnable mMidnightUpdater = new Runnable() {
        @Override
        public void run() {
            Utils.updateDateAddOld(ScreensaverActivity.this,mDateFormat, mDateFormatForAccessibility, mContentView);
            Utils.setMidnightUpdater(mHandler, mMidnightUpdater);
        }
    };
    
	private void showView() {
		// mLayout = new FloatView(getApplicationContext());
		// mLayout.setBackgroundResource(R.drawable.ic_launcher);
		if(linearLayout == null){
		linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.screenlight, null);
		
		seekBar_Down_Up = (SeekBar_vertical) linearLayout.findViewById(R.id.seekBarDownUp);
		textView = (TextView) linearLayout.findViewById(R.id.textview_screenlight_num);
		textView2 = (TextView)linearLayout.findViewById(R.id.textview_screenlight_num_2);
		
		ContentResolver resolver = getContentResolver();      
		  
	    try{          
	  
	    current_light = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);    
	  
	      }  
	  
	     catch(Exception e) {         
	  
	     e.printStackTrace();    
	  
	      }      
		
		//current_light = 1;
		textView.setText("");
		int temp = (int)(current_light/2.55);
		textView2.setText(""+temp);
		seekBar_Down_Up.setProgress(current_light);


		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		param =  new WindowManager.LayoutParams();

		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; 
		param.format = 1;
		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; 
		param.flags = param.flags
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		param.flags = param.flags
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; 

		param.alpha = 1.0f;

		param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL; 
		param.x = 0;
		param.y = 30;

		param.width = 230;
		param.height = 470;

		mWindowManager.addView(linearLayout, param);
		}
	}

    public ScreensaverActivity() {
        if (DEBUG) Log.d(TAG, "Screensaver allocated");
        mMoveSaverRunnable = new ScreensaverMoveSaverRunnable(mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mIntentReceiver, filter);
        getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.NEXT_ALARM_FORMATTED),
                false,
                mSettingsContentObserver);
        
        handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case BATTERY:
					int statue = msg.arg1;
					int num = msg.arg2;
					String battery = num+"%";
					String charge = getResources().getString(R.string.charge);
					if(num < 18 && (statue != BatteryManager.BATTERY_STATUS_CHARGING ||
		                     statue != BatteryManager.BATTERY_STATUS_FULL))
						textView_battery.setText(charge+"  "+battery);
					else textView_battery.setText(battery);
					
					String charging = getResources().getString(R.string.charging);
					String charged = getResources().getString(R.string.charged);
					
					
					if(statue == BatteryManager.BATTERY_STATUS_CHARGING)
						textView_battery.setText(charging+"  "+battery);
					if(num == 100  && (statue == BatteryManager.BATTERY_STATUS_CHARGING ||
		                     statue == BatteryManager.BATTERY_STATUS_FULL))
						textView_battery.setText(charged+"  "+battery);
					if(num == 0){
						textView_battery.setText("");
					}
					setBattery(num);
					break;
				case WIFI:
					int level = (Integer)msg.obj;
					imageView_wifi.setBackgroundResource(wifi_signal[level]);
					break;
				}
			}
        	
        };
        
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        wifiBatteryReceiver = new WifiBatteryReceiver();
        registerReceiver(wifiBatteryReceiver, intentFilter);
    }
    
    private void setBattery(int level){
		if(75<level && level<=100 )
			imageView_battery.setBackgroundResource(battery_signal[3]);
		if(50<level && level<=75 )
			imageView_battery.setBackgroundResource(battery_signal[2]);
		if(25<level && level<=50 )
			imageView_battery.setBackgroundResource(battery_signal[1]);
		if(0<level && level<=25 )
			imageView_battery.setBackgroundResource(battery_signal[0]);
		if(level == 0)
			imageView_battery.setBackgroundColor(Color.TRANSPARENT);
	}
	
	private class WifiBatteryReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				int num = (level*100)/scale;
				//String battery = num +"%";
				int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				handler.obtainMessage(BATTERY, status,num).sendToTarget();
				
				
			}else if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
				
				WifiInfo currentInfo = null;    
	            WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    		currentInfo = mWifiManager.getConnectionInfo();
	    		ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    		NetworkInfo info_wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    				if (currentInfo != null && info_wifi.isConnected()) {
	    					int level = getlevel(currentInfo.getRssi());
	    					handler.obtainMessage(WIFI, level).sendToTarget();
	    				}
			}else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
	            WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	            if(!mWifiManager.isWifiEnabled())
	            	imageView_wifi.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		
	}
	
	private int getlevel(int rssi){
		
		int level = 4;
		
		if (rssi <= 0 && rssi >= -50) {  
            level = 3;  
        } else if (rssi < -50 && rssi >= -70) {  
            level = 2;  
        } else if (rssi < -70 && rssi >= -80) {  
            level = 1;  
        } else if (rssi < -80 && rssi >= -100) {  
            level = 0;  
        } else {  
            level = 4;  
        }  
		
		return level;
	}

    @Override
    public void onResume() {
        super.onResume();
        Intent chargingIntent =
                registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = chargingIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        mPluggedIn = plugged == BatteryManager.BATTERY_PLUGGED_AC
                || plugged == BatteryManager.BATTERY_PLUGGED_USB
                || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        mDateFormat = getString(R.string.abbrev_wday_month_day_no_year);
        mDateFormatForAccessibility = getString(R.string.full_wday_month_day_no_year);

        setWakeLock();
        layoutClockSaver();
        mHandler.post(mMoveSaverRunnable);

        Utils.setMidnightUpdater(mHandler, mMidnightUpdater);
        
        move_count = 0;
             
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        
        TimerTask timerTask_test = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getRunningServiceInfo();
			}
		};
		Timer timer_test = new Timer();
		timer_test.schedule(timerTask_test, 0, 2000);
    }

    private void getRunningServiceInfo() {  
    	  
        // 设置一个默认Service的数量大小  
        int defaultNum = 20;  
        // 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程  
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager  
                .getRunningServices(defaultNum);  
   
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {  
  
            // 获得该Service的组件信息 可能是pkgname/servicename  
            ComponentName serviceCMP = runServiceInfo.service;  
            String serviceName = serviceCMP.getClassName(); // service 的类名  
            String pkgName = serviceCMP.getPackageName(); // 包名                         
            if(pkgName.equals("com.qiyi.gallery")||serviceName.equals("com.android.deskclock.Screensaver"))
            	ScreensaverActivity.this.finish();
            
        }
    }
    
    @Override
    public void onPause() {
    	
    	//if(linearLayout != null)
    	//	mWindowManager.removeView(linearLayout);
    	
        mHandler.removeCallbacks(mMoveSaverRunnable);
        Utils.cancelMidnightUpdater(mHandler, mMidnightUpdater);
        finish();
        super.onPause();
    }

    @Override
    public void onStop() {
        getContentResolver().unregisterContentObserver(mSettingsContentObserver);
        unregisterReceiver(mIntentReceiver);
        unregisterReceiver(wifiBatteryReceiver);
        super.onStop();
   }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) Log.d(TAG, "Screensaver configuration changed");
        super.onConfigurationChanged(newConfig);
        mHandler.removeCallbacks(mMoveSaverRunnable);
        layoutClockSaver();
        mHandler.postDelayed(mMoveSaverRunnable, 250);
    }
/*
    @Override
    public void onUserInteraction() {
        // We want the screen saver to exit upon user interaction.
        finish();
    }
*/
    
    
	
    private void setWakeLock() {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (mPluggedIn)
            winParams.flags |= mFlags;
        else
            winParams.flags &= (~mFlags);
        win.setAttributes(winParams);
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//showView();
			y_start =(int) event.getY();
			x_start =(int) event.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			
			move_count ++;
			y_move = (int) event.getY();
			x_move = (int) event.getX();
			
			int dy = y_move - y_start;
			int dx = x_move - x_start;
			
			if(move_count > 10 && Math.abs(dy) > 10)
				showView();
			if(Math.abs(dx)> 200){
				if(linearLayout != null)
		    		mWindowManager.removeView(linearLayout);
				linearLayout = null;
				finish();
			}
			if(dy > 0)
				current_light =current_light -20;
			else current_light = current_light +20;
			break;
		case MotionEvent.ACTION_UP:
			if(move_count<10)
				finish();
			if(linearLayout != null)
	    		mWindowManager.removeView(linearLayout);
			linearLayout = null;
			move_count = 0;
			break;
			
			
		default:
			break;
		}
		
		if(current_light < 10)
			current_light = 10;
		if(current_light > 255 )
			current_light = 255;
		
		if(linearLayout != null){
				
			textView.setText("");
			int temp = (int)(current_light/2.55);
			textView2.setText(""+temp);
			seekBar_Down_Up.setProgress(current_light);
			
			 WindowManager.LayoutParams lp = getWindow().getAttributes();     
			  
		     lp.screenBrightness = Float.valueOf(current_light) * (1f / 255f);    
		  
		     getWindow().setAttributes(lp);   
		     
		     android.provider.Settings.System.putInt(getContentResolver(),
                     android.provider.Settings.System.SCREEN_BRIGHTNESS,
                     current_light);
		}
		return super.onTouchEvent(event);
	}

    private void setClockStyle() {
        Utils.setClockStyle(this, mDigitalClock, mAnalogClock,
                SettingsActivity.KEY_CLOCK_STYLE);
        mSaverView = findViewById(R.id.main_clock);
        mClockStyle = (mSaverView == mDigitalClock ?
                Utils.CLOCK_TYPE_DIGITAL : Utils.CLOCK_TYPE_ANALOG);
        Utils.dimClockView(false, mSaverView);
    }

    private void layoutClockSaver() {
        setContentView(R.layout.desk_clock_saver);
        mDigitalClock = findViewById(R.id.digital_clock);
                
        mAnalogClock = findViewById(R.id.analog_clock);
        
        imageView_wifi = (ImageView)findViewById(R.id.imageview_wifi);
		textView_battery = (TextView)findViewById(R.id.textview_battery);
		imageView_battery = (ImageView)findViewById(R.id.imageview_battery);
        
        setClockStyle();
        Utils.setTimeFormat(ScreensaverActivity.this,(TextClock)mDigitalClock,
            (int)getResources().getDimension(R.dimen.bottom_text_size));

        mContentView = (View) mSaverView.getParent();
        mContentView.forceLayout();
        mSaverView.forceLayout();
      //  mSaverView.setAlpha(0);

        mMoveSaverRunnable.registerViews(mContentView, mSaverView);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Utils.updateDateAddOld(ScreensaverActivity.this,mDateFormat, mDateFormatForAccessibility,mContentView);
        Utils.refreshAlarm(ScreensaverActivity.this, mContentView);
    }

}
