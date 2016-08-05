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

public class TextClockDiff extends LinearLayout {

	private TextView textView_picture_clock;
	private TextView textView_picture_ampm;
	
	private int shiFen[] = new int[] {R.string.shan_wu,R.string.zhong_wu,R.string.shyia_wu,
			R.string.bang_wan,R.string.wan_shan,R.string.bang_ye,R.string.ling_chen};
	
	AssetManager mgr;
	Typeface tf ;

	public TextClockDiff(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TextClockDiff(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.textclock_diff_layout, this);
		
		textView_picture_clock = (TextView)(findViewById(R.id.textview_textclock_diff_clock));
		textView_picture_ampm = (TextView) (findViewById(R.id.textview_textclock_diff_ampm));
		
		/*mgr=getContext().getAssets();
		tf =Typeface.createFromAsset(mgr, "fonts/Expansiva.otf");	
		
		textView_picture_clock.setTypeface(tf);
		textView_picture_ampm.setTypeface(tf);
		*/
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		/**
		 * requests a tick on the next hard-second boundary
		 */
	}

	public void setCurrentTime(int mhour,int mminute) {

		int hour = mhour;
		int minute = mminute;
		int ampm = 0 ;

		if (is24DateFormate()) {
			
			textView_picture_clock.setText((hour<10 ?"0" + hour: ""+hour)+":"+(minute<10 ?"0" + minute: ""+minute));
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
			
			
			textView_picture_ampm.setVisibility(View.VISIBLE);
			
			if(mhour >= 6 && mhour < 12)
				ampm = shiFen[0];
			if(mhour >= 12 && mhour < 13)
				ampm = shiFen[1];
			if(mhour >= 13 && mhour < 18)
				ampm = shiFen[2];
			if(mhour >= 18 && mhour < 19)
				ampm = shiFen[3];
			if(mhour >= 19 && mhour < 24)
				ampm = shiFen[4];
			if(mhour >= 0 && mhour < 1)
				ampm = shiFen[5];
			if(mhour >= 1 && mhour < 6)
				ampm = shiFen[6];
			textView_picture_clock.setText((hour<10 ?"0" + hour: ""+hour)+":"+(minute<10 ?"0" + minute: ""+minute));
			textView_picture_ampm.setText(ampm);
		}

	}

	private boolean is24DateFormate() {

		return DateFormat.is24HourFormat(getContext());
	}
}
