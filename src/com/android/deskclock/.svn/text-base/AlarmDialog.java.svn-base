package com.android.deskclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import com.android.deskclock.PickerView.onSelectListener;
import com.android.deskclock.provider.Alarm;

import android.app.Dialog;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AlarmDialog implements RepeatDialog.onSetRepeatListen ,SoundDialog.onSetRingListen
	,LabelDialog.onSetLabelListen{

	final private Context mcontext;
	private Dialog mdialog ;
	
	PickerView hour_pv;
	PickerView minute_pv;
	PickerView ampm_pv;
	
	private NewAlarmState newAlarmState;
	
	private FrameLayout frameLayout;
	private Button button_repeat ;
	private Button button_sound ;
	private Button button_label;
	private Button button_cancel;
	private Button button_finish;
	private Button button_delete;
	private Button button_mend;
	
	private Alarm alarm;
	private View adapterItemView ;
	
	private AlarmClockFragment alarmClockFragment;
	private MySharedPreferences mySharedPreferences;
	
	String am ;
	String pm ;
	
	private int Count = 0;
	
	private final int[] DAY_ORDER = new int[] { Calendar.SUNDAY,
			Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
			Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, };
	
	class NewAlarmState{
		
		public boolean isAm ;
		public int    hour;
		public int    minute;
		public int[]  days;
		public Uri sound_uri;
		public String label;
		
	}
	
	public AlarmDialog(final Context mcontext) {
		super();
		this.mcontext = mcontext;
		
		initView();
		initData();
		initEvent();
		
		mySharedPreferences = new MySharedPreferences(mcontext);
		mySharedPreferences.putAllowAlarmScreenSaver(false);
	}
	
	public AlarmDialog(final Context mcontext, Alarm alarm ,View view){
		super();
		this.mcontext = mcontext;
		adapterItemView = view ;
		this.alarm = alarm;
		
		initView();
		setDeleteAndMendVisible();
		initData(alarm);
		initEvent();
		mySharedPreferences = new MySharedPreferences(mcontext);
		mySharedPreferences.putAllowAlarmScreenSaver(false);
	}
	
	public void initView(){
		
		mdialog = new Dialog(mcontext, R.style.Dialog_notitle);
		mdialog.setContentView(R.layout.alarmdialog);
        mdialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消�? 
        
        hour_pv = (PickerView) mdialog.findViewById(R.id.hour_pv);
		minute_pv = (PickerView) mdialog.findViewById(R.id.minute_pv);
		ampm_pv = (PickerView) mdialog.findViewById(R.id.ampm_pv);
		
		button_repeat = (Button)mdialog.findViewById(R.id.button_repeat);
		button_sound = (Button)mdialog.findViewById(R.id.button_sound);
		button_label = (Button)mdialog.findViewById(R.id.button_label);
		
		frameLayout = (FrameLayout)mdialog.findViewById(R.id.framelayout_ampm_pv);
		
		button_cancel = (Button)mdialog.findViewById(R.id.button_dialog_editor_cancel);
		button_finish = (Button)mdialog.findViewById(R.id.button_dialog_editor_finish);
		
		button_delete = (Button)mdialog.findViewById(R.id.button_dialog_editor_delete);
		button_mend   = (Button)mdialog.findViewById(R.id.button_dialog_editor_mend);

	}
	
	private void setDeleteAndMendVisible(){
		
		button_delete.setVisibility(View.VISIBLE);
		button_mend.setVisibility(View.VISIBLE);
		button_finish.setVisibility(View.GONE);
	}
	
	public void initData(){
		
		newAlarmState = new NewAlarmState();
		newAlarmState.days = new int[7];
		for(int i = 0;i< 7;i++){
			newAlarmState.days[i] = 0;
		}
		
		Time time = new Time();
		time.setToNow();
		
		newAlarmState.hour = time.hour;
		newAlarmState.minute = time.minute;
		
		
		if(is24DateFormate()){
			
		}
		else {
			
			if(newAlarmState.hour == 12){
				newAlarmState.hour = newAlarmState.hour + 0;
				
				newAlarmState.isAm = false;
			}
			
			if(newAlarmState.hour == 0){
				newAlarmState.hour = newAlarmState.hour +12;
				
				newAlarmState.isAm = true;
			}		
			
			if(newAlarmState.hour > 0 && newAlarmState.hour < 12){
				newAlarmState.hour = newAlarmState.hour ;
				
				newAlarmState.isAm = true;
			}
			
			if(newAlarmState.hour > 12 && newAlarmState.hour <= 23){
				newAlarmState.hour = newAlarmState.hour - 12;
				
				newAlarmState.isAm = false;
			}
		}		
		
		newAlarmState.sound_uri = RingtoneManager.getActualDefaultRingtoneUri(
				mcontext, RingtoneManager.TYPE_ALARM);
		if (newAlarmState.sound_uri == null) {
			newAlarmState.sound_uri = Uri.parse("content://settings/system/alarm_alert");
		}
		
		newAlarmState.label = mcontext.getResources().getString(R.string.dialog_label);
		
		if(is24DateFormate())
			frameLayout.setVisibility(View.GONE);
		else frameLayout.setVisibility(View.VISIBLE);
		
		List<String> hour = new ArrayList<String>();
		List<String> minute = new ArrayList<String>();
		List<String> ampm = new ArrayList<String>();
		
		if(is24DateFormate()){
			for (int i = 0; i < 24; i++)
			{
				hour.add(i<10 ?"0" + i: ""+i);
			}
		}else{
			for (int i = 1; i < 13; i++)
			{
				hour.add(i<10 ?"0" + i: ""+i);
			}
		}
		
		for (int i = 0; i < 60; i++)
		{
			minute.add(i < 10 ? "0" + i : "" + i);
		}
		
		am= mcontext.getResources().getString(R.string.dialog_am);
	    pm = mcontext.getResources().getString(R.string.dialog_pm);
		ampm.add(am);
		ampm.add(pm);
		
		hour_pv.setData(hour);
		minute_pv.setData(minute);
		ampm_pv.setData(ampm);
		if(is24DateFormate())
			hour_pv.setSelected(newAlarmState.hour);
		else {
			hour_pv.setSelected(newAlarmState.hour-1);
		}
		minute_pv.setSelected(newAlarmState.minute);
		ampm_pv.setSelected(newAlarmState.isAm?0:1);
		
		
	}
	
	
	
	public void initData(Alarm alarm){
		
		newAlarmState = new NewAlarmState();
		newAlarmState.days = new int[7];
		
		HashSet<Integer> setDays = alarm.daysOfWeek.getSetDays();

		if(setDays.size() >0){
			for (int i = 0; i < 7; i++) {
				if (setDays.contains(DAY_ORDER[i])) {
					newAlarmState.days[i] = DAY_ORDER[i];
				}else{
					newAlarmState.days[i] = 0;
				}
				
			}
		}
		
		newAlarmState.hour = alarm.hour;
		newAlarmState.minute = alarm.minutes;
		
		
		if(is24DateFormate()){
			
		}
		else {
			
			if(newAlarmState.hour == 12){
				newAlarmState.hour = newAlarmState.hour + 0;
				
				newAlarmState.isAm = false;
			}
			
			if(newAlarmState.hour == 0){
				newAlarmState.hour = newAlarmState.hour +12;
				
				newAlarmState.isAm = true;
			}		
			
			if(newAlarmState.hour > 0 && newAlarmState.hour < 12){
				newAlarmState.hour = newAlarmState.hour ;
				
				newAlarmState.isAm = true;
			}
			
			if(newAlarmState.hour > 12 && newAlarmState.hour <= 23){
				newAlarmState.hour = newAlarmState.hour - 12;
				
				newAlarmState.isAm = false;
			}
		}
		
		newAlarmState.sound_uri = alarm.alert;
		if (newAlarmState.sound_uri == null) {
			newAlarmState.sound_uri = Uri.parse("content://settings/system/alarm_alert");
		}
		
		newAlarmState.label = alarm.label;
		
		if(is24DateFormate())
			frameLayout.setVisibility(View.GONE);
		else frameLayout.setVisibility(View.VISIBLE);
		
		List<String> hour = new ArrayList<String>();
		List<String> minute = new ArrayList<String>();
		List<String> ampm = new ArrayList<String>();
		
		if(is24DateFormate()){
			for (int i = 0; i < 24; i++)
			{
				hour.add(i<10 ?"0" + i: ""+i);
			}
		}else{
			for (int i = 1; i < 13; i++)
			{
				hour.add(i<10 ?"0" + i: ""+i);
			}
		}
		
		for (int i = 0; i < 60; i++)
		{
			minute.add(i < 10 ? "0" + i : "" + i);
		}
		
		am= mcontext.getResources().getString(R.string.dialog_am);
	    pm = mcontext.getResources().getString(R.string.dialog_pm);
		ampm.add(am);
		ampm.add(pm);
		
		hour_pv.setData(hour);
		minute_pv.setData(minute);
		ampm_pv.setData(ampm);
		if(is24DateFormate())
			hour_pv.setSelected(newAlarmState.hour);
		else {
			hour_pv.setSelected(newAlarmState.hour-1);
		}
		minute_pv.setSelected(newAlarmState.minute);
		ampm_pv.setSelected(newAlarmState.isAm?0:1);
		
		
	}
	
	
	public void initEvent(){
		hour_pv.setOnSelectListener(new onSelectListener()
		{

			@Override
			public void onSelect(boolean direct,String text)
			{
				
				if(is24DateFormate())
				{
					newAlarmState.hour = Integer.parseInt(text);
				}else{
					
					if(Integer.parseInt(text)== 12)
						ampm_pv.setSelectBefore();
					
					if(newAlarmState.isAm &&  Integer.parseInt(text)== 12)
						newAlarmState.hour = Integer.parseInt(text)-12;
					if(newAlarmState.isAm && 1<= Integer.parseInt(text) && Integer.parseInt(text) <= 11)
						newAlarmState.hour = Integer.parseInt(text) ;
					
					if(!newAlarmState.isAm && Integer.parseInt(text) == 12)
						newAlarmState.hour = Integer.parseInt(text);
					if(!newAlarmState.isAm && 1<= Integer.parseInt(text) && Integer.parseInt(text) <= 11)
						newAlarmState.hour = Integer.parseInt(text)+12 ;
					
				}
			}
		});
	
		minute_pv.setOnSelectListener(new onSelectListener()
		{

			@Override
			public void onSelect(boolean direct,String text)
			{
				
				if(is24DateFormate())
				{
					if(Integer.parseInt(text) == 0 && !direct)
						hour_pv.setSelectBefore();
					else if(Integer.parseInt(text) == 0 && direct)
						hour_pv.setSelectAfter();
					
					String text2 = hour_pv.getCurrentSelect();
					newAlarmState.hour = Integer.parseInt(text2);
				}else{
					
					if(Integer.parseInt(text) == 0 && !direct)
						hour_pv.setSelectBefore();
					else if(Integer.parseInt(text) == 0 && direct)
						hour_pv.setSelectAfter();
					
					String text1 = hour_pv.getCurrentSelect();

					if(newAlarmState.isAm &&  Integer.parseInt(text1)== 12)
						newAlarmState.hour = Integer.parseInt(text1)-12;
					if(newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1) ;
					
					if(!newAlarmState.isAm && Integer.parseInt(text1) == 12)
						newAlarmState.hour = Integer.parseInt(text1);
					if(!newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1)+12 ;
				}
				newAlarmState.minute = Integer.parseInt(text);
			}
		});
		
		ampm_pv.setOnSelectListener(new onSelectListener() {
			
			@Override
			public void onSelect(boolean direct,String text) {
				// TODO Auto-generated method stub
				newAlarmState.isAm = !newAlarmState.isAm;
				
				String text1 = hour_pv.getCurrentSelect();
				if(is24DateFormate())
				{
					newAlarmState.hour = Integer.parseInt(text1);
				}else{
					if(newAlarmState.isAm &&  Integer.parseInt(text1)== 12)
						newAlarmState.hour = Integer.parseInt(text1)-12;
					if(newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1) ;
					
					if(!newAlarmState.isAm && Integer.parseInt(text1) == 12)
						newAlarmState.hour = Integer.parseInt(text1);
					if(!newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1)+12 ;
				}
			}
		});
		
		button_repeat.setOnClickListener(new OnClickListener() {
			
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
				
				RepeatDialog repeatDialog = new RepeatDialog(mcontext,newAlarmState.days);
				repeatDialog.setRepeatListen(AlarmDialog.this);
				repeatDialog.dialogShow();
				}
			}
		});
		
		button_sound.setOnClickListener(new OnClickListener() {
			
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
				
				
				SoundDialog soundDialog = new SoundDialog(mcontext,newAlarmState.sound_uri);
				soundDialog.setRingListen(AlarmDialog.this);
				soundDialog.dialogShow();
				}
			}
		});
		
		button_label.setOnClickListener(new OnClickListener() {
			
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
				
				
				LabelDialog labelDialog = new LabelDialog(mcontext,newAlarmState.label);
				labelDialog.setLabelListen(AlarmDialog.this);
				labelDialog.dialogShow();
				}
			}
		});
		
		button_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mySharedPreferences.putAllowAlarmScreenSaver(true);

				if(mdialog != null)
					mdialog.dismiss();
			}
		});
		
		button_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String text1 = hour_pv.getCurrentSelect();
				if(is24DateFormate())
				{
					newAlarmState.hour = Integer.parseInt(text1);
				}else{
					if(newAlarmState.isAm &&  Integer.parseInt(text1)== 12)
						newAlarmState.hour = Integer.parseInt(text1)-12;
					if(newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1) ;
					
					if(!newAlarmState.isAm && Integer.parseInt(text1) == 12)
						newAlarmState.hour = Integer.parseInt(text1);
					if(!newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1)+12 ;
				}
				
				alarmClockFragment.setAddNewAlarm(newAlarmState.hour, newAlarmState.minute, 
						newAlarmState.days,newAlarmState.sound_uri,newAlarmState.label);
				
				mySharedPreferences.putAllowAlarmScreenSaver(true);

				if(mdialog != null)
					mdialog.dismiss();

			}
		});
	
		button_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(alarmClockFragment != null)
					alarmClockFragment.asyncDeleteAlarm(alarm, adapterItemView);
				mySharedPreferences.putAllowAlarmScreenSaver(true);
				
				if(mdialog != null)
					mdialog.dismiss();
			}
		});
		
		button_mend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String text1 = hour_pv.getCurrentSelect();
				if(is24DateFormate())
				{
					newAlarmState.hour = Integer.parseInt(text1);
				}else{
					if(newAlarmState.isAm &&  Integer.parseInt(text1)== 12)
						newAlarmState.hour = Integer.parseInt(text1)-12;
					if(newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1) ;
					
					if(!newAlarmState.isAm && Integer.parseInt(text1) == 12)
						newAlarmState.hour = Integer.parseInt(text1);
					if(!newAlarmState.isAm && 1<= Integer.parseInt(text1) && Integer.parseInt(text1) <= 11)
						newAlarmState.hour = Integer.parseInt(text1)+12 ;
				}
				
				if(alarmClockFragment != null){
					alarmClockFragment.updateAlarm(alarm, newAlarmState.hour, newAlarmState.minute, 
							newAlarmState.days,newAlarmState.sound_uri,newAlarmState.label);
				}
				mySharedPreferences.putAllowAlarmScreenSaver(true);

				if(mdialog != null)
					mdialog.dismiss();
			}
		});
	}
	
	public void dialogShow(){
		if(mdialog != null)
			mdialog.show();
	}
	
	private boolean  is24DateFormate(){
		
		return DateFormat.is24HourFormat(mcontext);
	}

	@Override
	public void setRepeat(int[] days) {
		// TODO Auto-generated method stub
		newAlarmState.days = days;
	}

	@Override
	public void setRing(Uri uri) {
		// TODO Auto-generated method stub
		newAlarmState.sound_uri = uri;
	}

	@Override
	public void setLabel(String label) {
		// TODO Auto-generated method stub
		newAlarmState.label = label;
	}
	
	public void setAlarmClockFragment(AlarmClockFragment malarmClockFragment){
		alarmClockFragment = malarmClockFragment;
	}
}
