package com.tetuo41.locanovel.stamplog;

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
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.stamplog.StampLogActivity.StampLogState;

/**
 * スタンプログ詳細画面を表示するクラスです。
 * 
 * @author　HackathonG
 * @version 1.0
 */
public class StampLogDetailActivity extends Activity implements
		OnClickListener, OnCompletionListener {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int[] mSounds = new int[1];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stamp_log_detail);

		// ClickListener登録
		// findViewById(R.id.facebook_btn).setOnClickListener(this);
		// findViewById(R.id.twitter_btn).setOnClickListener(this);

		// ノベル読了したステージのスタンプログ詳細画面を表示
		StampDetailDisp();
	}

	/**
	 * コンストラクタ
	 * 
	 */
	public StampLogDetailActivity() {
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
	}

	/**
	 * ボタンクリック時の処理を記述する。
	 * 
	 * @param View
	 *            ボタンオブジェクト
	 */
	public void onClick(View v) {

//		switch (v.getId()) {
//		case R.id.facebook_btn:
//			// 「FaceBook」ボタンクリック時
//			FaceBookClick();
//
//			break;
//		case R.id.twitter_btn:
//			// 「Twitter」ボタンクリック時
//			TwitterClick();
//
//			break;
//		default:
//			break;
//		}
	}

	/**
	 * FaceBookボタンクリック時の処理を記述する。
	 */
//	private void FaceBookClick() {
//		if (mp.isPlaying()) {
//			// 再生中であれば
//			mp.pause();
//		}
//
//		// ボタンクリック時の音再生
//		mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);
//	}

	/**
	 * Twitterボタンクリック時の処理を記述する。
	 */
//	private void TwitterClick() {
//		if (mp.isPlaying()) {
//			// 再生中であれば
//			mp.pause();
//		}
//
//		// ボタンクリック時の音再生
//		mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);
//	}

	/**
	 * ノベル読了したステージのスタンプ詳細を表示する
	 * 
	 * */
	private void StampDetailDisp() {

		/** 音声再生処理 */
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();

		// スタンプラリー詳細画面表示時、音声を再生する
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

		Intent i = getIntent();
		StampLogState sls = (StampLogState) i
				.getSerializableExtra("StampLogState");

		/** スタンプ一覧画面からデータ取得 */
		int stamp_id = sls.getStampId();
		String novel_title = sls.getNovelTitle();
		String novel_data = sls.getNovelData();

		// ノベルデータを25文字づつ改行区切りでわける。
		StringBuilder sb = new StringBuilder();

		// 表示する行数(改行文字の数でもある)
		int row_count = novel_data.length() / 19;

		for (int j = 0; j < row_count; j++) {
			sb.append(novel_data.substring(19 * j, (19 * j) + 19));
			Log.d("DEBUG", sb.toString());
			sb.append("\n"); // 改行文字
		}
		// 残り文字列の連結
		sb.append(novel_data.substring(sb.length() - row_count));

		/** 表示するVIEW取得・テキストセット */
		// レコードID
		TextView tvRecordId = (TextView) findViewById(R.id.record_id);
		tvRecordId.setText("No." + stamp_id);

		// レコードタイトル(ノベルタイトル)
		TextView tvNovelTitle = (TextView) findViewById(R.id.record_title);
		tvNovelTitle.setText("「" + novel_title + "」");

		// あらすじ(ノベルデータ)
		TextView tvNovelData = (TextView) findViewById(R.id.novel_data);
		tvNovelData.setText(sb);
	}

	@Override
	protected void onResume() {
		super.onResume();

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
			Toast.makeText(getApplicationContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}
		// ボタンクリック時に呼び出す音をロードしておく
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSounds[0] = mSoundPool.load(getApplicationContext(), R.raw.buttom, 1);

	}

	/**
	 * アクティビティが「停止」の状態
	 * 
	 * */
	@Override
	protected void onPause() {
		super.onPause();

		// 音声を一旦停止する
		if (mp.isPlaying()) {
			mp.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 音をリリース
		if (mSoundPool != null) {
			mSoundPool.release();
		}
		if (mp != null) {
			mp.release();
			mp = null;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// 再生箇所を最初に戻す
		mp.seekTo(0);
		// 音声を再生させる
		mp.start();
	}

}
