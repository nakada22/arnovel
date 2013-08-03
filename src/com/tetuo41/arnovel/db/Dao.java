package com.tetuo41.arnovel.db;

import java.util.ArrayList;
import java.util.LinkedList;
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
	
	
	/**
	 * ステージセレクト画面での表示データを取得する。
	 * @return データリスト
	 * */
	public List<List<String>> StageSelctData(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		//[
		// [1, 四谷 於岩稲荷田宮神社（お岩稲荷）, 外苑東通りから５０メートル程入・・・, 東京都新宿区左門町17], 
		// [2, 平将門首塚, 東京丸の内、日本一の近代的オフ・・・, 東京都千代田区大手町1-1-1], 
		// [3, 道了堂跡, 鑓水という町の由来は寒村だった・・・, 東京都八王子市鑓水]
		//]

		// データ格納用(昇順)
		List<List<String>> ret = new LinkedList<List<String>>();
		
		// 戻り値：データ格納のリストの件数,データリスト
		// 必要な情報：データ格納のリスト件数、ステージタイトル、あらすじ、住所
		// あらすじは、ノベルデータから取得
		// 御互いのステージIDが一致するデータを取得
		
		/** ステージセレクト画面での表示データを取得 */
		try {
			// SQL文生成
			String SQL = "SELECT mn.stage_id, mn.novel_title, mn.novel_data," +
					" mss.address FROM mst_novel mn, mst_stage_select mss " +
					"WHERE mn.stage_id=mss.stage_id";
			
			Cursor c = db.rawQuery(SQL, null);
			
			if (c.moveToFirst()){
				// データがあれば
				int rowcount = c.getCount(); // 件数
				
				for (int i = 0; i < rowcount ; i++) {
					// 表示データリスト
					List<String> data_list = new ArrayList<String>();

					data_list.add(c.getString(0)); // ステージID格納
					data_list.add(c.getString(1)); // ノベルタイトル格納
					data_list.add(c.getString(2).substring(0, 30)
							+ "・・・"); // ノベルデータ格納(あらすじ)
					data_list.add(c.getString(3)); // 住所格納
					
					// データ格納
					ret.add(data_list);
					
					// 次の行へ
					c.moveToNext();
				}
			} else {
				// データが0件の場合
				ret.add(null);
			}
			
		} catch (RuntimeException e){
		} finally {
			db.close();
		}
		return ret;
		
	}
}