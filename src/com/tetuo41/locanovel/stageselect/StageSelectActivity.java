package com.tetuo41.locanovel.stageselect;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.camera.CameraPreviewActivity;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.db.Dao;

/**
 * ステージ選択画面
 * 
 * @author HackathonG
 * @version 1.0
 */
public class StageSelectActivity extends Activity implements
		OnCompletionListener {

	/** スクロール中かどうかフラグ */
	// boolean mBusy;

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** 外部サーバーの画像URL */
	private String stage_img_url;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int[] mSounds = new int[1];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stage_select);

		// ステージリスト表示メソッド
		StageSelectView();
	}

	/**
	 * コンストラクタ
	 */
	public StageSelectActivity() {
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
		this.stage_img_url = "http://" + cmndef.S_HOST_NAME + cmndef.IMAGE_DIR;
	}

	/**
	 * ステージリスト表示する
	 * 
	 */
	private void StageSelectView() {

		/** 音声再生処理 */
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();

		// ステージセレクト画面表示時、音声を再生する
		try {
			mp = MediaPlayer.create(getBaseContext(), R.raw.top);

			// サウンド音量設定0.0から1.0で設定
			mp.setVolume(0.5f, 0.5f);
			mp.start();

			// 再生終了を検出する
			mp.setOnCompletionListener(this);

		} catch (IllegalStateException e) {
			e.printStackTrace();
			// 音声の再生に失敗した場合
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		final ArrayList<StageSelectState> dataOfStage = new ArrayList<StageSelectState>();

		/** DBアクセスクラスオブジェクト */
		Dao dao = new Dao(getApplicationContext());

		// ステージセレクトデータ取得
		List<List<String>> stage_data = dao.StageSelctData();
		Log.d("DEBUG", "ステージセレクトデータ読込・取得完了");
		Log.d("DEBUG", stage_data.toString());

		// ノベルデータをDBから取得・セット
		for (int i = 0; i < stage_data.size(); i++) {
			StageSelectState sss = new StageSelectState();
			List<String> data = stage_data.get(i);
			sss.setStageId(Integer.valueOf(data.get(0)));
			sss.setPhoto(LocalImgRead(Integer.valueOf(data.get(0))));
			sss.setStageTitle(data.get(1));
			sss.setOutLine(data.get(2).substring(0, 30) + "・・・");
			sss.setAllOutLine(data.get(2)); // ノベル全文
			sss.setAddress(data.get(3));
			sss.setLongitude(Double.parseDouble(data.get(4))); // 経度
			sss.setLatitude(Double.parseDouble(data.get(5))); // 緯度
			sss.setNovelIntro1(data.get(6));// ノベル導入部分1
			sss.setNovelIntro2(data.get(7));// ノベル導入部分2
			sss.setNovelIntro3(data.get(8));// ノベル導入部分3
			dataOfStage.add(sss);
		}

		StageSelectAdapter ssa = new StageSelectAdapter(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				dataOfStage);
		final ListView lv = (ListView) findViewById(R.id.stagelist);
		lv.setAdapter(ssa);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {

				if (mp.isPlaying()) {
					Log.d("DEBUG", "再生中であれば  onItemClick");
					// 再生中であれば
					mp.pause();
				}
				// ボタンクリック時の音再生
				mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);

				// カメラプレビュー画面へ遷移した場合、カメラプレビューの起動(ノベルデータ保持)
				StageSelectState sss = dataOfStage.get(position);
				Intent i = new Intent(getApplicationContext(),
						CameraPreviewActivity.class);
				i.putExtra("StageSelectState", sss);
				startActivity(i);
			}
		});

		// TODO? 最初の段階で画像データ10件読み込み、スクロール時、10件ずつ読み込めるようにしたい
		// http://sakplus.jp/2011/05/21/stretchlist/

	}

	/**
	 * ローカル保存している画像データ(ステージ画像)をByte配列で返却する
	 * 
	 * @param stage_id
	 *            　ステージID
	 * */
	private byte[] LocalImgRead(int stage_id) {

		/** ビットマップ画像のbyte配列生成 */
		byte[] byteData = null;

		try {
			// 画像データ読出先 /data/data/com.tetuo41.locanovel/files
			FileInputStream fis = openFileInput("stage" + stage_id + ".png");
			BufferedInputStream bis = new BufferedInputStream(fis);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] w = new byte[1024];
			while (bis.read(w) >= 0) {
				baos.write(w, 0, 1024);
			}
			byteData = baos.toByteArray();

			fis.close();
			baos.close();
			bis.close();

		} catch (FileNotFoundException e) {
			// TODO ステージ画像ファイルがない場合、「準備中」の画像を表示させる。
			Log.e("ERROR", cmndef.STAGE_ERROR_MSG1 + e.toString());
			return null;

		} catch (IOException e) {
			// 画像読み込みに失敗した場合
			Log.e("ERROR", cmndef.STAGE_ERROR_MSG2 + e.toString());
			Toast.makeText(getApplicationContext(), cmndef.STAGE_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();
			return null;

		}

		// Bitmap画像のByte配列を返却する
		return byteData;

	}

	@Override
	protected void onResume() {
		Log.d("DEBUG", "StageSelectAct onResume()　START");
		super.onResume();

		// ボタンクリック時に呼び出す音をロードしておく
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSounds[0] = mSoundPool.load(this, R.raw.stage_select, 1);

		try {
			if (!mp.isPlaying()) {
				// 音声再生
				mp.start();

				// 再生終了を検出する
				mp.setOnCompletionListener(this);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// 音声の再生に失敗した場合
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * アクティビティが「停止」の状態
	 * 
	 * */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("DEBUG", "StageSelectAct onPause() Start");
		
		// 音声を一旦停止する
		if (mp.isPlaying()) {
			mp.pause();
		}
	}

	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "StageSelectAct onDestroy　START");
		super.onDestroy();

		// 音をリリース
		mSoundPool.release();
		if (mp != null) {
			mp.release();
			mp = null;
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d("DEBUG", "StageSelectAct onCompletion　START");

		// 再生箇所を最初に戻す
		mp.seekTo(0);
		// 音声を再生させる
		mp.start();
	}

}
