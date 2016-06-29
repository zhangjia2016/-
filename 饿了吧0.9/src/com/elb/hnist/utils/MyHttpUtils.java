package com.elb.hnist.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MyHttpUtils {
	
	public void getDataFromServe(HttpRequest.HttpMethod method,String url)
	{
		
		HttpUtils httpUtils = new HttpUtils();
		
		httpUtils.send(method, url, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = responseInfo.result;
				
				paraseData(result);
				
			}

			

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				
			}
			
			
		});	
	}
	private void paraseData(String result) {
		// TODO Auto-generated method stub
		
	}
}
