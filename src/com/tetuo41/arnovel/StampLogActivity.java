package com.tetuo41.arnovel;

import java.util.List;

import android.app.Activity;
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
		
		// ノベル読了したステージのスタンプ一覧を表示
		StampListView();
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
		case R.id.record_img4:
			break;
		case R.id.record_img5:
			break;
		case R.id.record_img6:
			break;
		case R.id.record_img7:
			break;
		case R.id.record_img8:
			break;
		case R.id.record_img9:
			break;
			
		default:
			break;
		}
	}
    
    /**
     * ノベル読了したステージのスタンプ一覧を表示する
     * 
     * */
    private void StampListView() {

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
		
		// データを画面にセットする
		for (int i = 0; i < stamp_data.size(); i++) {
			List<String> data = stamp_data.get(i);
			Log.d("DEBUG",data.toString());
//			data.get(2); // ノベルタイトル格納
//			data.get(3); // ノベルデータ格納(あらすじ)

			if (data.isEmpty()) {
				// 空の場合
			}
			
			switch (Integer.parseInt(data.get(0))) {
				// ステージIDがどれかによって表示する画像、データを変える。
			case 1:
				// ステージIDが1の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img1.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img1.setImageResource(R.drawable.blankstamp);
				}
				
				// ステージタイトルセット
				record_id_title1.setText
				("No." + data.get(0) + "「" + data.get(2) + "」");
				
				break;
			case 2:
				// ステージIDが2の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img2.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img2.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 3:
				// ステージIDが3の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img3.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img3.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 4:
				// ステージIDが4の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img4.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img4.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 5:
				// ステージIDが5の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img5.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img5.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 6:
				// ステージIDが6の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img6.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img6.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 7:
				// ステージIDが7の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img7.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img7.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 8:
				// ステージIDが8の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img8.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img8.setImageResource(R.drawable.blankstamp);
				}
				break;
			case 9:
				// ステージIDが9の場合
				if (Integer.parseInt(data.get(1)) == 1) {
					// スタンプフラグ「1」の場合お岩画像セット
					img9.setImageResource(R.drawable.oiwa_stamp);
				} else {
					img9.setImageResource(R.drawable.blankstamp);
				}
				break;
			default:
				// データがnullの場合
				
				break;
			}
			
			
			


		}
		
    	
    	
    }

}
