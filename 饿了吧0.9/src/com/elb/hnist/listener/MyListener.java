package com.elb.hnist.listener;

import com.elb.hnist.R;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * 
 * @author dell zhang
 *
 */
public class MyListener implements OnClickListener {

	@Override
	public void onClick(View v) {

		switch(v.getId())
		{
		case R.id.id_ll_four_one:
			Log.e("MyListener", "click");
			
			break;
		}
	}

}
