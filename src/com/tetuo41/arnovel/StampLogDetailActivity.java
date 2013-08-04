package com.tetuo41.arnovel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
* スタンプログラリー詳細画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class StampLogDetailActivity extends Activity {
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stamp_log_detail);
		
		Intent i = getIntent();
		//RecordState rs = (RecordState) i.getSerializableExtra("RecordState");

		// ノベル読了したステージのスタンプ詳細画面を表示
		StampDetailDisp();
    }
    
    /** 
     * コンストラクタ
     *
     */
    public StampLogDetailActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /**
     * ノベル読了したステージのスタンプ一覧を表示する
     * 
     * */
    private void StampDetailDisp() {
    	
    }

}
