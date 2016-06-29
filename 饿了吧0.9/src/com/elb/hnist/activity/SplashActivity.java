package com.elb.hnist.activity;

import com.elb.hnist.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * 闪屏界面的Activity
 * @author dell zhang
 *
 */
public class SplashActivity extends Activity {

	private ImageView iv_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initAnima();
	}

	private void initAnima() {
		
		AnimationSet set = new AnimationSet(this, null);
		
		RotateAnimation rotate = new RotateAnimation(0, 360, 
				RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(3000);
		rotate.setFillAfter(true);
		
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(3000);
		scale.setFillAfter(true);
		
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(3000);
		alpha.setFillAfter(true);
		
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				jumpNextActivity();
			}
		});
		
		iv_splash.startAnimation(set);
	}

	protected void jumpNextActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void initView() {
		setContentView(R.layout.splash_layout);
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
	}
}
