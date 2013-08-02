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
	 * */
	public void InitDataInsert(String key, List<String> data){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] put_table = new String[]{"mst_novel","mst_stage_select"};
		
		try {
			
			// TODO ノベルマスタのDB登録
			Cursor c = db.rawQuery("SELECT mn.novel_id FROM " +
					put_table[0] + " mn WHERE mn.novel_id=?", 
					new String[]{key});
			
			if (c.moveToFirst()){
				Log.d("debug","c.moveToFirst()");
				// ノベルIDがある場合、Update
				
			} else {
				Log.d("debug","c.moveToFirst() else");
				// ノベルIDがない場合、全てをInsert
				ContentValues cv = new ContentValues();
				cv.put(DbConstants.CLM_NOVEL_ID, key);
				cv.put(DbConstants.CLM_STAGE_ID, key); // ノベルIDと同じIDを登録
				cv.put(DbConstants.CLM_LONGITUDE, data.get(0));
				cv.put(DbConstants.CLM_LATITUDE, data.get(1));
				cv.put(DbConstants.CLM_NOVEL_TITLE, data.get(2));
				cv.put(DbConstants.CLM_NOVEL_INTRO1, data.get(3));
				cv.put(DbConstants.CLM_NOVEL_INTRO2, data.get(4));
				cv.put(DbConstants.CLM_NOVEL_INTRO3, data.get(5));
				cv.put("novel_data", data.get(6));
				db.insert(DbConstants.TABLE2, null, cv);
				
			}
		} catch (RuntimeException e){
			
		} finally {
			db.close();
		}
		
	}
	
}