package com.tetuo41.locanovel.stageselect;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
		
    	final ArrayList<StageSelectState> dataOfStage = 
    			new ArrayList<StageSelectState>();
    	
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
    		sss.setPhoto(LocalImgRead(Integer.valueOf(data.get(0))));
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
    
    /**
     * ローカル保存している画像データ(ステージ画像)をByte配列で返却する
     * @param stage_id　ステージID
     * */
    private byte[] LocalImgRead(int stage_id) {
    	
    	/** ビットマップ画像のbyte配列生成 */
    	byte[] byteData = null;

		try {
			// 画像データ読出先 /data/data/com.tetuo41.locanovel/files
			FileInputStream fis = openFileInput("stage" + stage_id + ".png");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] w = new byte[1024];
			while (bis.read(w) >= 0) {
				baos.write(w, 0, 1024);
			}
			byteData = baos.toByteArray();
			
			fis.close();
			baos.close();
			bis.close();
			
		} catch (FileNotFoundException e) {
			// TODO ステージ画像ファイルがない場合、「準備中」の画像を表示させる。
			Log.e("ERROR", cmndef.STAGE_ERROR_MSG1 + e.toString());
			return null;
            
		} catch (IOException e) {
			// 画像読み込みに失敗した場合
			Log.e("ERROR", cmndef.STAGE_ERROR_MSG2 + e.toString());
			Toast.makeText(getApplicationContext(), 
					cmndef.STAGE_ERROR_MSG2, Toast.LENGTH_SHORT).show();
            return null;
            
		}
		
		// Bitmap画像のByte配列を返却する
		return byteData;
		
    }
    
}
