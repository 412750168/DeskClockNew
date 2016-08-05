/*
 * Copyright (C) 2011 The Android Open Source Project
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

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.deskclock.Utils.ScreensaverMoveSaverRunnable;
import com.android.deskclock.provider.Alarm;

public class Screensaver extends DreamService {
    static final boolean DEBUG = false;
    static final String TAG = "DeskClock/Screensaver";

    private View mContentView, mSaverView;
    private View mAnalogClock, mDigitalClock;
    private String mDateFormat;
    private String mDateFormatForAccessibility;

    private final Handler mHandler = new Handler();

    private final ScreensaverMoveSaverRunnable mMoveSaverRunnable;
    
  //zzl
    
  	private WindowManager mWindowManager;
  	private WindowManager.LayoutParams param;
  	// private FloatView mLayout;

  	private LinearLayout linearLayout=null;
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
	 
	 private TextView textView_iqi_message;
	    
	 private int[] wifi_signal = {R.drawable.i_wifi_0,R.drawable.i_wifi_1,R.drawable.i_wifi_2,R.drawable.i_wifi_3};
	 private int[] battery_signal = {R.drawable.i_battery_25,R.drawable.i_barrery_50,R.drawable.i_battery_75,R.drawable.i_battery_100};
	   
	 private static final int BATTERY = 0x400 +1;
	 private static final int WIFI = 0x400 +2;
	 private static final int IQI_MESSAGE = 0x400 + 3;
	 
	 private Handler handler;
	 private WifiBatteryReceiver wifiBatteryReceiver;

    private final ContentObserver mSettingsContentObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            Utils.refreshAlarm(Screensaver.this, mContentView);
        }
    };

    // Thread that runs every midnight and refreshes the date.
    private final Runnable mMidnightUpdater = new Runnable() {
        @Override
        public void run() {
            Utils.updateDateAddOld(Screensaver.this,mDateFormat, mDateFormatForAccessibility, mContentView);
            Utils.setMidnightUpdater(mHandler, mMidnightUpdater);
        }
    };

    /**
     * Receiver to handle time reference changes.
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null && (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED))) {
                Utils.updateDateAddOld(Screensaver.this,mDateFormat, mDateFormatForAccessibility, mContentView);
                Utils.refreshAlarm(Screensaver.this, mContentView);
                Utils.setMidnightUpdater(mHandler, mMidnightUpdater);
            }
        }
    };

    public Screensaver() {
        if (DEBUG) Log.d(TAG, "Screensaver allocated");
        mMoveSaverRunnable = new ScreensaverMoveSaverRunnable(mHandler);
    }

    
    
    @Override
	public boolean dispatchTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
    	
    	switch (arg0.getAction()) {
    	case MotionEvent.ACTION_DOWN:
			//showView();
			y_start =(int) arg0.getY();
			x_start =(int) arg0.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			
			move_count ++;
			y_move = (int) arg0.getY();
			x_move = (int) arg0.getX();
			
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
		     
		   /*  try {
		    	 PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		    	 Class<?> pmClass = Class.forName(pm.getClass().getName());
		    	 Method method = pmClass.getDeclaredMethod("setBacklightBrightness",
		    	 int.class);
		    	 method.setAccessible(true);
		    	 method.invoke(pm, current_light);
		    	 } catch (Exception e) {
		    	 e.printStackTrace();
		    	 }
		     */
		}
    	
		return super.dispatchTouchEvent(arg0);
	}



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
    
    @Override
    public void onCreate() {
        if (DEBUG) Log.d(TAG, "Screensaver created");
        super.onCreate();

        mDateFormat = getString(R.string.abbrev_wday_month_day_no_year);
        mDateFormatForAccessibility = getString(R.string.full_wday_month_day_no_year);
        
        
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
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) Log.d(TAG, "Screensaver configuration changed");
        super.onConfigurationChanged(newConfig);
        mHandler.removeCallbacks(mMoveSaverRunnable);
        layoutClockSaver();
        //mHandler.post(mMoveSaverRunnable);
    }

    @Override
    public void onAttachedToWindow() {
        if (DEBUG) Log.d(TAG, "Screensaver attached to window");
        super.onAttachedToWindow();

        // We want the screen saver to exit upon user interaction.
        setInteractive(false);

        setFullscreen(true);

        layoutClockSaver();

        // Setup handlers for time reference changes and date updates.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mIntentReceiver, filter);
        Utils.setMidnightUpdater(mHandler, mMidnightUpdater);

        getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.NEXT_ALARM_FORMATTED),
                false,
                mSettingsContentObserver);
        mHandler.post(mMoveSaverRunnable);
        
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
					
				case IQI_MESSAGE:
					int i = (Integer)msg.obj;
					if(i== 0)
						textView_iqi_message.setVisibility(View.GONE);
					else {
						textView_iqi_message.setVisibility(View.VISIBLE);
						String format = getResources().getString(R.string.iqi_message);
						String number = String.format(format, "  "+i+"");
						textView_iqi_message.setText(number);
					}
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
        
        TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int num = queryMessage();
				handler.obtainMessage(IQI_MESSAGE, num).sendToTarget();
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(timerTask, 500, 2000);
        
    }
    
    private int queryMessage(){
    	
    	int num = 0;
        final Uri URI = Uri.parse("content://com.qiyi.minitv.video.messagecenter/itvcloud");
        String select = "is_read=0";
    	
    	ContentResolver cr = this.getContentResolver();
    	Cursor cursor  = cr.query(URI, null,
                select, null, null);
    	
    	if (cursor == null) {
            return num;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    num = num + 1;
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

    	return num;
    }

    @Override
    public void onDetachedFromWindow() {
        if (DEBUG) Log.d(TAG, "Screensaver detached from window");
        super.onDetachedFromWindow();

        mHandler.removeCallbacks(mMoveSaverRunnable);
        getContentResolver().unregisterContentObserver(mSettingsContentObserver);

        // Tear down handlers for time reference changes and date updates.
        Utils.cancelMidnightUpdater(mHandler, mMidnightUpdater);
        unregisterReceiver(mIntentReceiver);
        unregisterReceiver(wifiBatteryReceiver);
        

    }

    private void setClockStyle() {
        Utils.setClockStyle(this, mDigitalClock, mAnalogClock,
                ScreensaverSettingsActivity.KEY_CLOCK_STYLE);
        mSaverView = findViewById(R.id.main_clock);
        boolean dimNightMode = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(ScreensaverSettingsActivity.KEY_NIGHT_MODE, false);
        Utils.dimClockView(false, mSaverView);
        setScreenBright(!dimNightMode);
    }

    private void layoutClockSaver() {
        setContentView(R.layout.desk_clock_saver);
        mDigitalClock = findViewById(R.id.digital_clock);
        mAnalogClock =findViewById(R.id.analog_clock);
        
        imageView_wifi = (ImageView)findViewById(R.id.imageview_wifi);
		textView_battery = (TextView)findViewById(R.id.textview_battery);
		imageView_battery = (ImageView)findViewById(R.id.imageview_battery);
		
		textView_iqi_message = (TextView)findViewById(R.id.textview_iqi_message);
        
        setClockStyle();
        Utils.setTimeFormat(Screensaver.this,(TextClock)mDigitalClock,
            (int)getResources().getDimension(R.dimen.bottom_text_size));

        mContentView = (View) mSaverView.getParent();
        //mSaverView.setAlpha(0);

        mMoveSaverRunnable.registerViews(mContentView, mSaverView);

        Utils.updateDateAddOld(Screensaver.this,mDateFormat, mDateFormatForAccessibility, mContentView);
        Utils.refreshAlarm(Screensaver.this, mContentView);
    }
}
