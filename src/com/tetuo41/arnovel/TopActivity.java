package com.tetuo41.arnovel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

/**
* トップ画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class TopActivity extends Activity implements View.OnClickListener{
	
	/** カレントパッケージ名 */
	private static final String CURRENT_PACKAGE = 
			TopActivity.class.getPackage().getName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top);
		
		// ClickListener登録
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.record).setOnClickListener(this);
		
    }
    
    /** 
     * コンストラクタ
     */
    public TopActivity() {
    	// 処理なし
    }
    
    /** 
     * ボタンクリック時の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.start:
			// STARTボタンクリック時
			Log.d("DEBUG", "STARTボタンクリック");
			StartClick();
			
			break;
		case R.id.record:
			// RECORDボタンクリック時
			Log.d("DEBUG", "RECORDボタンクリック");
			RecordClick();
			
			break;
		default:
			break;
		}
	}
    
    /** 
     * STARTボタンクリック時の処理を記述する。
     */
    private void StartClick() {
    	
    	try {
    		// STARTボタンクリック時、スタンプログ画面へ遷移
    		Intent intent = new Intent();
    		intent.setClassName(CURRENT_PACKAGE, CURRENT_PACKAGE + ".StageSelectActivity");
    		startActivity(intent);
    		
    	} catch (RuntimeException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialog.Builder adb = new AlertDialog.Builder(this);
    		adb.setTitle("エラー");
    		adb.setMessage("ステージセレクト画面へ遷移できませんでした");
    		adb.setPositiveButton("OK",
    	            new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int which) {
    	                	// 処理なし
    	                }
    	            });
    		adb.show();
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /** 
     * RECORDボタンクリック時の処理を記述する。
     */
    private void RecordClick() {
    	
    	try {
    		// RECORDボタンクリック時、スタンプログ画面へ遷移
    		Intent intent = new Intent();
    		intent.setClassName(CURRENT_PACKAGE, CURRENT_PACKAGE + ".StampLogActivity");
    		startActivity(intent);
    		
    	} catch (RuntimeException e) {
    		// スタンプログ画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialog.Builder adb = new AlertDialog.Builder(this);
    		adb.setTitle("エラー");
    		adb.setMessage("スタンプログ画面へ遷移できませんでした");
    		adb.setPositiveButton("OK",
    	            new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int which) {
    	                	// 処理なし
    	                }
    	            });
    		adb.show();
    		
    		// 処理を終了する
    		return;
    	}
    }
}
