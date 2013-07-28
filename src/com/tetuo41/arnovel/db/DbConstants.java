package com.tetuo41.arnovel.db;

/**
* データベース関連の定数を定義する。
* @author　HackathonG
* @version 1.0
*/
public class DbConstants {

	/** データベースの名前とバージョン */
    public static final String DATABASE_NAME = "LocaNovelDB";
    public static final int DATABASE_VERSION = 1;

    /** 作成・使用するテーブル名を定義する */
    public static final String TABLE1 = "mst_stamp_rally";
    public static final String TABLE2 = "mst_novel";
    public static final String TABLE3 = "mst_stage_select";
    
    /** 使用するテーブル名のカラム名を定義する */
    // スタンプラリーマスタ(mst_stamp_rally)
    /** スタンプID */
    public static final String CLM_STAMP_ID = "stamp_id";
    
    /** スタンプフラグ */
    public static final String CLM_STAMP_FLG = "stamp_flg";
    
    /** 登録日時 */
    public static final String CLM_REGIST_DATE = "regist_date";
    
    // ノベルマスタ (mst_novel)
    /** ノベルID */
    public static final String CLM_NOVEL_ID = "novel_id";
    
    /** 経度 */
    public static final String CLM_LONGITUDE = "longitude";
    
    /** 緯度 */
    public static final String CLM_LATITUDE = "latitude";
    
    /** ノベルタイトル */
    public static final String CLM_NOVEL_TITLE = "novel_title";
    
    /** ノベルデータ */
    public static final String CLM_NOVEL_DATA = "novel_data";
    
    // ステージセレクトマスタ(mst_stage_select)
    /** ステージID */
    public static final String CLM_STAGE_ID = "stage_id";
    
    /** 画像名 */
    public static final String CLM_IMAGE_NAME = "image_name";
    
    /** 住所 */
    public static final String CLM_ADDRESS = "address";
    
    /** CREATE文を定義する */
    // スタンプラリーマスタのCREATE文を定義
    public static final String CREATE_TABLE1 =
            "CREATE TABLE " + TABLE1 + " ("
                            + CLM_STAMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + CLM_STAGE_ID + " INTEGER NOT NULL, "
                            + CLM_STAMP_FLG + " INTEGER NOT NULL, "
                            + CLM_REGIST_DATE + " TEXT "
                            + ");";

    // ノベルマスタのCREATE文を定義
    public static final String CREATE_TABLE2 =
            "CREATE TABLE " + TABLE2 + " ("
                            + CLM_NOVEL_ID + " INTEGER PRIMARY KEY, "
                            + CLM_STAGE_ID + " INTEGER NOT NULL, "
                            + CLM_LONGITUDE + " TEXT NOT NULL, "
                            + CLM_LATITUDE + " TEXT NOT NULL, "
                            + CLM_NOVEL_TITLE + " TEXT NOT NULL, "
                            + CLM_NOVEL_DATA + " TEXT NOT NULL "
                            + " );";
    
    // ステージセレクトマスタのCREATE文を定義
    public static final String CREATE_TABLE3 =
            "CREATE TABLE " + TABLE3 + " ("
                            + CLM_STAGE_ID + " INTEGER PRIMARY KEY, "
                            + CLM_IMAGE_NAME + " TEXT NOT NULL, "
                            + CLM_ADDRESS + " TEXT NOT NULL "
                            + " );";

    /** DROP文を定義する */
    public static final String DATABASE_UPDATE1 ="DROP TABLE IF EXISTS " + TABLE1;
    public static final String DATABASE_UPDATE2 ="DROP TABLE IF EXISTS " + TABLE2;
    public static final String DATABASE_UPDATE3 ="DROP TABLE IF EXISTS " + TABLE3;
    
}