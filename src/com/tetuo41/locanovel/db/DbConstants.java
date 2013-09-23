package com.tetuo41.locanovel.db;

/**
* データベース関連の定数を定義する。
* @author　HackathonG
* @version 1.0
*/
public class DbConstants {

	/** データベースの名前とバージョン */
    public static final String DATABASE_NAME = "LocaNovelDB";
    public static final int DATABASE_VERSION = 1;
    public static final String DB_PATH = "/data/data/com.tetuo41.locanovel/databases/";  
    
    /** 作成・使用するテーブル名を定義する */
    public static final String TABLE1 = "mst_stamp_rally";
    public static final String TABLE2 = "mst_novel";
    public static final String TABLE3 = "mst_stage_select";
    public static final String TABLE4 = "mst_locate_info";
    
    /** 使用するテーブル名のカラム名を定義する */
    // スタンプラリーマスタ(mst_stamp_rally)
    /** スタンプID */
    public static final String CLM_STAMP_ID = "stamp_id";
    
    /** スタンプフラグ */
    public static final String CLM_STAMP_FLG = "stamp_flg";
    
    /** 更新日時 */
    public static final String CLM_UPDATE_DATE = "update_date";
    
    // ノベルマスタ (mst_novel)
    /** ノベルID */
    public static final String CLM_NOVEL_ID = "novel_id";
    
    /** 経度 */
    public static final String CLM_LONGITUDE = "longitude";
    
    /** 緯度 */
    public static final String CLM_LATITUDE = "latitude";
    
    /** ノベルタイトル */
    public static final String CLM_NOVEL_TITLE = "novel_title";

    /** ノベル導入部分1 */
    public static final String CLM_NOVEL_INTRO1 = "novel_intro1";

    /** ノベル導入部分2 */
    public static final String CLM_NOVEL_INTRO2 = "novel_intro2";

    /** ノベル導入部分3 */
    public static final String CLM_NOVEL_INTRO3 = "novel_intro3";

    /** ノベルデータ */
    public static final String CLM_NOVEL_DATA = "novel_data";
    
    /** 撮影画像名 */
    public static final String CLM_LOCATE_IMG_NAME = "locate_img_name";
    
    // ステージセレクトマスタ(mst_stage_select)
    /** ステージID */
    public static final String CLM_STAGE_ID = "stage_id";
    
    /** 画像名 */
    public static final String CLM_IMAGE_NAME = "image_name";
    
    /** 住所 */
    public static final String CLM_ADDRESS = "address";
    
    // 位置情報マスタ(mst_locate_info)
    /** 登録日時 */
    public static final String CLM_REGIST_DATE = "regist_date";
    
    /** CREATE文を定義する */
    // スタンプラリーマスタのCREATE文を定義
    public static final String CREATE_TABLE1 =
            "CREATE TABLE " + TABLE1 + " ("
                            + CLM_STAMP_ID + " INTEGER PRIMARY KEY,"
                            + CLM_STAGE_ID + " INTEGER NOT NULL, "
                            + CLM_STAMP_FLG + " INTEGER NOT NULL, "
                            + CLM_UPDATE_DATE + " TEXT "
                            + ");";

    // ノベルマスタのCREATE文を定義
    public static final String CREATE_TABLE2 =
            "CREATE TABLE " + TABLE2 + " ("
                            + CLM_NOVEL_ID + " INTEGER PRIMARY KEY, "
                            + CLM_STAGE_ID + " INTEGER NOT NULL, "
                            + CLM_LONGITUDE + " TEXT NOT NULL, "
                            + CLM_LATITUDE + " TEXT NOT NULL, "
                            + CLM_NOVEL_TITLE + " TEXT NOT NULL, "
                            + CLM_NOVEL_INTRO1 + " TEXT NOT NULL, "
                            + CLM_NOVEL_INTRO2 + " TEXT NOT NULL, "
                            + CLM_NOVEL_INTRO3 + " TEXT NOT NULL, "
                            + CLM_NOVEL_DATA + " TEXT NOT NULL, "
                            + CLM_LOCATE_IMG_NAME + " TEXT "
                            + " );";
    
    // ステージセレクトマスタのCREATE文を定義
    public static final String CREATE_TABLE3 =
            "CREATE TABLE " + TABLE3 + " ("
                            + CLM_STAGE_ID + " INTEGER PRIMARY KEY, "
                            + CLM_IMAGE_NAME + " TEXT NOT NULL, "
                            + CLM_ADDRESS + " TEXT NOT NULL "
                            + " );";

    // 位置情報マスタのCREATE文を定義
    public static final String CREATE_TABLE4 =
            "CREATE TABLE " + TABLE4 + " ("
                            + CLM_LONGITUDE + " REAL DEFAULT '0.0', "
                            + CLM_LATITUDE + " REAL DEFAULT '0.0', "
                            + CLM_REGIST_DATE + " TEXT NOT NULL, "
                            + "PRIMARY KEY(" + CLM_LONGITUDE + ", " + CLM_LATITUDE
                            + ") );";
    
    /** DROP文を定義する */
    public static final String DATABASE_UPDATE1 ="DROP TABLE IF EXISTS " + TABLE1;
    public static final String DATABASE_UPDATE2 ="DROP TABLE IF EXISTS " + TABLE2;
    public static final String DATABASE_UPDATE3 ="DROP TABLE IF EXISTS " + TABLE3;
    public static final String DATABASE_UPDATE4 ="DROP TABLE IF EXISTS " + TABLE4;
    
}