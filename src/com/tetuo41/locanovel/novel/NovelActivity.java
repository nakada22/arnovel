package com.tetuo41.locanovel.novel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tetuo41.locanovel.R;
import com.tetuo41.locanovel.common.CommonDef;
import com.tetuo41.locanovel.common.CommonUtil;
import com.tetuo41.locanovel.db.Dao;
import com.tetuo41.locanovel.stageselect.StageSelectState;

/**
 * ノベル表示画面を表示するクラスです。
 * 
 * @author　HackathonG
 * @version 1.0
 */
public class NovelActivity extends Activity implements OnClickListener,
		OnCompletionListener {

	/** 共通クラスオブジェクト */
	private CommonDef cmndef;

	/** 背景画像用 */
	private LinearLayout bg_layout;
	private String bg_pass;

	/** スクロールビューで必要なオブジェクト */
	private NovelScrollView scroll;
	private TextView novel_layout;
	private boolean flg = false;
	private StageSelectState sss;
	private View read_comp_view;
	private boolean disp_chg_flg = false;

	/** 音声関連のインスタンス生成 */
	private MediaPlayer mp;
	private SoundPool mSoundPool;
	private int[] mSounds = new int[1];

	/** クリックイベント実行可否フラグ */
	private boolean ClickEventFlg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ノベル表示用ScrollViewのオブジェクトを取得
		scroll = (NovelScrollView) LayoutInflater.from(this).inflate(
				R.layout.novel, null);
		setContentView(scroll);

		// ノベルデータ表示用TextView
		novel_layout = (TextView) scroll.findViewById(R.id.novel_data);
		read_comp_view = getLayoutInflater().inflate(R.layout.novel2, null);

		// ノベル部分をリスト表示する
		NovelDisp();

	}

	/**
	 * コンストラクタ
	 */
	public NovelActivity() {
		cmndef = new CommonDef();
	}

	/**
	 * ノベル導入部分を表示する
	 * 
	 * */
	private void NovelDisp() {

		/** 音声再生処理 */
		if (mp != null) {
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
		try {
			mp = MediaPlayer.create(getBaseContext(), R.raw.novel);

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

		// 背景用画像パス取得、背景画像セット
		Intent i = getIntent();
		String bg_pass = (String) i.getSerializableExtra("back_ground");
		bg_layout = (LinearLayout) scroll.findViewById(R.id.novel_back_ground);
		if (!bg_pass.equals("")) {
			// 背景画像名がセットされていれば
			Drawable d = Drawable.createFromPath(bg_pass);
			bg_layout.setBackgroundDrawable(d);
		}

		// ステージセレクト画面より引継ぎして取得したノベルデータ
		sss = (StageSelectState) i.getSerializableExtra("StageSelectState");

		// ノベルデータをセット
		String novel_data = sss.getAllOutLine();
		novel_layout.setText(novel_data);

		if (novel_data.length() <= 500) {
			// ノベルデータが500文字以下であれば、「読了」ボタンビューにセット・表示処理を行う
			bg_layout.addView(read_comp_view);
			flg = true; // 読了ボタンは初回だけ表示

			findViewById(R.id.read_comp).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// クリックイベントが許可されていなければ実行しない
						    if (!ClickEventFlg) return;
						    
						    // クリックイベントを禁止する
							ClickEventFlg = false;
							
							// 「読了」クリック時
							ReadFinishClick();
						}
					});
		}

		scroll.setScrollToBottomListener(new NovelScrollView.ScrollToBottomListener() {
			@Override
			public void onScrollToBottom(NovelScrollView scrollView) {
				Log.d("DEBUG", "onScrollToBottom Start");

				if (flg == false) {

					// 初回の最下部スクロール時だけ、最下部にスクロールした時のイベント
					// 「読了」ボタンビューにセット・表示処理を行う
					bg_layout.addView(read_comp_view);
					flg = true;

					// ボタンクリック時の処理
					findViewById(R.id.read_comp).setOnClickListener(
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// クリックイベントが許可されていなければ実行しない
								    if (!ClickEventFlg) return;
								    
								    // クリックイベントを禁止する
									ClickEventFlg = false;
									
									// 「読了」クリック時
									ReadFinishClick();
								}
							});
				}
			}
		});

		// 一番下までスクロールさせる
		// scrollView.post(new Runnable() {
		// @Override public run() {
		// scrollView.fullScroll(ScrollView.FOCUS_DOWN);
		// }
		// });

	}

	/**
	 * フォーカスが移った直後か失った直後の処理
	 * 
	 * @param hasFocus
	 *            フォーカスの状態
	 * */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.d("DEBUG", "onWindowFocusChanged Start");

		// フォーカス変更後の処理
		if (disp_chg_flg == true) {

			/** 読了ボタン押したとき */
			// 読了ボタン非表示
			read_comp_view.setVisibility(View.INVISIBLE);

			// ノベルデータ非表示
			novel_layout.setVisibility(View.INVISIBLE);
			disp_chg_flg = false;
		} else {

			/** ノベル完了画面からノベル表示画面へ戻った時 */
			// 読了ボタン表示
			read_comp_view.setVisibility(View.VISIBLE);

			// ノベルデータ表示
			novel_layout.setVisibility(View.VISIBLE);

		}

		scroll.setScrollBottomMargin(1000);
	}

	/**
	 * ボタンクリック時の処理を記述する。
	 * 
	 * @param View
	 *            ボタンオブジェクト
	 */
	public void onClick(View v) {
		// クリックイベントが許可されていなければ実行しない
	    if (!ClickEventFlg) return;
	    
	    // クリックイベントを禁止する
		ClickEventFlg = false;
		
		switch (v.getId()) {
		case R.drawable.read_comp:
			// 「読了」クリック時
			ReadFinishClick();

			break;
		default:
			break;
		}
	}

	/**
	 * 「読了」ボタンクリック時の処理を記述する。
	 */
	private void ReadFinishClick() {

		try {
			Log.d("DEBUG", "ReadFinishClick Start");

			if (mp.isPlaying()) {
				// 再生中であれば
				mp.pause();
			}

			// ボタンクリック時の音再生
			mSoundPool.play(mSounds[0], 1.0F, 1.0F, 0, 0, 1.0F);

			// DBアクセスクラスオブジェクト
			Dao dao = new Dao(getApplicationContext());

			// stampフラグを「1」Update
			dao.StampFlgUpdate(sss.getStageId());

			// 読了ボタンクリック時、ノベル完了画面へ遷移
			Intent i = new Intent(getApplicationContext(),
					NovelCompActivity.class);
			i.putExtra("back_ground", bg_pass);
			i.putExtra("StageSelectState", sss);
			startActivity(i);

		} catch (ActivityNotFoundException e) {
			// ノベル完了画面へ遷移できなかった場合
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);

			// 処理を終了する
			return;

		} catch (RuntimeException e) {
			// 通常通らないルート
			Log.e("ERROR", e.toString());

			// アラートダイアログで警告を表示
			AlertDialogView("エラー", cmndef.NOVEL_ERROR_MSG1);

			// 処理を終了する
			return;
		}
	}

	/**
	 * アクティビティが「停止」の状態
	 * */
	@Override
	protected void onPause() {
		Log.d("DEBUG", "onPause() Start");
		super.onPause();

		if (mp.isPlaying()) {
			mp.pause();
		}
		
		// 読了ボタン押した時通る
		disp_chg_flg = true;

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
			Toast.makeText(getBaseContext(), cmndef.CMN_ERROR_MSG2,
					Toast.LENGTH_SHORT).show();

		}

		// ボタンクリック時に呼び出す音をロードしておく
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSounds[0] = mSoundPool.load(getApplicationContext(), R.raw.finish_buttom, 1);

		// クリックイベントを許可する
		ClickEventFlg = true;
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
	public void onCompletion(MediaPlayer mp) {
		// 再生箇所を最初に戻す
		mp.seekTo(0);
		// 音声を再生させる
		mp.start();
	}
}
