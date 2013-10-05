package com.tetuo41.locanovel.camera;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.db.Dao;
import com.tetuo41.locanovel.stageselect.StageSelectActivity;
import com.tetuo41.locanovel.stageselect.StageSelectState;
import com.tetuo41.locanovel.stamplog.StampLogActivity;

/**
 * カメラプレビュー起動クラス
 * 
 * @author HackathonG
 * @version 1.0
 */
public class CameraPreviewActivity extends Activity implements
		LocationListener, OnCompletionListener, OnClickListener {

	/** カメラインスタンス */
	private Camera mCam = null;

	/** 共通クラスオブジェクト */
	private CommonDef cmndef;

	/** GPS(ネットワーク)位置情報取得機能に必要なオブジェクト */
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String bestProvider;
	private double longitude;
	private double latitude;
	private CameraPreview mCamPreview = null;

	/** MediaPlayerインスタンス生成 */
	private MediaPlayer mp;

	/** ステージセレクト画面からのデータオブジェクト */
	private StageSelectState sss;

	/** タイマーオブジェクト */
	private Timer mTimer;

	/** ハンドラオブジェクト */
	private Handler Handler = new Handler();

	/** Daoオブジェクト生成 */
	private Dao dao;

	/** Notifiオブジェクト生成 */
	private Notification notifi;
	private NotificationManager nm;

	/** ノーティフィケーションの識別子 */
	private int identifier = 0;

	/** クリックイベント実行可否フラグ */
	private boolean ClickEventFlg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera_preview);

		// Daoインスタンス生成
		dao = new Dao(getBaseContext());

		// Notificationインスタンス生成
		notifi = new Notification();

		// NotificationManager取得
		nm = (NotificationManager) getSystemService(getBaseContext().NOTIFICATION_SERVICE);

		// ステージ選択画面からのデータ取得
		Intent getintent = getIntent();
		sss = (StageSelectState) getintent
				.getSerializableExtra("StageSelectState");

		// 音声再生処理
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
		
		try {
			mp = MediaPlayer.create(getBaseContext(), R.raw.camera_preview);

			// サウンド音量設定0.0から1.0で設定
			mp.setVolume(0.7f, 0.7f);
			
			// 1.5秒後に音を再生させる
			new CountDownTimer(1500, 100) {
				@Override
				public void onTick(long millisUntilFinished) {}
				public void onFinish() {
					// 音を再生させる
					mp.start();
				}
			}.start();
			
			// 再生終了を検出する
			mp.setOnCompletionListener(this);

		} catch (IllegalStateException e) {
			e.printStackTrace();
			// 音声の再生に失敗した場合
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		// Timerを使って、1000ミリ秒間隔で位置情報を取得・更新
		mTimer = new Timer(true);
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Handler.post(new Runnable() {
					public void run() {
						// 位置情報を取得・更新する処理
						initLocationService();

					}
				});
			}
		}, 100, 1000);
	}

	/**
	 * コンストラクタ
	 */
	public CameraPreviewActivity() {
		cmndef = new CommonDef();
	}

	/**
	 * ボタンクリック時の処理を記述する。
	 * 
	 * @param View ボタンオブジェクト
	 */
	@Override
	public void onClick(View v) {
		// クリックイベントが許可されていなければ実行しない
	    if (!ClickEventFlg) return;
	    
	    // クリックイベントを禁止する
		ClickEventFlg = false;
		
		switch (v.getId()) {
		case 1:
			try {
				// 「撮影」ボタンクリック時処理
				mCam.takePicture(mCamPreview.mShutterListener, 
						null, mCamPreview.mPictureListener);
				
				// 1.5秒間待たせる
				new CountDownTimer(1500, 100) {
					@Override
					public void onTick(long millisUntilFinished) {}
					public void onFinish() {
						// クリックイベントを許可する
						ClickEventFlg = true;
					}
				}.start();
				
			} catch (Exception e) {
				Log.d("DEBUG", e.toString());
			}
			break;
		default:
			break;
		}
	}
	/**
	 * カメラオープン処理を行う
	 */
	private void CameraOpen() {
		try {
			if (mCam == null) {
				// カメラを開く
				mCam = Camera.open();
				// プレビューを90度回転
				mCam.setDisplayOrientation(90);

			}

			// marker画像読み込み
			ImageView marker = new ImageView(this);
			marker.setImageResource(R.drawable.marker);
			marker.setScaleType(ImageView.ScaleType.FIT_CENTER);
			
			// 「撮影する」ボタンを読み込み
			Button photo_btn = new Button(this);
			photo_btn.setId(1);
			photo_btn.setTextColor(Color.WHITE);
			photo_btn.setTextSize(16);
			photo_btn.setBackgroundResource(R.drawable.read_comp);
			photo_btn.setText(getString(R.string.photography));
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(300, 100);
			lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
			lp.setMargins(0, 300, 0, 0);
			photo_btn.setLayoutParams(lp);
			
			// カメラプレビュー起動(データもセット)
			mCamPreview = new CameraPreview(getApplicationContext(), mCam, sss,
					mTimer, mp);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mCamPreview);
			preview.addView(photo_btn, lp);
			preview.addView(marker, createParam(150, 150));
			
			// 「撮影する」ボタンClickListener登録
			preview.findViewById(1).setOnClickListener(this);
			
		} catch (Exception e) {
			// エラー発生時
			Log.w("WARN", e.toString());
			Toast.makeText(getApplicationContext(), cmndef.CAMERA_ERROR_MSG3,
					Toast.LENGTH_SHORT).show();

			// アクティビティを終了する
			this.finish();
		}
	}

	private FrameLayout.LayoutParams createParam(int w, int h) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
		params.gravity = Gravity.CENTER_VERTICAL | 
				Gravity.CENTER_HORIZONTAL;
		return params;
	}

	/**
	 * 位置情報を取得するための初期処理
	 * 
	 * */
	protected void initLocationService() {
		Log.d("DEBUG", "initLocationService Start");

		// LocationManagerインスタンスを取得
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// 位置情報機能非搭載端末の場合
		if (locationManager == null) {
			Log.d("DEBUG", "位置情報機能非搭載端末の場合");
			return;
		}

		// プロバイダ選択時の位置情報サービスの基準を指定
		Criteria criteria = new Criteria();
		// 位置情報の精度は指定しない
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 消費電力の条件を指定しない
		// criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setBearingRequired(false); // 方位不要
		criteria.setSpeedRequired(false); // 速度不要
		criteria.setAltitudeRequired(false);// 高度不要

		bestProvider = locationManager.getBestProvider(criteria, true);
		if (bestProvider == null) {
			Log.d("DEBUG", "位置情報が有効になっていない場合");

			// 位置情報が有効になっていない場合
			stopLocationService();
			return;
		}

		// 位置情報の取得を開始
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Log.d("DEBUG", "onLocationChanged Start");

				// 位置情報が更新した時
				Log.d("DEBUG", "位置情報が更新されました");

				// 更新された経度・緯度
				longitude = location.getLongitude();
				latitude = location.getLatitude();

				Log.d("----------", "----------");
				Log.d("Longitude（経度）", String.valueOf(longitude));
				Log.d("Latitude（緯度）", String.valueOf(latitude));

				setLocation(longitude, latitude);
			}

			@Override
			public void onProviderDisabled(final String provider) {
			}

			@Override
			public void onProviderEnabled(final String provider) {
			}

			@Override
			public void onStatusChanged(final String provider,
					final int status, final Bundle extras) {
			}
		};
		locationManager.requestLocationUpdates(bestProvider, 1000, 1,
				locationListener);

		Log.d("DEBUG", "initLocationService End");

	}

	/**
	 * ロケーションサービスを終了させたい場合の処理
	 * 
	 * */
	private void stopLocationService() {
		if (locationManager != null) {
			if (locationListener != null) {
				locationManager.removeUpdates(locationListener);
				locationListener = null;
			}
			locationManager = null;
		}
	}

	/**
	 * ロケーションサービスをセットする場合の処理
	 * 
	 * @param longitude
	 *            更新された経度
	 * @param latitude
	 *            更新された緯度
	 * */
	private void setLocation(double longitude, double latitude) {
		stopLocationService();

		// ここに位置情報が取得できた場合の処理を記述
		try {

			// 更新された位置情報を一時的にDB(位置情報マスタ)へ保存し
			// 撮影時への位置情報のカメラプレビューへ受け渡す
			if (dao.RegisteUpdateLocate(longitude, latitude)) {

				// 更新された位置情報をDBへ登録した場合、ステータスバーに更新を通知
				// アイコン・メッセージ・現在時刻を設定
				notifi.icon = R.drawable.ic_locanovel;
				notifi.tickerText = cmndef.CAMERA_INFO_MSG3;
				notifi.when = System.currentTimeMillis();

				// PendingIntentの生成(詳細クリック時、ステージセレクト画面遷移)
				Intent i = new Intent(getApplicationContext(),
						StageSelectActivity.class);
				PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

				// 事前にノーティフィケーション通知削除しておく
				if (nm != null) {
					for (int j = 0; j <= identifier; j++) {
						nm.cancel(j);
					}
				}

				// ノーティフィケーションの詳細設定
				notifi.setLatestEventInfo(getApplicationContext(),
						cmndef.CAMERA_INFO_MSG2, cmndef.CAMERA_INFO_MSG3, pi);

				// Notificationの通知
				nm.notify(identifier, notifi);

				// ノーティフィケーションの識別子をインクリメント
				identifier++;

			}
		} catch (Exception e) {
			// エラー発生時
			Log.w("WARN", e.toString());
		}
	}

	/**
	 * 位置情報が更新した時にコールされる
	 * 
	 * */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * アクティビティが「停止」の状態
	 * */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("DEBUG", "CameraPreviewAct onPause() Start");

		// ノーティフィケーション通知削除
		if (nm != null) {
			for (int i = 0; i <= identifier; i++) {
				nm.cancel(i);
			}
		}

		// NovelIntroActivity起動時にもよばれる。
		// シャッター押下時にも呼ばれる。
		if (mCam != null) {
			mCam.release();
			mCam = null;
		}
		
		// リスナーの削除
		if (locationManager != null) {
			// 位置情報の更新の取得が不要になった場合
			locationManager.removeUpdates(this);
		}
		
		// タイマーキャンセル
		mTimer.cancel();

	}

	/**
	 * アクティビティが「実行中」の状態
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("DEBUG", "CameraPreviewAct onResume() Start");

		// 位置情報マスタを初期化
		dao.DeleteLocateMaster();

		// カメラオープン
		CameraOpen();

		// 位置情報の更新を取得
		if (locationManager != null) {
			Log.d("DEBUG", "位置情報の更新を取得");

			locationManager.requestLocationUpdates(bestProvider, 0, 0,
					locationListener);
		}
		// クリックイベントを許可する
		ClickEventFlg = true;
		
	}

	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "CameraPreviewAct onDestroy　START");
		super.onDestroy();

		if (mp != null) {
			mp.release();
			mp = null;
		}

		// ノーティフィケーション通知削除
		if (nm != null) {
			for (int i = 0; i <= identifier; i++) {
				nm.cancel(i);
			}
			nm = null;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d("DEBUG", "CameraPreviewAct onCompletion　START");

		// 再生箇所を最初に戻す
		mp.seekTo(0);
		// 音声を再生させる
		mp.start();
	}

}
