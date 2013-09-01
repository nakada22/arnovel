package com.tetuo41.locanovel.stageselect;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * ステージ毎の物語(ノベル)等の状態を持つ
 * @author HackathonG
 */
public class StageSelectState implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 写真(ステージ画像) */ 
	private byte[] photo_img;
	
	/** ステージタイトル */ 
	private String stage_title;
	
	/** ノベルデータ(あらすじ) */ 
	private String outline;

	/** ノベルデータ(全文) */ 
	private String alloutline;

	/** 住所 */ 
	private String address;
	
	/** ステージID */ 
	private int stage_id;
	
	/** 経度 */ 
	private double longitude;
	
	/** 緯度 */ 
	private double latitude;
	
	/** ノベル導入文１ */ 
	private String novel_intro1;
	
	/** ノベル導入文２ */ 
	private String novel_intro2;
	
	/** ノベル導入文３ */ 
	private String novel_intro3;
	
	public void setPhoto(byte[] _photo_img) {
		this.photo_img = _photo_img;
	}
	public byte[] getPhoto() {
		return this.photo_img;
	}
	
	public void setStageTitle(String _stage_title) {
		this.stage_title = _stage_title;
	}
	public String getStageTitle() {
		return this.stage_title;
	}
	
	public void setOutLine(String _outline) {
		this.outline = _outline;
	}

	public String getOutLine() {
		return this.outline;
	}
	
	public void setAllOutLine(String _alloutline) {
		this.alloutline = _alloutline;
	}

	public String getAllOutLine() {
		return this.alloutline;
	}
	
	public void setAddress(String _address) {
		this.address = _address;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setStageId(int _stage_id) {
		this.stage_id = _stage_id;
	}
	
	public int getStageId() {
		return this.stage_id;
	}

	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setNovelIntro1(String _novel_intro1) {
		this.novel_intro1 = _novel_intro1;
	}
	
	public String getNovelIntro1() {
		return this.novel_intro1;
	}
	
	public void setNovelIntro2(String _novel_intro2) {
		this.novel_intro2 = _novel_intro2;
	}
	
	public String getNovelIntro2() {
		return this.novel_intro2;
	}
	
	public void setNovelIntro3(String _novel_intro3) {
		this.novel_intro3 = _novel_intro3;
	}
	
	public String getNovelIntro3() {
		return this.novel_intro3;
	}
}