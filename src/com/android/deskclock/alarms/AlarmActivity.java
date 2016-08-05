/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.deskclock.alarms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.deskclock.GetFont;
import com.android.deskclock.Log;
import com.android.deskclock.MySharedPreferences;
import com.android.deskclock.R;
import com.android.deskclock.ScrollLayout;
import com.android.deskclock.SettingsActivity;
import com.android.deskclock.Utils;
import com.android.deskclock.provider.AlarmInstance;
import com.android.deskclock.widget.multiwaveview.GlowPadView;

/**
 * Alarm activity that pops up a visible indicator when the alarm goes off.
 */
public class AlarmActivity extends Activity implements ScrollLayout.OnScrollToScreenListener{
    // AlarmActivity listens for this broadcast intent, so that other applications
    // can snooze the alarm (after ALARM_ALERT_ACTION and before ALARM_DONE_ACTION).
    public static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";

    // AlarmActivity listens for this broadcast intent, so that other applications
    // can dismiss the alarm (after ALARM_ALERT_ACTION and before ALARM_DONE_ACTION).
    public static final String ALARM_DISMISS_ACTION = "com.android.deskclock.ALARM_DISMISS";
    
	private static final String STOP_TEST_ALARM_SOUND = "zzl.bestidear.stop.alarm.sound";


    // Controller for GlowPadView.
    private class GlowPadController extends Handler implements GlowPadView.OnTriggerListener {
        private static final int PING_MESSAGE_WHAT = 101;
        private static final long PING_AUTO_REPEAT_DELAY_MSEC = 1200;

        public void startPinger() {
            sendEmptyMessage(PING_MESSAGE_WHAT);
        }

        public void stopPinger() {
            removeMessages(PING_MESSAGE_WHAT);
        }

        @Override
        public void handleMessage(Message msg) {
            ping();
            sendEmptyMessageDelayed(PING_MESSAGE_WHAT, PING_AUTO_REPEAT_DELAY_MSEC);
        }

        @Override
        public void onGrabbed(View v, int handle) {
            stopPinger();
        }

        @Override
        public void onReleased(View v, int handle) {
            startPinger();

        }

        @Override
        public void onTrigger(View v, int target) {
            switch (mGlowPadView.getResourceIdForTarget(target)) {
                case R.drawable.ic_alarm_alert_snooze:
                    Log.v("AlarmActivity - GlowPad snooze trigger");
                    snooze();
                    break;

                case R.drawable.ic_alarm_alert_dismiss:
                    Log.v("AlarmActivity - GlowPad dismiss trigger");
                    dismiss();
                    break;
                default:
                    // Code should never reach here.
                    Log.e("Trigger detected on unhandled resource. Skipping.");
            }
        }

        @Override
        public void onGrabbedStateChange(View v, int handle) {
        }

        @Override
        public void onFinishFinalAnimation() {
        }
    }

   
	private AlarmInstance mInstance;
    private int mVolumeBehavior;
    private GlowPadView mGlowPadView;
    private Button mButton_stop;
    private Button mButton_lazy;
    private AbsoluteLayout absoluteLayout_alarm_background;
    
    private ScrollLayout scrollLayout;
    
    private TextView textView_date;
    private GlowPadController glowPadController = new GlowPadController();
    
    private TimerTask timerTask  = null;
    private Timer timer = null;
    
    private BroadcastReceiver mReceiver = null;
    private Handler handler_update = null;
    
    private int[] background = new int[]{R.drawable.alarm_in,R.drawable.alarm_left,R.drawable.alarm_in
    		,R.drawable.alarm_right};
    private int current_backgroud = 0;

    private void snooze() {
    	 if(timer != null){
 			timerTask.cancel();
 			timer.cancel();
 			timer = null;
 			timerTask = null;
 		}
        AlarmStateManager.setSnoozeState(this, mInstance);
        new MySharedPreferences(AlarmActivity.this).putAlarmInstance(mInstance.mAlarmId);
    }

