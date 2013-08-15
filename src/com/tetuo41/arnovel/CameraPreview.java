package com.tetuo41.arnovel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
 * カメラプレビューの処理を行うクラス
 * 
 * @author HackathonG
 * @version 1.0
 */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** 経度・緯度情報 */
	public double longitude;
	public double latitude;
	
	/** カメラオブジェクト */
	private Camera mCam;

	/** コンテキスト */
	private Context context;

	/** ステージセレクト画面から送られてきたデータ */
	private StageSelectState sss;

	/** 画面タッチ二度押し禁止フラグ */
	private boolean notouch_flg = false;

	/** SDカードの画像保存パス */
	private static final String SDCARD_FOLDER = Environment
			.getExternalStorageDirectory().getPath() + "/locanovel/";

	/**
	 * コンストラクタ
	 */
	public CameraPreview(Context context, Camera cam, StageSelectState sss,
			double longitude, double latitude) {
		super(context);
		this.context = context;
		this.sss = sss;
		this.longitude = longitude;
		this.latitude = latitude;
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
		mCam = cam;
		
		// サーフェスホルダーの取得とコールバック通知先の設定
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 画像保存用フォルダ作成
		File dirs = new File(SDCARD_FOLDER);
		if (!dirs.exists()) {
			// フォルダなければ作成
			dirs.mkdir();
		}
	}

	/**
	 * 画面(Surface)を描画する。
	 * 
	 * @param holder
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCam == null) {
				// カメラオブジェクトがない場合は作成
				try {
					mCam = Camera.open();
				} catch (Exception e) {
					// エラー発生時
					Log.w("WARN", e.toString());
					Toast.makeText(context, cmndef.CAMERA_ERROR_MSG3,
							Toast.LENGTH_LONG).show();
				}
			}

			// カメラインスタンスに、画像表示先を設定
			mCam.setPreviewDisplay(holder);

			// プレビュー開始
			mCam.startPreview();

		} catch (Exception e) {
			// 例外発生時
			Log.w("WARN", e.toString());
			// Toast.makeText(context, cmndef.CAMERA_ERROR_MSG3, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * SurfaceView 破棄時 (プレビュー画面からステージセレクト画面へBackした場合)
	 * 
	 * @param holder
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCam.release();
		mCam = null;
	}

	/** JPEGイメージ生成後に呼ばれるコールバック */
	private PictureCallback mPictureListener = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			// 画像ファイル名
			SimpleDateFormat date = new SimpleDateFormat("_yyyyMMddkkmmss");
			String datName = cmndef.APP_NAME + date.format(new Date()) + ".jpg";

			// Log.d("DEBUG", "onPictureTaken Start");
			try {
				if (data != null) {
					// 撮影画像保存
					savePhotoData(datName, data);
				}

			} catch (Exception e) {
				// 撮影画像の保存できなかった場合
				Log.e("ERROR", e.toString());
				Toast.makeText(context, cmndef.CAMERA_ERROR_MSG1,
						Toast.LENGTH_LONG).show();

				if (camera != null) {
					camera.release();
					camera = null;
				}
				// 処理を終了する
				return;
			}

			// TODO 撮影画像の位置情報がノベル位置情報と一致しなければ、Toast表示、画像削除。
			Log.d("DEBUG", "撮影画像を背景としNovelIntroActivity起動");
			double n_longitude = sss.getLongitude();// 経度(ノベル位置情報)
			double n_latitude = sss.getLatitude(); 	// 緯度(ノベル位置情報)
			
			double p_longitude = longitude; // 経度(撮影画像の位置情報)
			double p_latitude = latitude; 	// 緯度(撮影画像の位置情報)
			
			Log.d("DEBUG", "経度(ノベル位置情報)=" + n_longitude);
			Log.d("DEBUG", "緯度(ノベル位置情報)=" + n_latitude);
			Log.d("DEBUG", "経度(撮影画像の位置情報)=" + p_longitude);
			Log.d("DEBUG", "緯度(撮影画像の位置情報)=" + p_latitude);
			
			if (p_longitude == 0.0 || p_latitude == 0.0) {
				// 撮影時における位置情報(経度・緯度)が取得できていない場合
				// 位置情報の一致に失敗したため、Toast表示してユーザーに促す
	        	Toast.makeText(context,cmndef.CAMERA_ERROR_MSG4, 
	        			Toast.LENGTH_LONG).show();
	        	
	        	// TODO 撮影画像は不要なため、削除
	        	
	        	// プレビュー再開始
	    		if (mCam != null) {
	    			mCam.startPreview();
	    		}
	    		
			} else {
				// 経度・緯度が一致した場合
				
			}
			
			
			Intent i = new Intent(context, NovelIntroActivity.class);
			i.putExtra("StageSelectState", sss);

			// 背景画像用のパスをセット
			i.putExtra("back_ground", SDCARD_FOLDER + datName);

			// 外部Activityを自分のActivityスタックとは別に立てる
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(i);
		}
	};

	/** シャッターが押されたときに呼ばれるコールバック */
	private ShutterCallback mShutterListener = new ShutterCallback() {
		public void onShutter() {
			Log.d("DEBUG", "onShutter Start");
		}
	};

	/**
	 * オートフォーカス完了時のコールバック
	 */
