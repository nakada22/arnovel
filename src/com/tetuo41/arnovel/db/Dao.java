package com.tetuo41.arnovel.db;

import java.text.DecimalFormat;

import android.content.Context;
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
		helper.getWritableDatabase();
	}

	
}