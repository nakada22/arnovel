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
import android.widget.Button;
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
		setContentView(R.layout.novel_intro);
		
		// ノベル導入部分を表示する
		NovelIntroDisp();
		
		// ClickListener登録
		findViewById(R.id.gomen_blowoff).setOnClickListener(this);
		findViewById(R.id.kikasete_blowoff).setOnClickListener(this);
    }
    
    /** 
     * コンストラクタ
     */
    public NovelIntroActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /**
     * ノベル導入部分を表示する
     * 
     * */
    private void NovelIntroDisp() {
    	
		// 背景用画像パス取得、背景画像セット
    	Intent i = getIntent();
		bg_pass = (String) i.getSerializableExtra("back_ground");
    	Drawable d = Drawable.createFromPath(bg_pass);
    	novel_layout = (ImageView) findViewById(R.id.back_ground);
		novel_layout.setBackgroundDrawable(d);
		
		// ステージセレクトActivityより取得したデータを取得
		sss = (StageSelectState)i.getSerializableExtra("StageSelectState");
		
		// ノベル導入部分1
		String novel_intro1 = sss.getNovelIntro1();
		Button btNovelIntro1 = (Button) findViewById(R.id.novel_intro1);
		btNovelIntro1.setText(novel_intro1);

		// ノベル導入部分2
		String novel_intro2 = sss.getNovelIntro2();
		Button btNovelIntro2 = (Button) findViewById(R.id.novel_intro2);
		btNovelIntro2.setText(novel_intro2);

		// ノベル導入部分3
		String novel_intro3 = sss.getNovelIntro3();
		Button btNovelIntro3 = (Button) findViewById(R.id.novel_intro3);
		btNovelIntro3.setText(novel_intro3);
    }
    
    /** 
     * ボタンクリック時の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.kikasete_blowoff:
			// 「聞かせて」クリック時
			TellClick();
			
			break;
		case R.id.gomen_blowoff:
			// 「ごめん、やめておく」ボタンクリック時
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
    		// 「聞かせて」ボタンクリック時、ノベル表示画面へ遷移
    		Intent i = new Intent(getApplicationContext(), NovelActivity.class);
    		i.putExtra("back_ground", bg_pass);
			startActivity(i);
    		
    	} catch (ActivityNotFoundException e) {
    		// ノベル表示画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG1);
    		// 処理を終了する
    		return;
    		
    	} catch (RuntimeException e) {
    		// ノベル表示画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /** 
     * 「ごめん、やめておく」ボタンクリック時の処理を記述する。
     */
    private void SorryStopClick() {
    	try {
    		
    		// アクティビティを終了させ、ステージセレクト画面へ遷移
    		sss = null;
    		this.finish();
    		Intent i = new Intent(getApplicationContext(), StageSelectActivity.class);
    		startActivity(i);
			
    	} catch (ActivityNotFoundException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG2);
    		
    		// 処理を終了する
    		return;
    	} catch (RuntimeException e) {
    		// ステージセレクト画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG2);
    		
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
