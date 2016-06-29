package com.elb.hnist.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.elb.hnist.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshListView extends ListView implements OnScrollListener {

	public static final int PULL_REFRESH = 0;
	public static final int RELEASE_REFRESH = 1;
	public static final int REFRESHING = 2;

	int currentState = PULL_REFRESH;
	
	public boolean isLoadMore = false;

	private View headerView;
	private View footerView;
	private int headerViewHeight;
	private int startY=-1;
	private int endY;
	private int dy;
	private int padding;
	private ImageView jianTou;
	private ProgressBar progressBar;
	private TextView title;
	private TextView time;
	private RotateAnimation rotateUp;
	private RotateAnimation rotateDown;

	public RefreshListView(Context context) {
		super(context);

		initHeaderView();
		initFooterView();
		initAnim();
	}


	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
		initAnim();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		initAnim();
	}

	private void initAnim() {
			rotateUp = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			rotateUp.setDuration(300);
			rotateUp.setFillAfter(true);
			
			rotateDown = new RotateAnimation(180, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			rotateDown.setDuration(300);
			rotateDown.setFillAfter(true);
	}
	

	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.list_footer_view, null);
		
		footerView.measure(0, 0);
		
		footerViewHeigt = footerView.getMeasuredHeight();
		
		footerView.setPadding(0, -footerViewHeigt, 0, 0);
		
		this.addFooterView(footerView);
		this.setOnScrollListener(this);
	}

	private void initHeaderView() {

		headerView = View
				.inflate(getContext(), R.layout.list_header_view, null);
		

		jianTou = (ImageView) headerView
				.findViewById(R.id.id_iv_refresh_image);
		progressBar = (ProgressBar) headerView
				.findViewById(R.id.id_pb_refresh_progressbar);
		title = (TextView) headerView.findViewById(R.id.id_tv_refresh_state);
		time = (TextView) headerView.findViewById(R.id.id_tv_refresh_time);

		headerView.measure(0, 0);

		headerViewHeight = headerView.getMeasuredHeight();

		headerView.setPadding(0, -headerViewHeight, 0, 0);
		
		
		time.setText("最近刷新时间："+getCurrentTime());

		this.addHeaderView(headerView);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(currentState == REFRESHING || isLoadMore)
			{
				break;
			}
			
			if (startY == -1) {
				// 确保y有效
				startY = (int) ev.getRawY();
			}
			endY = (int) ev.getRawY();
			dy = (endY - startY)/2;

			if (dy >= 0 && getFirstVisiblePosition() == 0)// 是往下拉且当前是第一个，才能显示下拉header
			{
				padding = dy - headerViewHeight;
				
					headerView.setPadding(0, padding, 0, 0);

				if (padding > 0 && currentState == PULL_REFRESH) {
					currentState = RELEASE_REFRESH;
					changeState();
				}
				if (padding <= 0 && currentState != PULL_REFRESH) {
					currentState = PULL_REFRESH;
					changeState();
				}
				super.onTouchEvent(ev);
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:

			startY = -1; // 重置
			
			if(currentState == RELEASE_REFRESH)
			{
				currentState = REFRESHING;
				headerView.setPadding(0, 0, 0, 0);
				
				jianTou.clearAnimation();
				title.setText("正在刷新");
				
				progressBar.setVisibility(VISIBLE);
				jianTou.setVisibility(INVISIBLE);
				
				if(onRefreshListener != null)
				{
					onRefreshListener.onRefresh();
				}
				
			}else
			{
				
				headerView.setPadding(0, -headerViewHeight, 0, 0);
			}

			break;
		}

		return super.onTouchEvent(ev);
	}

	private void changeState() {

		switch (currentState) {
		case PULL_REFRESH:
			title.setText("下拉刷新");
			progressBar.setVisibility(INVISIBLE);
			jianTou.setVisibility(VISIBLE);
			jianTou.startAnimation(rotateDown);
			break;
		case RELEASE_REFRESH:
			title.setText("松开刷新");
			progressBar.setVisibility(INVISIBLE);
			jianTou.setVisibility(VISIBLE);
			jianTou.startAnimation(rotateUp);
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING)
		{
			if(getLastVisiblePosition() == getCount() -1 && !isLoadMore &&currentState!=REFRESHING)
			{
				isLoadMore = true;
				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount() -1);
				
				if(onRefreshListener != null)
				{
					onRefreshListener.onLoadMore();
				}
			}
		}
		

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}
	
	@SuppressLint("SimpleDateFormat")
	public String getCurrentTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return format.format(new Date());
		
		
	}
	
	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.onRefreshListener = listener;
	}
	
	OnRefreshListener onRefreshListener;
	private int footerViewHeigt;
	
	public interface OnRefreshListener
	{
		public void onRefresh();
		
		public void onLoadMore();
		
	}
	public void completeRefresh() {

		if(isLoadMore)
		{
			Toast.makeText(getContext(), "加载更多成功", Toast.LENGTH_SHORT).show();
			
			footerView.setPadding(0, -footerViewHeigt, 0, 0);
			isLoadMore = false;
		}else
		{
			Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
			
			headerView.setPadding(0, -headerViewHeight, 0, 0);
			
			currentState = PULL_REFRESH;
			
			title.setText("下拉刷新");
			progressBar.setVisibility(INVISIBLE);
			jianTou.setVisibility(VISIBLE);
			time.setText("最近刷新时间："+getCurrentTime());
		}
	};

}
