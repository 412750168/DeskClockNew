/*
 * Copyright (C) 2007 The Android Open Source Project
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

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

import com.android.deskclock.alarms.AlarmStateManager;
import com.android.deskclock.provider.Alarm;

import com.android.deskclock.provider.AlarmInstance;
import com.android.deskclock.timer.TimerObj;

public class AlarmInitReceiver extends BroadcastReceiver {

    // A flag that indicates that switching the volume button default was done
    private static final String PREF_VOLUME_DEF_DONE = "vol_def_done";

    /**
     * Sets alarm on ACTION_BOOT_COMPLETED.  Resets alarm on
     * TIME_SET, TIMEZONE_CHANGED
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        Log.v("AlarmInitReceiver " + action);

        final PendingResult result = goAsync();
        final WakeLock wl = AlarmAlertWakeLock.createPartialWakeLock(context);
        
        if(intent.getAction().equals("android.intent.action.TIME_SET")||intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")){
        	
			ContentResolver cr = context.getContentResolver();
			
            List<Alarm> enabledAlarmList = Alarm.getAlarms(cr, Alarm.ENABLED + "=1");
            for(Alarm alarm : enabledAlarmList){
            	asyncUpdateAlarm(context,alarm, false);
            }
            
          
        }
        
        wl.acquire();

        // We need to increment the global id out of the async task to prevent
        // race conditions
        AlarmStateManager.updateGloablIntentId(context);
        AsyncHandler.post(new Runnable() {
            @Override public void run() {
                // Remove the snooze alarm after a boot.
                if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    // Clear stopwatch and timers data
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    Log.v("AlarmInitReceiver - Reset timers and clear stopwatch data");
                    TimerObj.resetTimersInSharedPrefs(prefs);
                    Utils.clearSwSharedPref(prefs);

                    if (!prefs.getBoolean(PREF_VOLUME_DEF_DONE, false)) {
                        // Fix the default
                        Log.v("AlarmInitReceiver - resetting volume button default");
                        switchVolumeButtonDefault(prefs);
                    }
                }

                // Update all the alarm instances on time change event
                AlarmStateManager.fixAlarmInstances(context);

                result.finish();
                Log.v("AlarmInitReceiver finished");
                wl.release();
            }
        });
    }

    private void asyncUpdateAlarm(final Context context,final Alarm alarm, final boolean popToast) {
		
		final AsyncTask<Void, Void, AlarmInstance> updateTask = new AsyncTask<Void, Void, AlarmInstance>() {
			@Override
			protected AlarmInstance doInBackground(Void... parameters) {
				ContentResolver cr = context.getContentResolver();

				// Dismiss all old instances
				AlarmStateManager.deleteAllInstances(context, alarm.id);
				
				// Update alarm
				Alarm.updateAlarm(cr, alarm);
				
				if (alarm.enabled) {
					return setupAlarmInstance(context, alarm);
				}

				return null;
			}

			@Override
			protected void onPostExecute(AlarmInstance instance) {
				if (popToast && instance != null) {
					AlarmUtils.popAlarmSetToast(context, instance
							.getAlarmTime().getTimeInMillis());
				}
			}
		};
		updateTask.execute();
	}

    
    private static AlarmInstance setupAlarmInstance(Context context, Alarm alarm) {
		ContentResolver cr = context.getContentResolver();
		AlarmInstance newInstance = alarm.createInstanceAfter(Calendar
				.getInstance());
		newInstance = AlarmInstance.addInstance(cr, newInstance);
		// Register instance to state manager
		AlarmStateManager.registerInstance(context, newInstance, true);
		return newInstance;
	}
    
    private void switchVolumeButtonDefault(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(SettingsActivity.KEY_VOLUME_BEHAVIOR,
            SettingsActivity.DEFAULT_VOLUME_BEHAVIOR);

        // Make sure we do it only once
        editor.putBoolean(PREF_VOLUME_DEF_DONE, true);
        editor.apply();
    }
}
