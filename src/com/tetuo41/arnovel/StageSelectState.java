package com.tetuo41.arnovel;

import java.io.Serializable;

/**
 * ステージ毎の物語(ノベル)等の状態を持つ
 * @author HackathonG
 */
public class StageSelectState implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 写真のURL */ 
	private String photo_url;
	
	/** ステージタイトル */ 
	private String stage_title;
	
	/** あらすじ */ 
	private String outline;

	/** 住所 */ 
	private String address;
	
	public void setPhotoUrl(String _photo_url) {
		this.photo_url = _photo_url;
	}
	public String getPhotoUrl() {
		return this.photo_url;
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
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String _address) {
		this.address = _address;
	}
}