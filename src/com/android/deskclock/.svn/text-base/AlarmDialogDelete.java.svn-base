package com.android.deskclock;


import java.util.Calendar;
import java.util.HashSet;

import com.android.deskclock.provider.Alarm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AlarmDialogDelete {

	private final int[] DAY_ORDER = new int[] { Calendar.SUNDAY,
			Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
			Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, };
	
	private static final int DAYSUMMUNTIME = 24*60;

	private Dialog dialog;

	private Button b_h_up;
	private Button b_h_down;
	private EditText edit_hour;

	private Button b_ampm;
	
	private Button b_m_up;
	private Button b_m_down;
	private EditText edit_minute;

	private ToggleButton b_sun;
	private ToggleButton b_mon;
	private ToggleButton b_tue;
	private ToggleButton b_wed;
	private ToggleButton b_thr;
	private ToggleButton b_fri;
	private ToggleButton b_sat;

	private TextView text_after_alarm;
	private CheckBox checkbox_every_week;

	private Button b_sure;
	private Button b_delete;
	private Button b_mend;
	private Button b_cancel;

	private int hour_count;
	private int minute_count;
	private int hour_move_count;
	
	private AlarmClockFragment alarmClockFragment;
	private Alarm deleteAlarm;
	private View adapterItemView;
	
	private Context mContext;
	private boolean isAM ;
	
	public AlarmDialogDelete(Context context, Alarm alarm ,View view) {
		super();
		
		mContext = context;
		deleteAlarm = alarm;
		adapterItemView = view;
		
		dialog = new Dialog(context, R.style.Dialog_notitle);
		dialog.setContentView(R.layout.dialog_new);
        dialog.setCanceledOnTouchOutside(false);// ÉèÖÃµã»÷ÆÁÄ»Dialog²»ÏûÊ§  

        initDialogView();
        hideAndShowSettingButton(deleteAlarm);
		initData(alarm.hour,alarm.minutes);
		initDialogViewEvent();
	}
	
	public void showDialog(){
		if(dialog != null)
		{
			dialog.show();
		}
	}
	
	private void hideAndShowSettingButton(Alarm alarm){
		
		//b_h_up.setVisibility(View.GONE);
		//b_h_down.setVisibility(View.GONE);
		//b_m_up.setVisibility(View.GONE);
		//b_m_down.setVisibility(View.GONE);
		
	/*	b_h_up.setBackgroundColor(Color.TRANSPARENT);
		b_h_up.setText("");
		b_h_down.setBackgroundColor(Color.TRANSPARENT);
		b_h_down.setText("");
		b_m_up.setBackgroundColor(Color.TRANSPARENT);
		b_m_up.setText("");
		b_m_down.setBackgroundColor(Color.TRANSPARENT);
		b_m_down.setText("");
	*/	
		b_sure.setVisibility(View.GONE);
		b_delete.setVisibility(View.VISIBLE);
		b_mend.setVisibility(View.VISIBLE);
		
		HashSet<Integer> setDays = alarm.daysOfWeek.getSetDays();
		checkbox_every_week.setChecked(true);

		if(setDays.size() >0){
			for (int i = 0; i < 7; i++) {
				if (setDays.contains(DAY_ORDER[i])) {
					switch(i){
					case 0:
						b_sun.setChecked(true);
						break;
					case 1:
						b_mon.setChecked(true);
						break;
					case 2:
						b_tue.setChecked(true);
						break;
					case 3:
						b_wed.setChecked(true);
						break;
					case 4:
						b_thr.setChecked(true);
						break;
					case 5:
						b_fri.setChecked(true);
						break;
					case 6:
						b_sat.setChecked(true);
						break;
						default:
							break;
					
					}
				}
			}
		
		}
		//checkbox_every_week.setEnabled(false);
		//setDisableToggleButton();

		//edit_hour.setEnabled(false);
		//edit_minute.setEnabled(false);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}
	
	private void initData(int hour,int minute) {
		Time time = new Time();
		time.setToNow();
		
		isAM = false;
		if(is24DateFormate())
			b_ampm.setText("");
		
		hour_count = hour;

		if(is24DateFormate())
			edit_hour.setText("" + hour_count);
		else {
			
			if(hour_count == 12){
				hour_count = hour_count + 0;
				edit_hour.setText("" + hour_count);
				b_ampm.setText(R.string.dialog_pm);
				isAM = false;
			}
			
			if(hour_count == 0){
				hour_count = hour_count +12;
				edit_hour.setText("" + hour_count);
				b_ampm.setText(R.string.dialog_am);
				isAM = true;
			}		
			
			if(hour_count > 0 && hour_count < 12){
				hour_count = hour_count ;
				edit_hour.setText("" + hour_count);
				b_ampm.setText(R.string.dialog_am);
				isAM = true;
			}
			
			if(hour_count > 12 && hour_count <= 23){
				hour_count = hour_count - 12;
				edit_hour.setText("" + hour_count);
				b_ampm.setText(R.string.dialog_pm);
				isAM = false;
			}
		}

		minute_count = minute;
		if(minute_count >=0 && minute_count<= 9)
			edit_minute.setText(""+"0"+minute_count);
		else edit_minute.setText("" + minute_count);

		hour_move_count = 0;
		
		if(!checkbox_every_week.isChecked())
			setDisableToggleButton();
		
		caculaterAlarmLeftTime();
		
	}

	private void initDialogView() {
		if (dialog != null) {
			b_h_up = (Button) dialog.findViewById(R.id.button_dialog_hour_up);
			b_h_down = (Button) dialog
					.findViewById(R.id.button_dialog_hour_down);
			edit_hour = (EditText) dialog
					.findViewById(R.id.edittext_dialog_hour);

			b_ampm = (Button)dialog.findViewById(R.id.button_dialog_ampm);
			
			b_m_up = (Button) dialog.findViewById(R.id.button_dialog_minute_up);
			b_m_down = (Button) dialog
					.findViewById(R.id.button_dialog_minute_down);
			edit_minute = (EditText) dialog
					.findViewById(R.id.edittext_dialog_minute);

			b_sun = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_sun);
			b_mon = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_mon);
			b_tue = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_tue);
			b_wed = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_wed);
			b_thr = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_thr);
			b_fri = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_fri);
			b_sat = (ToggleButton) dialog
					.findViewById(R.id.togglebutton_dialog_sat);

			text_after_alarm = (TextView) dialog
					.findViewById(R.id.text_dialog_after_time_alarm);
			checkbox_every_week = (CheckBox) dialog
					.findViewById(R.id.checkbox_dialog_every_week);

			b_sure = (Button) dialog.findViewById(R.id.button_dialog_sure);
			b_delete = (Button) dialog.findViewById(R.id.button_dialog_delete);
			b_mend = (Button) dialog.findViewById(R.id.button_dialog_mend);
			b_cancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);
		}

	}

	private void setDisableToggleButton(){
		b_sun.setEnabled(false);
		b_mon.setEnabled(false);
		b_tue.setEnabled(false);
		b_wed.setEnabled(false);
		b_thr.setEnabled(false);
		b_fri.setEnabled(false);
		b_sat.setEnabled(false);
	}
	
	private void setEnableToggleButton(){
		b_sun.setEnabled(true);
		b_mon.setEnabled(true);
		b_tue.setEnabled(true);
		b_wed.setEnabled(true);
		b_thr.setEnabled(true);
		b_fri.setEnabled(true);
		b_sat.setEnabled(true);
	}
	
