package com.elb.hnist.activity;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.elb.hnist.R;
import com.elb.hnist.viewpager.DianCaiPager;
import com.elb.hnist.viewpager.MyInfoPager;
import com.elb.hnist.viewpager.WaiMaiPager;
import com.elb.hnist.viewpager.base.DianCaiPagerBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 主界面的Activity
 * 
 * @author dell zhang
 * 
 */
public class DianCaiActivity extends Activity {

	@ViewInject(R.id.id_rg_top_group)
	public RadioGroup radioGroupTop;
	@ViewInject(R.id.id_rg_bottom_group)
	public RadioGroup radioGroupBottom;
	@ViewInject(R.id.id_iv_gundong)
	public ImageView imageViewGunDong;
	@ViewInject(R.id.id_vp_diancai_content)
	public ViewPager viewPagerCotent;
	@ViewInject(R.id.id_iv_search_icon)
	public ImageView searchIcon;

	@ViewInject(R.id.id_ll_title_bar)
	public LinearLayout linearLayoutTitle;
	@ViewInject(R.id.id_ll_select_bar)
	public LinearLayout linearLayoutSelect;

	@ViewInject(R.id.id_ll_myinfo_tab)
	public LinearLayout linearLayoutMyInfo;

	public int currentTitle = 0;
	public int lastGunDongPosition = 0;
	private int offset;
	public int screenWidth;

	private List<DianCaiPagerBase> listBase;

	public DianCaiPagerBase dianCaiPagerBase = new DianCaiPagerBase(this);
	private int startPosition;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		WindowManager wm = this.getWindowManager();
		screenWidth = wm.getDefaultDisplay().getWidth();
		// Log.e("DianCaiActivity", String.valueOf(screenWidth));
		// Toast.makeText(this, String.valueOf(screenWidth), Toast.LENGTH_LONG)
		// .show();
		// 获取屏幕宽度
		// DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// screenWidth = metrics.widthPixels;

		initView();

		initDatas();

