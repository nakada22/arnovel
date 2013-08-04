package com.tetuo41.arnovel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;
import com.tetuo41.arnovel.db.Dao;

/**
* スタンプログラリー画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class StampLogActivity extends Activity implements OnClickListener {
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stamp_log);
		
		// ノベル読了したステージのスタンプ一覧を表示
		StampListDisp();
    }
    
    /** 
     * コンストラクタ
     *
     */
    public StampLogActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    	// http://atgb.cocolog-nifty.com/astimegoesby/2011/02/listviewactivit.html
    }
    
    /** 
     * エントリ一個の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.record_img1:
			
			break;
		case R.id.record_img2:
			break;
		case R.id.record_img3:
			break;
			
		default:
			break;
		}
	}
    
    /**
     * ノベル読了したステージのスタンプ一覧を表示する
     * 
     * */
    private void StampListDisp() {

    	/** DBアクセスクラスオブジェクト */
		Dao dao = new Dao(getApplicationContext());
		
		// TODO スタンプフラグが「1」のステージID, ステージタイトル、ノベルデータをDBから取得・セット
		
		
    	
    	
    }

}
