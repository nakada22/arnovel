package com.tetuo41.arnovel;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraPreviewActivity extends Activity{
	// カメラインスタンス
    private Camera mCam = null;

    // カメラプレビュークラス
    private CameraPreview mCamPreview = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_preview);
        
        // カメラインスタンスの取得
        try {
            mCam = Camera.open();
        } catch (Exception e) {
        	// エラー発生時
            Log.w("WARN", e.toString());
        	Toast.makeText(getApplicationContext(), 
        			"カメラの起動に失敗しました", Toast.LENGTH_LONG).show();
        	this.finish();
        	
        }
        
        // FrameLayout に CameraPreview クラスを設定
        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
        mCamPreview = new CameraPreview(this, mCam);
        preview.addView(mCamPreview);
        
    }
	
	@Override
    protected void onPause() {
		Log.d("DEBUG", "onPause() Start");
        super.onPause();
        
        // カメラ破棄インスタンスを解放
        if (mCam != null) {
            mCam.release();
            mCam = null;
        }
    }
	
}

