package com.tetuo41.locanovel.stageselect;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;

/**
 * ステージセレクト画面へデータを渡すためのアダプター
 * @author HackathonG
 */
public class StageSelectAdapter extends ArrayAdapter<StageSelectState> {

	private ArrayList<StageSelectState> list;
    private LayoutInflater inflater;
    private ViewHolder     holder;
    private Context context;
    
    /** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
    /** 
     * コンストラクタ
     *
     * @param Context
     * @param textViewResourceId
     * @param ArrayList
     */
	public StageSelectAdapter(Context context, int textViewResourceId, 
			ArrayList<StageSelectState> _list) {
		super(context, textViewResourceId, _list);
		cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    	
		this.context = context;
        this.list = _list;
        this.inflater = (LayoutInflater) context.getSystemService
        		(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	/** 
     * ListViewで表示される分だけデータをロードする
     *
     * @param position
     * @param convertView
     * @param ViewGroup
     * @return View ビューオブジェクト
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // ViewHolder
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.stage_item, null);

            // Viewホルダー
            holder = new ViewHolder();
            holder.photo = (ImageView) view.findViewById(R.id.stage1);
            holder.stage_title = (TextView) view.findViewById(R.id.stage_title);
            holder.outline = (TextView) view.findViewById(R.id.outline);
            holder.address = (TextView) view.findViewById(R.id.address);

            view.setTag(holder);

        } else {
        	holder = (ViewHolder) convertView.getTag();
		}

        // ステージセレクト画面での各種値
        StageSelectState item = list.get(position);
        if (item != null) {
            if (holder.photo != null) {
            	
            	// 画像のURLをセット
            	String image_url = item.getPhotoUrl().toString();
            	try{
            		// 非同期で画像読込を実行
                    DownloadImageTask task = 
                    		new DownloadImageTask(holder.photo, context);
                    task.execute(image_url);
                    
                } 
//            	catch (NetworkOnMainThreadException e) {
//                	// 3.0以降「StrictMode」がデフォルトで有効になっており
//            		// メインスレッドでネットワーク処理を行うと例外がスローされる
//            		
//                	// 画像の読込が失敗した場合
//            		Log.e("WARN","NetworkOnMainThreadException");
//            		Toast.makeText(context, "画像の読込が失敗しました。", 
//            				Toast.LENGTH_LONG).show();
//            		
//            	} 
            	catch (Exception e) {
            		// 画像の読込が失敗した場合
            		Log.w("WARN", e.toString());
            		Toast.makeText(context, cmndef.STAGE_ERROR_MSG1, 
            				Toast.LENGTH_LONG).show();
            		
            	}
            	
            }
            
            if (holder.stage_title != null) {
            	// ステージタイトルがあれば
            	holder.stage_title.setText(item.getStageTitle());
            }
            if (holder.outline != null) {
            	// あらすじがあれば
            	holder.outline.setText(item.getOutLine());
            }
            if (holder.address != null) {
            	// 住所があれば
            	holder.address.setText(item.getAddress());
            }
        }
        return view;
    }

    /**
     * Viewを保持してgetView高速化。
     */
    private class ViewHolder {
    	/** ステージ画像 */
    	ImageView photo;
    	
    	/** ステージタイトル */
    	TextView stage_title;
    	
    	/** あらすじ */
    	TextView outline;
    	
    	/** 住所 */
    	TextView address;
    }
}
