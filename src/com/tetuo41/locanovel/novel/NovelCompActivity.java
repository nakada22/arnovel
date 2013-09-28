package com.tetuo41.locanovel.novel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.TopActivity;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.stageselect.StageSelectState;

/**
 * ノベル完了画面を表示するクラスです。
 * 
 * @author　HackathonG
 * @version 1.0
 */
public class NovelCompActivity extends Activity implements OnClickListener,
		OnCompletionListener {

	/** 共通クラスオブジェクト */
	private CommonUtil cmnutil;
	private CommonDef cmndef;

	/** 背景画像用イメージビュー */
	private ImageView novel_layout;

	/** ステージセレクト画面からのデータ(ステージID、ステージタイトル) */
	StageSelectState sss;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int[] mSounds = new int[1];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 背景に撮影した画像をセット
		setContentView(R.layout.novel_comp);

		// ノベル完了画面を表示する
		NovelCompDisp();

		// ClickListener登録
		findViewById(R.id.menu_back).setOnClickListener(this);

	}

	/**
	 * コンストラクタ
	 * 
	 * @return
	 */
	public NovelCompActivity() {
		cmnutil = new CommonUtil();
		cmndef = new CommonDef();
	}

	/**
	 * ノベル導入部分を表示する
	 * 
	 * */
	private void NovelCompDisp() {

		/** 音声再生処理 */
//		if (mp != null) {
//			mp.release();
//			mp = null;
//		}
//		mp = new MediaPlayer();
//		try {
//			mp = MediaPlayer.create(getBaseContext(), R.raw.novel_comp);
//
//			// サウンド音量設定0.0から1.0で設定
//			mp.setVolume(0.1f, 0.1f);
//			mp.start();
//
//			// 再生終了を検出する
//			mp.setOnCompletionListener(this);
//
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//			// 音声の再生に失敗した場合
//			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
//					Toast.LENGTH_SHORT).show();
//
//		}

		// 背景用画像パス取得、背景画像セット
		Intent i = getIntent();
		String bg_pass = (String) i.getSerializableExtra("back_ground");
		Drawable d = Drawable.createFromPath(bg_pass);
		novel_layout = (ImageView) findViewById(R.id.novel_comp_back_ground);
		novel_layout.setBackgroundDrawable(d);

		// ステージセレクトActivityより取得したデータを取得
		sss = (StageSelectState) i.getSerializableExtra("StageSelectState");

		// スタンプ保存完了文字列 をセット
		TextView tv_stamp_save = (TextView) findViewById(R.id.stamp_save);
		int stage_id = sss.getStageId(); // ステージID
		String stage_title = sss.getStageTitle(); // ステージタイトル
		tv_stamp_save.setText("スタンプNo." + stage_id + "「" + stage_title
				+ "」を保存しました。");

	}

	/**
	 * ボタンクリック時の処理を記述する。
	 * 
	 * @param View
	 *            ボタンオブジェクト
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.menu_back:
			// 「メニューに戻る」クリック時
			MenuBackClick();

			break;
		default:
			break;
		}
	}

	/**
	 * 「メニューに戻る」ボタンクリック時の処理を記述する。
	 */
	private void MenuBackClick() {

		try {
//			if (mp.isPlaying()) {
//				// 再生中であれば
//				mp.pause();
//
//				// ボタンクリック時の音再生
//				mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);
//
//			}

			// メニューに戻るボタンクリック時、トップ画面へ遷移
			Intent i = new Intent(getApplicationContext(), TopActivity.class);
			startActivity(i);

		} catch (ActivityNotFoundException e) {
			// トップ画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);
			// 処理を終了する
			return;

		} catch (RuntimeException e) {
			// 通常は通らないルート
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.TOP_ERROR_MSG1);

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
//		// 再生箇所を最初に戻す
//		mp.seekTo(0);
//		// 音声を再生させる
//		mp.start();
	}
}
