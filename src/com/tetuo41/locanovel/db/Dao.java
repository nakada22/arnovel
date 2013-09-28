package com.tetuo41.locanovel.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Dao {
	
    /** DB作成オブジェクトをインスタンス化 */
	private DbOpenHelper helper = null;
	SQLiteDatabase db;
	
	/** 現在日時オブジェクト(yyyy/MM/dd HH:mm:ss形式) */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
    /** 
     * コンストラクタ
     * @param Context コンテキスト
    　　　*/
	public Dao(Context context) {
		helper = new DbOpenHelper(context);
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
			String SQL = "SELECT mn.stage_id, mn.novel_title, mn.novel_data, " +
					" mss.address, mn.longitude, mn.latitude, mn.novel_intro1, " +
					" mn.novel_intro2, mn.novel_intro3 FROM mst_novel mn, " +
					" mst_stage_select mss WHERE mn.stage_id=mss.stage_id";
			
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
					data_list.add(c.getString(2)); // ノベルデータ格納(あらすじ)
					data_list.add(c.getString(3)); // 住所格納
					data_list.add(c.getString(4)); // 経度
					data_list.add(c.getString(5)); // 緯度
					data_list.add(c.getString(6)); // ノベル導入部分1
					data_list.add(c.getString(7)); // ノベル導入部分2
					data_list.add(c.getString(8)); // ノベル導入部分3
					
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
					
					// ノベルデータ格納(あらすじ)
					if (c.getString(3).length() >= 90) {
						// 90文字以上であれば90文字まで取得
						data_list.add(c.getString(3).substring(0, 90)
								+ "・・・");
					} else {
						data_list.add(c.getString(3));
					}
					
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
	
	/**
	 * ノベル表示画面で「読了」ボタン押下時のアップデート処理
	 * Stampフラグのアップデート処理
	 * @param ステージID
	 * 
	 * */
	public void StampFlgUpdate(int stage_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		/** スタンプラリーマスタのスタンプフラグを「1」へUpdate */
		try {
			Cursor c = db.rawQuery("SELECT ms.stamp_flg FROM " +
					DbConstants.TABLE1 + " ms WHERE ms.stage_id=?", 
					new String[]{String.valueOf(stage_id)});
			ContentValues cv = new ContentValues();
			
			// 現在日時取得
			String currenttime = sdf.format(Calendar.getInstance().getTime());
			
			if (c.moveToFirst()){
				String stamp_flg = c.getString(0);
				
				if (stamp_flg != "1") {
					// スタンプフラグが既に1でない場合は、Update
					cv.put(DbConstants.CLM_STAMP_FLG, 1);
					cv.put(DbConstants.CLM_UPDATE_DATE, currenttime);
					db.update(DbConstants.TABLE1, cv, DbConstants.CLM_STAGE_ID+"=?",
							new String[]{String.valueOf(stage_id)});
				}
			}
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
	}
	
	/**
	 * ステージ選択時、スタンプフラグを確認するための処理
	 * @param ステージID
	 * @return true：スタンプフラグが1の場合 false：スタンプフラグが1以外の場合
	 * 
	 * */
	public boolean StampFlgCheck(int stage_id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		
		/** スタンプラリーマスタのスタンプフラグを確認する */
		try {
			Cursor c = db.rawQuery("SELECT ms.stamp_flg FROM " +
					DbConstants.TABLE1 + " ms WHERE ms.stage_id=?", 
					new String[]{String.valueOf(stage_id)});
			
			if (c.moveToFirst()){
				// ステージIDに対応するデータがあれば
				String stamp_flg = c.getString(0);
				Log.d("DEBUG",stamp_flg);
				
				if (stamp_flg.equals("1")) {
					// スタンプフラグが1の場合はtrueを返却
					return true;
				}
			}
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		return false;
	}
	
	/**
	 * 撮影時の位置情報判定処理がＯＫだった場合にノベルマスタに
	 * 背景画像名を登録する
	 * @param stage_id ステージID
	 * @param locate_img_name 背景画像名
	 * 
	 * */
	public void RegisteLocateImg(int stage_id, String locate_img_name) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
		
		/** ノベルマスタのnovel_idを確認する */
		try {
			Cursor c = db.rawQuery("SELECT mn.novel_id FROM " +
					DbConstants.TABLE2 + " mn WHERE mn.stage_id=? " +
							"AND mn.novel_id=mn.stage_id", 
					new String[]{String.valueOf(stage_id)});
			ContentValues cv = new ContentValues();
			
			if (c.moveToFirst()){
				String novel_id = c.getString(0);
				Log.d("DEBUG","ノベルIDがある場合、背景画像名Update");
				// ノベルIDがある場合、背景画像名Update
				cv.put(DbConstants.CLM_LOCATE_IMG_NAME, locate_img_name);
				db.update(DbConstants.TABLE2, cv, DbConstants.CLM_NOVEL_ID+"=? ",
						new String[]{novel_id});
				
			}
			
		} catch (SQLiteException e) {
			// 背景画像名の登録に失敗した場合
			Log.e("ERROR", e.toString());
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		
	}
	
	/**
	 * ノベルIDに対応する背景画像名を取得する
	 * @param stage_id ステージID
	 * @return locate_img_name 撮影画像名
	 * 
	 * */
	public String GetLocateImgName(int stage_id) {
		
		SQLiteDatabase db = helper.getReadableDatabase();
		
		/** ノベルマスタのlocate_img_nameを確認する */
		try {
			Cursor c = db.rawQuery("SELECT mn.locate_img_name FROM " +
					DbConstants.TABLE2 + " mn WHERE mn.stage_id=? " +
							"AND mn.novel_id=mn.stage_id", 
					new String[]{String.valueOf(stage_id)});
			
			if (c.moveToFirst()){
				// ノベルIDがある場合
				String locate_img_name = c.getString(0);
				return locate_img_name;
			}
			
		} catch (SQLiteException e) {
			// 背景画像名の取得に失敗した場合
			Log.e("ERROR", e.toString());
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		return null;
		
	}
	
	
	/**
	 * カメラプレビュー起動時における更新された位置情報を登録する
	 * @param longitude 更新された経度
	 * @param latitude 更新された緯度
	 * @return true：同じ経度・緯度がある場合/false：同じ経度・緯度がない場合
	 * 
	 * */
	public boolean RegisteUpdateLocate(double longitude, double latitude) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		/** 位置情報マスタに更新された位置情報を登録する */
		try {
			Cursor c = db.query(DbConstants.TABLE4,
					new String[] { "count(regist_date)" },
					"longitude=? AND latitude=?",
					new String[] {String.valueOf(longitude), String.valueOf(latitude)},
					null, null, null, null);
			
			// 現在日時取得
			String currenttime = sdf.format(Calendar.getInstance().getTime());
			
			c.moveToFirst();
			if(c.getInt(0) == 0) {
				// 同じ経度・緯度がなければ登録する
				ContentValues cv = new ContentValues();
				cv.put(DbConstants.CLM_LONGITUDE, longitude);
				cv.put(DbConstants.CLM_LATITUDE, latitude);
				cv.put(DbConstants.CLM_REGIST_DATE, currenttime);
				db.insert(DbConstants.TABLE4, null, cv);
				return true;
				
			}
		} catch (SQLiteException e) {
			// 更新された位置情報の登録に失敗した場合
			Log.e("ERROR", e.toString());
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		
		// 同じ経度・緯度がない場合
		return false;
	}
	
	/**
	 * 位置情報判定処理でOKだった場合の最新の位置情報取得する
	 * @return 経度・緯度情報の配列
	 * */
	public ArrayList<Double> GetNewestLocate() {
		
		SQLiteDatabase db = helper.getReadableDatabase();
		
		// 返却値格納用配列
		ArrayList<Double> ret = new ArrayList<Double>();

		/** 位置情報マスタに最新の更新された位置情報を登録する */
		try {
			Cursor c = db.rawQuery("SELECT longitude, latitude" +
					" FROM " + DbConstants.TABLE4 + " WHERE regist_date = " +
					"(SELECT MAX(regist_date) FROM " + DbConstants.TABLE4 + ")", 
					new String[]{});
			
			if (c.moveToFirst()){
				// 位置情報があれば格納
				ret.add(c.getDouble(0)); // 経度 longitude
				ret.add(c.getDouble(1)); // 緯度 latitude
			} else {
				// なければ
				ret.add(0.0); // 経度 longitude
				ret.add(0.0); // 緯度 latitude
			}
			
		} catch (SQLiteException e) {
			// 更新された位置情報の登録に失敗した場合
			Log.e("ERROR", e.toString());
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}
		
		return ret;
	}
	
	/**
	 * 位置情報マスタのデータを初期化する
	 * 
	 * */
	public void DeleteLocateMaster() {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			db.delete(DbConstants.TABLE4, null, null);
		} catch (SQLiteException e) {
			// 位置情報マスタのデータを初期化に失敗した場合
			Log.e("ERROR", e.toString());
			
		} catch (RuntimeException e){
			Log.d("DEBUG",e.toString());
		} finally {
			db.close();
		}

	}
}