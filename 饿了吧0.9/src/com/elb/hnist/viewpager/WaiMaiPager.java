package com.elb.hnist.viewpager;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.elb.hnist.R;
import com.elb.hnist.activity.RestaurantActivity;
import com.elb.hnist.domain.WaiMaiData;
import com.elb.hnist.domain.WaiMaiData.CanGuan;
import com.elb.hnist.view.RefreshListView;
import com.elb.hnist.view.RefreshListView.OnRefreshListener;
import com.elb.hnist.viewpager.base.DianCaiPagerBase;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class WaiMaiPager extends DianCaiPagerBase {
	

	

	private RefreshListView listViewWaiMai;

	private RelativeLayout progress;

	private ArrayList<CanGuan> canGuanList;
	private WaiMaiData waiMaiData;
	private BaseAdapter adapter;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();

			listViewWaiMai.completeRefresh();
		}

	};

	public WaiMaiPager(Activity activity) {
		super(activity);
		
		initViews();
		adapter = new MyAdapter();
	}

	public View initViews() {
		View view = View
				.inflate(mActivity, R.layout.diancai_pager_layout, null);
		listViewWaiMai = (RefreshListView) view
				.findViewById(R.id.id_lv_diancai);
		progress = (RelativeLayout) view.findViewById(R.id.id_rl_progress);

		listViewWaiMai.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getRefreshData();
			}

			@Override
			public void onLoadMore() {
				getLoadMoreData();
			}

		});

		return view;
	}

	private void getLoadMoreData() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);

					canGuanList.get(canGuanList.size() - 1).title = "加载更多的数据";

					handler.sendEmptyMessage(0x110);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	private void getRefreshData() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);

					canGuanList.get(0).title = "新餐馆";

					handler.sendEmptyMessage(0x110);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return canGuanList.size();
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
			View view;
			ViewHolder holder;
			if (null == convertView) {
				view = View.inflate(mActivity, R.layout.waimai_listview_item,
						null);
				holder = new ViewHolder();
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			holder.imageView = (ImageView) view
					.findViewById(R.id.id_iv_waimai_food_image);
			holder.textViewFoodName = (TextView) view
					.findViewById(R.id.id_tv_waimai_food_name);
			holder.textViewFoodPrice = (TextView) view
					.findViewById(R.id.id_tv_waimai_food_price);

			holder.textViewFoodName.setText(canGuanList.get(position).title);
			holder.textViewFoodPrice.setText("人均￥"
					+ canGuanList.get(position).price);

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			TextView textViewFoodName;
			TextView textViewFoodPrice;
		}

	}

	public void initDatas() {
		progress.setVisibility(ViewGroup.VISIBLE);
		
		Log.i("DianCaiActivity", "DianCaiPager:initDatas()");
		
		getDataFromServe(HttpMethod.GET,
				application.getWaiMaiUrl());
		
		
		
		initListener();

	}

	private void initListener() {

		listViewWaiMai.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(mActivity, RestaurantActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("canGuanName", canGuanList.get(position-1).title);
				bundle.putInt("canGuanId", canGuanList.get(position-1).id);
				bundle.putInt("canGuanPhone", canGuanList.get(position-1).phone);
				bundle.putInt("type", 2);
				
				intent.putExtras(bundle);
				
				
				mActivity.startActivity(intent);
				
				mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				
				Toast.makeText(mActivity, canGuanList.get(position-1).title, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getDataFromServe(HttpMethod method, String url) {

		HttpUtils httpUtils = new HttpUtils();

		httpUtils.send(method, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result = responseInfo.result;

				progress.setVisibility(ViewGroup.GONE);

				paraseData(result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}

		});

	}

	private void paraseData(String res) {

		Gson gson = new Gson();
		waiMaiData = gson.fromJson(res, WaiMaiData.class);
		canGuanList = waiMaiData.data;
		adapter = new MyAdapter();
		listViewWaiMai.setAdapter(adapter);

	}

}
