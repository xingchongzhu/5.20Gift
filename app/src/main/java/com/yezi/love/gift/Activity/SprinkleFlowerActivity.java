package com.yezi.love.gift.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yezi.love.gift.Activity.textpath.SyncTextPathView;
import com.yezi.love.gift.Activity.textpath.TextUtil;
import com.yezi.love.gift.Activity.textpath.painter.PenPainter;
import com.yezi.love.gift.Activity.utils.PlayingMusicServices;
import com.yezi.love.gift.Activity.utils.TextCallBack;
import com.yezi.love.gift.Activity.view.BezierLayout;
import com.yezi.love.gift.Activity.view.FllowerAnimation;
import com.yezi.love.gift.Activity.view.LuckPanLayout;
import com.yezi.love.gift.Activity.view.RotatePan;
import com.yezi.love520.gift.R;

public class SprinkleFlowerActivity extends Activity implements TextCallBack,LuckPanLayout.AnimationEndListener{

	/**
	 * 规定开始音乐、暂停音乐、结束音乐的标志
	 */
	public  static final int PLAT_MUSIC=1;
	public  static final int PAUSE_MUSIC=2;
	public  static final int STOP_MUSIC=3;

	public final static String SHAREDPREFERENCE = "setting";
	public final static String  TIMECOUNT = "time";
		public final static String GIFT = "gift";
	public final static String DEVIED = "#";
	public final static int MAXTIEM = 3;
	private Button btn_start;
	// 撒花特效
	private RelativeLayout rlt_animation_layout;
	private FllowerAnimation fllowerAnimation;
	private Handler uiHandler = new Handler();
	private BezierLayout mBezierLayout;
	private SyncTextPathView textLover;
	private SyncTextPathView text520;

	private RotatePan rotatePan;
	private LuckPanLayout luckPanLayout;
	private ImageView goBtn;
	private ImageView yunIv;
	private String[] strs ;
	private View big_turntable;
	private TextView hit_user_tv;
	private TextView gitf_hint;
	private String describes[] = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.sprinkle);
		mBezierLayout = (BezierLayout)this.findViewById(R.id.bezierlayout);
		mBezierLayout.setTextCallBack(this);
		textLover = (SyncTextPathView)this.findViewById(R.id.textLover);
		text520 = (SyncTextPathView)this.findViewById(R.id.text520);
		mBezierLayout.setHandler(uiHandler);

		textLover.setPathPainter(new PenPainter());
		text520.setPathPainter(new PenPainter());



		big_turntable = (View) findViewById(R.id.big_turntable);
		strs = getResources().getStringArray(R.array.names);
		luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
		luckPanLayout.setAnimationEndListener(this);
		goBtn = (ImageView)findViewById(R.id.go);
		yunIv = (ImageView)findViewById(R.id.yun);
		hit_user_tv = (TextView)findViewById(R.id.hit_user_tv);
		gitf_hint = (TextView)findViewById(R.id.gitf_hint);
		describes = getResources().getStringArray(R.array.describe);
		showGift();
		playingmusic(PLAT_MUSIC);
	}

	public void showGift(){
		int count = getSharedPreference(this,TIMECOUNT);
		hit_user_tv.setText(String.format(getResources().getString(R.string.chance_time),	MAXTIEM - getSharedPreference(this,TIMECOUNT)));
		String gifts[] = getGiftSharedPreference(this,GIFT).split(DEVIED);
		String str = "";
		if(count > 0){
			int n = 1;
			for (int i = 0; i < gifts.length; i++) {
				if(!TextUtils.isEmpty(gifts[i])) {
					String tit = String.format(getResources().getString(R.string.title), n);
					str += tit + gifts[i] + "\n";
					n++;
				}
			}
		}else{
			str = gifts[gifts.length-1];
		}
		gitf_hint.setText(str);
	}

	public static void saveCountSharedPreference(Context context,String key, int value){
		SharedPreferences mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putInt(key,value);
		editor.commit();
	}

	public static void saveGiftSharedPreference(Context context,String key, String value){
		SharedPreferences mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}

	public static int getSharedPreference(Context context,String key){
		SharedPreferences mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return mSharedPreferences.getInt(key,0);

	}

	public static String getGiftSharedPreference(Context context,String key){
		SharedPreferences mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, 0);
		return mSharedPreferences.getString(key,context.getResources().getString(R.string.gifts_empty));

	}

	public void rotation(View view){
		if(getSharedPreference(this,TIMECOUNT) < MAXTIEM){
			luckPanLayout.rotate(-1,100);
		}else{
			Toast.makeText(this,getResources().getString(R.string.gifts_finash),Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void endAnimation(int position) {
		saveGiftSharedPreference(this,GIFT, (getSharedPreference(this,TIMECOUNT) == 0 ?"":getGiftSharedPreference(this,GIFT))+DEVIED+describes[position]);
		saveCountSharedPreference(this,TIMECOUNT, getSharedPreference(this,TIMECOUNT)+1);
		showGift();
		//Toast.makeText(this,"Position = "+position+","+strs[position], Toast.LENGTH_SHORT).show();
	}

	@Override
	public void playLove(){
		textLover.startAnimation(0,1);
	}

	@Override
	public void play520(){
		text520.startAnimation(0,1);
	}

	int duration = 3000;

	@Override
	public void choiceGift(){

		setHideAnimation( mBezierLayout,  duration );
		setHideAnimation( textLover,  duration );
		setHideAnimation( text520,  duration );
		setShowAnimation(big_turntable, duration );
		big_turntable.setVisibility(View.VISIBLE);
	}
	private AlphaAnimation mHideAnimation= null;
	private AlphaAnimation mShowAnimation= null;

	/**

	 * View渐隐动画效果

	 *

	 */

	private void setHideAnimation( View view, int duration ){
		if( null == view || duration < 0 ){
			return;
		}
		/*if( null != mHideAnimation ){
			mHideAnimation.cancel( );
		}*/

		mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
		mHideAnimation.setDuration( duration );
		mHideAnimation.setFillAfter( true );
		view.startAnimation( mHideAnimation );
		/*mShowAnimation.setAnimationListener(new Animation.AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation){

			}

			@Override
			public void onAnimationEnd(Animation animation){
				mBezierLayout.removeAllViews();
			}

			@Override
			public void onAnimationRepeat(Animation animation){

			}

		});*/

	}

	/**
	 * View渐现动画效果
	 *
	 */
	private void setShowAnimation( View view, int duration ){
		if( null == view || duration < 0 ){
			return;
		}
		if( null != mShowAnimation ){
			mShowAnimation.cancel( );
		}
		mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
		mShowAnimation.setDuration( duration );
		mShowAnimation.setFillAfter( true );
		view.startAnimation( mShowAnimation );
	}

	private void playingmusic(int type) {
		//启动服务，播放音乐
		Intent intent=new Intent(this,PlayingMusicServices.class);
		intent.putExtra("type",type);
		startService(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		playingmusic(STOP_MUSIC);
	}
}
