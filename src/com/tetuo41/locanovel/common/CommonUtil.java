package com.tetuo41.locanovel.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;

/**
 * 全クラス共通で使用するメソッド等を 規定するユーティリティークラスです。
 * 
 * @author HackathonG
 * @version 1.0
 */
public class CommonUtil {

	/**
	 * コンストラクタ 　　　
	 */
	public CommonUtil() {
		// 処理なし
	}

	/**
	 * 外部サーバーよりCSVファイルを読み込む
	 * 
	 * @param u
	 *            URL
	 * @param Context
	 *            コンテキスト
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ConnectException
	 * @return Map<String, List<String>> データ
	 */
	public Map<String, List<String>> ReadCsvFile(String u, Context context)
			throws MalformedURLException, IOException, RuntimeException {

		// LinkedHashMapからMapのインスタンスを生成(要素の挿入順を維持)
		Map<String, List<String>> ret = new LinkedHashMap<String, List<String>>();

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

				// ステージリスト初期化
				List<String> stage_list = new ArrayList<String>();

				// トークンの出力
				for (int i = 0; 0 < st.countTokens(); i++) {
					if (i == 0) {
						key = st.nextToken();
					} else {
						stage_list.add(st.nextToken());
					}
				}
				ret.put(key, stage_list);

			}
			input.close();
			return ret;

		} catch (MalformedURLException e) {
			// URLが間違っている場合
			throw new MalformedURLException(e.toString());

		} catch (IOException e) {
			// CSVファイル読み込み失敗したとき
			throw new IOException(e.toString());

		} catch (RuntimeException e) {
			// インターネットに接続できなかった場合
			throw new RuntimeException(e.toString());

		}
	}

}