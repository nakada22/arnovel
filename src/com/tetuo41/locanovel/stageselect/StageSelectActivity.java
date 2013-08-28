package com.tetuo41.locanovel.stageselect;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.camera.CameraPreviewActivity;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.db.Dao;

/**
 * ステージ選択画面
 * @author HackathonG
 * @version 1.0
 */
public class StageSelectActivity extends Activity{
	
	/** スクロール中かどうかフラグ */
	// boolean mBusy;
	
	static final int REQUEST_CAPTURE_IMAGE = 0;
	ImageView imageView1;
	
	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** 外部サーバーの画像URL */
	String stage_img_url;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stage_select);
		
		// ステージリスト表示メソッド
		StageSelectView();
    }
    
    /** 
     * コンストラクタ
     */
    public StageSelectActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    	this.stage_img_url = "http://" + cmndef.S_HOST_NAME + 
    			cmndef.IMAGE_DIR;
    }
    /** 
     * ステージリスト表示する
     * 
     */
    private void StageSelectView() {
		
    	final ArrayList<StageSelectState> dataOfStage = new ArrayList<StageSelectState>();
    	/** DBアクセスクラスオブジェクト */
		Dao dao = new Dao(getApplicationContext());
		
    	// ステージセレクトデータ取得
		List<List<String>> stage_data = dao.StageSelctData();
		Log.d("DEBUG", "ステージセレクトデータ読込・取得完了");
		Log.d("DEBUG",stage_data.toString());

    	// ノベルデータをDBから取得・セット
    	for (int i = 0; i < stage_data.size(); i++) {
    		StageSelectState sss = new StageSelectState();
    		List<String> data = stage_data.get(i);
    		sss.setStageId(Integer.valueOf(data.get(0)));
    		sss.setPhotoUrl(stage_img_url + "stage" + data.get(0) +".png");
    		sss.setStageTitle(data.get(1));
    		sss.setOutLine(data.get(2).substring(0, 30) + "・・・");
    		sss.setAllOutLine(data.get(2)); // ノベル全文
    		sss.setAddress(data.get(3));
    		sss.setLongitude(Double.parseDouble(data.get(4))); // 経度
    		sss.setLatitude(Double.parseDouble(data.get(5))); // 緯度
    		sss.setNovelIntro1(data.get(6));// ノベル導入部分1
    		sss.setNovelIntro2(data.get(7));// ノベル導入部分2
    		sss.setNovelIntro3(data.get(8));// ノベル導入部分3
    		dataOfStage.add(sss);
    	}
    	
    	StageSelectAdapter ssa = new StageSelectAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, dataOfStage);
		final ListView lv = (ListView) findViewById(R.id.stagelist);
		lv.setAdapter(ssa);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				
				// カメラプレビューの起動(ノベルデータ保持)
				StageSelectState sss = dataOfStage.get(position);

				Intent i = new Intent(getApplicationContext(), CameraPreviewActivity.class);
				i.putExtra("StageSelectState", sss);
				startActivity(i);
			}
		});
		
		// TODO? 最初の段階で画像データ10件読み込み、スクロール時、10件ずつ読み込めるようにしたい
		// http://sakplus.jp/2011/05/21/stretchlist/
		
    }
}
