package com.tetuo41.arnovel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
 
public class StampLogActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stamp_log);
		
    }
    
    /** 
     * コンストラクタ
     *
     */
    public StampLogActivity() {
    	// 処理なし
    }
    
    
}
