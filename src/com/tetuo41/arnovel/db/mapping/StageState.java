package com.tetuo41.arnovel.db.mapping;

import java.io.Serializable;

/**
 * ステージ毎の物語(ノベル)等の状態を持つ
 */
public class StageState implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 写真 */ 
	private String photo;
	
	/** タイトル */ 
	private String title;
	
	/** あらすじ */ 
	private String outline;

	/** 住所 */ 
	private String address;
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPhoto() {
		return this.photo;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}
	
	public void setOutline(String outline) {
		this.outline = outline;
	}

	public String getOutline() {
		return this.outline;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}