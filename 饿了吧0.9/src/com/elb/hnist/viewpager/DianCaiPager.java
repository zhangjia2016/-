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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elb.hnist.R;
import com.elb.hnist.activity.RestaurantActivity;
import com.elb.hnist.domain.DianCaiData;
import com.elb.hnist.domain.DianCaiData.FanDian;
import com.elb.hnist.view.RefreshListView;
import com.elb.hnist.view.RefreshListView.OnRefreshListener;
import com.elb.hnist.viewpager.base.DianCaiPagerBase;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DianCaiPager extends DianCaiPagerBase {
	

	private RefreshListView listViewDianCai;
	
	private RelativeLayout progress;

	private BaseAdapter adapter;


	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();

			listViewDianCai.completeRefresh();
		}

	};

	private DianCaiData dianCaiData;

	private ArrayList<FanDian> fanDianList;

	public DianCaiPager(Activity activity) {
		super(activity);
		adapter = new MyAdapter();
		initViews();
	}

	public View initViews() {
		View view = View
				.inflate(mActivity, R.layout.diancai_pager_layout, null);
		listViewDianCai = (RefreshListView) view
				.findViewById(R.id.id_lv_diancai);
		progress = (RelativeLayout) view.findViewById(R.id.id_rl_progress);

		listViewDianCai.setOnRefreshListener(new OnRefreshListener() {

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
					
					fanDianList.get(fanDianList.size()-1).title = "加载更多的数据";

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
					
					fanDianList.get(0).title = "这是新餐厅";
					
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
			return fanDianList.size();
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
				view = View.inflate(mActivity, R.layout.diancai_listview_item,
						null);
				holder = new ViewHolder();
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			holder.bar = (RatingBar) view
					.findViewById(R.id.id_rb_diancai_food_level);
			holder.imageView = (ImageView) view
					.findViewById(R.id.id_iv_diancai_food_image);
			holder.textViewFoodName = (TextView) view
					.findViewById(R.id.id_tv_diancai_food_name);
			holder.textViewFoodPrice = (TextView) view
					.findViewById(R.id.id_tv_diancai_food_price);

//			holder.imageView.setImageResource(listData.get(position)
//					.getImgSrc());
			holder.textViewFoodName.setText(fanDianList.get(position)
					.title);
			holder.textViewFoodPrice.setText(fanDianList.get(position)
					.price + "");

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			TextView textViewFoodName;
			TextView textViewFoodPrice;
			RatingBar bar;
		}

	}

	public void initDatas() {
//		listData.clear();

		progress.setVisibility(ViewGroup.VISIBLE);
		
		Log.i("DianCaiActivity", "DianCaiPager:initDatas()");
		
		getDataFromServe(HttpMethod.GET, application.getDianCaiUrl());
//
//		TypedArray ar = mActivity.getResources().obtainTypedArray(
//				R.array.diancai_img_resource_id);
//		int len = ar.length();
//		resIds = new int[len];
//		for (int i = 0; i < len; i++)
//			resIds[i] = ar.getResourceId(i, 0);
//		ar.recycle();
//
//		String[] foodName = mActivity.getResources().getStringArray(
//				R.array.diancai_string_food_name);
//		String[] foodLevel = mActivity.getResources().getStringArray(
//				R.array.diancai_string_food_level);
//		String[] foodPrice = mActivity.getResources().getStringArray(
//				R.array.diancai_string_food_price);
//
//		for (int i = 0; i < resIds.length; i++) {
//			DianCaiDataBean dianCaiData = new DianCaiDataBean(resIds[i],
//					foodName[i], Float.parseFloat(foodLevel[i]),
//					Float.parseFloat(foodPrice[i]));
//
//			listData.add(dianCaiData);
//		}
		initListener();
	}

	private void initListener() {
		
		listViewDianCai.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, RestaurantActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("fanDianName", fanDianList.get(position-1).title);
				bundle.putInt("fanDianId", fanDianList.get(position-1).id);
				bundle.putInt("fanDianPhone", fanDianList.get(position-1).phone);
				bundle.putInt("type", 1);
				
				intent.putExtras(bundle);
				
				
				mActivity.startActivity(intent);
				
				mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				
				Toast.makeText(mActivity, fanDianList.get(position-1).title, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getDataFromServe(HttpRequest.HttpMethod method, String url) {
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
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
				.show();
				error.printStackTrace();
			}

		});

	}

	private void paraseData(String res) {
		
//		Toast.makeText(mActivity, res, Toast.LENGTH_SHORT).show();
		Gson gson = new Gson();
		dianCaiData = gson.fromJson(res, DianCaiData.class);
		fanDianList = dianCaiData.data;
		
		listViewDianCai.setAdapter(adapter);
		
		
//		for (FanDian fanDian : list) {
//			Log.i("DianCaiActivity", "serve data:" + fanDian);
//		}
//		Log.i("DianCaiActivity", "serve data:" + dianCaiData);
	}
	
	
	

}
