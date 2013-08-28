package com.tetuo41.locanovel.stageselect;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Web上の画像を読込タスク 
 * @author HackathonG
 */
class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
    private ImageView imageView;
    private Context context;

    /** 
     * コンストラクタ
     *
     * @param imageView
     * @param context
     */
    public DownloadImageTask(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
    }

    /** 
     * タスク本体。画像をビットマップとして読み込んで返す
     *
     * @param params
     * @return Bitmap 画像
     */
    @Override
    protected synchronized Bitmap doInBackground(String... params) {
        synchronized (context){
            try {
            	String str_url = params[0];
                URL imageUrl = new URL(str_url);
                InputStream imageIs;

                // 読み込み実行
                imageIs = imageUrl.openStream();
                Bitmap bm = BitmapFactory.decodeStream(imageIs);
                Log.d("DEBUG", "画像読み込み完了");

                return bm;
            } catch (Exception e) {
                Log.w("WARN", "画像読み込みタスクで例外発生：" 
                    + e.toString());
                return null;
            }
        }
    }

    // タスク完了時
    @Override
    protected void onPostExecute(Bitmap result) {
        if(result != null){
            Log.d("DEBUG", "ビューに画像をセット");
            imageView.setImageBitmap(result);
        }
    }
}
