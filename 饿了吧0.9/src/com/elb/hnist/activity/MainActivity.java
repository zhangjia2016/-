package com.elb.hnist.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elb.hnist.R;
import com.elb.hnist.domain.AdvanceBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


/**
 * 初始界面的Activity
 * @author dell zhang
 *
 */
public class MainActivity extends Activity implements OnClickListener{
	
	private RelativeLayout search_layout;
	
	private int isExist = 0;

	/**
	 * viewPage field
	 */
	private ViewPager viewPager;

	private LinearLayout dot_layout;
	private TextView tv_intro;
	private List<AdvanceBean> list = new ArrayList<AdvanceBean>();
	private ImageView iv;
	
	@ViewInject(R.id.id_ll_four_one)
	private LinearLayout four_nav_one;
	@ViewInject(R.id.id_ll_four_two)
	private LinearLayout four_nav_two;
	@ViewInject(R.id.id_ll_four_three)
	private LinearLayout four_nav_three;
	@ViewInject(R.id.id_ll_four_four)
	private LinearLayout four_nav_four;
	/**
	 * handle:处理viewpage自动滚动
	 */
	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
			
			handle.sendEmptyMessageDelayed(0, 2000);
		}
		
	};
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
		initListener();

	}

	private void initListener() {

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {

				updateIntroAndDot();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		search_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "点我干嘛！！", Toast.LENGTH_LONG).show();
			}
		});
		
//		four_one.setOnClickListener(new MyListener());
	}

	public void initView() {
		setContentView(R.layout.activity_main);
		
		ViewUtils.inject(this);
		
		
		search_layout = (RelativeLayout) findViewById(R.id.id_rl_search_layout);
		viewPager = (ViewPager) findViewById(R.id.id_vp_huandeng);
		dot_layout = (LinearLayout) findViewById(R.id.id_ll_dot_layout);
		tv_intro = (TextView) findViewById(R.id.id_tv_intro);
		initData();
		
		handle.sendEmptyMessageDelayed(0, 2000);
		
	}

	private void initDot() {

		for(int i=0;i<list.size();i++)
		{
			View view = new View(this);
			LayoutParams param = new LayoutParams(7, 7);
			if(i!=0)
			{
				param.setMargins(5, 0, 0, 0);
			}
			view.setLayoutParams(param);
			view.setBackgroundResource(R.drawable.dot_select);
			
			
			dot_layout.addView(view);
		}
	}

	private void initData() {

		list.add(new AdvanceBean(R.drawable.d, "你好，我是第一张图片"));
		list.add(new AdvanceBean(R.drawable.b, "你好，我是第二张图片"));
		list.add(new AdvanceBean(R.drawable.c, "你好，我是第三张图片"));
		list.add(new AdvanceBean(R.drawable.d, "你好，我是第四张图片"));
		list.add(new AdvanceBean(R.drawable.e, "你好，我是第五张图片"));
		
		
		initDot();	
		
		viewPager.setAdapter(new MyPagerAdapter());

		int current = (Integer.MAX_VALUE/2)-(Integer.MAX_VALUE/2)%list.size();
		viewPager.setCurrentItem(current);
		
		updateIntroAndDot();
		
		
		

	}

	private void updateIntroAndDot() {

		int currentPage = viewPager.getCurrentItem()%list.size();
		tv_intro.setText(list.get(currentPage).getIntro());
		
		for(int i=0;i<dot_layout.getChildCount();i++)
		{
			dot_layout.getChildAt(i).setEnabled(i==currentPage);
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = View.inflate(MainActivity.this, R.layout.page_item_layout,
					null);
			iv = (ImageView) view.findViewById(R.id.id_iv_page_item);
			AdvanceBean ad = list.get(position%list.size());
			iv.setImageResource(ad.getImageSourceId());
			
			container.addView(view);

			return view;
		}

	}

	@Override
	@OnClick({R.id.id_ll_four_one, R.id.id_ll_four_two, R.id.id_ll_four_three, R.id.id_ll_four_four})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.id_ll_four_one:
			intent = new Intent();
			intent.putExtra("startMode", 0);
			intent.setClass(this,  DianCaiActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		case R.id.id_ll_four_two:
			intent = new Intent();
			intent.putExtra("startMode", 1);
			intent.setClass(this,  DianCaiActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		case R.id.id_ll_four_three:
			intent = new Intent();
			intent.putExtra("startMode", 3);
			intent.setClass(this,  RestaurantActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		case R.id.id_ll_four_four:
			intent = new Intent();
			intent.putExtra("startMode", 2);
			intent.setClass(this,  DianCaiActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			isExist++;
			if(isExist == 1)
			{
				Toast.makeText(this, "请再按一次退出！", Toast.LENGTH_SHORT).show();
			}else if(isExist == 2)
			{
				finish();
			}
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExist = 0;
				}
			}, 3000);
		}
		
		return false;
	}

}
