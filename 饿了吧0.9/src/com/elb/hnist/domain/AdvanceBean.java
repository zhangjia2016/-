package com.elb.hnist.domain;


/**
 * 广告ViewPager的实体类
 * @author dell zhang
 *
 */
public class AdvanceBean {
	private int imageSourceId;
	private String intro;
	
	
	public AdvanceBean(int imageSourceId, String intro) {
		super();
		this.imageSourceId = imageSourceId;
		this.intro = intro;
	}
	public int getImageSourceId() {
		return imageSourceId;
	}
	public void setImageSourceId(int imageSourceId) {
		this.imageSourceId = imageSourceId;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
}
