package com.android.deskclock;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class GetFont {

	Context context;
	AssetManager mgr;

	public GetFont(Context context) {
		super();
		this.context = context;
		mgr=this.context.getAssets();//µÃµ½AssetManager

	}
	
	public Typeface getLcdFonttype(){
		
		Typeface tf=Typeface.createFromAsset(mgr, "fonts/Digital.ttf");	
		return tf;
	}
	
	public Typeface getExpansivatype(){
		
		Typeface tf=Typeface.createFromAsset(mgr, "fonts/Expansiva.otf");	
		return tf;
	}
}
