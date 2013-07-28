package com.tetuo41.arnovel.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;


/**
 * 全クラス共通で使用するメソッド等を
 * 規定するユーティリティークラスです。
 * @author HackathonG
 * @version 1.0
 */
public class CommonUtil {
	
	/** 
     * コンストラクタ
    　　　*/
	public CommonUtil(){
		// 処理なし
	}

	/** 
     * キャッシュディレクトリ内のファイル生成する。
     * @param Context コンテキスト
     * @param file_name ファイル名
    　　　*/
	public static File getDiskCacheDir(Context context, String file_name) {  
	    final String cachePath = context.getCacheDir().getPath();  
	    return new File(cachePath + File.separator + file_name);  
	}
	
	/**
	 * 外部サーバーよりCSVファイルを読み込む
	 * @param u URL
	 * @param Context コンテキスト
	 * @throws IOException,MalformedURLException,RuntimeException
	 */
	public Map<String, String> doNet(String u, Context context) 
			throws IOException,MalformedURLException,RuntimeException {
		// HashMapからMapのインスタンスを生成(戻り値用文字列 )
		Map<String, String> ret = new HashMap<String, String>();

		try {
			URL url = new URL(u); // URLクラスのインスタンス作成
			URLConnection con = url.openConnection(); // コネクションを開く。
			InputStream is = con.getInputStream(); // 接続先のデータを取得

			// 取得した文字列を文字列にして返す。
			BufferedReader input = new BufferedReader(new InputStreamReader(is,
					"Shift_JIS"));
			String line = "";

			while ((line = input.readLine()) != null) {
				// 1行をデータの要素に分割
				StringTokenizer st = new StringTokenizer(line, ",");

				// トークンの出力
				while (st.hasMoreTokens()) {
					ret.put(st.nextToken(), st.nextToken());
				}
			}
			input.close();
			return ret;

		} catch (MalformedURLException e) {
			// URLが間違っている場合
			throw e;
			
		} catch (IOException e) {
			// CSVファイル読み込み失敗したとき
			throw e;
			
		} catch (RuntimeException e) {
			// インターネットに接続できなかった場合
			throw e;
		}
	}
}