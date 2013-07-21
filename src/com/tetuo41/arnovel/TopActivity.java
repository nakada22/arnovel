package com.tetuo41.arnovel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
 
public class TopActivity extends Activity implements View.OnClickListener{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top);
		
    }
    
    /*
     * クリック時の処理
     * Test
     * 
     * */
    
    
    public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.start:
//			// STARTボタンんクリック時
//			break;
//		case R.id.record:
//			// RECORDボタンクリック時
//			break;
//		default:
//			break;
		}
	}
}
