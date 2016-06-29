package com.elb.hnist.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.elb.hnist.R;
import com.elb.hnist.domain.DishData.Dish;
import com.elb.hnist.utils.Utils;

public class DingDanActivity extends Activity {

	private ArrayList<Dish> dishList = null;
	private ArrayList<Dish> showDishList;
	private Serializable serializableExtra;
	private ListView dingDanShow;
	private RadioButton online;
	private RadioButton offline;
	private TextView tvZongJia;
	private Button confirm;
	
	ProgressDialog dialog;
	
	private int tiaoZhuanType;
	private float zongjia = 0;
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		serializableExtra = this.getIntent().getSerializableExtra("gouWuChe");
		tiaoZhuanType = this.getIntent().getIntExtra("type", 1);

		this.dishList = (ArrayList<Dish>) serializableExtra;

//		 Toast.makeText(this, dishList.get(1).currentNumber + " "
//		 +dishList.get(1).foodName + dishList.size(), Toast.LENGTH_LONG).show();

		initViews(this.dishList);

		
	}

	private void initViews(ArrayList<Dish> dishs) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dingdan_layout);
		
		online = (RadioButton) findViewById(R.id.id_rb_zxzf);
		offline = (RadioButton) findViewById(R.id.id_rb_cdfk);
		tvZongJia = (TextView) findViewById(R.id.id_tv_zongjia);
		confirm = (Button) findViewById(R.id.id_btn_qrdd);
		dingDanShow = (ListView) findViewById(R.id.id_lv_dingdan_show);

		dishList = new ArrayList<Dish>();
		showDishList = new ArrayList<Dish>();
		
		
		initDatas(dishs);
	}

	private void initDatas(ArrayList<Dish> dishs) {
		if (dishs != null && dishs.size() != 0) {
			for (int i = 0; i < dishs.size(); i++) {
				if (dishs.get(i).currentNumber != 0) {
					showDishList.add(dishs.get(i));
					zongjia = zongjia + dishs.get(i).currentNumber * dishs.get(i).price;
				}
			}
			
		}
		Toast.makeText(this,  "showDishList.size()" + showDishList.size(), Toast.LENGTH_LONG).show();
		 
		dingDanShow.setAdapter(new MyAdapter());
		tvZongJia.setText("￥" + zongjia);
		
		initListener(showDishList);

	}

	private void initListener(ArrayList<Dish> dishs) {

		
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online.isChecked())
				{
					Utils.showToast(DingDanActivity.this, "在线支付");
					
					showProgressDialog();
					
					Intent intent = new Intent(DingDanActivity.this, MainActivity.class);
					 
					
				}else if(offline.isChecked())
				{
					Utils.showToast(DingDanActivity.this, "餐到付款");
					showProgressDialog();
					
					Intent intent = new Intent(DingDanActivity.this, MainActivity.class);
				}
			}
		});
	}

	protected void showProgressDialog() {
		
		dialog = new ProgressDialog(DingDanActivity.this);
		
		//设置进度条风格，风格为圆形，旋转的 
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		//设置ProgressDialog 标题 
		dialog.setTitle("正在提交订单"); 
		//设置ProgressDialog 标题图标 
//		dialog.setIcon(android.R.drawable.); 		
		//设置ProgressDialog 的进度条是否不明确 
		dialog.setIndeterminate(false); 
		//设置ProgressDialog 是否可以按退回按键取消 
		dialog.setCancelable(false); 
		//显示 
		dialog.show();	
		
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				closeProgressDialog(dialog);
			}
		}, 2000);
		
		Utils.showToast(DingDanActivity.this, "提交成功");
		
	}
	protected void closeProgressDialog(ProgressDialog dialog2) {
		dialog2.dismiss();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return showDishList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return showDishList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = View.inflate(DingDanActivity.this,
						R.layout.show_gouwuche_item, null);
			}

			TextView name = (TextView) convertView
					.findViewById(R.id.id_tv_food_name_sgwc);
			TextView number = (TextView) convertView
					.findViewById(R.id.id_tv_food_number_sgwc);
			TextView total = (TextView) convertView
					.findViewById(R.id.id_tv_food_total_sgwc);

			name.setText(showDishList.get(position).foodName);
			number.setText("x" + showDishList.get(position).currentNumber);
			total.setText("￥" + showDishList.get(position).price
					* showDishList.get(position).currentNumber);

			return convertView;
		}

	}

	@Override
	public void finish() {
		super.finish();

		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}

}
