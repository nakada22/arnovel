package com.tetuo41.arnovel.db;

import java.text.DecimalFormat;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
		//helper.getWritableDatabase();
		
		if (db != null) db.close();
		SQLiteDatabase.openDatabase(DbConstants.DB_PATH + DbConstants.DATABASE_NAME, 
				null, SQLiteDatabase.OPEN_READWRITE);
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
				// TODO ノベルIDがあれば、そのノベルデータをUpdateする
				
				
			} else {
				// ノベルIDがない場合、全てをInsert
				ContentValues cv = new ContentValues();
				cv.put("novel_id", key);
				cv.put("stage_id", key); // ノベルIDと同じIDを登録
				cv.put("longitude", data.get(0));
				cv.put("latitude", data.get(1));
				cv.put("novel_title", data.get(2));
				cv.put("novel_data", data.get(3));
				db.insert(DbConstants.TABLE2, null, cv);
				
			}
		} catch (RuntimeException e){
			
		}
		
	}
	
}