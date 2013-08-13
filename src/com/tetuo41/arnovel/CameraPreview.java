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

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** カメラオブジェクト */
	private Camera mCam;

	// 参考URL http://androidguide.nomaki.jp/html/device/camera/camIntro.html
	// TODO オートフォーカス機能
	
	protected Context context;
	private static final String SDCARD_FOLDER = Environment
			.getExternalStorageDirectory().getPath() + "/locanovel/";

	/**
	 * コンストラクタ
	 */
	public CameraPreview(Context context, Camera cam) {
		super(context);
		this.context = context;
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
		mCam = cam;

		// サーフェスホルダーの取得とコールバック通知先の設定
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 保存用フォルダ作成(なければ)
		File dirs = new File(SDCARD_FOLDER);
		if (!dirs.exists()) {
			dirs.mkdir();
		}
	}

	/**
	 * SurfaceView 生成
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
		        	Toast.makeText(context, 
		        			"カメラの起動に失敗しました", Toast.LENGTH_LONG).show();
		        }
			}
			// カメラインスタンスに、画像表示先を設定
			mCam.setPreviewDisplay(holder);

			// プレビュー開始
			mCam.startPreview();

		} catch (IOException e) {
			// 例外発生時
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * SurfaceView 破棄時 
	 * (プレビュー画面からステージセレクト画面へBackした場合)
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCam.release();
		mCam = null;
	}

	// JPEGイメージ生成後に呼ばれるコールバック
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
			}
			
			// TODO 撮影画像の位置情報がノベル位置情報と一致しなければ、画像削除。
			// TODO Toast表示、ノベルデータを受け渡し
			// 撮影画像を背景としNovelActivity起動
			Log.d("DEBUG", "撮影画像を背景としNovelIntroActivity起動");
			Intent i = new Intent(context, NovelIntroActivity.class);
			// i.putExtra("NovelIntroActivity", sss);
			
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
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// タッチ押下時、撮影データを取得。
			// 撮影データはPictureCallback.onPictureTaken()にbyte列で渡される
			mCam.takePicture(mShutterListener, null, mPictureListener);

		}
		return true;
	}

	/**
	 * 撮影画像を保存する。
	 * @param datName 画像ファイル名
	 * @param data 撮影データ
	 * 
	 * */
	private void savePhotoData(String datName, byte[] data) throws Exception {

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

		ContentValues cv = new ContentValues();
		try {
			// 追加した画像をギャラリーに表示
			ContentResolver contentResolver = context.getContentResolver();
			cv.put(Images.Media.TITLE, datName);
			cv.put(Images.Media.MIME_TYPE, "image/jpeg");
			cv.put("_data", SDCARD_FOLDER + datName);
			contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, cv);

		} catch (Exception e) {
			Log.w("WARN", e.toString());
			Toast.makeText(context, cmndef.CAMERA_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		// カメラプレビュー
		// mCam.startPreview();

	}

	/**
	 * SurfaceHolder が変化したときのイベント
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("DEBUG", "surfaceChanged called.");
		// 画面回転に対応する場合は、ここでプレビューを停止し、
		// 回転による処理を実施、再度プレビューを開始する。

	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
	}
}