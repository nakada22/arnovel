package com.tetuo41.arnovel;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;
import com.tetuo41.arnovel.db.Dao;

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
		
		// プレビュー撮影画像
    	//ImageView imageView1 = (ImageView)findViewById(R.id.camera_preview);
    	
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
    		sss.setPhotoUrl(stage_img_url + "stage" + data.get(0) +".png");
    		sss.setStageTitle(data.get(1));
    		sss.setOutLine(data.get(2));
    		sss.setAddress(data.get(3));
    		dataOfStage.add(sss);
    	}
    	
    	StageSelectAdapter ssa = new StageSelectAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, dataOfStage);
		final ListView lv = (ListView) findViewById(R.id.stagelist);
		lv.setAdapter(ssa);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				
				// TODO カメラアプリの起動、ノベルデータ保持？　GPS機能も起動
//				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				i.addCategory(Intent.CATEGORY_DEFAULT);
//				//　ここの0の番号で呼び出し元と呼び出し先で対象かどうか判断する
//				startActivityForResult(i,REQUEST_CAPTURE_IMAGE);
				// インテントのインスタンス生成
				// Intent i = new Intent();
				// i.setAction("android.media.action.IMAGE_CAPTURE");
				// startActivityForResult(i, REQUEST_CAPTURE_IMAGE);
			
				// 参考URL http://techbooster.jpn.org/andriod/device/9632/ 
				//        http://blog.ayakix.com/2011/09/androidoverlay.html
				StageSelectState sss = dataOfStage.get(position);

				Intent i = new Intent(getApplicationContext(), CameraPreviewActivity.class);
				i.putExtra("CameraPreview", sss);
				startActivity(i);
			}
		});
		
		// TODO? 最初の段階で画像データ10件読み込み、スクロール時、10件ずつ読み込めるようにしたい
		// http://sakplus.jp/2011/05/21/stretchlist/
		
    }
    
    @Override
	protected void onActivityResult( int requestCode, int resultCode, 
		Intent data) {
    	
    	try {
    		
        	if(REQUEST_CAPTURE_IMAGE == requestCode 
    			&& resultCode == Activity.RESULT_OK ){
        		
        		Toast.makeText(this, "Activity.RESULT_OK", Toast.LENGTH_LONG).show();
        		
        		// カメラからの結果を取得(ここでNull PointerException)
    			Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
    			imageView1.setImageBitmap(capturedImage);
    			Toast.makeText(this, "setImageBitmap OK", Toast.LENGTH_LONG).show();
        		
    			View view = getLayoutInflater().inflate(R.layout.camera_preview, null);
    			Toast.makeText(this, "etLayoutInflater().infl OK", Toast.LENGTH_LONG).show();
    			
    			addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
    			LayoutParams.FILL_PARENT));
    			
    		}
    	} catch(Exception e) {
    		Log.d("DEBUG", e.toString());
    		// Null PointerException!!!!
    		Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
    		Toast.makeText(this, "カメラからの結果取得に失敗しました", Toast.LENGTH_LONG).show();
    	}
    	
	}
    
}
