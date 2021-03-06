package com.tetuo41.locanovel.common;

import android.os.Environment;


/**
 * 全クラス共通で使用する定数を
 * 規定する定数クラスです。
 * @author HackathonG
 */
public class CommonDef {

	/** アプリ関連 */
    public final String APP_NAME = "LocaNovel";
    
	/** サーバ情報 */
    public final String S_HOST_NAME = "133.242.168.69";
    public final String S_DIR = "/";
    
    /** ファイル名 */
    public final String NOVEL_FILE = "loca_novel.csv";
    public final String STAGE_FILE = "stage.csv";

    /** サーバディレクトリ名 */
    public final String DATA_DIR = "/hack_g/data/";
    public final String IMAGE_DIR = "/hack_g/image/locanovel/";
    
	/** SDカードの画像保存パス */
    public final String SDCARD_FOLDER = Environment
			.getExternalStorageDirectory().getPath() + "/locanovel/";

    /** 各種画面用メッセージ */
    public final String TOP_LOADING_MSG = "ノベルデータ更新中";
    
    /** エラーメッセージ */
    // トップ画面用メッセージ
    public final String TOP_ERROR_MSG1 = "ステージセレクト画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG2 = "スタンプログ画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG3 = "ノベルファイルの読込に失敗しました";
    public final String TOP_ERROR_MSG4 = "データファイル読み込み先のURLが間違っています";
    
    // スタンプ一覧画面用メッセージ
    public final String STAMP_ERROR_MSG1 = "スタンプログ詳細画面へ遷移できませんでした";
    
    // ステージ一覧画面用メッセージ
    public final String STAGE_ERROR_MSG1 = "ステージ画像ファイルがありません";
    public final String STAGE_ERROR_MSG2 = "ステージ画像の読込に失敗しました";
    
    // 共通用メッセージ
    public final String CMN_ERROR_MSG1 = "ネットワークに接続できません";
    public final String CMN_ERROR_MSG2 = "音声の再生に失敗しました";
    
    // カメラプレビュー画面用メッセージ
    public final String CAMERA_ERROR_MSG1 = "撮影画像の保存に失敗しました";
    public final String CAMERA_ERROR_MSG2 = "ファイルの保存中にエラーが発生しました";
    public final String CAMERA_ERROR_MSG3 = "カメラの起動に失敗しました";
    public final String CAMERA_ERROR_MSG4 = "撮影時における位置情報の取得ができませんでした";
    public final String CAMERA_INFO_MSG1 = "この場所は選択したステージ画像の場所ではありません";
    public final CharSequence CAMERA_INFO_MSG2 = "位置情報更新中";
    public final CharSequence CAMERA_INFO_MSG3 = "位置情報が更新されました";
    
    // ノベル導入画面用メッセージ
    public final String NOVEL_INTRO_ERROR_MSG1 = "ノベル表示画面へ遷移できませんでした";
    public final String NOVEL_INTRO_ERROR_MSG2 = "ステージセレクト画面へ遷移できませんでした";
    
    // ノベル表示画面用メッセージ
    public final String NOVEL_ERROR_MSG1 = "ノベル完了画面へ遷移できませんでした";
    
    // データベース用メッセージ
    public final String DB_ERROR_MSG1 = "テーブルの作成に失敗しました";
    public final String DB_ERROR_MSG2 = "データベースのバージョンアップに失敗しました";

}