package com.tetuo41.arnovel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;
import com.tetuo41.arnovel.db.Dao;
import com.tetuo41.arnovel.db.DbConstants;

/**
* トップ画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class TopActivity extends Activity implements OnClickListener{
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** 外部サーバーにあるCSVファイルのURL */
	private String novel_url;
	private String stage_url;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Log.e(this.getClass().getName(),"onCreate");
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top);
		
		// ClickListener登録
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.record).setOnClickListener(this);
		
		// 最初は、初期データ登録が完了するまでボタンクリックを無効化しておく
		findViewById(R.id.start).setClickable(false);
		findViewById(R.id.record).setClickable(false);
		
		try {
			// 非同期で外部サーバよりCSVファイル読込
			// 初期データDB登録
			Init init = new Init();
			init.execute();
			
		}
//		catch (NetworkOnMainThreadException e) {
//			// CSVファイル読み込み失敗したとき
//			Log.e("ERROR","NetworkOnMainThreadException");
//    		Toast.makeText(this, cmndef.TOP_ERROR_MSG3, Toast.LENGTH_LONG).show();
//			
//		}
		catch (RuntimeException e) {
			// インターネットに接続できなかった場合
			Log.e("ERROR", e.toString());
			Toast.makeText(this, cmndef.CMN_ERROR_MSG1, Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			// CSVファイル読み込み失敗したとき
			Log.e("ERROR", e.toString());
			Toast.makeText(this, cmndef.TOP_ERROR_MSG3, Toast.LENGTH_LONG).show();
			
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
			StartClick();
			
			break;
		case R.id.record:
			// RECORDボタンクリック時
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
    		Intent i = new Intent(getApplicationContext(), StageSelectActivity.class);
			startActivity(i);
    		
    	} catch (ActivityNotFoundException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    	} catch (RuntimeException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);
    		
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
    		Intent i = new Intent(getApplicationContext(), StampLogActivity.class);
			startActivity(i);
			
    	} catch (ActivityNotFoundException e) {
    		// スタンプログ画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG2);
    		
    		// 処理を終了する
    		return;
    	} catch (RuntimeException e) {
    		// スタンプログ画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG2);
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /**
     * アラートダイアログを表示する
     * @param タイトル
     * @param 表示するメッセージ
     * 
     * */
    private void AlertDialogView(String title, String message) {
    	
    	// アラートダイアログで警告を表示
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(message);
		adb.setPositiveButton("OK",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	// 処理なし
	                }
	            });
		adb.show();
    }
    
    /**
     * 内部初期処理クラス
     * 外部サーバーよりCSVファイル読込、DBへ初期データ登録
     * 
     * */
    class Init extends AsyncTask<String,Void,Map<String, List<String>>>{
    	
    	protected Map<String, List<String>> doInBackground(String... params) {
    		
    		try {
    			/** DBアクセスクラスオブジェクト */
    			Dao dao = new Dao(getApplicationContext());
    			
        		/** 1.ノベルファイル読込・取得 */
        		Map<String, List<String>> novel_data = 
        				cmnutil.ReadCsvFile(novel_url, getApplicationContext());
        		Log.d("DEBUG", "ノベルファイル読込・取得完了");
        		Log.d("DEBUG",novel_data.toString());
        		
        		Set novel_keySet = novel_data.keySet();
        		Iterator novel_keyIte = novel_keySet.iterator();
        		// Log.d("DEBUG", novel_keySet.toString()); // [1, 2]

        		/**　1-1.ノベルデータ登録 */
        		while (novel_keyIte.hasNext()) {
        			// ノベルID
        			String key = novel_keyIte.next().toString();
        			// ノベルデータリスト
        			List<String> value = novel_data.get(key);
        			
        			// ノベルデータの初期データDB登録
        			dao.InitDataInsert(key, value, DbConstants.TABLE2);
        		}
        		
        		/** 2.ステージデータファイル読み込み */
        		Map<String, List<String>> stage_data = 
        				cmnutil.ReadCsvFile(stage_url, getApplicationContext());
        		Log.d("DEBUG", "ステージデータファイル読込・取得完了");
        		Log.d("DEBUG",stage_data.toString());
        		
        		Set stage_keySet = stage_data.keySet();
        		Iterator stage_keyIte = stage_keySet.iterator();
        		
        		/** 2-1 ステージデータをDBに収録 */
        		while (stage_keyIte.hasNext()) {
        			// ステージID
        			String key = stage_keyIte.next().toString();
        			// ステージセレクトデータリスト
        			List<String> value = stage_data.get(key);
        			
        			// ステージセレクトデータの初期データDB登録
        			dao.InitDataInsert(key, value, DbConstants.TABLE3);
        		}
        		
        		/** 3.スタンプラリーマスタの初期データ登録 */
        		dao.InitDataInsert(null, null, DbConstants.TABLE1);
        		
        		// START, RECORDボタン有効化
        		findViewById(R.id.start).setClickable(true);
        		findViewById(R.id.record).setClickable(true);
        		
        		return null;
        	} catch (Exception e) {
        		Log.e("ERROR", cmndef.TOP_ERROR_MSG3 + e.toString());
                return null;
    		}
    	}
    }
}
