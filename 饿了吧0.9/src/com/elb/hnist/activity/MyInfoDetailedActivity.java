package com.elb.hnist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.elb.hnist.R;

public class MyInfoDetailedActivity extends Activity {
	
	private FrameLayout flMyInfo;
	private TextView myInfoTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		initView();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.my_info_detailed_layout);
		
		flMyInfo = (FrameLayout) findViewById(R.id.id_fl_my_info_detailed);
		myInfoTitle = (TextView) findViewById(R.id.id_tv_my_info_title);
		
		initDatas();
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		super.finish();

		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}

}
