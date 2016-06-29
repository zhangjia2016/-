package com.elb.hnist.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elb.hnist.R;
import com.elb.hnist.domain.DishData;
import com.elb.hnist.domain.DishData.Dish;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RestaurantActivity extends FragmentActivity {

	public static final String DIANCAI_FRAGMENT_TAG = "fragment_diancai";
	public static final String WAIMAI_FRAGMENT_TAG = "fragment_waimai";
	private String restaurantUrl = "http://192.168.191.1/elm/fandian_food.json";
	
	private Bundle fanDianBundle;
	private int tiaoZhuanType;

	private FrameLayout frameLayout;
	private ListView listView;
	private MyAdapter adapter;
	
	private DishData dishData;
	private ArrayList<Dish> dishList;

	private int zongshu = 0;
	private float zongjia = 0;
	public TextView singlePrice;
	public TextView foodNumber;
	public ImageView minus;// 减号
	public ImageView add;// 加号
	public int[] foodNumberArr;

	public TextView totalNumber;
	public TextView totalMoney;

	public TextView jiesuan;
	public TextView tiaojian;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		fanDianBundle = getIntent().getExtras();
		tiaoZhuanType = fanDianBundle.getInt("type");

		initViews();
		initData();

		// initFragment();

		super.onCreate(savedInstanceState);
	}

	private void initViews() {
		setContentView(R.layout.restaurant_layout);

		frameLayout = (FrameLayout) findViewById(R.id.id_fl_restaurant);

		View view = View.inflate(this, R.layout.dc_fragment_layout, null);
		View footerViw = View.inflate(this,
				R.layout.restaurant_listview_footer, null);
		
		TextView fanDianName = (TextView) view.findViewById(R.id.id_tv_fandian_name);
		if(tiaoZhuanType == 1)
		{
			fanDianName.setText(fanDianBundle.getString("fanDianName"));
		}else
		{
			fanDianName.setText(fanDianBundle.getString("canGuanName"));
		}
		

		jiesuan = (TextView) view.findViewById(R.id.id_tv_jiesuan);
		tiaojian = (TextView) view.findViewById(R.id.id_tv_peisong_tiaojian);

		totalNumber = (TextView) view.findViewById(R.id.id_tv_food_number);
		totalMoney = (TextView) view.findViewById(R.id.id_tv_food_money);

		listView = (ListView) view.findViewById(R.id.id_lv_diancai_food);
		listView.addFooterView(footerViw);

		initData();

		adapter = new MyAdapter();

		

		frameLayout.addView(view);

	}

	private void initData() {
		
		if(tiaoZhuanType == 1)
		{
			restaurantUrl = "http://192.168.191.1/elm/fandian_food.json";
		}else
		{
			restaurantUrl = "http://192.168.191.1/elm/canguan_food.json";
		}
		

		getDataFromServe(HttpMethod.GET,
				restaurantUrl);
		
		initListener();

	}

	private void initListener() {

		jiesuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(RestaurantActivity.this, DingDanActivity.class);	
				intent.putExtra("gouWuChe", (Serializable)dishList);
				intent.putExtra("type", tiaoZhuanType);
				startActivity(intent);
				
				RestaurantActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
				paraseData(result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(RestaurantActivity.this, "网络异常", Toast.LENGTH_SHORT)
						.show();
				error.printStackTrace();
			}

		});

	}

	private void paraseData(String res) {

		// Toast.makeText(mActivity, res, Toast.LENGTH_SHORT).show();
		Gson gson = new Gson();
		dishData = gson.fromJson(res, DishData.class);
		dishList = dishData.data;
		
		foodNumberArr = new int[dishList.size()];
		
		for(int i=0;i<dishList.size();i++)
		{
			
			foodNumberArr[i] = 0;
		}

		listView.setAdapter(adapter);
		// for (FanDian fanDian : list) {
		// Log.i("DianCaiActivity", "serve data:" + fanDian);
		// }
		// Log.i("DianCaiActivity", "serve data:" + dianCaiData);
	}

	// private void initFragment() {
	//
	// FragmentManager fragment = getSupportFragmentManager();
	// FragmentTransaction transaction = fragment.beginTransaction();
	//
	// transaction.replace(R.layout.restaurant_layout, new DianCaiFragment(),
	// DIANCAI_FRAGMENT_TAG);
	//
	// transaction.commit();
	//
	//
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();

		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dishList.size();
		}

		@Override
		public Object getItem(int position) {
			return dishList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(RestaurantActivity.this,
						R.layout.fandian_listview_item, null);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.foodName = (TextView) convertView
					.findViewById(R.id.id_tv_fandian_food_name);
			holder.singlePrice = (TextView) convertView
					.findViewById(R.id.id_tv_fandian_sale_price);
			holder.foodNumber = (TextView) convertView
					.findViewById(R.id.id_tv_fandian_food_total);
			holder.saleNumber = (TextView) convertView
					.findViewById(R.id.id_tv_fandian_sale_number);
			// holder.foodNumber.setTag(position);
			holder.minus = (ImageView) convertView
					.findViewById(R.id.id_iv_fandian_jian);
			holder.add = (ImageView) convertView
					.findViewById(R.id.id_iv_fandian_add);

			holder.foodNumber.setText(foodNumberArr[position] + "");
			holder.foodName.setText(dishList.get(position).foodName);
			holder.singlePrice.setText("￥" + dishList.get(position).price);
			holder.saleNumber.setText("月售" + dishList.get(position).saleQuantity + "份");
			if(foodNumberArr[position] > 0)
			{
				holder.minus.setVisibility(View.VISIBLE);
				holder.foodNumber.setVisibility(View.VISIBLE);

			}else
			{
				holder.minus.setVisibility(View.GONE);
				holder.foodNumber.setVisibility(View.GONE);

			}
			
			// singlePrice = (TextView) view
			// .findViewById(R.id.id_tv_fandian_sale_price);
			// foodNumber = (TextView) view
			// .findViewById(R.id.id_tv_fandian_food_total);
			// minus = (ImageView) view
			// .findViewById(R.id.id_iv_fandian_jian);
			// add = (ImageView) view.findViewById(R.id.id_iv_fandian_add);
			// holder.add.setTag(position);

			holder.add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Toast.makeText(RestaurantActivity.this,
//							"add onclick" + position, Toast.LENGTH_SHORT)
//							.show();
					float foodPrice = dishList.get(position).price;
					
					dishList.get(position).currentNumber++;
					
					holder.minus.setVisibility(View.VISIBLE);
					holder.foodNumber.setVisibility(View.VISIBLE);

					holder.foodNumber.setText(++foodNumberArr[position] + "");

					zongshu = zongshu + 1;
					zongjia = zongjia + foodPrice;
					totalNumber.setText(zongshu + "");
					totalMoney.setText(zongjia + "");

					tiaojian.setVisibility(View.GONE);
					jiesuan.setVisibility(View.VISIBLE);
					Toast.makeText(RestaurantActivity.this, "currentItemNumber" + dishList.get(position).currentNumber, Toast.LENGTH_SHORT).show();
				}
			});

			holder.minus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Toast.makeText(RestaurantActivity.this, "minus onclick",
//							Toast.LENGTH_SHORT).show();
					int ifoodNumber = --foodNumberArr[position];
					float foodPrice = dishList.get(position).price;
					
					dishList.get(position).currentNumber--;
					
					if (ifoodNumber == 0) {
						holder.minus.setVisibility(View.GONE);
						holder.foodNumber.setVisibility(View.GONE);
					} else {
						holder.minus.setVisibility(View.VISIBLE);
						holder.foodNumber.setVisibility(View.VISIBLE);
					}
					

					holder.foodNumber.setText(ifoodNumber + "");

					zongshu = zongshu - 1;
					zongjia = zongjia - foodPrice;
					totalNumber.setText(zongshu + "");
					totalMoney.setText(zongjia + "");
					if (zongjia > 0 || zongshu > 0) {
						tiaojian.setVisibility(View.GONE);
						jiesuan.setVisibility(View.VISIBLE);
					} else {
						tiaojian.setVisibility(View.VISIBLE);
						jiesuan.setVisibility(View.GONE);
					}
					
					Toast.makeText(RestaurantActivity.this, "currentItemNumber" + dishList.get(position).currentNumber, Toast.LENGTH_SHORT).show();
				}
				
			});

			return convertView;
		}

		protected void operationMinus() {

		}

		protected void operationAdd(ViewHolder holder) {
			

		}

		// class MyOnClickListener implements OnClickListener
		// {
		//
		// int position;
		//
		// public MyOnClickListener(int pos)
		// {
		// this.position = pos;
		// }
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if(position == (Integer)v.getTag())
		// {
		//
		// }
		// }
		//
		// }

	}

	class ViewHolder {
		public TextView singlePrice;
		public TextView foodName;
		public TextView foodNumber;
		public TextView saleNumber;
		private ImageView minus;// 减号
		private ImageView add;// 加号
	}
}
