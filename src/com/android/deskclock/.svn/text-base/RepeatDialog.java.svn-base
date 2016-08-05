package com.android.deskclock;

import android.R.dimen;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RepeatDialog {
	
	private Context mcontext;
	private Dialog mdialog ;
	private ListView mlistView;
	private SingleAdapter singleAdapter;
	
	private int[] weeks = new int[]{R.string.dialog_sun,R.string.dialog_mon,R.string.dialog_tue
	,R.string.dialog_wed,R.string.dialog_thr,R.string.dialog_fri,R.string.dialog_sat}; 

	private int[] mdays = new int[7];
	
	private onSetRepeatListen setRepeatListen;
	
	private Button button_cancel;
	private Button button_finish;
	
	public RepeatDialog(Context context ,int days[]) {
		super();
		mcontext = context;
		mdialog = new Dialog(mcontext, R.style.Dialog_notitle);
		
		mdialog.setContentView(R.layout.repeat_date);
        mdialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消�? 
        
        mlistView = (ListView)mdialog.findViewById(R.id.listview_repeat_date);
        singleAdapter = new SingleAdapter();
        mlistView.setAdapter(singleAdapter);
           
        this.mdays = days;
        
        button_cancel = (Button)mdialog.findViewById(R.id.button_repeat_cancel);
        button_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mdialog != null)
					mdialog.dismiss();
			}
		});
        
        button_finish = (Button)mdialog.findViewById(R.id.button_repeat_finish);
        button_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i = 0;i< mlistView.getCount();i++)
				{
					if(((SingleView)mlistView.getChildAt(i)).isChecked())
						mdays[i]= i+ 1;
					else mdays[i] = 0;
					setRepeatListen.setRepeat(mdays);
					mdialog.dismiss();
				}
			}
		});
	}
	
	public void dialogShow(){
		if(mdialog != null)
			mdialog.show();
	}
	
	
	public class SingleView extends LinearLayout {

	     private TextView mText;
	     private CheckBox mCheckBox;

	     public SingleView(Context context, AttributeSet attrs) {
	            super(context, attrs);
	           initView(context);
	     }

	     public SingleView (Context context) {
	            super(context);
	           initView(context);
	     }

	     private void initView(Context context){
	            // 填充布局
	           LayoutInflater inflater = LayoutInflater.from(context);
	           View v = inflater.inflate(R.layout.item_checkbox_layout , this, true);
	           mText = (TextView) v.findViewById(R.id.textview_title);
	           mCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
	           mCheckBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isChecked())
						mText.setTextColor(Color.GREEN);
		            else {
		            	mText.setTextColor(Color.WHITE);
		            }
				}
			});
	     }

	     public void setChecked( boolean checked) {
	            mCheckBox.setChecked(checked);
	            if(checked)
	            	mText.setTextColor(Color.GREEN);
	            else {
	            	mText.setTextColor(Color.WHITE);
	            }
	           
	     }

	     public boolean isChecked() {
	            return mCheckBox.isChecked();
	     }
	     
	     public void setTitle(String text){
	            mText.setText(text);
	     }
	}
	
	 private class SingleAdapter extends BaseAdapter {

         @Override
         public int getCount() {
              return weeks.length;
        }

         @Override
         public Object getItem( int position) {
              // TODO Auto-generated method stub
              return null;
        }

         @Override
         public long getItemId( int position) {
              // TODO Auto-generated method stub
              return 0;
        }

     
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			 final SingleView singleView = new SingleView(mcontext);
			 String str = mcontext.getResources().getString(weeks[arg0]);
             singleView.setTitle(str);
             
             if(mdays[arg0]!= 0)
            	 singleView.setChecked(true);
             
             singleView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					boolean flag = singleView.isChecked();
					singleView.setChecked(!flag);
				}
			});
             
             return singleView;
		}

  }
	 
	 public interface onSetRepeatListen{
		 public void setRepeat(int days[]);
	 }

	 public void setRepeatListen(onSetRepeatListen setRepeatListen){
		 this.setRepeatListen = setRepeatListen;
	 }
}
