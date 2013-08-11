package com.tetuo41.arnovel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.tetuo41.arnovel.StampLogActivity.StampLogState;
import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
* スタンプログ詳細画面を表示するクラスです。
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
		
		// ノベル読了したステージのスタンプログ詳細画面を表示
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
    	Intent i = getIntent();
    	StampLogState sls = (StampLogState) i.getSerializableExtra("StampLogState");
    	
    	/** スタンプ一覧画面からデータ取得 */
    	String stamp_id = String.valueOf(sls.getStampId());
    	String novel_title = sls.getNovelTitle();
    	String novel_data = sls.getNovelData();
    	// ノベルデータを30文字づつ改行区切りでわける。
    	// まず、25文字づつ 70
    	String str;
    	StringBuilder sb = new StringBuilder();
    	
    	for (int j = 0; j < novel_data.length() / 25; j++) {
    		sb.append(novel_data.substring(25*j,(25*j)+25));
    		Log.d("DEBUG", sb.toString());
        	sb.append("\n"); // 改行文字
    	}
    	
    	sb.append(novel_data.substring(sb.length()-1));
  	
    	/** 表示するVIEW取得・テキストセット */
    	// レコードID
		TextView tvRecordId = (TextView) findViewById(R.id.record_id);
		tvRecordId.setText("No." + stamp_id);
		
		// レコードタイトル(ノベルタイトル)
		TextView tvNovelTitle = (TextView) findViewById(R.id.record_title);
		tvNovelTitle.setText("「" + novel_title + "」");
		
		// あらすじ(ノベルデータ)
		TextView tvNovelData = (TextView) findViewById(R.id.novel_data);
		tvNovelData.setText(sb);
    }

}
