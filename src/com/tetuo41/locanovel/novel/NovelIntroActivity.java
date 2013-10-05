package com.tetuo41.locanovel.novel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.stageselect.StageSelectActivity;
import com.tetuo41.locanovel.stageselect.StageSelectState;
import com.tetuo41.locanovel.stamplog.StampLogDetailActivity;

/**
 * ノベル導入画面を表示するクラスです。
 * 
 * @author　HackathonG
 * @version 1.0
 */
public class NovelIntroActivity extends Activity implements
		OnCompletionListener {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** 背景画像用 */
	private RelativeLayout novel_layout;
	private String bg_pass;

	/** ステージセレクト画面からのデータ */
	private StageSelectState sss;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int[] mSounds = new int[2];

	/** クリックイベント実行可否フラグ */
	private boolean ClickEventFlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// レイアウト作成
		novel_layout = new RelativeLayout(this);
		novel_layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		setContentView(novel_layout);

		// ノベル導入部分を表示する
		NovelIntroDisp();
	}

	/**
	 * コンストラクタ
	 */
	public NovelIntroActivity() {
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
	}

	/**
	 * ノベル導入部分を表示する
	 * 
	 * */
	private void NovelIntroDisp() {

		/** 音声再生処理 */
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
		try {
			mp = MediaPlayer.create(getBaseContext(), R.raw.novel_intro);

			// サウンド音量設定0.0から1.0で設定
			mp.setVolume(1.0f, 1.0f);
			mp.start();

			// 再生終了を検出する
			mp.setOnCompletionListener(this);

		} catch (IllegalStateException e) {
			e.printStackTrace();
			// 音声の再生に失敗した場合
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		// ステージセレクト画面より データを取得
		Intent i = getIntent();
		sss = (StageSelectState) i.getSerializableExtra("StageSelectState");

		// 背景用画像パス取得、背景画像セット
		bg_pass = (String) i.getSerializableExtra("back_ground");
		Drawable d = Drawable.createFromPath(bg_pass);
		novel_layout.setBackgroundDrawable(d);

		// 6秒カウントダウン、2秒間隔に吹き出しを表示させる
		new CountDownTimer(6000, 1000) {

			// 1秒毎カウントダウン処理
			public void onTick(long millisUntilFinished) {
				if (millisUntilFinished / 1000 == 5) {
					// ノベル導入部分1(1秒後)
					String novel_intro1 = sss.getNovelIntro1();
					BlowOffMake(novel_intro1, 0, 10, 130, 0,
							R.drawable.b_blow_off);

				} else if (millisUntilFinished / 1000 == 3) {
					// ノベル導入部分2(3秒後)
					String novel_intro2 = sss.getNovelIntro2();
					BlowOffMake(novel_intro2, 130, 200, 0, 0,
							R.drawable.m_blow_off);

				} else if (millisUntilFinished / 1000 == 1) {
					// ノベル導入部分3(5秒後)
					String novel_intro3 = sss.getNovelIntro3();
					BlowOffMake(novel_intro3, 0, 400, 130, 0,
							R.drawable.b_blow_off);

				}
			}

			public void onFinish() {
				// カウントが0になった時の処理
				// Answer ボタン表示
				View under_intro_view = getLayoutInflater().inflate(
						R.layout.novel_intro, null);
				novel_layout.addView(under_intro_view);

				/**
				 * ボタンクリック時の処理
				 * */
				findViewById(R.id.gomen_blowoff).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// クリックイベントが許可されていなければ実行しない
								if (!ClickEventFlg)
									return;

								// クリックイベントを禁止する
								ClickEventFlg = false;

								// 「ごめん、やめておく」ボタンクリック時
								SorryStopClick();
							}
						});
				findViewById(R.id.kikasete_blowoff).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// クリックイベントが許可されていなければ実行しない
								if (!ClickEventFlg)
									return;

								// クリックイベントを禁止する
								ClickEventFlg = false;

								// 「聞かせて」クリック時
								TellClick();
							}
						});
			}
		}.start();
	}

	/**
	 * 吹き出し作成処理を行う
	 * 
	 * @param 吹き出しに表示する文字列
	 * @param マージン左
	 * @param マージン上
	 * @param マージン右
	 * @param マージン下
	 * @param 吹き出し画像のリソース
	 *            (背景)
	 * 
	 * */
	private void BlowOffMake(String str_intro, int m_left, int m_top,
			int m_right, int m_btm, int setback) {

		Button btn = new Button(this);
		btn.setTextColor(Color.WHITE);
		btn.setTextSize(14);
		btn.setGravity(Gravity.LEFT);
		btn.setBackgroundResource(setback);
		btn.setText(str_intro);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(550,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(m_left, m_top, m_right, m_btm);
		novel_layout.addView(btn, lp);

	}

	/**
	 * 「聞かせて」ボタンクリック時の処理を記述する。
	 */
	private void TellClick() {

		try {
			if (mp.isPlaying()) {
				Log.d("DEBUG", "再生中であれば TellClick");
				// 再生中であれば
				mp.pause();
			}

			// ボタンクリック時の音再生
			mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);

			// 1秒後にノベル表示画面へ遷移させる
			new CountDownTimer(1000, 100) {
				@Override
				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					Intent i = new Intent(getApplicationContext(),
							NovelActivity.class);
					i.putExtra("back_ground", bg_pass);
					i.putExtra("StageSelectState", sss);
					startActivity(i);
				}
			}.start();

		} catch (ActivityNotFoundException e) {
			// ノベル表示画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG1);
			// 処理を終了する
			return;

		} catch (Exception e) {
			// 通常は通らないルート
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG1);

			// 処理を終了する
			return;
		}
	}

	/**
	 * 「ごめん、やめておく」ボタンクリック時の処理を記述する。
	 */
	private void SorryStopClick() {
		try {

			if (mp.isPlaying()) {
				Log.d("DEBUG", "再生中であれば SorryStopClick");
				// 再生中であれば
				mp.pause();
			}

			// ボタンクリック時の音再生
			mSoundPool.play(mSounds[1], 1.0F, 1.0F, 0, 0, 1.0F);

			// 音を0.5秒くらい再生させるため1秒くらい待つ
			new CountDownTimer(500, 100) {
				@Override
				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					// ステージセレクト画面へ遷移
					sss = null;
					Intent i = new Intent(getApplicationContext(),
							StageSelectActivity.class);
					startActivity(i);
				}
			}.start();

		} catch (ActivityNotFoundException e) {
			// ステージセレクト画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG2);

			// 処理を終了する
			return;
		} catch (Exception e) {
			// 通常は通らないルート
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_INTRO_ERROR_MSG2);

			// 処理を終了する
			return;
		}
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
	 * 画面が切り替わった場合などの処理を記述する
	 * 
	 * */
	@Override
	protected void onPause() {
		Log.d("DEBUG", "NovelIntroAct onPause　START");
		super.onPause();

		// 音声を一旦停止する
		if (mp.isPlaying()) {
			mp.pause();
		}
	}

	@Override
	protected void onResume() {
		Log.d("DEBUG", "NovelIntroAct onResume　START");
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
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		// ボタンクリック時に呼び出す音をロードしておく
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		// 聞かせて用
		mSounds[0] = mSoundPool.load(getApplicationContext(), R.raw.buttom, 1);
		// ごめんやめておく用
		mSounds[1] = mSoundPool.load(getApplicationContext(),
				R.raw.finish_buttom, 1);

		// クリックイベントを許可する
		ClickEventFlg = true;

	}

	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "NovelIntroAct onDestroy　START");
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
