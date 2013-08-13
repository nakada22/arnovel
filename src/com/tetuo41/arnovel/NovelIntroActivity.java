package com.tetuo41.arnovel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
* ノベル導入画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class NovelIntroActivity extends Activity implements OnClickListener{
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	private ImageView novel_layout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// テーマに撮影した画像をセット
		setContentView(R.layout.novel_intro);
		
		Intent i = getIntent();
		// 背景用画像パス取得
    	String bg_pass = (String) i.getSerializableExtra("back_ground");
    	Drawable d = Drawable.createFromPath(bg_pass);
    	novel_layout = (ImageView) findViewById(R.id.back_ground);
		novel_layout.setBackgroundDrawable(d);
		
		// ClickListener登録
		//findViewById(R.id.start).setOnClickListener(this);
		//findViewById(R.id.record).setOnClickListener(this);
    }
    
    /** 
     * コンストラクタ
     */
    public NovelIntroActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /** 
     * ボタンクリック時の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.start:
			// STARTボタンクリック時
			TellClick();
			
			break;
		case R.id.record:
			// RECORDボタンクリック時
			SorryStopClick();
			
			break;
		default:
			break;
		}
	}
    
    /** 
     * 「聞かせて」ボタンクリック時の処理を記述する。
     */
    private void TellClick() {
    	
    	try {
    		// STARTボタンクリック時、スタンプログ画面へ遷移
    		//Intent i = new Intent(getApplicationContext(), NovelActivity.class);
			//startActivity(i);
    		
    	} catch (ActivityNotFoundException e) {
    		// ノベル表示画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);
    		// 処理を終了する
    		return;
    		
    	} catch (RuntimeException e) {
    		// ノベル表示画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /** 
     * 「ごめん、やめておく」ボタンクリック時の処理を記述する。
     */
    private void SorryStopClick() {
    	
    	try {
    		// ごめん、やめておくボタンクリック時、スタンプログ画面へ遷移
    		//Intent i = new Intent(getApplicationContext(), StampLogActivity.class);
			//startActivity(i);
			
    	} catch (ActivityNotFoundException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.TOP_ERROR_MSG2);
    		
    		// 処理を終了する
    		return;
    	} catch (RuntimeException e) {
    		// ステージセレクト画面へ遷移できなかった場合
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
    
    
}