//	private Camera.AutoFocusCallback mAutoFocusListener = new Camera.AutoFocusCallback() {
//		public void onAutoFocus(boolean success, Camera mCam) {
//			Log.d("DEBUG", "onAutoFocus　Start");
//
//			// タッチダウン時、撮影データを取得。
//			// 撮影データはPictureCallback.onPictureTaken()にbyte列で渡される
//			mCam.takePicture(mShutterListener, null, mPictureListener);
//
//		}
//	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("DEBUG", "onTouchEvent Start");
		// 撮影中の2度押し禁止用フラグ設定
		if (notouch_flg) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/** タッチダウン時の処理 */
			try {
				Log.d("DEBUG", "タッチダウン開始");

				// 撮影中の2度押し禁止用フラグ
				notouch_flg = true;

				// オートフォーカス(完了時にコールバックにて撮影処理実行)
				// mCam.autoFocus(mAutoFocusListener);

				// 撮影
				mCam.takePicture(mShutterListener, null, mPictureListener);
				
				// 撮影完了したらフラグを戻す
				notouch_flg = false;

			} catch (Exception e) {
				Log.d("DEBUG", e.toString());
			}
			break;

		}
		return true;

	}

	/**
	 * 撮影画像を保存する。
	 * 
	 * @param 画像ファイル名
	 * @param 撮影データ
	 * 
	 * */
	private void savePhotoData(String datName, byte[] data) throws Exception {

		FileOutputStream outStream = null;

		try {
			// SDカードへ撮影画像作成・保存
			outStream = new FileOutputStream(SDCARD_FOLDER + datName);
			outStream.write(data);
			outStream.close();
		} catch (Exception e) {
			if (outStream != null) {
				outStream.close();
			}
			// 例外をスローする
			throw e;
		}

		ContentValues cv = new ContentValues();
		try {
			// 追加した画像をギャラリーに表示
			ContentResolver contentResolver = context.getContentResolver();
			cv.put(Images.Media.TITLE, datName);
			cv.put(Images.Media.MIME_TYPE, "image/jpeg");
			cv.put("_data", SDCARD_FOLDER + datName);
			contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, cv);

		} catch (Exception e) {
			// ファイルの保存に失敗した場合
			Log.w("WARN", e.toString());
			Toast.makeText(context, cmndef.CAMERA_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * SurfaceHolder が変化したときのイベント
	 * 
	 * @param holder
	 * @param フォーマット
	 * @param 幅
	 * @param 高さ
	 * 
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		Log.d("DEBUG", "surfaceChanged called.");
		// 画面回転に対応する場合は、ここでプレビューを停止し、
		// 回転による処理を実施、再度プレビューを開始する。

		// プレビュー開始
//		if (mCam != null) {
//			mCam.startPreview();
//		}

	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
	}
}