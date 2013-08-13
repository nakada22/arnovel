package com.tetuo41.arnovel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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
		
		// ClickListener登録
		findViewById(R.id.record_img1).setOnClickListener(this);
		findViewById(R.id.record_img2).setOnClickListener(this);
		findViewById(R.id.record_img3).setOnClickListener(this);
		findViewById(R.id.record_img4).setOnClickListener(this);
		findViewById(R.id.record_img5).setOnClickListener(this);
		findViewById(R.id.record_img6).setOnClickListener(this);
		findViewById(R.id.record_img7).setOnClickListener(this);
		findViewById(R.id.record_img8).setOnClickListener(this);
		findViewById(R.id.record_img9).setOnClickListener(this);
		
		// スタンプ一覧画面用を表示、詳細画面へ渡す
		StampListView();
		
    }
    
    /** 
     * コンストラクタ
     *
     */
    public StampLogActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /** 
     * エントリ一個の処理を記述する。
     * @param View ボタンオブジェクト
     */
    public void onClick(View v) {
    	// 詳細データ取得
    	ArrayList<StampLogState> dataOfState = StampListView();
    	
    	switch (v.getId()) {
		case R.id.record_img1:
			
			StampImgClick(1, dataOfState);
			break;
		case R.id.record_img2:
			StampImgClick(2, dataOfState);
			break;
		case R.id.record_img3:
			StampImgClick(3, dataOfState);
			break;
		case R.id.record_img4:
			StampImgClick(4, dataOfState);
			break;
		case R.id.record_img5:
			StampImgClick(5, dataOfState);
			break;
		case R.id.record_img6:
			StampImgClick(6, dataOfState);
			break;
		case R.id.record_img7:
			StampImgClick(7, dataOfState);
			break;
		case R.id.record_img8:
			StampImgClick(8, dataOfState);
			break;
		case R.id.record_img9:
			StampImgClick(9, dataOfState);
			break;
			
		default:
			break;
		}
	}
    
    /**
     * ノベル読了したステージのスタンプ一覧を表示する
     * @return  スタンプ詳細画面で表示するデータ
     * */
    private ArrayList<StampLogState> StampListView() {

    	// スタンプログ詳細画面へ受け渡す値を入れ物を用意
    	final ArrayList<StampLogState> dataOfStamp = 
    			new ArrayList<StampLogState>();
    	
    	// 空の要素を9個分あらかじめ入れておく
    	for (int i = 0; i < 10; i++) {
    		dataOfStamp.add(null);
    	}
    	
    	/** DBアクセスクラスオブジェクト */
		Dao dao = new Dao(getApplicationContext());

		/** 画像インスタンス生成 */
    	ImageView img1 = (ImageView) findViewById(R.id.record_img1);
    	ImageView img2 = (ImageView) findViewById(R.id.record_img2);
    	ImageView img3 = (ImageView) findViewById(R.id.record_img3);
    	ImageView img4 = (ImageView) findViewById(R.id.record_img4);
    	ImageView img5 = (ImageView) findViewById(R.id.record_img5);
    	ImageView img6 = (ImageView) findViewById(R.id.record_img6);
    	ImageView img7 = (ImageView) findViewById(R.id.record_img7);
    	ImageView img8 = (ImageView) findViewById(R.id.record_img8);
    	ImageView img9 = (ImageView) findViewById(R.id.record_img9);
    	
    	/** テキストビュー(レコードタイトル・レコードID)インスタンス生成 */
    	TextView record_id_title1 = (TextView) findViewById(R.id.record_id_title1);
    	TextView record_id_title2 = (TextView) findViewById(R.id.record_id_title2);
    	TextView record_id_title3 = (TextView) findViewById(R.id.record_id_title3);
    	TextView record_id_title4 = (TextView) findViewById(R.id.record_id_title4);
    	TextView record_id_title5 = (TextView) findViewById(R.id.record_id_title5);
    	TextView record_id_title6 = (TextView) findViewById(R.id.record_id_title6);
    	TextView record_id_title7 = (TextView) findViewById(R.id.record_id_title7);
    	TextView record_id_title8 = (TextView) findViewById(R.id.record_id_title8);
    	TextView record_id_title9 = (TextView) findViewById(R.id.record_id_title9);
    	
		// スタンプ一覧データ取得 
		List<List<String>> stamp_data = dao.StampRecordData();
		Log.d("DEBUG", "スタンプ一覧データ読込・取得完了");
		Log.d("DEBUG",stamp_data.toString());
		
		StampLogState sls = new StampLogState();
		
		// データを画面にセットする
		for (int i = 0; i < stamp_data.size(); i++) {
			
			// 1件分のデータ
			List<String> data = stamp_data.get(i);
			Log.d("DEBUG",data.toString());
//			data.get(2); // ノベルタイトル格納
//			data.get(3); // ノベルデータ格納(あらすじ)
			int stage_id = Integer.parseInt(data.get(0));
			
			switch (stage_id) {
				// ステージID(data.get(0))がどれかによって表示する画像、データを変える。
			case 1:
				// ステージIDが1の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img1, record_id_title1);
				dataOfStamp.add(stage_id, sls);
				break;
				
			case 2:
				// ステージIDが2の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img2, record_id_title2);
				dataOfStamp.add(stage_id, sls);
				break;
				
			case 3:
				// ステージIDが3の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img3, record_id_title3);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 4:
				// ステージIDが4の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img4, record_id_title4);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 5:
				// ステージIDが5の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img5, record_id_title5);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 6:
				// ステージIDが6の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img6, record_id_title6);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 7:
				// ステージIDが7の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img7, record_id_title7);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 8:
				// ステージIDが8の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img8, record_id_title8);
				dataOfStamp.add(stage_id, sls);
				break;
			
			case 9:
				// ステージIDが9の場合
				sls = StampLogDisp(data.get(0),
						data.get(1),data.get(2),data.get(3), 
						img9, record_id_title9);
				dataOfStamp.add(stage_id, sls);
				break;
			
			default:
				// データがnullの場合
				break;
			}
			
		}
		// データ受け渡し
		return dataOfStamp;
    }

    /**
     * スタンプ一覧画面で表示するスタンプ画像やデータの判定処理を行う
     * @param スタンプID
     * @param スタンプフラグ
     * @param ステージタイトル
     * @param ノベルデータ
     * @param イメージビュー
     * @param テキストビュー
     * @return スタンプログデータ
     * 
     * */
    private StampLogState StampLogDisp(String stamp_id, String stamp_flg, 
    		String stage_title, String novel_data,
    		ImageView imgv, TextView tv) {
    	
    	if (Integer.parseInt(stamp_flg) == 1) {
			// スタンプフラグ「1」の場合お岩画像セット
    		imgv.setImageResource(R.drawable.oiwa_stamp);
		} else {
			imgv.setImageResource(R.drawable.blankstamp);
		}
		
		// ステージタイトルセット
    	tv.setText
		("No." + stamp_id + "「" + stage_title + "」");
		
    	// スタンプログ詳細画面で表示する値セット
		StampLogState sls = new StampLogState();
		sls.setStampId(Integer.parseInt(stamp_id));
		sls.setStampFlg(Integer.parseInt(stamp_flg));
		sls.setNovelTitle(stage_title);
		sls.setNovelData(novel_data);
		return sls;
    }
    
    /** 
     * スタンプ画像クリック時の処理を記述する。
     * @param StampLogState
     */
    private void StampImgClick(int posion, ArrayList<StampLogState> sls) {
    	
    	try {
    		// スタンプ画像クリック時、スタンプログ詳細画面へ遷移
    		StampLogState data = sls.get(posion);
    		if (data.stamp_flg == 1) {
    			// スタンプフラグが1の時だけ詳細画面へ遷移
        		Intent i = new Intent(getApplicationContext(), StampLogDetailActivity.class);
        		i.putExtra("StampLogState", data);
    			startActivity(i);
    		}
    		
    	} catch (ActivityNotFoundException e) {
    		// スタンプログ詳細画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
  
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.STAMP_ERROR_MSG1);
    		// 処理を終了する
    		return;
    	} catch (RuntimeException e) {
    		// スタンプログ詳細画面へ遷移できなかった場合
    		Log.e("ERROR", e.toString());
    		
    		// アラートダイアログで警告を表示
    		AlertDialogView("エラー", cmndef.STAMP_ERROR_MSG1);
    		
    		// 処理を終了する
    		return;
    	}
    }
    
    /**
     * 内部クラス
     * をシリアライズ(直列化)
     */
    public static class StampLogState implements Serializable {
    	
    	private static final long serialVersionUID = 1L;

    	/** スタンプID */ 
    	private int stamp_id;

    	/** スタンプフラグ */ 
    	private int stamp_flg;
    	
    	/** ノベルタイトル */ 
    	private String novel_title;
    	
    	/** ノベルデータ(あらすじ) */ 
    	private String novel_data;

    	public void setStampId(int _stamp_id) {
    		this.stamp_id = _stamp_id;
    	}
    	public int getStampId() {
    		return this.stamp_id;
    	}
    	
    	public void setStampFlg(int _stamp_flg) {
    		this.stamp_flg = _stamp_flg;
    	}
    	public int getStampFlg() {
    		return this.stamp_flg;
    	}
    	
    	public void setNovelTitle(String _novel_title) {
    		this.novel_title = _novel_title;
    	}
    	public String getNovelTitle() {
    		return this.novel_title;
    	}
    	
    	public void setNovelData(String _novel_data) {
    		this.novel_data = _novel_data;
    	}

    	public String getNovelData() {
    		return this.novel_data;
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
