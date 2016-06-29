package com.elb.hnist.application;

import android.app.Application;

public class MyApplication extends Application{
	
	private String dianCaiUrl;
	private String waiMaiUrl;
	

	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		this.setDianCaiUrl("http://192.168.191.1/elm/fandian.json");
		this.setWaiMaiUrl("http://192.168.191.1/elm/canguan.json");
	}
	
	public String getDianCaiUrl() {
		return dianCaiUrl;
	}


	public void setDianCaiUrl(String dianCaiUrl) {
		this.dianCaiUrl = dianCaiUrl;
	}


	public String getWaiMaiUrl() {
		return waiMaiUrl;
	}


	public void setWaiMaiUrl(String waiMaiUrl) {
		this.waiMaiUrl = waiMaiUrl;
	}


}
