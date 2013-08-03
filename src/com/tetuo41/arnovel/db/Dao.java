package com.tetuo41.arnovel.db;

import java.text.DecimalFormat;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tetuo41.arnovel.common.CommonUtil;

public class Dao {
	
    /** DB作成オブジェクトをインスタンス化 */
	private DbOpenHelper helper = null;
	
	/** 共通クラスインスタンス化 */
	private CommonUtil cmnutil;
	
	/** DecimalFormat */
	DecimalFormat df5 = new DecimalFormat("00000");
	
	SQLiteDatabase db;
	
    /** 
     * コンストラクタ
     * @param Context コンテキスト
    　　　*/
	public Dao(Context context) {
		helper = new DbOpenHelper(context);
		cmnutil = new CommonUtil();
	}
	
	/**
	 * アプリ起動時における初期データを投入する
	 * @param key ID(ノベルID, ステージID)
	 * @param data 登録データ
	 * @param table 登録テーブル名
	 * */
	public void InitDataInsert(String key, List<String> data, String table){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		if (table.equals(DbConstants.TABLE2)) {
			
			/** ノベルマスタのDB登録 */
			try {
				Cursor c = db.rawQuery("SELECT mn.novel_id FROM " +
						DbConstants.TABLE2 + " mn WHERE mn.novel_id=?", 
						new String[]{key});
				ContentValues cv = new ContentValues();
				
				if (c.moveToFirst()){
					
					Log.d("DEBUG","ノベルマスタ c.moveToFirst()");
					// ノベルIDがある場合、Update
					cv.put(DbConstants.CLM_LONGITUDE, data.get(0));
					cv.put(DbConstants.CLM_LATITUDE, data.get(1));
					cv.put(DbConstants.CLM_NOVEL_TITLE, data.get(2));
					cv.put(DbConstants.CLM_NOVEL_INTRO1, data.get(3));
					cv.put(DbConstants.CLM_NOVEL_INTRO2, data.get(4));
					cv.put(DbConstants.CLM_NOVEL_INTRO3, data.get(5));
					cv.put(DbConstants.CLM_NOVEL_DATA, data.get(6));
					db.update(DbConstants.TABLE2, cv, DbConstants.CLM_NOVEL_ID+"=? ",
							new String[]{key});
					
				} else {
					
					Log.d("DEBUG","c.moveToFirst() else");
					// ノベルIDがない場合、全てをInsert
					cv.put(DbConstants.CLM_NOVEL_ID, key);
					cv.put(DbConstants.CLM_STAGE_ID, key); // ノベルIDと同じIDを登録
					cv.put(DbConstants.CLM_LONGITUDE, data.get(0));
					cv.put(DbConstants.CLM_LATITUDE, data.get(1));
					cv.put(DbConstants.CLM_NOVEL_TITLE, data.get(2));
					cv.put(DbConstants.CLM_NOVEL_INTRO1, data.get(3));
					cv.put(DbConstants.CLM_NOVEL_INTRO2, data.get(4));
					cv.put(DbConstants.CLM_NOVEL_INTRO3, data.get(5));
					cv.put(DbConstants.CLM_NOVEL_DATA, data.get(6));
					db.insert(DbConstants.TABLE2, null, cv);
					
				}
			} catch (RuntimeException e){	
			} finally {
				db.close();
			}
			
		} else if (table.equals(DbConstants.TABLE3)) {
			
			/** ステージセレクトマスタのDB登録 */
			try {
				Cursor c = db.rawQuery("SELECT mss.stage_id FROM " +
						DbConstants.TABLE3 + " mss WHERE mss.stage_id=?", 
						new String[]{key});
				ContentValues cv = new ContentValues();
				
				if (c.moveToFirst()){
					
					Log.d("DEBUG","ステージセレクトマスタ c.moveToFirst()");
					// ステージIDがある場合、Update
					cv.put(DbConstants.CLM_IMAGE_NAME, data.get(0));
					cv.put(DbConstants.CLM_ADDRESS, data.get(1));
					db.update(DbConstants.TABLE3, cv, DbConstants.CLM_STAGE_ID+"=? ",
							new String[]{key});
					
				} else {
					
					Log.d("DEBUG","ステージセレクトマスタ c.moveToFirst() else");
					// ステージIDがない場合、全てをInsert
					cv.put(DbConstants.CLM_STAGE_ID, key);
					cv.put(DbConstants.CLM_IMAGE_NAME, data.get(0));
					cv.put(DbConstants.CLM_ADDRESS, data.get(1));
					db.insert(DbConstants.TABLE3, null, cv);
				}
			} catch (RuntimeException e){
			} finally {
				db.close();
			}
		}
	}
}