package com.tetuo41.locanovel.db;

import com.tetuo41.locanovel.common.CommonDef;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
* データベースの作成、更新を行う。
* @author　HackathonG
* @version 1.0
*/
public class DbOpenHelper extends SQLiteOpenHelper {

	/** 共通クラスオブジェクト */
	private CommonDef cmndef;
	
	/**
	* コンストラクタ
	* @param context コンテキスト
	*/
    public DbOpenHelper(Context context) {
        super(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION);
        cmndef = new CommonDef();
    }

	/**
	* データベースの初期化を行う。
	* @param SQLiteDatabase データベース名
	*/
    @Override
    public void onCreate(SQLiteDatabase db) {
    	try {
        	db.execSQL(DbConstants.CREATE_TABLE1);
            db.execSQL(DbConstants.CREATE_TABLE2);
            db.execSQL(DbConstants.CREATE_TABLE3);
            db.execSQL(DbConstants.CREATE_TABLE4);
    	} catch (SQLException e) {
    		// テーブルの作成に失敗した場合
    		Log.e("ERROR", cmndef.DB_ERROR_MSG1);
    		
    	}
    }

    /**
	* データベースのバージョンアップ時の処理
	* @param SQLiteDatabase データベース名
	* @param oldVersion 旧データベースバージョン
	* @param newVersion 新データベースバージョン
	*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    	Log.d("DEBUG", "DbOpenHelper onUpgrade called.");
    	//if( oldVersion == 1 && newVersion == 2 ){
        	String [] DATABASE_UPDATE = {DbConstants.DATABASE_UPDATE1,
        	         DbConstants.DATABASE_UPDATE2,
        		     DbConstants.DATABASE_UPDATE3,
        		     DbConstants.DATABASE_UPDATE4};
        	
        	try {
        		for (int i = 0; i < DATABASE_UPDATE.length; i++) {
    	        	db.execSQL(DATABASE_UPDATE[i]);
    	        }
        	} catch (SQLException e) {
        		// データベースのバージョンアップに失敗した場合
        		Log.e("ERROR", cmndef.DB_ERROR_MSG2);
        	}
	        onCreate(db);
        //}
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d("DEBUG", "DbOpenHelper onDowngrade called.");
    	
    	// onUpgradeを読ぶ
    	onUpgrade(db, oldVersion, newVersion);
    }
}