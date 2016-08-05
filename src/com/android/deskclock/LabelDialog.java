package com.android.deskclock;

import com.android.deskclock.RepeatDialog.onSetRepeatListen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LabelDialog {
	
	private Context mcontext;
	private Dialog mdialog ;
	private String mlabel;
	private onSetLabelListen setLabelListen;
	
	private EditText editText ;
	private Button button_cancel;
	private Button button_finish;
	
	public LabelDialog(Context mcontext,String label) {
		super();
		this.mcontext = mcontext;
		this.mlabel = label;
		mdialog = new Dialog(mcontext, R.style.Dialog_notitle);
		mdialog.setContentView(R.layout.label_sign);
        mdialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消�? 
        
        editText = (EditText)mdialog.findViewById(R.id.edittext_lable);
        editText.setText(mlabel);
        
        button_cancel = (Button)mdialog.findViewById(R.id.button_label_cancel);
        button_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mdialog.dismiss();
			}
		});
        
        button_finish = (Button)mdialog.findViewById(R.id.button_label_finish);
        button_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLabelListen.setLabel(editText.getText().toString());
				mdialog.dismiss();
			}
		});
        
	}
	
	public void dialogShow(){
		if(mdialog != null)
			mdialog.show();
	}
	
	 public interface onSetLabelListen{
		 public void setLabel(String label);
	 }

	 public void setLabelListen(onSetLabelListen setLabelListen){
		 this.setLabelListen = setLabelListen;
	 }

}
