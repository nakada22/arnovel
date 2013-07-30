package com.tetuo41.arnovel;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;
import com.tetuo41.arnovel.db.Dao;

/**
* トップ画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class TopActivity extends Activity implements View.OnClickListener{
	
	/** カレントパッケージ名 */
	private static final String CURRENT_PACKAGE = 
			TopActivity.class.getPackage().getName();
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** 外部サーバーにあるCSVファイルのURL */
	String novel_url;
	String stage_url;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top);
		
		// ClickListener登録
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.record).setOnClickListener(this);
		
		/** DBアクセスクラスオブジェクト */
		Dao dao = new Dao(getApplicationContext());
		
		try {
			// 非同期で外部サーバよりCSVファイル読込
			// TODO 初期データDB登録
			Init init = new Init();
			init.execute();
		} catch (NetworkOnMainThreadException e) {
			// CSVファイル読み込み失敗したとき
			Log.e("ERROR","NetworkOnMainThreadException");
    		Toast.makeText(this, "CSVファイルの読込に失敗しました。", Toast.LENGTH_LONG).show();
			
		} catch (RuntimeException e) {
			// インターネットに接続できなかった場合
			Log.e("ERROR", e.toString());
			Toast.makeText(this, "インターネットに接続えきませんでした。", Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			// CSVファイル読み込み失敗したとき
			Log.e("ERROR", e.toString());
			Toast.makeText(this, "CSVファイルの読込に失敗しました。", Toast.LENGTH_LONG).show();
			
		}
    }
    
    /** 
     * コンストラクタ
     */
    public TopActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    	this.novel_url = "http://" + cmndef.S_HOST_NAME + 
    			cmndef.DATA_DIR + cmndef.NOVEL_FILE;
    	this.stage_url = "http://" + cmndef.S_HOST_NAME + 
    			cmndef.DATA_DIR + cmndef.STAGE_FILE;
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
    
    /**
     * 内部初期処理クラス
     * 外部サーバーよりCSVファイル読込、DBへ初期データ登録
     * 
     * */
    class Init extends AsyncTask<String,Void,Map<String, List<String>>>{
    	
    	protected Map<String, List<String>> doInBackground(String... params) {
    		
    		try {
        		// ノベルファイル読込・取得
        		Map<String, List<String>> novel_data = 
        				cmnutil.ReadCsvFile(novel_url, getApplicationContext());
        		Log.d("DEBUG", "ノベルファイル読込・取得完了");
        		
            	// ステージデータファイル読み込み
        		Map<String, List<String>> stage_data = 
        				cmnutil.ReadCsvFile(stage_url, getApplicationContext());
        		Log.d("DEBUG", "ステージデータファイル読込・取得完了");
        		
        		// TODO データをDBに収録
        		
        		
        		return null;
        	} catch (Exception e) {
        		Log.w("WARN", "CSVファイル読込タスクで例外発生：" + e.toString());
                return null;
    		}
    	}
    }
}
