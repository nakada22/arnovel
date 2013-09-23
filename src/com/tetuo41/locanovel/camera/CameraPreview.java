package com.tetuo41.locanovel.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.db.Dao;
import com.tetuo41.locanovel.novel.NovelIntroActivity;
import com.tetuo41.locanovel.stageselect.StageSelectState;

/**
 * カメラプレビューの処理を行うクラス
 * 
 * @author HackathonG
 * @version 1.0
 */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PictureCallback {

	/** 共通クラスオブジェクト */
	private CommonDef cmndef;

	/** 経度・緯度情報 */
	private double longitude;
	private double latitude;

	/** カメラオブジェクト */
	private Camera mCam;

	/** コンテキスト */
	private Context context;

	/** MediaPlayerインスタンス生成 */
	private MediaPlayer mp;

	/** ステージセレクト画面から送られてきたデータ */
	private StageSelectState sss;

	/** タイマーオブジェクト */
	private Timer mTimer;

	/** Daoオブジェクト生成 */
	private Dao dao;
	
	/** 画面タッチ二度押し禁止フラグ */
	// private boolean notouch_flg = false;

	/**
	 * コンストラクタ
	 */
	public CameraPreview(Context context, Camera cam, StageSelectState sss,
			Timer mTimer, MediaPlayer mp) {
		super(context);
		this.context = context;
		this.sss = sss;
		this.mTimer = mTimer;
		this.mp = mp;
		
		cmndef = new CommonDef();
		mCam = cam;

		// Daoインスタンス生成
		dao = new Dao(context);
				
		// サーフェスホルダーの取得とコールバック通知先の設定
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// 画像保存用フォルダ作成
		File dirs = new File(cmndef.SDCARD_FOLDER);
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
		Log.d("DEBUG", "surfaceCreated Start");

		try {
			if (mCam == null) {
				mCam = Camera.open();
			}

		} catch (Exception e) {
			// エラー発生時
			Log.w("WARN", e.toString());
		}

		// カメラインスタンスに、画像表示先を設定
		try {
			mCam.setPreviewDisplay(holder);
		} catch (Exception e) {}
	}

	/**
	 * SurfaceView 破棄時 (プレビュー画面からステージセレクト画面へBackした場合)
	 * 
	 * @param holder
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("DEBUG", "surfaceDestroyed Start");
		if (mCam != null) {
			mCam.release();
			mCam = null;
		}
		
		if (mp != null) {
			mp.release();
			mp = null;
		}

	}

	/** JPEGイメージ生成後に呼ばれるコールバック */
	private PictureCallback mPictureListener = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			// 画像ファイル名
			SimpleDateFormat date = new SimpleDateFormat("_yyyyMMddkkmmss");
			String datName = cmndef.APP_NAME + date.format(new Date()) + ".jpg";

			try {
				if (data != null) {
					// 撮影画像保存
					savePhotoData(datName, data);
				}
			} catch (Exception e) {
				// 撮影画像の保存できなかった場合
				Log.e("ERROR", e.toString());
				Toast.makeText(context, cmndef.CAMERA_ERROR_MSG1,
						Toast.LENGTH_SHORT).show();

				if (camera != null) {
					camera.release();
					camera = null;
				}
				// 処理を終了する
				return;
			}

			// 撮影画像の位置情報がノベル位置情報と一致しなければ、Toast表示、画像削除。
			Log.d("DEBUG", "撮影画像を背景としNovelIntroActivity起動");
			double n_longitude = sss.getLongitude();// 経度(ノベル位置情報)
			double n_latitude = sss.getLatitude(); // 緯度(ノベル位置情報)

			// 経度・緯度(撮影画像の位置情報)を位置情報マスタから最新の情報を取得する
			ArrayList<Double> locate_info = dao.GetNewestLocate();
			double p_longitude = locate_info.get(0); // 経度(撮影画像の位置情報)
			double p_latitude = locate_info.get(1); // 緯度(撮影画像の位置情報)

			Log.d("DEBUG", "経度(ノベル位置情報)=" + n_longitude);
			Log.d("DEBUG", "緯度(ノベル位置情報)=" + n_latitude);
			Log.d("DEBUG", "経度(撮影画像の位置情報)=" + p_longitude);
			Log.d("DEBUG", "緯度(撮影画像の位置情報)=" + p_latitude);

			Toast.makeText(context, "経度(ノベル位置情報)=" + n_longitude,
					Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "緯度(ノベル位置情報)=" + n_latitude,
					Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "経度(撮影画像の位置情報)=" + p_longitude,
					Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "緯度(撮影画像の位置情報)=" + p_latitude,
					Toast.LENGTH_SHORT).show();

			if (p_longitude == 0.0 || p_latitude == 0.0) {

				/** 撮影時における位置情報(経度・緯度)が取得できていない場合 */
				// 位置情報の一致に失敗したため、Toast表示してユーザーに促す
				Toast.makeText(context, cmndef.CAMERA_ERROR_MSG4,
						Toast.LENGTH_SHORT).show();

				// 位置情報判定に失敗した撮影画像は、不要なためSDカード内ファイル削除
				File sd_file = new File(cmndef.SDCARD_FOLDER + datName);
				if (sd_file != null) {
					sd_file.delete();
				}

				// 再プレビュー開始
				if (camera != null) {
					camera.startPreview();
				}
				return;

			} else {

				/** 位置情報取得でOKだった場合 */
				// 例:139.752170 < 139.752185 < 139.7522
				// 参考URL
				// http://java-reference.sakuraweb.com/java_number_bigdecimal.html
				// 比較対象の絶対値に対して十分に大きな差による大小比較を行う
				// 誤差範囲の基準(BigDecimalで誤差が出ない演算を行う)
				BigDecimal big_base = new BigDecimal("0.0015");

				// 経度(ノベル位置情報)　例：139.752185
				BigDecimal big_n_longitude = new BigDecimal(n_longitude);
				double left_n_longitude = Math.abs(big_base.subtract(
						big_n_longitude).doubleValue());// 0.0015減算(絶対値)
				double right_n_longitude = Math.abs(big_base.add(
						big_n_longitude).doubleValue());// 0.0015加算(絶対値)

				// 緯度(ノベル位置情報)　例：35.708992
				BigDecimal big_n_latitude = new BigDecimal(n_latitude);
				double left_n_latitude = Math.abs(big_base.subtract(
						big_n_latitude).doubleValue());// 0.0015減算(絶対値)
				double right_n_latitude = Math.abs(big_base.add(big_n_latitude)
						.doubleValue());// 0.0015加算(絶対値)

				Log.d("DEBUG", "BigDecimal後 left_n_longitude="
						+ left_n_longitude);
				Log.d("DEBUG", "BigDecimal後 right_n_longitude="
						+ right_n_longitude);
				Log.d("DEBUG", "BigDecimal後 left_n_latitude=" + left_n_latitude);
				Log.d("DEBUG", "BigDecimal後 right_n_latitude="
						+ right_n_latitude);

				if (p_longitude >= left_n_longitude
						&& p_longitude <= right_n_longitude) {
					Log.d("DEBUG", "経度比較で範囲内であり");

					// 経度比較で範囲内であり
					if (p_latitude >= left_n_latitude
							&& p_latitude <= right_n_latitude) {
						Log.d("DEBUG", "緯度比較で範囲内です");

						try {
							if (mp.isPlaying()) {
								// 再生中であれば
								mp.pause();
							}
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
						
						// 緯度比較で範囲内であればＯＫ，ノベル導入画面へ
						Intent i = new Intent(context, NovelIntroActivity.class);
						i.putExtra("StageSelectState", sss);

						// 背景画像名をノベルマスタに登録
						Dao dao = new Dao(getContext());
						dao.RegisteLocateImg(sss.getStageId(), datName);
						
						// 背景画像用のパスをセット
						i.putExtra("back_ground", cmndef.SDCARD_FOLDER + datName);

						// 外部Activityを自分のActivityスタックとは別に立てる
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getContext().startActivity(i);

						// タイマーキャンセル
						mTimer.cancel();

					} else {
						// 撮影画像がステージ選択した場所ではない場合
						Log.i("INFO", cmndef.CAMERA_INFO_MSG1);
						Toast.makeText(context, cmndef.CAMERA_INFO_MSG1,
								Toast.LENGTH_SHORT).show();

						// 撮影画像削除
						File sd_file = new File(cmndef.SDCARD_FOLDER + datName);
						if (sd_file != null) {
							sd_file.delete();
						}

						// 再プレビュー開始
						if (camera != null) {
							camera.startPreview();
						}
					}
				} else {
					// 撮影画像がステージ選択した場所ではない場合
					Log.i("INFO", cmndef.CAMERA_INFO_MSG1);
					Toast.makeText(context, cmndef.CAMERA_INFO_MSG1,
							Toast.LENGTH_SHORT).show();

					// 撮影画像削除
					File sd_file = new File(cmndef.SDCARD_FOLDER + datName);
					if (sd_file != null) {
						sd_file.delete();
					}

					// 再プレビュー開始
					if (camera != null) {
						camera.startPreview();
					}
				}
			}

			// // 以下の処理はテスト用で、取得できていれば、どんな位置情報でもノベル導入画面に行けるようにしたものである
			// // TODO 一時的にテスト用でコメントアウトを外している。
			// Intent i = new Intent(context, NovelIntroActivity.class);
			// i.putExtra("StageSelectState", sss);
			//
			// // 背景画像用のパスをセット
			// i.putExtra("back_ground", SDCARD_FOLDER + datName);
			//
			// // 外部Activityを自分のActivityスタックとは別に立てる
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// getContext().startActivity(i);

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
	// private Camera.AutoFocusCallback mAutoFocusListener = new
	// Camera.AutoFocusCallback() {
	// public void onAutoFocus(boolean success, Camera mCam) {
	// Log.d("DEBUG", "onAutoFocus　Start");
	//
	// // タッチダウン時、撮影データを取得。
	// // 撮影データはPictureCallback.onPictureTaken()にbyte列で渡される
	// mCam.takePicture(mShutterListener, null, mPictureListener);
	//
	// }
	// };

	/**
	 * 画面タッチ時のコールバック
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("DEBUG", "onTouchEvent Start");
		// 撮影中の2度押し禁止用フラグ設定
		// if (notouch_flg) {
		// return true;
		// }

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/** タッチダウン時の処理 */
			try {
				Log.d("DEBUG", "タッチダウン開始");

				// 撮影中の2度押し禁止用フラグ
				// notouch_flg = true;

				// オートフォーカス(完了時にコールバックにて撮影処理実行)
				// mCam.autoFocus(mAutoFocusListener);

				// 撮影
				mCam.takePicture(mShutterListener, null, mPictureListener);

				// 撮影完了したらフラグを戻す
				// notouch_flg = false;

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
			outStream = new FileOutputStream(cmndef.SDCARD_FOLDER + datName);
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
			cv.put("_data", cmndef.SDCARD_FOLDER + datName);
			contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, cv);

		} catch (Exception e) {
			// ファイルの保存に失敗した場合
			Log.w("WARN", e.toString());
			Toast.makeText(context, cmndef.CAMERA_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

			// 例外をスローする
			throw e;
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
		
		// 正しいカメラのプレビューサイズ(撮影画像)、90°傾きズレ修正
		if (mCam != null) {
			mCam.stopPreview();
			List<Size> previewSizes = mCam.getParameters()
					.getSupportedPreviewSizes();
			Size size = previewSizes.get(0);
			Camera.Parameters parameters = mCam.getParameters();
			parameters.setRotation(90);
			parameters.setPreviewSize(size.width, size.height);
			mCam.setParameters(parameters);
			mCam.startPreview();
		}
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {}
	
}