package com.tetuo41.arnovel;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * ステージ選択画面
 * @author HackathonG
 * @version 1.0
 */
public class StageSelectActivity extends Activity{
	
	/** スクロール中かどうかフラグ */
	// boolean mBusy;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stage_select);
		
		// ステージリスト表示メソッド
		StageSelectView();
    }
    
    /** 
     * ステージリスト表示する
     * 
     */
    private void StageSelectView() {
		
    	final ArrayList<StageSelectState> dataOfStage = new ArrayList<StageSelectState>();
    	
    	// TODO ノベルデータをDBから取得・セット
    	for (int i = 1; i <= 3; i++) {
    		StageSelectState sss = new StageSelectState();
    		sss.setPhotoUrl("http://sashihara.web.fc2.com/image/locanovel/stage" + i +".png");
    		sss.setStageTitle("ステージタイトル");
    		sss.setOutLine("あらすじあらすじあらすじあらすじ");
    		sss.setAddress("東京都新宿区左門町17");
    		dataOfStage.add(sss);
    	}
    	
    	StageSelectAdapter ssa = new StageSelectAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, dataOfStage);
		final ListView lv = (ListView) findViewById(R.id.stagelist);
		lv.setAdapter(ssa);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				StageSelectState sss = dataOfStage.get(position);
				
				// TODO カメラプレビュー起動、ノベルデータ保持？
				//Intent i = new Intent(StageSelectActivity.this, DailyActivity.class);
				//i.putExtra("DailyState", ds);
				//startActivity(i);
			}
		});
		
		//TODO? 最初の段階で画像データ10件読み込み、スクロール時、10件ずつ読み込めるようにしたい
		// http://sakplus.jp/2011/05/21/stretchlist/
		
    }
}
