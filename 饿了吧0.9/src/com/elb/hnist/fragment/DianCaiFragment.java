package com.elb.hnist.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.elb.hnist.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DianCaiFragment extends BaseFragment {
	
	@ViewInject(R.id.id_tv_fandian_name)
	private TextView fanDianName;
	@ViewInject(R.id.id_lv_diancai_food)
	private ListView listView;

	@Override
	public View initViews() {
		
		View view = View.inflate(mActivity, R.layout.restaurant_layout, null);
		
		ViewUtils.inject(view);
		
		return view;
	}
	
	public void initDatas()
	{
		
	}

}
