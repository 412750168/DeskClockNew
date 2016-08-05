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

public class PictureClockSame extends LinearLayout {

	private ImageView imageView_hour_ten;
	private ImageView imageView_hour_one;
	private ImageView imageView_minute_ten;
	private ImageView imageView_minute_one;
	private TextView textView_picture_ampm;

	private final Handler mHandler = new Handler();
	private Runnable runnable;
	private boolean runnable_stopped = false;

	private int num[] = new int[] { R.drawable.time_0, R.drawable.time_1,
			R.drawable.time_2, R.drawable.time_3, R.drawable.time_4,
			R.drawable.time_5, R.drawable.time_6, R.drawable.time_7,
			R.drawable.time_8, R.drawable.time_9 };
	
	private int shiFen[] = new int[] {R.string.shan_wu,R.string.zhong_wu,R.string.shyia_wu,
			R.string.bang_wan,R.string.wan_shan,R.string.bang_ye,R.string.ling_chen};
	
	AssetManager mgr;
	Typeface tf ;

	public PictureClockSame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PictureClockSame(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.picture_layout, this);

		imageView_hour_ten = (ImageView) (findViewById(R.id.image_hour_ten));
		imageView_hour_one = (ImageView) (findViewById(R.id.image_hour_one));

		imageView_minute_ten = (ImageView) (findViewById(R.id.image_minute_ten));
		imageView_minute_one = (ImageView) (findViewById(R.id.image_minute_one));

		textView_picture_ampm = (TextView) (findViewById(R.id.textview_picture_ampm));
		
		mgr=getContext().getAssets();
		tf =Typeface.createFromAsset(mgr, "fonts/Expansiva.otf");	
		textView_picture_ampm.setTypeface(tf);
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
				//mHandler.postAtTime(runnable, next);// 递归执行，就达到秒针一直在动的效果
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
			imageView_hour_ten.setBackgroundResource(num[hour / 10]);
			imageView_hour_one.setBackgroundResource(num[hour % 10]);
			imageView_minute_ten.setBackgroundResource(num[minute / 10]);
			imageView_minute_one.setBackgroundResource(num[minute % 10]);
			textView_picture_ampm.setVisibility(View.GONE);
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
			
			imageView_hour_ten.setBackgroundResource(num[hour / 10]);
			imageView_hour_one.setBackgroundResource(num[hour % 10]);
			imageView_minute_ten.setBackgroundResource(num[minute / 10]);
			imageView_minute_one.setBackgroundResource(num[minute % 10]);
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
			
			textView_picture_ampm.setText(ampm);
		}

	}

	private boolean is24DateFormate() {

		return DateFormat.is24HourFormat(getContext());
	}
}
