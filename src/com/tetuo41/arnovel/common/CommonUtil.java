package com.tetuo41.arnovel.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	 * @return Map<String, List<String>> データ
	 */
	public Map<String, List<String>> ReadCsvFile(String u, Context context) 
			throws IOException, MalformedURLException, RuntimeException {
		
		// HashMapからMapのインスタンスを生成(戻り値用文字列 )
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		
		// ステージリスト
		List<String> stage_list = new ArrayList<String>();
		
		try {
			// URLクラスのインスタンス作成・コネクションを開く
			URLConnection con = new URL(u).openConnection();
			
			// 接続先のデータを取得
			InputStream is = con.getInputStream();

			// 取得した文字列を文字列にして返す。
			BufferedReader input = new BufferedReader(new InputStreamReader(is,
					"Shift_JIS"));
			String line = "";

			// 1,stage1.png,東京都新宿区左門町17
			while ((line = input.readLine()) != null) {
				
				// 1行をデータの要素に分割
				StringTokenizer st = new StringTokenizer(line, ",");
				String key = null;
				int cnt = 0;
				
				// トークンの出力
				while (st.hasMoreTokens()) {
					cnt++;
					if (cnt == 1) {
						// 最初の要素(ID)の場合をKEYとし値を保持しておく
						key = st.nextToken();
						continue;
					}
					stage_list.add(st.nextToken());	
				}
				ret.put(key, stage_list);
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