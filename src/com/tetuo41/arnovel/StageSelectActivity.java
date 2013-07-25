package com.tetuo41.arnovel;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * ステージ選択画面
 * @author HackathonG
 */
public class StageSelectActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stage_select);
		
		StageSelectView();
		// TextViewに線をセット
		//TextView textView_line = (TextView) findViewById(R.id.textView_line);
		//textView_line.setBackgroundResource(R.layout.line);
		
    }
    
    /** 
     * ステージリスト表示する
     * 
     */
    private void StageSelectView() {
    	
    	final ArrayList<StageSelectState> dataOfStage = new ArrayList<StageSelectState>();
    	
    	// ノベルデータをセット
    	for (int i = 1; i <= 5; i++) {
    		StageSelectState sss = new StageSelectState();
    		sss.setPhotoUrl("http://tetuo41.com/image/locanovel/stage1.png");
    		sss.setAddress("東京都新宿区左門町17");
    		sss.setOutLine("あらすじあらすじあらすじあらすじ");
    		sss.setStageTitle("ステージタイトル");
    		dataOfStage.add(sss);
    	}
    	
    	StageSelectAdapter ssa = new StageSelectAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, dataOfStage);
		ListView lv = (ListView) findViewById(R.id.stagelist);
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
    }
    
    
}