private void caculaterAlarmLeftTime(){
		
		Time time = new Time();
		time.setToNow();
		
		int currentTime = time.hour * 60 + time.minute;
		int tempTime =0;
		if(is24DateFormate())
			tempTime= hour_count *60 + minute_count;
		else{
			if(isAM && hour_count == 12)
				tempTime = (hour_count-12)*60 + minute_count;
			if(isAM && 1<= hour_count && hour_count <= 11)
				tempTime= hour_count *60 + minute_count;
			
			if(!isAM && hour_count == 12)
				tempTime = (hour_count)*60 + minute_count;
			if(!isAM && 1<= hour_count && hour_count <= 11)
				tempTime= (hour_count +12 )*60 + minute_count;
		}
		
		int temp_left_time = 0;
		
        final Calendar c = Calendar.getInstance();  
        int currentDaysofWeek = c.get(Calendar.DAY_OF_WEEK);
        int tempDaysofWeek = 0;
        int days[] = getDaysofWeek();
        int i= 0;
        int isCheckedCount = 0;
        boolean flag = false;
        
        for(;i< 7 ;i++)
        	if(days[i]!= 0)
        		isCheckedCount = isCheckedCount + 1;
        
        if(checkbox_every_week.isChecked()&& isCheckedCount > 0){
        
       
        
        if(days[currentDaysofWeek-1]!=0 && isCheckedCount == 1 ){
        	tempDaysofWeek = c.get(Calendar.DAY_OF_WEEK);
        }else {
        	for(int m = currentDaysofWeek-1;m <=6;m++)
        	{
        		if(days[m]!=0){
        			tempDaysofWeek = days[m];
        			flag = true;
        			break;
        		}
        	}
        	if(!flag){
        		for(int n = 0;n <=currentDaysofWeek -1;n++){
        			if(days[n]!=0)
        			{
        				tempDaysofWeek = days[n];
        				break;       						
        			}
        		}
        	}
        }
           
        if(tempDaysofWeek == currentDaysofWeek && isCheckedCount == 1){
        	
        	if(tempTime > currentTime)
        		temp_left_time = tempTime - currentTime;
        	else {
        			temp_left_time = 7*DAYSUMMUNTIME - (currentTime - tempTime);
        	}
        	
        }else{
        	
        	if(tempDaysofWeek == currentDaysofWeek){
        		if(tempTime > currentTime)
            		temp_left_time = tempTime - currentTime;
            	else {
            			//temp_left_time = 7*DAYSUMMUNTIME - (currentTime - tempTime);
            		flag = false;
            		for(int m = currentDaysofWeek;m <=6;m++)
                	{
                		if(days[m]!=0){
                			tempDaysofWeek = days[m];
                			flag = true;
                			break;
                		}
                	}
                	if(!flag){
                		for(int n = 0;n <=currentDaysofWeek -2;n++){
                			if(days[n]!=0)
                			{
                				tempDaysofWeek = days[n];
                				break;       						
                			}
                		}
                	}
            	}
        	}
        	if(tempDaysofWeek < currentDaysofWeek){
        		if(tempTime > currentTime )
        			temp_left_time = ( 7 - (currentDaysofWeek - tempDaysofWeek ))*DAYSUMMUNTIME + (tempTime -currentTime);
        		else temp_left_time =( 7 - (currentDaysofWeek - tempDaysofWeek ))*DAYSUMMUNTIME - (currentTime - tempTime);
        	}
        	
        	if( tempDaysofWeek > currentDaysofWeek){
        		
        		if(tempTime > currentTime)
        			temp_left_time = (tempDaysofWeek - currentDaysofWeek)*DAYSUMMUNTIME +(tempTime - currentTime);
        		else temp_left_time = (tempDaysofWeek - currentDaysofWeek)*DAYSUMMUNTIME - (currentTime - tempTime);
        	}
        	
        }
        }else{
        	if(tempTime > currentTime)
        		temp_left_time = tempTime - currentTime;
        	else temp_left_time = 1*DAYSUMMUNTIME - (currentTime - tempTime);
        }
        
        int DAY = temp_left_time/DAYSUMMUNTIME;
        int HOUR = (temp_left_time%DAYSUMMUNTIME)/60;
        int MINUTE = (temp_left_time%DAYSUMMUNTIME)%60;
        
        String sAgeFormat1 = mContext.getResources().getString(R.string.dialog_setting_show_alarm);  
        String sFinal1 = String.format(sAgeFormat1, DAY+"",HOUR+"",MINUTE+"");
        text_after_alarm.setText(sFinal1);

		//text_after_alarm.setText("Day:"+ DAY +":Hou:"+ HOUR + ":Min:" + MINUTE );
		
	}

