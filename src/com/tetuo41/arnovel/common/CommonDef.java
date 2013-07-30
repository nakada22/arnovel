package com.tetuo41.arnovel.common;


/**
 * 全クラス共通で使用する定数を
 * 規定する定数クラスです。
 * @author HackathonG
 */
public class CommonDef {
	
	/** FTP情報 */
    public final String S_HOST_NAME = "sashihara.web.fc2.com";
    public final String S_USER_NAME = "sashihara";
    public final String S_PASSWARD = "test123";
    public final String S_DIR = "/";
    public final int S_PORT = 21;
    
    /** ファイル名 */
    public final String NOVEL_FILE = "loca_novel.csv";
    public final String STAGE_FILE = "stage.csv";

    /** ディレクトリ名 */
    public final String DATA_DIR = "/data/";
    
    /** エラーメッセージ */
    // トップ画面用メッセージ
    public final String TOP_ERROR_MSG1 = "ステージセレクト画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG2 = "スタンプログ画面へ遷移できませんでした";
    public final String TOP_ERROR_MSG3 = "CSVファイルの読込に失敗しました";
    
    // 共通用メッセージ
    public final String CMN_ERROR_MSG1 = "インターネットに接続できませんでした";
    
    
    
    
}