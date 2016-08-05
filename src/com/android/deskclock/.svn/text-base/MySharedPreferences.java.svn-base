package com.android.deskclock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	
	private static final String NAME = "zzlDeskclock";
	static final String ID  = "id";
	static final String SCREEN = "screen";

	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	private Context context;
	
	public MySharedPreferences(Context context) {
		super();
		this.context = context;
		
	}
	
	public void putAlarmInstance(Long id){
		
		mySharedPreferences = this.context.getSharedPreferences(NAME,
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		editor.putLong(ID, id);
		editor.commit();
	}
	
	public Long getAlarmInstance(){
		mySharedPreferences = this.context.getSharedPreferences(NAME,
				Activity.MODE_PRIVATE);
		Long id = mySharedPreferences.getLong(ID, 0);
		return id;
	}
	
	public void putAllowAlarmScreenSaver(boolean flag){
		
		mySharedPreferences = this.context.getSharedPreferences(NAME,
				Activity.MODE_PRIVATE);
		editor = mySharedPreferences.edit();
		editor.putBoolean(SCREEN, flag);
		editor.commit();
	}
	
	public boolean getAllowAlarmScreenSaver(){
		mySharedPreferences = this.context.getSharedPreferences(NAME,
				Activity.MODE_PRIVATE);
		boolean flag = mySharedPreferences.getBoolean(SCREEN, true);
		return flag;
	}

}
