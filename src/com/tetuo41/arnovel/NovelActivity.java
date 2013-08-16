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
import android.view.WindowManager;
import android.widget.ImageView;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
* ノベル表示画面を表示するクラスです。
* @author　HackathonG
* @version 1.0
*/
public class NovelActivity extends Activity implements OnClickListener{
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** 背景画像用 */
	private ImageView novel_layout;
	private String bg_pass;
	
	/** ステージセレクト画面からのデータ */
	private StageSelectState sss;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// テーマに撮影した画像をセット
		setContentView(R.layout.novel);
		
		// ノベル部分をリスト表示する
		NovelDisp();
		
		// ClickListener登録
		//findViewById(R.id.novellist).setOnClickListener(this);
				
    }
    
    /** 
     * コンストラクタ
     */
    public NovelActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /**
     * ノベル導入部分を表示する
     * 
     * */
    private void NovelDisp() {
    	
		// 背景用画像パス取得、背景画像セット
    	Intent i = getIntent();
		String bg_pass = (String) i.getSerializableExtra("back_ground");
    	Drawable d = Drawable.createFromPath(bg_pass);
    	novel_layout = (ImageView) findViewById(R.id.novel_back_ground);
		novel_layout.setBackgroundDrawable(d);
		
		// ステージセレクトActivityより取得したデータを取得
		// sss = (StageSelectState)i.getSerializableExtra("StageSelectState");
		
    }
    
    /** 
     * ボタンクリック時の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.kikasete_blowoff:
			// 「読了」クリック時
			ReadFinishClick();
			
			break;
		default:
			break;
		}
	}
    
    /** 
     * 「読了」ボタンクリック時の処理を記述する。
     */
    private void ReadFinishClick() {
    	
    	try {
    		// 読了ボタンクリック時、ノベル完了画面へ遷移
    		Intent i = new Intent(getApplicationContext(), NovelCompActivity.class);
    		i.putExtra("back_ground", bg_pass);
			startActivity(i);
    		
    	} catch (ActivityNotFoundException e) {
    		// ノベル完了画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    		
    	} catch (RuntimeException e) {
    		// ノベル完了画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);
    		
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