		initListener();
	}

	public void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.diancai_layout);
		ViewUtils.inject(this);

		// 设置下滑图片的初始位置
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.id_category_selector);
		int bmpWidth = bitmap.getWidth();
		Log.i("DianCaiActivity", "bmpWidth" + String.valueOf(bmpWidth));
		offset = (screenWidth / 3 - bmpWidth) / 2;
		Log.i("DianCaiActivity", String.valueOf(offset) + ":offset");
		Matrix metrics = new Matrix();
		metrics.postTranslate(offset, 0);

		imageViewGunDong.setImageMatrix(metrics);

		listBase = new ArrayList<DianCaiPagerBase>();

		Intent intent = getIntent();
		startPosition = intent.getIntExtra("startMode", 0);

		Log.i("DianCaiActivity", "StartMode:" + startPosition);

		searchIcon.setVisibility(View.VISIBLE);
		
	}
	


	private void initListener() {
		// TODO Auto-generated method stub

		// 筛选项radioGroup
		radioGroupTop.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.id_rb_top_juli:
					Log.i("DianCaiActivity", "距离");
					currentTitle = 0;
					gunDongAnima();
					break;
				case R.id.id_rb_top_haoping:
					Log.i("DianCaiActivity", "haoping");
					currentTitle = 1;
					gunDongAnima();
					break;
				case R.id.id_rb_top_renjun:
					Log.i("DianCaiActivity", "renjun");
					currentTitle = 2;
					gunDongAnima();
					break;
				}
			}
		});

		// 底部界面选择radioGroup
		radioGroupBottom
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.id_rb_bottom_diancai:
							Log.i("DianCaiActivity", "距离");
							if (viewPagerCotent.getCurrentItem() != 0) {
								viewPagerCotent.setCurrentItem(0, false);
							}
							break;
						case R.id.id_rb_bottom_waimai:
							Log.i("DianCaiActivity", "waimai");
							if (viewPagerCotent.getCurrentItem() != 1) {
								viewPagerCotent.setCurrentItem(1, false);
							}
							break;
						case R.id.id_rb_bottom_my:
							Log.i("DianCaiActivity", "wode");
							if (viewPagerCotent.getCurrentItem() != 2) {
								viewPagerCotent.setCurrentItem(2, false);
							}
							break;
						}
					}
				});

		// 主界面ViewPager监听
		viewPagerCotent.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * arg0:当前选中的page
			 */
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				Log.i("DianCaiActivity", "onPageSelected" + position);
				listBase.get(position).initDatas();
				switch (position) {
				case 0:
					radioGroupBottom.check(R.id.id_rb_bottom_diancai);

					if (linearLayoutSelect.getVisibility() == ViewGroup.GONE) {
						linearLayoutSelect.setVisibility(ViewGroup.VISIBLE);
						linearLayoutTitle.setVisibility(ViewGroup.VISIBLE);
						linearLayoutMyInfo.setVisibility(ViewGroup.GONE);
					}

					break;
				case 1:
					radioGroupBottom.check(R.id.id_rb_bottom_waimai);

					if (linearLayoutSelect.getVisibility() == ViewGroup.GONE) {
						linearLayoutSelect.setVisibility(ViewGroup.VISIBLE);
						linearLayoutTitle.setVisibility(ViewGroup.VISIBLE);
						linearLayoutMyInfo.setVisibility(ViewGroup.GONE);
					}

					break;
				case 2:
					radioGroupBottom.check(R.id.id_rb_bottom_my);

					if (linearLayoutSelect.getVisibility() == ViewGroup.VISIBLE) {
						linearLayoutSelect.setVisibility(ViewGroup.GONE);
						linearLayoutTitle.setVisibility(ViewGroup.GONE);
						linearLayoutMyInfo.setVisibility(ViewGroup.VISIBLE);
					}
					break;
				default:

					break;
				}

			}

			/**
			 * 
			 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用 arg0:当前页面，及你点击滑动的页面
			 * arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			/**
			 * 有三种状态（0，1，2）arg0 ==1正在滑动，a rg0==2滑动完毕了，arg0==0的什么都没做
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 标题选择排序下面的图片的动画
	 */
	public void gunDongAnima() {
		TranslateAnimation animation = new TranslateAnimation(
				lastGunDongPosition * (screenWidth / 3), currentTitle
						* (screenWidth / 3), 0, 0);
		animation.setDuration(500);
		animation.setFillAfter(true);
		imageViewGunDong.startAnimation(animation);

		lastGunDongPosition = currentTitle;

	}

	private void initDatas() {
		// TODO Auto-generated method stub

		listBase.add(new DianCaiPager(this));
		listBase.add(new WaiMaiPager(this));
		listBase.add(new MyInfoPager(this));

		switch (startPosition) {
		case 0:
			Log.i("DianCaiActivity", "InitDatas:" + startPosition);
			viewPagerCotent.setCurrentItem(0);
			radioGroupBottom.check(R.id.id_rb_bottom_diancai);
			listBase.get(startPosition).initDatas();
			linearLayoutSelect.setVisibility(ViewGroup.VISIBLE);
			linearLayoutTitle.setVisibility(ViewGroup.VISIBLE);
			linearLayoutMyInfo.setVisibility(ViewGroup.GONE);

			break;
		case 1:

			viewPagerCotent.setCurrentItem(1);
			radioGroupBottom.check(R.id.id_rb_bottom_waimai);
			listBase.get(startPosition).initDatas();
			Log.i("DianCaiActivity", "InitDatas:" + startPosition);
			linearLayoutSelect.setVisibility(ViewGroup.VISIBLE);
			linearLayoutTitle.setVisibility(ViewGroup.VISIBLE);
			linearLayoutMyInfo.setVisibility(ViewGroup.GONE);

			break;
		case 2:

			viewPagerCotent.setCurrentItem(2);
			radioGroupBottom.check(R.id.id_rb_bottom_my);
			listBase.get(startPosition).initDatas();
			Log.i("DianCaiActivity", "InitDatas:" + startPosition);
			linearLayoutSelect.setVisibility(ViewGroup.GONE);
			linearLayoutTitle.setVisibility(ViewGroup.GONE);
			linearLayoutMyInfo.setVisibility(ViewGroup.VISIBLE);

			break;
		default:

			break;
		}

		viewPagerCotent.setAdapter(new MyAdataper());

	}

	class MyAdataper extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listBase.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			DianCaiPagerBase dianCaiPager = listBase.get(position);

			View view = dianCaiPager.initViews();
			// dianCaiPager.initDatas();
			container.addView(view);
			return view;

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
	}

}
