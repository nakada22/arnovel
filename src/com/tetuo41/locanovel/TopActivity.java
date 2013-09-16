package com.tetuo41.locanovel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.db.Dao;
import com.tetuo41.locanovel.db.DbConstants;
import com.tetuo41.locanovel.stageselect.StageSelectActivity;
import com.tetuo41.locanovel.stamplog.StampLogActivity;

/**
 * トップ画面を表示するクラスです。
 * 
 * @author　HackathonG
 * @version 1.0
 */
public class TopActivity extends Activity implements OnClickListener,
		OnCompletionListener {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** 外部サーバーにあるCSVファイル、ステージ画像のURL */
	private String novel_url;
	private String stage_url;
	private String stage_img_url;

	/** 初期データ登録中に表示するプログレスダイアログ */
	private ProgressDialog pd;

	/** ConnectivityManagerのインスタンス生成 */
	private ConnectivityManager cm;
	private NetworkInfo nInfo;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int mSounds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top);

		/** 音声再生処理 */
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();

		// TODO? 初回アプリインストール時の初期登録を未実施の状態でボタンを押すとエラーが出る

		// ネットワーク通信が可能な状態か確認
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		nInfo = cm.getActiveNetworkInfo();
		if (nInfo == null) {
			// インターネットに接続できなかった場合
			Toast.makeText(this, cmndef.CMN_ERROR_MSG1, Toast.LENGTH_SHORT)
					.show();

		} else {
			// 非同期で外部サーバよりCSVファイル読込
			// 初期データDB登録、ステージ画像ファイル保存
			Init init = new Init(this);
			init.execute();

		}

		// ClickListener登録
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.record).setOnClickListener(this);

	}

	/**
	 * コンストラクタ
	 */
	public TopActivity() {
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
		this.novel_url = "http://" + cmndef.S_HOST_NAME + cmndef.DATA_DIR
				+ cmndef.NOVEL_FILE;
		this.stage_url = "http://" + cmndef.S_HOST_NAME + cmndef.DATA_DIR
				+ cmndef.STAGE_FILE;
		this.stage_img_url = "http://" + cmndef.S_HOST_NAME + cmndef.IMAGE_DIR;
	}

	/**
	 * ボタンクリック時の処理を記述する。
	 * 
	 * @param View
	 *            ボタンオブジェクト
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.start:
			// STARTボタンクリック時
			StartClick();

			break;
		case R.id.record:
			// RECORDボタンクリック時
			RecordClick();

			break;
		default:
			break;
		}
	}

	/**
	 * STARTボタンクリック時の処理を記述する。
	 */
	private void StartClick() {

		try {

			if (mp.isPlaying()) {
				Log.d("DEBUG", "再生中であれば  StartClick");
				// 再生中であれば
				mp.pause();
			}

			// ボタンクリック時の音再生
			mSoundPool.play(mSounds, 1.0F, 1.0F, 1, 0, 1.0F);

			// STARTボタンクリック時、スタンプログ画面へ遷移
			Intent i = new Intent(this, StageSelectActivity.class);
			startActivity(i);

		} catch (ActivityNotFoundException e) {
			// ステージセレクト画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);

			// 処理を終了する
			return;
		} catch (Exception e) {
			// ステージセレクト画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);

			// 処理を終了する
			return;
		}
	}

	/**
	 * RECORDボタンクリック時の処理を記述する。
	 */
	private void RecordClick() {

		try {
			if (mp.isPlaying()) {
				Log.d("DEBUG", "再生中であれば  RecordClick");
				// 再生中であれば
				mp.pause();
			}
			// ボタンクリック時の音再生
			mSoundPool.play(mSounds, 1.0F, 1.0F, 1, 0, 1.0F);

			// RECORDボタンクリック時、スタンプログ画面へ遷移
			Intent i = new Intent(getApplicationContext(),
					StampLogActivity.class);
			startActivity(i);

		} catch (ActivityNotFoundException e) {
			// スタンプログ画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG2);

			// 処理を終了する
			return;
		} catch (Exception e) {
			// スタンプログ画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG2);

			// 処理を終了する
			return;
		}
	}

	@Override
	protected void onResume() {
		Log.d("DEBUG", "TopAct onResume　START");
		super.onResume();
		
		// アプリ起動時もここを通る
		try {
			if (!mp.isPlaying()) {
				// 音声再生
				mp.seekTo(0);
				mp.start();

				// 再生終了を検出する
				mp.setOnCompletionListener(this);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// 音声の再生に失敗した場合
			Toast.makeText(getApplicationContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}
		
		// ボタンクリック時に呼び出す音をロードしておく
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mSounds = mSoundPool.load(getApplicationContext(), R.raw.buttom, 1);
	}

	@Override
	protected void onPause() {
		// 他のアクティビティに遷移する場合

		Log.d("DEBUG", "TopAct onPause　START");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "TopAct onDestroy　START");
		super.onDestroy();

		// 音をリリース
		mSoundPool.release();
		if (mp != null) {
			mp.release();
			mp = null;
		}
	}

	/**
	 * 再生が完了したときに呼ばれ、再生をループさせる
	 * 
	 * */
	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d("DEBUG", "onCompletion　START");
		// 再生箇所を最初に戻す
		mp.seekTo(0);
		// 音声を再生させる
		mp.start();

	}

	/**
	 * アラートダイアログを表示する
	 * 
	 * @param タイトル
	 * @param 表示するメッセージ
	 * 
	 * */
	private void AlertDialogView(String title, String message) {

		// アラートダイアログで警告を表示
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(message);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 処理なし
			}
		});
		adb.show();
	}

	/**
	 * 内部初期処理クラス 外部サーバーよりCSVファイル読込、DBへ初期データ登録、画像ファイル保存
	 * 
	 * */
	class Init extends AsyncTask<String, Void, Boolean> {

		private Context context;

		public Init(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// 初期データ登録前の事前処理(ローディングダイアログ表示)
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.setMessage(cmndef.TOP_LOADING_MSG);
			pd.show();
		}

		protected Boolean doInBackground(String... params) {

			// 初期データ登録時は、排他で登録処理を行う
			synchronized (getApplicationContext()) {
				try {
					/** DBアクセスクラスオブジェクト */
					Dao dao = new Dao(getApplicationContext());

					/** 1.ノベルファイル読込・取得 */
					Map<String, List<String>> novel_data = cmnutil.ReadCsvFile(
							novel_url, getApplicationContext());
					Log.d("DEBUG", "ノベルファイル読込・取得完了");
					Log.d("DEBUG", novel_data.toString());

					Set novel_keySet = novel_data.keySet();
					Iterator novel_keyIte = novel_keySet.iterator();
					// Log.d("DEBUG", novel_keySet.toString()); // [1, 2]

					/** 　1-1.ノベルデータ登録 */
					while (novel_keyIte.hasNext()) {
						// ノベルID
						String key = novel_keyIte.next().toString();

						// ノベルデータリスト
						List<String> value = novel_data.get(key);

						// ノベルデータの初期データDB登録
						dao.InitDataInsert(key, value, DbConstants.TABLE2);
					}

					/** 2.ステージデータファイル読み込み */
					Map<String, List<String>> stage_data = cmnutil.ReadCsvFile(
							stage_url, getApplicationContext());
					Log.d("DEBUG", "ステージデータファイル読込・取得完了");
					Log.d("DEBUG", stage_data.toString());

					Set stage_keySet = stage_data.keySet();
					Iterator stage_keyIte = stage_keySet.iterator();

					/** 2-1 ステージデータをDBに収録 */
					while (stage_keyIte.hasNext()) {
						// ステージID
						String key = stage_keyIte.next().toString();

						// ステージセレクトデータリスト
						List<String> value = stage_data.get(key);

						// ステージセレクトデータの初期データDB登録
						dao.InitDataInsert(key, value, DbConstants.TABLE3);

						/** 2-2.ステージ画像ファイル保存 */
						stage_img_url = "http://" + cmndef.S_HOST_NAME
								+ cmndef.IMAGE_DIR;
						stage_img_url = stage_img_url
								+ ("stage" + key + ".png");

						Log.d("DEBUG", stage_img_url);
						URL imageUrl = new URL(stage_img_url);
						InputStream imageIs;

						// ステージ画像読み込み
						imageIs = imageUrl.openStream();
						Bitmap bm = BitmapFactory.decodeStream(imageIs);
						Log.d("DEBUG", "TopAct 画像読み込み完了");

						OutputStream os = openFileOutput(
								"stage" + key + ".png", MODE_PRIVATE);
						bm.compress(Bitmap.CompressFormat.PNG, 100, os);
						os.close();

					}

					/** 3.スタンプラリーマスタの初期データ登録 */
					dao.InitDataInsert(null, null, DbConstants.TABLE1);
					return true;

				} catch (MalformedURLException e) {
					// URLが間違っている場合
					Log.e("ERROR", cmndef.TOP_ERROR_MSG3 + e.toString());

					// ダイアログを終了させる
					pd.dismiss();
					return false;

				} catch (IOException e) {
					// CSVファイル読み込み失敗したとき
					Log.e("ERROR", cmndef.TOP_ERROR_MSG3 + e.toString());
					Toast.makeText(getApplicationContext(),
							cmndef.TOP_ERROR_MSG3, Toast.LENGTH_SHORT).show();

					// ダイアログを終了させる
					pd.dismiss();
					return false;

				} catch (RuntimeException e) {
					// インターネットに接続できなかった場合
					Log.e("ERROR", cmndef.CMN_ERROR_MSG1 + e.toString());
					Toast.makeText(getApplicationContext(),
							cmndef.CMN_ERROR_MSG1, Toast.LENGTH_SHORT).show();

					// ダイアログを終了させる
					pd.dismiss();
					return false;

				}
			}

		}

		// サーバー通信終了処理(メインスレッドで実行する処理)
		@Override
		protected void onPostExecute(Boolean result) {
			// 処理が終わったらロード中のダイアログを終了させる
			pd.dismiss();

			// ダイアログ表示が終わったら、音声を再生する
			try {
				mp = MediaPlayer.create(context, R.raw.top);

				// サウンド音量設定0.0から1.0で設定
				mp.setVolume(0.5f, 0.5f);
				mp.start();

			} catch (IllegalStateException e) {
				e.printStackTrace();
				// 音声の再生に失敗した場合
				Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
						Toast.LENGTH_SHORT).show();

			}
		}

	}

}
