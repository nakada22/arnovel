package com.tetuo41.arnovel.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
			} catch (SQLiteException e) {
				// 初期データDB登録に失敗した場合
				Log.e("ERROR", e.toString());
				
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
			} catch (SQLiteException e) {
				// 初期データDB登録に失敗した場合
				Log.e("ERROR", e.toString());
				
			} catch (RuntimeException e){
			} finally {
				db.close();
			}
		} else if (table.equals(DbConstants.TABLE1)) {
			
			/** スタンプラリーマスタ(mst_stamp_rally)登録 */ 
			try {
				// ステージセレクトマスタのデータ差分をデータとして登録
				Cursor c = db.rawQuery(
						"SELECT mss.stage_id FROM " + DbConstants.TABLE3 + 
						" mss WHERE mss.stage_id NOT IN (SELECT msr.stage_id FROM " + 
								DbConstants.TABLE1 + " msr)", null);
				ContentValues cv = new ContentValues();
				
				if (c.moveToFirst()){
					Log.d("DEBUG","スタンプラリーマスタ差分登録 c.moveToFirst()");
					// 基本はアプリ2回目起動時以降(差分のステージIDのみ登録)
					int rowcount = c.getCount(); // 件数
					
					for (int i = 0; i < rowcount ; i++) {
						cv.put(DbConstants.CLM_STAMP_ID, c.getString(0));
						cv.put(DbConstants.CLM_STAGE_ID, c.getString(0));
						cv.put(DbConstants.CLM_STAMP_FLG, 0);
						db.insert(DbConstants.TABLE1, null, cv);

						// 次の行へ
						c.moveToNext();
					}
				} else {
					Log.d("DEBUG","スタンプラリーマスタ Else c.moveToFirst()");
					// 差分0件の場合、登録しない
					
				}
				
			} catch (SQLiteException e) {
				// 初期データDB登録に失敗した場合
				Log.e("ERROR", e.toString());
				
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
		
		// データ格納用(昇順)
		List<List<String>> ret = new LinkedList<List<String>>();
		
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
					// 必要な情報：データ格納のリスト件数、ステージタイトル、あらすじ、住所
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
	
	/**
	 * スタンプ一覧画面で表示するデータを取得する。
	 * @return ステージID別データ
	 * 
	 * */
	public List<List<String>> StampRecordData() {
		//　ステージID, スタンプフラグ、ステージタイトル、ノベルデータをDBから取得・セット
		SQLiteDatabase db = helper.getReadableDatabase();
		
		// データ格納用(昇順)
		List<List<String>> ret = new LinkedList<List<String>>();
		
		/** スタンプ一覧画面での表示データを取得 */
		try {
			// SQL文生成
			String SQL = "SELECT mn.stage_id, msr.stamp_flg, " +
					" mn.novel_title, mn.novel_data" +
					" FROM mst_novel mn, mst_stage_select mss, " +
					" mst_stamp_rally msr WHERE mn.stage_id=mss.stage_id AND " + 
					" mn.stage_id=msr.stage_id GROUP BY mn.stage_id";
			
			Cursor c = db.rawQuery(SQL, null);
			int rowcount = c.getCount(); // 件数
			
			if (c.moveToFirst()){
				Log.d("DEBUG",this.getClass().getName()+"【データがあれば】");
				
				// データがあれば
						
				Log.d("DEBUG",this.getClass().getName() + rowcount);
				for (int i = 0; i < rowcount; i++) {
					// 表示データリスト
					List<String> data_list = new ArrayList<String>();
					
					data_list.add(c.getString(0)); // ステージID格納
					data_list.add(c.getString(1)); // スタンプフラグ格納
					data_list.add(c.getString(2)); // ノベルタイトル格納
					data_list.add(c.getString(3).substring(0, 90)
							+ "・・・"); // ノベルデータ格納(あらすじ)
					// データ格納
					ret.add(data_list);
					
					// 次の行へ
					c.moveToNext();
				}
				// 表示データがない件数分、空のデータを入れておく。
				//for (int i = 0; i < 9 - rowcount; i++) {
				//	ret.add(new ArrayList<String>());
				//}
					
			} else {
				// データが0件の場合
				Log.d("DEBUG",this.getClass().getName()+"【データが0件の場合】");
				ret.add(null);
			}
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		
		
		return ret;
	}
	
}