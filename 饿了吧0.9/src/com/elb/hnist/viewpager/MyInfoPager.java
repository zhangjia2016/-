package com.elb.hnist.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.elb.hnist.R;
import com.elb.hnist.domain.MyInfoBean;
import com.elb.hnist.viewpager.base.DianCaiPagerBase;


public class MyInfoPager extends DianCaiPagerBase {
	

	private ListView listMyInfo;
	
	
	
	private List<MyInfoBean> myInfoList;
	
	
	private BaseAdapter adapter;


	public MyInfoPager(Activity activity) {
		super(activity);
		myInfoList = new ArrayList<MyInfoBean>();
		adapter = new MyAdapter();
		
	}
	
	
	public View initViews()
	{
		View view = View.inflate(mActivity, R.layout.myinfo_layout, null);
		
		listMyInfo = (ListView) view.findViewById(R.id.id_lv_my_info);
		
		listMyInfo.setAdapter(adapter);
		
		
		return view;
	}
	
	public void initDatas()
	{
		myInfoList.clear();
		
		String[] operations = mActivity.getResources().getStringArray(R.array.operation);
		
		for(int i = 0; i<operations.length; i++)
		{
			MyInfoBean infoBean = new MyInfoBean(operations[i]);
			
			myInfoList.add(infoBean);
		}
		
		adapter.notifyDataSetChanged();
	}
	
	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			View itemView = View.inflate(mActivity, R.layout.info_item_layout, null);
			
			TextView textView = (TextView) itemView.findViewById(R.id.id_tv_info_item);
		
			textView.setText(myInfoList.get(position).getOperation());
			
			
			
			return itemView;
		}
		
	}
	
}