private void function_hour_up(){
	hour_count++;
	if(is24DateFormate()){
		if (hour_count > 23)
			hour_count = 0;
	}else{
		if (hour_count > 12){
			hour_count = 1;
			setAmPm();
		}
	}
	edit_hour.setText("" + hour_count);

}
private void function_hour_down(){
	hour_count--;
	if(is24DateFormate()){
		if (hour_count < 0)
			hour_count = 23;
	}else{
		if (hour_count < 1){
			hour_count = 12;
			setAmPm();
		}
	}
	edit_hour.setText("" + hour_count);
}
private void function_minute_up(){
	minute_count++;
	if (minute_count > 59){
		minute_count = 0;
		function_hour_up();
	}
	if(minute_count >=0 && minute_count<= 9)
		edit_minute.setText(""+"0"+minute_count);
	else edit_minute.setText("" + minute_count);

}
private void function_minute_down(){
	minute_count--;
	if (minute_count < 0){
		minute_count = 59;
		function_hour_down();
	}
	if(minute_count >=0 && minute_count<= 9)
		edit_minute.setText(""+"0"+minute_count);
	else edit_minute.setText("" + minute_count);
}

	
	private void initDialogViewEvent() {
		b_h_up.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					hour_move_count++;
					if (hour_move_count > 20) {
						function_hour_up();

					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					function_hour_up();

					hour_move_count = 0;
				}
				caculaterAlarmLeftTime();
				return false;
			}
		});

		b_h_down.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					hour_move_count++;
					if (hour_move_count > 20) {
						function_hour_down();

					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					function_hour_down();

					hour_move_count = 0;
				}
				caculaterAlarmLeftTime();

				return false;
			}
		});

		b_m_up.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					hour_move_count++;
					if (hour_move_count > 20) {
						function_minute_up();

					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					function_minute_up();

					hour_move_count = 0;
				}
				caculaterAlarmLeftTime();

				return false;
			}
		});

		b_m_down.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					hour_move_count++;
					if (hour_move_count > 20) {
						function_minute_down();
					}
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					function_minute_down();

					hour_move_count = 0;
				}
				caculaterAlarmLeftTime();

				return false;
			}
		});

		edit_hour.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String hour = edit_hour.getText().toString().trim();
				if(!hour.equals("")){
				if (isNumber(hour)) {
					int temp_hour_count = Integer.parseInt(hour);
					if(is24DateFormate()){
						if (0 <= temp_hour_count && temp_hour_count <= 23)
							hour_count = temp_hour_count;
						else
							initData(hour_count,minute_count);
					}else{
						if (1 <= temp_hour_count && temp_hour_count <= 12)
							hour_count = temp_hour_count;
						else
							initData(hour_count,minute_count);
					}
				} else {
					initData(hour_count,minute_count);
				}
				caculaterAlarmLeftTime();
				}
			}
		});
		
		edit_hour.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				edit_hour.setCursorVisible(true);
				return false;
			}
		});


		edit_minute.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String hour = edit_minute.getText().toString().trim();
				if(!hour.equals("")){
				if (isNumber(hour)) {
					int temp_hour_count = Integer.parseInt(hour);
					if (0 <= temp_hour_count && temp_hour_count <= 59)
						minute_count = temp_hour_count;
					else
						initData(hour_count,minute_count);
				} else {
					initData(hour_count,minute_count);
				}
				caculaterAlarmLeftTime();
				}
			}
		});
		
		edit_minute.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				edit_minute.setCursorVisible(true);
				return false;
			}
		});
		
		b_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(alarmClockFragment != null){
					if(checkbox_every_week.isChecked())
					{
						alarmClockFragment.setAddNewAlarm(hour_count, minute_count, getDaysofWeek());
					}else alarmClockFragment.setAddNewAlarm(hour_count, minute_count); //Ìí¼ÓÄÖÖÓ
				}
								
				if(dialog != null)
					dialog.dismiss();
			}
		});
		
		b_mend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(is24DateFormate())
				{
					hour_count = hour_count;
				}else{
					if(isAM && hour_count == 12)
						hour_count = hour_count-12;
					if(isAM && 1<= hour_count && hour_count <= 11)
						hour_count= hour_count ;
					
					if(!isAM && hour_count == 12)
						hour_count = hour_count;
					if(!isAM && 1<= hour_count && hour_count <= 11)
						hour_count = hour_count +12 ;
				}
				if(alarmClockFragment != null){
					alarmClockFragment.updateAlarm(deleteAlarm, hour_count, minute_count, getDaysofWeek());
				}
								
				if(dialog != null)
					dialog.dismiss();
			}
		});

		b_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(alarmClockFragment != null)
					alarmClockFragment.asyncDeleteAlarm(deleteAlarm, adapterItemView);
				
				if(dialog != null)
					dialog.dismiss();
			}
		});
		
		b_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(dialog != null)
					dialog.dismiss();
			}
		});
		
		checkbox_every_week.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkbox_every_week.isChecked())
					setEnableToggleButton();
				else setDisableToggleButton();
			}
		});
		
		
