package com.elb.hnist.viewpager.base;



import com.elb.hnist.application.MyApplication;

import android.app.Activity;
import android.view.View;

public class DianCaiPagerBase {
	
	public Activity mActivity;
	public MyApplication application;
	
	
	
	public DianCaiPagerBase(Activity activity)
	{
		this.mActivity = activity;
		this.application = (MyApplication) mActivity.getApplication();
	}
	
	
	public View initViews(){return null;}
	
	public void initDatas(){}
}
