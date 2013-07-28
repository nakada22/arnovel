package com.tetuo41.arnovel.db;

import java.text.DecimalFormat;

import android.content.Context;

import com.tetuo41.arnovel.common.CommonUtil;

public class Dao {
	
	/** FTP情報を記述する */ 
	String sHost = "sashihara.web.fc2.com";
    String sUser = "sashihara";
    String sPass = "test123";
    String sDir = "/";
    int    nPort = 21;
    
    /** DB作成オブジェクトをインスタンス化 */
	private DbOpenHelper helper = null;
	
	/** 共通クラスインスタンス化 */
	private CommonUtil cmnutil;
	
	/** DecimalFormat */
	DecimalFormat df5 = new DecimalFormat("00000");
	
    /** 
     * コンストラクタ
     * @param Context コンテキスト
    　　　*/
	public Dao(Context context) {
		helper = new DbOpenHelper(context);
		cmnutil = new CommonUtil();
	}

	
}