    private void dismiss() {
    	 if(timer != null){
 			timerTask.cancel();
 			timer.cancel();
 			timer = null;
 			timerTask = null;
 		}
        AlarmStateManager.setDismissState(this, mInstance);
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        long instanceId = AlarmInstance.getId(getIntent().getData());
        mInstance = AlarmInstance.getInstance(this.getContentResolver(), instanceId);
        
        if (mInstance != null) {
            Log.v("Displaying alarm for instance: " + mInstance);
            MySharedPreferences mySharedPreferences = new MySharedPreferences(AlarmActivity.this);
            if(mInstance.mAlarmId == mySharedPreferences.getAlarmInstance())
            	mySharedPreferences.putAlarmInstance((long) 0);
        } else {
            // The alarm got deleted before the activity got created, so just finish()
            Log.v("Error displaying alarm for intent: " + getIntent());
            finish();
            return;
        }

        // Get the volume/camera button behavior setting
        final String vol =
                PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.KEY_VOLUME_BEHAVIOR,
                        SettingsActivity.DEFAULT_VOLUME_BEHAVIOR);
        mVolumeBehavior = Integer.parseInt(vol);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        // In order to allow tablets to freely rotate and phones to stick
        // with "nosensor" (use default device orientation) we have to have
        // the manifest start with an orientation of unspecified" and only limit
        // to "nosensor" for phones. Otherwise we get behavior like in b/8728671
        // where tablets start off in their default orientation and then are
        // able to freely rotate.
        if (!getResources().getBoolean(R.bool.config_rotateAlarmAlert)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        updateLayout();

        // Register to get the alarm done/snooze/dismiss intent.
        IntentFilter filter = new IntentFilter(AlarmService.ALARM_DONE_ACTION);
        filter.addAction(ALARM_SNOOZE_ACTION);
        filter.addAction(ALARM_DISMISS_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.v("AlarmActivity - Broadcast Receiver - " + action);
                if (action.equals(ALARM_SNOOZE_ACTION)) {
                    snooze();
                } else if (action.equals(ALARM_DISMISS_ACTION)) {
                    dismiss();
                } else if (action.equals(AlarmService.ALARM_DONE_ACTION)) {
                    finish();
                } else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                    dismiss();

                } else if(action.equals(Intent.ACTION_SHUTDOWN)){
                	dismiss();
                }else {
                    Log.i("Unknown broadcast in AlarmActivity: " + action);
                }
            }
        };
        
        if(mReceiver != null)
        	registerReceiver(mReceiver, filter);
                
         timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
                dismiss();
			}
		};
		
		timer = new Timer();
		timer.schedule(timerTask, 2*60*1000);
		
		handler_update = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				absoluteLayout_alarm_background.setBackgroundResource(background[current_backgroud]);
				current_backgroud = current_backgroud + 1;
				if(current_backgroud == 4)
					current_backgroud = 0;

			}
			
		};
		
		sendBroadcast(new Intent(STOP_TEST_ALARM_SOUND));
    }

    

    @Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
    	dismiss();
		super.onUserLeaveHint();
	}

	private void updateTitle() {
        final String titleText = mInstance.getLabelOrDefault(this);
        TextView tv = (TextView)findViewById(R.id.alertTitle);
        tv.setText(titleText);
        super.setTitle(titleText);
    }

    private void updateLayout() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.alarm_alert, null);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(view);
        updateTitle();
        Utils.setTimeFormat(AlarmActivity.this,(TextClock)(view.findViewById(R.id.digitalClock)),
                (int)getResources().getDimension(R.dimen.bottom_text_size));

        // Setup GlowPadController
        mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        mGlowPadView.setOnTriggerListener(glowPadController);
        glowPadController.startPinger();
        
        mButton_stop = (Button)findViewById(R.id.button_alarm_new_stop);
        mButton_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                dismiss();
			}
		});
        
        mButton_lazy = (Button)findViewById(R.id.button_alarm_new_lazy);
        mButton_lazy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				snooze();
			}
		});
        
        textView_date = (TextView)findViewById(R.id.textview_alarm_new_date);
        String mDateFormat;
        String mDateFormatForAccessibility;
        mDateFormat = getString(R.string.abbrev_wday_month_day_no_year);
        mDateFormatForAccessibility = getString(R.string.full_wday_month_day_no_year);
       // updateDate( textView_date, mDateFormat, mDateFormatForAccessibility);
        updateDate2(textView_date);
        scrollLayout = (ScrollLayout)findViewById(R.id.scrolllayout_stop_alarm);
        scrollLayout.setOnScrollToScreen(AlarmActivity.this);
        scrollLayout.setToScreen(1);
        
        absoluteLayout_alarm_background = (AbsoluteLayout)findViewById(R.id.AbsoluteLayout_alarm_background);
        TimerTask timerTask2 = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				handler_update.obtainMessage().sendToTarget();
				
			}
		};
		Timer timer2 = new Timer();
		timer2.schedule(timerTask2, 1000, 500);
        
    }

    private void updateDate(TextView textView,String dateformat,String dateFormatForAccessibility){
    	
    	 Date now = new Date();        
         GetFont getFont = new GetFont(AlarmActivity.this);
         textView.setTypeface(getFont.getExpansivatype());
         if (textView != null) {
             final Locale l = Locale.getDefault();
             String fmt = DateFormat.getBestDateTimePattern(l, dateformat);
             SimpleDateFormat sdf = new SimpleDateFormat(fmt, l);
             textView.setText(sdf.format(now).replace("/", "-").replace(" ", "\n"));
             textView.setVisibility(View.VISIBLE);
             fmt = DateFormat.getBestDateTimePattern(l, dateFormatForAccessibility);
             sdf = new SimpleDateFormat(fmt, l);
             textView.setContentDescription(sdf.format(now));
         }
    }
    
    private void updateDate2(TextView textView){
    	
    	int[] weeks = new int[]{R.string.dialog_sun,R.string.dialog_mon,R.string.dialog_tue,
        		R.string.dialog_wed,R.string.dialog_thr,R.string.dialog_fri,R.string.dialog_sat};
    	
        GetFont getFont = new GetFont(AlarmActivity.this);
        textView.setTypeface(getFont.getExpansivatype());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        month = month +1;
        
        
        String str_week = this.getResources().getString(weeks[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        if (textView != null) {
            textView.setText(year+"-"+(month>9?month:"0"+month)+"-"+(day>9?day:"0"+day)+"\n\n"+str_week);
        }
   }
    
    private void ping() {
        mGlowPadView.ping();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glowPadController.startPinger();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glowPadController.stopPinger();
        
    }

    @Override
    public void onDestroy() {
        try{
        if(mReceiver != null){
        	unregisterReceiver(mReceiver);
        	mReceiver = null;
        }
        }catch(java.lang.IllegalArgumentException e){
        	e.printStackTrace();
        }
        super.onDestroy();      

    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
    	dismiss();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Do this on key down to handle a few of the system keys.
        Log.v("AlarmActivity - dispatchKeyEvent - " + event.getKeyCode());
        switch (event.getKeyCode()) {
            // Volume keys and camera keys dismiss the alarm
            case KeyEvent.KEYCODE_POWER:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_MUTE:
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_FOCUS:
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (mVolumeBehavior) {
                        case 1:
                            snooze();
                            break;

                        case 2:
                            dismiss();
                            break;

                        default:
                            break;
                    }
                }
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

	@Override
	public void doAction(int whichScreen) {
		// TODO Auto-generated method stub
		if(whichScreen == 0)
			dismiss();
	}
}