b_sun.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
	
		b_mon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_tue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_wed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_thr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_fri.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_sat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				caculaterAlarmLeftTime();
			}
		});
		
		b_ampm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAmPm();
			}
		});
	}
	
	private void setAmPm(){
		isAM = !isAM;
		if(isAM)
			b_ampm.setText(R.string.dialog_am);
		else b_ampm.setText(R.string.dialog_pm);
	}
	
	
	private int[] getDaysofWeek(){
		
		int days[] = new int[7];
		
		if(!checkbox_every_week.isChecked()){
			for(int i=0;i<7;i++)
				days[i] = 0;
			return days;
		}
		
		if(b_sun.isChecked())
			days[0] = 1;
		else days[0] = 0;
		
		if(b_mon.isChecked())
			days[1] = 2;
		else days[1] = 0;
		
		if(b_tue.isChecked())
			days[2] = 3;
		else days[2] = 0;
		
		if(b_wed.isChecked())
			days[3] = 4;
		else days[3] = 0;
		
		if(b_thr.isChecked())
			days[4] = 5;
		else days[4] = 0;
		
		if(b_fri.isChecked())
			days[5] = 6;
		else days[5] = 0;
		
		if(b_sat.isChecked())
			days[6] = 7;
		else days[6] = 0;
		
		return days;
	}

	private boolean isNumber(String str) {
		if(str.equals(""))
			return false;
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("[0-9]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	public void setAlarmClockFragment(AlarmClockFragment malarmClockFragment){
		alarmClockFragment = malarmClockFragment;
	}
	
	private boolean  is24DateFormate(){
		
		return DateFormat.is24HourFormat(mContext);
	}
}
