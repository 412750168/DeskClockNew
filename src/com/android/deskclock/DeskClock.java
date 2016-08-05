package com.android.deskclock;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import junit.framework.Test;

import com.android.deskclock.alarms.AlarmStateManager;
import com.android.deskclock.provider.Alarm;
import com.android.deskclock.stopwatch.StopwatchFragment;
import com.android.deskclock.stopwatch.StopwatchService;
import com.android.deskclock.stopwatch.Stopwatches;
import com.android.deskclock.timer.TimerFragment;
import com.android.deskclock.timer.TimerObj;
import com.android.deskclock.timer.Timers;
import com.android.deskclock.worldclock.CitiesActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeskClock extends Activity implements LabelDialogFragment.TimerLabelDialogHandler,
LabelDialogFragment.AlarmLabelDialogHandler{
	
	private static final boolean DEBUG = false;

    private static final String LOG_TAG = "DeskClock";

    // Alarm action for midnight (so we can update the date display).
    private static final String KEY_SELECTED_TAB = "selected_tab";
    private static final String KEY_CLOCK_STATE = "clock_state";

    public static final String SELECT_TAB_INTENT_EXTRA = "deskclock.select.tab";

    public static final int ALARM_TAB_INDEX = 0;
    public static final int CLOCK_TAB_INDEX = 1;
    public static final int TIMER_TAB_INDEX = 2;
    public static final int STOPWATCH_TAB_INDEX = 3;
    // Tabs indices are switched for right-to-left since there is no
    // native support for RTL in the ViewPager.
    public static final int RTL_ALARM_TAB_INDEX = 3;
    public static final int RTL_CLOCK_TAB_INDEX = 2;
    public static final int RTL_TIMER_TAB_INDEX = 1;
    public static final int RTL_STOPWATCH_TAB_INDEX = 0;	
    
    private static final int BATTERY = 0x400 +1;
    private static final int WIFI = 0x400 +2;
    
    private ClockFragment clockFragment;
    private AlarmClockFragment alarmClockFragment;
    private TimerFragment timerFragment;
    private StopwatchFragment stopwatchFragment;
    
    private ImageView imageView_wifi;
    private TextView textView_battery;
    private ImageView imageView_battery;
    
    private int[] wifi_signal = {R.drawable.i_wifi_0,R.drawable.i_wifi_1,R.drawable.i_wifi_2,R.drawable.i_wifi_3};
    private int[] battery_signal = {R.drawable.i_battery_25,R.drawable.i_barrery_50,R.drawable.i_battery_75,R.drawable.i_battery_100};
   
   // private TimerTask timerTask ;
   // private Timer timer;
    private Handler handler;
    
    private Button button_enter_fullscreen = null;
    private Button button_setting = null;
    private Button button_add_alarm = null;
    private Button button_back = null;
    private ImageView imageView_finish= null;
    private TextView textView_finish = null;
    private LinearLayout linearLayout_back = null;
    private LinearLayout linearLayout_title_bar = null;
    
    private int Count = 0;
    private WifiBatteryReceiver wifiBatteryReceiver ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.desk_clock_new);
		
		/*android.app.ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		View customView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, new LinearLayout(this), false);
		
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(customView);
		*/
		imageView_wifi = (ImageView)findViewById(R.id.imageview_wifi);
		textView_battery = (TextView)findViewById(R.id.textview_battery);
		imageView_battery = (ImageView)findViewById(R.id.imageview_battery);
		
		android.app.FragmentManager fragmentManager = getFragmentManager();
		android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		clockFragment = new ClockFragment();
		alarmClockFragment = new AlarmClockFragment();
		
		timerFragment = new TimerFragment();
		stopwatchFragment = new StopwatchFragment();
		
		fragmentTransaction.add(R.id.deskclock_new, clockFragment);
		fragmentTransaction.add(R.id.alarmclock_new, alarmClockFragment);
		fragmentTransaction.commit();
		
		clockFragment.setInterfaceTestSettingAlarm(alarmClockFragment);
		
		setHomeTimeZone();
		// We need to update the system next alarm time on app startup because the
        // user might have clear our data.
        AlarmStateManager.updateNextAlarm(this);
        
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
					if(num == 100 && (statue == BatteryManager.BATTERY_STATUS_CHARGING ||
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
        
        button_enter_fullscreen = (Button)findViewById(R.id.button_actionbar_enter_fullscreen);
		button_setting = (Button)findViewById(R.id.button_actionbar_setting);
		button_add_alarm = (Button)findViewById(R.id.button_action_bar_add_alarm);
		button_back = (Button)findViewById(R.id.button_actionbar_finish);
		
		imageView_finish = (ImageView)findViewById(R.id.imageView_finish);
		textView_finish = (TextView)findViewById(R.id.textview_finish);
		linearLayout_back = (LinearLayout)findViewById(R.id.linearlayout_back);
		
		linearLayout_title_bar = (LinearLayout)findViewById(R.id.linearlayout_title_bar);
		linearLayout_title_bar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				 if(event.getAction() == MotionEvent.ACTION_DOWN){

		             v.setAlpha(0.5f);

		          }else if(event.getAction() == MotionEvent.ACTION_UP){

		             v.setAlpha(1.0f);

		          }
				
				return true;
			}
		});
		
		linearLayout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		imageView_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		textView_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		button_enter_fullscreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DeskClock.this, ScreensaverActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});
		
		button_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Count == 0){
					Count ++;
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Count = 0;
						}
					}, 1000);
					Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		});
		
		button_add_alarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alarmClockFragment.addNewAlarm();
			}
		});
		
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// We only want to show notifications for stopwatch/timer when the app is closed so
        // that we don't have to worry about keeping the notifications in perfect sync with
        // the app.
        Intent stopwatchIntent = new Intent(getApplicationContext(), StopwatchService.class);
        stopwatchIntent.setAction(Stopwatches.KILL_NOTIF);
        startService(stopwatchIntent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, true);
        editor.apply();
        Intent timerIntent = new Intent();
        timerIntent.setAction(Timers.NOTIF_IN_USE_CANCEL);
        sendBroadcast(timerIntent);
        
     /*   timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MySharedPreferences mySharedPreferences = new MySharedPreferences(DeskClock.this);
				if(mySharedPreferences.getAllowAlarmScreenSaver())
					startActivity(new Intent(DeskClock.this, ScreensaverActivity.class));
			
			}
		};
		
		timer = new Timer();
		timer.schedule(timerTask, 30000,30000);*/
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		int id = item.getItemId();
		switch (id) {

		case android.R.id.home:
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
	/*	if(timer != null){
			timerTask.cancel();
			timer.cancel();
		}
		
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MySharedPreferences mySharedPreferences = new MySharedPreferences(DeskClock.this);
				if(mySharedPreferences.getAllowAlarmScreenSaver())
					startActivity(new Intent(DeskClock.this, ScreensaverActivity.class));
				
			}
		};
		
		timer = new Timer();
		timer.schedule(timerTask, 30000,30000);
		*/
		super.onUserInteraction();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(getApplicationContext(), StopwatchService.class);
        intent.setAction(Stopwatches.SHOW_NOTIF);
        startService(intent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, false);
        editor.apply();
        Utils.showInUseNotifications(this);
        
    /*    if(timer != null){
			timerTask.cancel();
			timer.cancel();
			timer.purge();
			timer = null;
			timerTask = null;
		}*/
		super.onPause();
	}
	
	

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		unregisterReceiver(wifiBatteryReceiver);
	}

	public void clockButtonsOnClick(View v) {
        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.cities_button:
                startActivity(new Intent(this, CitiesActivity.class));
                break;
            default:
                break;
        }
    }

	@Override
	public void onDialogLabelSet(Alarm alarm, String label, String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogLabelSet(TimerObj timer, String label, String tag) {
		// TODO Auto-generated method stub
		
	}
	
	public void registerPageChangedListener(DeskClockFragment frag) {
        
    }

    public void unregisterPageChangedListener(DeskClockFragment frag) {
       
    }
    
    /***
     * Insert the local time zone as the Home Time Zone if one is not set
     */
    private void setHomeTimeZone() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String homeTimeZone = prefs.getString(SettingsActivity.KEY_HOME_TZ, "");
        if (!homeTimeZone.isEmpty()) {
            return;
        }
        homeTimeZone = TimeZone.getDefault().getID();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SettingsActivity.KEY_HOME_TZ, homeTimeZone);
        editor.apply();
        Log.v(LOG_TAG, "Setting home time zone to " + homeTimeZone);
    }
    
    public static abstract class OnTapListener implements OnTouchListener {
        private float mLastTouchX;
        private float mLastTouchY;
        private long mLastTouchTime;
        private final TextView mMakePressedTextView;
        private final int mPressedColor, mGrayColor;
        private final float MAX_MOVEMENT_ALLOWED = 20;
        private final long MAX_TIME_ALLOWED = 500;

        public OnTapListener(Activity activity, TextView makePressedView) {
            mMakePressedTextView = makePressedView;
            mPressedColor = activity.getResources().getColor(Utils.getPressedColorId());
            mGrayColor = activity.getResources().getColor(Utils.getGrayColorId());
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    mLastTouchTime = Utils.getTimeNow();
                    mLastTouchX = e.getX();
                    mLastTouchY = e.getY();
                    if (mMakePressedTextView != null) {
                        mMakePressedTextView.setTextColor(mPressedColor);
                    }
                    break;
                case (MotionEvent.ACTION_UP):
                    float xDiff = Math.abs(e.getX()-mLastTouchX);
                    float yDiff = Math.abs(e.getY()-mLastTouchY);
                    long timeDiff = (Utils.getTimeNow() - mLastTouchTime);
                    if (xDiff < MAX_MOVEMENT_ALLOWED && yDiff < MAX_MOVEMENT_ALLOWED
                            && timeDiff < MAX_TIME_ALLOWED) {
                        if (mMakePressedTextView != null) {
                            v = mMakePressedTextView;
                        }
                        processClick(v);
                        resetValues();
                        return true;
                    }
                    resetValues();
                    break;
                case (MotionEvent.ACTION_MOVE):
                    xDiff = Math.abs(e.getX()-mLastTouchX);
                    yDiff = Math.abs(e.getY()-mLastTouchY);
                    if (xDiff >= MAX_MOVEMENT_ALLOWED || yDiff >= MAX_MOVEMENT_ALLOWED) {
                        resetValues();
                    }
                    break;
                default:
                    resetValues();
            }
            return false;
        }

        private void resetValues() {
            mLastTouchX = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchY = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchTime = -1*MAX_TIME_ALLOWED + 1;
            if (mMakePressedTextView != null) {
                mMakePressedTextView.setTextColor(mGrayColor);
            }
        }

        protected abstract void processClick(View v);
    }

}
