package com.tetuo41.arnovel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;
import com.tetuo41.arnovel.db.Dao;

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
	private LinearLayout bg_layout;
	private String bg_pass;
	
	/** スクロールビューで必要なオブジェクト */
	private NovelScrollView scroll;
    private TextView novel_layout;
    private boolean flg = false;
    private StageSelectState sss; 
    private View read_comp_view;
    private boolean disp_chg_flg = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
        // ノベル表示用ScrollViewのオブジェクトを取得
		scroll = (NovelScrollView) LayoutInflater.from(this)
        		.inflate(R.layout.novel, null);
        setContentView(scroll);
		
        // ノベルデータ表示用TextView
        novel_layout = (TextView) scroll.findViewById(R.id.novel_data);
        read_comp_view = getLayoutInflater().inflate(R.layout.novel2, null);
		
		// 参考URL:http://qiita.com/haratchatta/items/86aa8517a91fea1e772f
		// ノベル部分をリスト表示する
		NovelDisp();

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
    	bg_layout = (LinearLayout)scroll.findViewById(R.id.novel_back_ground);
		bg_layout.setBackgroundDrawable(d);
		
		// ステージセレクト画面より引継ぎして取得したノベルデータ
		sss = (StageSelectState) i.getSerializableExtra("StageSelectState");
		
		// ノベルデータをセット
    	String novel_data = sss.getAllOutLine();
    	novel_layout.setText(novel_data);
    	
    	if (novel_data.length() <= 500) {
    		// ノベルデータが500文字以下であれば、「読了」ボタンビューにセット・表示処理を行う
    		bg_layout.addView(read_comp_view);
    		flg = true; // 読了ボタンは初回だけ表示
    		
    		findViewById(R.id.read_comp).setOnClickListener(
        		new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	// 「読了」クリック時
                    	ReadFinishClick();
                    }
            });
    	}
    	
        scroll.setScrollToBottomListener(new NovelScrollView.ScrollToBottomListener() {
            @Override
            public void onScrollToBottom(NovelScrollView scrollView) {
            	Log.d("DEBUG", "onScrollToBottom Start");
            	
            	if (flg == false) {
            		
            		// 初回の最下部スクロール時だけ、最下部にスクロールした時のイベント
                	// 「読了」ボタンビューにセット・表示処理を行う
            		bg_layout.addView(read_comp_view);
            		flg = true;
            		
            		// ボタンクリック時の処理
            		findViewById(R.id.read_comp).setOnClickListener(
                		new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            	// 「読了」クリック時
                            	ReadFinishClick();
                            }
                    });
            		
            	}
            	
            }

        });
    	
    	// 一番下までスクロールさせる
//    	scrollView.post(new Runnable() {
//    		  @Override public run() {
//    		    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//    		  }
//    		});
    	
    }
    
    /**
     * フォーカスが移った直後か失った直後の処理
     * @param hasFocus フォーカスの状態
     * */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("DEBUG", "onWindowFocusChanged Start");
        
        // フォーカス変更後の処理
        if (disp_chg_flg == true) {
        	
        	/** 読了ボタン押したとき */ 
        	// 読了ボタン非表示
        	read_comp_view.setVisibility(View.INVISIBLE);
        	
        	// ノベルデータ非表示
        	novel_layout.setVisibility(View.INVISIBLE);
        	disp_chg_flg = false;
        } else {
        	
        	/** ノベル完了画面からノベル表示画面へ戻った時 */
        	// 読了ボタン表示
        	read_comp_view.setVisibility(View.VISIBLE);
        	
        	// ノベルデータ表示
        	novel_layout.setVisibility(View.VISIBLE);
        	
        }
    	
        scroll.setScrollBottomMargin(1000);
    }
    
    
    /** 
     * ボタンクリック時の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.drawable.read_comp:
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
    		Log.d("DEBUG", "ReadFinishClick Start");
    		// DBアクセスクラスオブジェクト
			Dao dao = new Dao(getApplicationContext());
			
			// stampフラグを「1」Update
    		dao.StampFlgUpdate(sss.getStageId());
    		
    		// 読了ボタンクリック時、ノベル完了画面へ遷移
    		Intent i = new Intent(getApplicationContext(), NovelCompActivity.class);
    		i.putExtra("back_ground", bg_pass);
    		i.putExtra("StageSelectState", sss);
			startActivity(i);
    		
    	} catch (ActivityNotFoundException e) {
    		// ノベル完了画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    		
    	} catch (RuntimeException e) {
    		// 通常通らないルート
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /**
   	 * アクティビティが「停止」の状態
   	 * */
   	@Override
   protected void onPause() {
	super.onPause();
	Log.d("DEBUG", "onPause() Start");
	
	// 読了ボタン押した時通る
	disp_chg_flg = true;
	
	Log.d("DEBUG", "onPause() End");
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
