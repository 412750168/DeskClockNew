package com.android.deskclock;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextClockSame extends LinearLayout {

	private TextView textView_picture_ampm;

	private final Handler mHandler = new Handler();
	private Runnable runnable;
	private boolean runnable_stopped = false;
	
	private int shiFen[] = new int[] {R.string.shan_wu,R.string.zhong_wu,R.string.shyia_wu,
			R.string.bang_wan,R.string.wan_shan,R.string.bang_ye,R.string.ling_chen};
	
	AssetManager mgr;
	Typeface tf ;

	public TextClockSame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TextClockSame(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.textclock_same_layout, this);

		textView_picture_ampm = (TextView) (findViewById(R.id.textview_textclock_same_ampm));
		
		/*mgr=getContext().getAssets();
		tf =Typeface.createFromAsset(mgr, "fonts/Expansiva.otf");	
		textView_picture_ampm.setTypeface(tf);
		*/
	}

	@Override
	protected void onAttachedToWindow() {
		runnable_stopped = false;// 添加到窗口中就要更新时间了
		super.onAttachedToWindow();

		/**
		 * requests a tick on the next hard-second boundary
		 */
		runnable = new Runnable() {
			public void run() {
				if (runnable_stopped)
					return;
				changeCurrentTime();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);// 计算下次需要更新的时间间隔
				mHandler.postAtTime(runnable, next);// 递归执行，就达到秒针一直在动的效果
			}
		};
		runnable.run();
	}

	private void changeCurrentTime() {

		Time time = new Time();
		time.setToNow();

		int hour = time.hour;
		int minute = time.minute;
		int ampm = 0 ;

		if (is24DateFormate()) {
			
			textView_picture_ampm.setText("  "+(hour<10 ?"0" + hour: ""+hour)+":"+(minute<10 ?"0" + minute: ""+minute));
		} else {

			if (hour == 12) {
				hour = hour + 0;

			}

			if (hour == 0) {
				hour = hour + 12;

			}

			if (hour > 0 && hour < 12) {
				hour = hour;

			}

			if (hour > 12 && hour <= 23) {
				hour = hour - 12;

			}
			
			
			textView_picture_ampm.setVisibility(View.VISIBLE);
			
			if(time.hour >= 6 && time.hour < 12)
				ampm = shiFen[0];
			if(time.hour >= 12 && time.hour < 13)
				ampm = shiFen[1];
			if(time.hour >= 13 && time.hour < 18)
				ampm = shiFen[2];
			if(time.hour >= 18 && time.hour < 19)
				ampm = shiFen[3];
			if(time.hour >= 19 && time.hour < 24)
				ampm = shiFen[4];
			if(time.hour >= 0 && time.hour < 1)
				ampm = shiFen[5];
			if(time.hour >= 1 && time.hour < 6)
				ampm = shiFen[6];
			String str = getResources().getString(ampm);
			textView_picture_ampm.setText(str+" "+(hour<10 ?"0" + hour: ""+hour)+":"+(minute<10 ?"0" + minute: ""+minute));
		}

	}

	private boolean is24DateFormate() {

		return DateFormat.is24HourFormat(getContext());
	}
}
