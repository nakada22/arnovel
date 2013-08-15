package com.tetuo41.arnovel;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tetuo41.arnovel.common.CommonDef;
import com.tetuo41.arnovel.common.CommonUtil;

/**
 * カメラプレビュー起動クラス
 * @author HackathonG
 * @version 1.0
 */
public class CameraPreviewActivity extends Activity 
	implements LocationListener {
	// ■エミュレータで位置情報を取得する方法
	// 最後にエミュレータで位置情報を擬似的に受け取る方法を説明します。
	// AndroidのSDKにはDDMS(Dalvik Debug Monitor Server)というデバックツールが付属しています。
	// (EclipseのAndroidプラグインを使用している場合は、パースペクティブとして開くことが出来ます。)
	//
	// このDDMSの「Location Controls」を使用すると、エミュレータに位置情報を送信することが出来ます。
	
	/** カメラインスタンス */
    private Camera mCam = null;
    
    /** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;
	
	/** GPS(ネットワーク)位置情報取得機能に必要なオブジェクト */
	protected LocationManager locationManager;
	private LocationListener locationListener;
	protected String bestProvider;
	public double longitude;
	public double latitude;
	
	CameraPreview mCamPreview = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_preview);
        
        // 位置情報取得機能を起動
        initLocationService();
    }
	
	/** 
     * コンストラクタ
     */
    public CameraPreviewActivity() {
    	cmnutil = new CommonUtil();
    	cmndef = new CommonDef();
    }
    
    /** 
     * カメラオープン処理を行う
     */
    private void CameraOpen() {
    	try {
    		if (mCam == null) {
    			// カメラを開く
                mCam = Camera.open();
    		}
    		
    		/** ステージ選択画面からのデータ取得し、CameraPreview経由でNovelIntro用にセット */
            Intent getintent = getIntent();
        	StageSelectState sss = (StageSelectState) getintent.
        			getSerializableExtra("StageSelectState");
        	
    		// カメラプレビュー起動(データもセット)
    		mCamPreview = new CameraPreview(this, mCam, sss, longitude, latitude);
            FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
            preview.addView(mCamPreview);
            
        } catch (Exception e) {
        	// エラー発生時
            Log.w("WARN", e.toString());
        	Toast.makeText(getApplicationContext(), 
        			cmndef.CAMERA_ERROR_MSG3, Toast.LENGTH_LONG).show();
        	
        	// アクティビティを終了する
        	this.finish();
        }
    	
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
        criteria.setBearingRequired(false);	// 方位不要
		criteria.setSpeedRequired(false);	// 速度不要
		criteria.setAltitudeRequired(false);// 高度不要
		
        bestProvider = locationManager.getBestProvider(criteria, true);
        if (bestProvider == null) {
        	Log.d("DEBUG", "位置情報が有効になっていない場合");
        	
        	// 位置情報が有効になっていない場合
        	stopLocationService();
			return;
        }
        
        // 該当のプロバイダから最後に取得した位置情報(キャッシュ)を取得
        Location lastKnownLocation = 
        		locationManager.getLastKnownLocation(bestProvider);
        if (lastKnownLocation != null){
        	Log.d("DEBUG", "lastKnownLocationがNullではない場合");

        	// 経度・緯度
        	longitude = lastKnownLocation.getLongitude();
        	latitude = lastKnownLocation.getLatitude();
        	
        	Toast.makeText(getApplicationContext(), "Longitude（経度）="
        			+ String.valueOf(longitude), Toast.LENGTH_LONG).show();
        	Toast.makeText(getApplicationContext(), "Longitude（緯度）="
                	+ String.valueOf(latitude), Toast.LENGTH_LONG).show();
            		
        	Log.d("----------", "----------");
			Log.d("Longitude（経度）", String.valueOf(longitude));
			Log.d("Latitude（緯度）", String.valueOf(latitude));
				
        	setLocation(lastKnownLocation);
			return;
        } else {
        	Log.d("DEBUG", "lastKnownLocationがNullの場合");
        }
        
        // 位置情報の取得を開始
 		locationListener = new LocationListener() {
 			@Override
 			public void onLocationChanged(final Location location) {
 				// 位置情報が更新した時
 				Log.d("DEBUG", "onLocationChanged２ Start");
 				
 				// 経度・緯度
 	        	longitude = location.getLongitude();
 	        	latitude = location.getLatitude();
 	        	
 	        	Toast.makeText(getApplicationContext(), "Longitude（経度）="
 	        			+ String.valueOf(longitude), Toast.LENGTH_LONG).show();
 	        	Toast.makeText(getApplicationContext(), "Longitude（緯度）="
 	                	+ String.valueOf(latitude), Toast.LENGTH_LONG).show();
 	            		
 	        	Log.d("----------", "----------");
 				Log.d("Longitude（経度）", String.valueOf(longitude));
 				Log.d("Latitude（緯度）", String.valueOf(latitude));
 				
 				setLocation(location);
 			}
 			@Override public void onProviderDisabled(final String provider) {}
 			@Override public void onProviderEnabled(final String provider) {}
 			@Override public void onStatusChanged(
 					final String provider, final int status, final Bundle extras) {}
 		};
 		locationManager.requestLocationUpdates(
 				bestProvider, 60000, 0, locationListener);
        
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
     * */
    private void setLocation(Location location) {
		stopLocationService();
		// ここに位置情報が取得できた場合の処理を記述
		Log.d("DEBUG", "setLocation Start(位置情報が取得できた場合の処理)");
        
	}
    
    /**
     * 位置情報が更新した時にコールされる
     * @param ロケーション
     * */
    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onProviderDisabled(String provider) {}

    /**
     * 位置情報のステータスが更新されるとコールされる
     * 
     * */
    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(
      String provider, int status, Bundle extras) {}
    
    /**
	 * アクティビティが「停止」の状態
	 * */
	@Override
    protected void onPause() {
		super.onPause();
		Log.d("DEBUG", "onPause() Start");
        // NovelIntroActivity起動時にもよばれる。
		
        // リスナーの削除
        if (locationManager != null) {
        	// 位置情報の更新の取得が不要になった場合
            locationManager.removeUpdates(this);
        }
        
        Log.d("DEBUG", "onPause() End");
    }
	
	/**
	 * アクティビティが「実行中」の状態
	 * */
	@Override
	protected void onResume(){
		super.onResume();
		Log.d("DEBUG", "onResume() Start");
		
        // カメラオープン
        CameraOpen();
        
		// 位置情報の更新を取得
		if (locationManager != null) {
			Log.d("DEBUG", "位置情報の更新を取得");
			
			locationManager.requestLocationUpdates(
					bestProvider, 0, 0, locationListener);
		}
		Log.d("DEBUG", "onResume() End");
	}
	
}

