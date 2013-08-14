package com.tetuo41.arnovel.common;


/**
 * 全クラス共通で使用する定数を
 * 規定する定数クラスです。
 * @author HackathonG
 */
public class CommonDef {

	/** アプリ関連 */
    public final String APP_NAME = "LocaNovel";
    
	/** サーバ情報 */
    public final String S_HOST_NAME = "sashihara.web.fc2.com";
    public final String S_DIR = "/";
    
    /** ファイル名 */
    public final String NOVEL_FILE = "loca_novel.csv";
    public final String STAGE_FILE = "stage.csv";

    /** サーバディレクトリ名 */
    public final String DATA_DIR = "/data/";
    public final String IMAGE_DIR = "/image/locanovel/";
    
    /** エラーメッセージ */
    // トップ画面用メッセージ
    public final String TOP_ERROR_MSG1 = "ステージセレクト画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG2 = "スタンプログ画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG3 = "CSVファイルの読込に失敗しました";
    
    // スタンプ一覧画面用メッセージ
    public final String STAMP_ERROR_MSG1 = "スタンプログ詳細画面へ遷移できませんでした";
    
    // スタンプ一覧画面用メッセージ
    public final String STAGE_ERROR_MSG1 = "画像の読込が失敗しました";
    
    // 共通用メッセージ
    public final String CMN_ERROR_MSG1 = "インターネットに接続できませんでした";
    
    // カメラプレビュー画面用メッセージ
    public final String CAMERA_ERROR_MSG1 = "撮影画像の保存に失敗しました";
    public final String CAMERA_ERROR_MSG2 = "ファイルの保存中にエラーが発生しました";
    public final String CAMERA_ERROR_MSG3 = "カメラの起動に失敗しました";
    
    
    
}