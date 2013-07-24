package com.tetuo41.arnovel;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
 
public class StageSelectActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stage_select);
		
		// TextViewに線をセット
		//TextView textView_line = (TextView) findViewById(R.id.textView_line);
		//textView_line.setBackgroundResource(R.layout.line);
		
    }
    
    /** 
     * コンストラクタ
     *
     * @param なし
     * @return なし
     */
    public StageSelectActivity() {
    	// 処理なし
    }
    
    
}
