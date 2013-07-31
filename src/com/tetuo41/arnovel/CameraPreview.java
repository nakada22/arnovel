package com.tetuo41.arnovel;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {
	
	
    protected Context context;
    private SurfaceHolder holder;
	protected Camera camera;
	private static final String SDCARD_FOLDER = "/sdcard/locanovel/";

	public CameraPreview(Context context) {
		super(context);
		this.context = context;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        
		//holder = getHolder();
		//holder.addCallback(this);
		
		//プッシュバッッファの指定
		//holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		// 保存用フォルダ作成
		File dirs = new File(SDCARD_FOLDER);
		if (!dirs.exists()) {
			dirs.mkdir();
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (camera == null) {
            try {
                camera = Camera.open();
            } catch (RuntimeException e) {
                ((Activity)context).finish();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
		
		 
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//camera.stopPreview();
		//Camera.Parameters params = camera.getParameters();
		//params.setPreviewFormat(format);
		//params.setPreviewSize(width, height);
		//camera.setParameters(params);

		// 縦画面対応
		// setRotation(90)はxperiaなどで効かないよう。
		// 出力画像を回転させる方向で実装しておいた方がよい
		//camera.setDisplayOrientation(90);
		//params.setRotation(90);

		//camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		camera.setPreviewCallback(null);
//	    camera.stopPreview();
//	    camera.release();
//	    camera = null;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//camera.takePicture(null, null, this);
		}
		return true;
	}

	private void savePhotoData(String datName, byte[] data) throws Exception {
		// TODO Auto-generated method stub
		FileOutputStream outStream = null;

		try {
			outStream = new FileOutputStream(SDCARD_FOLDER + datName);
			outStream.write(data);
			outStream.close();
		} catch (Exception e) {
			if (outStream != null) {
				outStream.close();
			}
			throw e;
		}
	}
}