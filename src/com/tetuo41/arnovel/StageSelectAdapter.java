package com.tetuo41.arnovel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ステージセレクト画面へデータを渡すためのアダプター
 * @author HackathonG
 */
public class StageSelectAdapter extends ArrayAdapter<StageSelectState> {

	private ArrayList<StageSelectState> list;
    private LayoutInflater inflater;
    private ViewHolder     holder;

    /** 
     * コンストラクタ
     *
     * @param Context
     * @param textViewResourceId
     * @param ArrayList<StageSelectState>
     */
	public StageSelectAdapter(Context context, int textViewResourceId, ArrayList<StageSelectState> _list) {
		super(context, textViewResourceId, _list);

        this.list = _list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

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
            	
            	try {
            		// 画像のURLをセット
                	// 例： http://tetuo41.com/image/locanovel/stage1.png
                	String image_url = item.getPhotoUrl().toString();  
                	InputStream is = (InputStream) new URL(image_url).getContent();
                	Drawable d = Drawable.createFromStream(is, "");
                	is.close();
                	holder.photo.setImageDrawable(d);
                	
            	} catch (NetworkOnMainThreadException e) {
            		// 3.0以降「StrictMode」がデフォルトで有効になっており
            		// メインスレッドでネットワーク処理を行うと例外がスローされる
            		
            	} catch (MalformedURLException e) {
            		
            	} catch (IOException e) {
            		
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
