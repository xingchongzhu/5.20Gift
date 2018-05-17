package com.yezi.love.gift.Activity.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;

import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yezi.love.gift.Activity.textpath.SyncTextPathView;
import com.yezi.love.gift.Activity.textpath.painter.FireworksPainter;
import com.yezi.love.gift.Activity.textpath.painter.PenPainter;
import com.yezi.love.gift.Activity.utils.TextCallBack;
import com.yezi.love520.gift.R;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.Attributes;

public class BezierLayout extends RelativeLayout {
	Drawable[] mDrawables= new Drawable[]{getResources().getDrawable(R.drawable.lift_flower),
			getResources().getDrawable(R.drawable.lift_flower),getResources().getDrawable(R.drawable.lift_flower),getResources().getDrawable(R.drawable.lift_flower)};

	private Drawable beateLoveDrawable = getResources().getDrawable(R.drawable.mylove);
	private LayoutParams mParams;

	private int mDrawWidth;
	private int mDrawHeight;
	private int mHeight;
	private int mWidth;
	private Random mRandom=new Random();
	private Context context;
	private Handler uiHandler;
	Timer timer = null;
	Timer timer1 = new Timer();
	private int screenHeigth;
	private int screenWidth;
	private int row = 3;
	private float offsetX;
	private float offsetY;
	private float heartScale = 1;
	private int initFlower[] = {60,150};
	private int initFlower1[] = {30,20};
	private float heartSizeScale = 0.4f;
	private float heartSizeScaleSize = 0.6f;
	private boolean isStop = false;

	private TextCallBack textCallBackListener;


	public BezierLayout(Context context) {
		this(context,null);
		this.context = context;
	}

	public BezierLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		this.context = context;
	}

	public void setHandler(Handler uiHandler){
		this.uiHandler = uiHandler;
	}

	public void setTextCallBack(TextCallBack textCallBackListener){
		this.textCallBackListener = textCallBackListener;
	}

	public BezierLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}
	private void init(){
		Resources resources = context.getResources();
		mDrawWidth=mDrawables[2].getIntrinsicWidth();
		mDrawHeight=mDrawables[2].getIntrinsicHeight();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenHeigth = dm.heightPixels;
		screenWidth = dm.widthPixels;
	}

	int n = 0;
	int cloume = 0;
	private boolean finish = false;
	private boolean remove = false;
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if(uiHandler != null) {

				if((cloume+1) <= initFlower.length && n >= initFlower[cloume]){
					cloume++;
					n=0;
				}else if(((cloume+1) > initFlower.length || n >= initFlower[initFlower.length-1])){
					timer.cancel();
					finish = true;
					timer1.schedule(task1, 5000, 1000);
					n = 0;
				}
				n++;
				if(n%10 == 0){
					row++;
				}
				if(n == initFlower[initFlower.length-1]){

					Runnable runnable1 = new Runnable() {
						@Override
						public void run() {
							if(textCallBackListener != null){
								textCallBackListener.playLove();
							}
						}
					};
					uiHandler.postDelayed(runnable1,5000);

					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							PointF point = new PointF();
							point.set(mRandom.nextInt(Math.max(mDrawWidth, mWidth)), mDrawHeight/2);
							addLove(point);
						}
					};
					uiHandler.postDelayed(runnable,12000);


					finish = true;
				}else{
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							PointF point = new PointF();
							point.set(mRandom.nextInt(Math.max(mDrawWidth, mWidth)), mDrawHeight/2);
							//每点击一次添加一个礼物
							addPresent(point);
						}
					};
					uiHandler.post(runnable);
				}
			}
		}
	};

	TimerTask task1 = new TimerTask() {
		@Override
		public void run() {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					if(n >= initFlower1[0]){
						remove = true;
					}
					n++;
					PointF point = new PointF();
					point.set(mRandom.nextInt(Math.max(mDrawWidth, mWidth)), mDrawHeight/2);
					//每点击一次添加一个礼物
					addPresent(point);
				}
			};
			uiHandler.post(runnable);
		}
	};

	public void add520(){
		int textSize = 500;
		SyncTextPathView mSyncTextPathView = new SyncTextPathView(context,"520",textSize,6000);
		mSyncTextPathView.setPathPainter(new FireworksPainter());
		/*mSyncTextPathView.setText(text);
		mSyncTextPathView.setDuration(12000);*/
		mSyncTextPathView.setShowPainter(true);
		mSyncTextPathView.setmTextInCenter(true);
		//mSyncTextPathView.setTextSize(500);
		mParams=new LayoutParams(mWidth,(int)(textSize*1.2));
		addView(mSyncTextPathView,mParams);
		mSyncTextPathView.startAnimation(0,1);
	}

	public void addyezi(){
		int textSize = 250;
		SyncTextPathView mSyncTextPathView1 = new SyncTextPathView(context,"LOVE叶子",textSize,6000);
		mSyncTextPathView1.setPathPainter(new PenPainter());
		mSyncTextPathView1.setShowPainter(true);
		mSyncTextPathView1.setmTextInCenter(true);
		mParams=new LayoutParams(mWidth,(int)(textSize*1.2));
		mSyncTextPathView1.setY(mHeight-(int)(textSize*2));
		addView(mSyncTextPathView1,mParams);
		mSyncTextPathView1.startAnimation(0,1);
	}

	public void addLove(PointF point){
		//ImageView iv = (ImageView) findViewById(R.id.);
/*		mParams=new LayoutParams((int)(beateLoveDrawable.getIntrinsicWidth()*heartSizeScale)
				,(int)(beateLoveDrawable.getIntrinsicHeight()*heartSizeScale));*/
		mParams=new LayoutParams((int)(beateLoveDrawable.getIntrinsicWidth()*heartSizeScale),(int)(beateLoveDrawable.getIntrinsicHeight()*heartSizeScale));
		ImageView imageView=new ImageView(getContext());
		imageView.setImageDrawable(beateLoveDrawable);
		addView(imageView,mParams);
		AnimatorSet animatorset=getObjectAnimator(imageView,point,true);
		animatorset.setTarget(imageView);
		animatorset.start();
		imageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						stop();
						if(textCallBackListener != null){
							textCallBackListener.choiceGift();
						}
						//BezierLayout.this.removeAllViews();
					}
				};
				uiHandler.postDelayed(runnable,0);
			}
		});
	}

	public void createFlower(PointF point)
	{
		//ImageView iv = (ImageView) findViewById(R.id.);
		mParams=new LayoutParams(beateLoveDrawable.getIntrinsicWidth()/8,beateLoveDrawable.getIntrinsicHeight()/8);
		ImageView imageView=new ImageView(getContext());
		imageView.setImageDrawable(beateLoveDrawable);
		addView(imageView,mParams);
		imageView.setX(point.x);
		imageView.setY(point.y);
		Animator anim = AnimatorInflater.loadAnimator(context, R.animator.love);
		anim.setTarget(imageView);
		anim.start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//对按下事件监听，获取点击的位置
		/*if(event.getAction()== MotionEvent.ACTION_DOWN){
			PointF point=new PointF();
			point.set(event.getX(),event.getY());
			//每点击一次添加一个礼物
			addPresent(point);
			return true;
		}*/
		return super.onTouchEvent(event);

	}

	private void addPresent(PointF point) {
		mParams=new LayoutParams(mDrawWidth,mDrawHeight);
		/*//设置左边距为落下点-图片宽度的一半（这是为了让图片的中心为落下手指位置）
		mParams.leftMargin=(int)(point.x-mDrawWidth/2);
		//设置上边距为落下点-图片高度的一半（这是为了让图片的中心为落下手指位置）
		mParams.topMargin=(int)(point.y-mDrawHeight/2);*/
		ImageView imageView=new ImageView(getContext());
		//随机设置图片
		imageView.setImageDrawable(mDrawables[mRandom.nextInt(4)]);
		//设置动画
		AnimatorSet animatorset=getObjectAnimator(imageView,point,false);
		animatorset.setTarget(imageView);
		addView(imageView,mParams);
		animatorset.start();
	}

	public AnimatorSet getObjectAnimator(final ImageView imageview, PointF point,boolean isHeart) {
		//动画：放大+透明度+贝塞尔曲线
		//放大+透明度
		float scale = (mRandom.nextInt(12)%(14-4+1)+4)/10f;
		float aplpha = remove?0:1f;
		ObjectAnimator scalexanimator=ObjectAnimator.ofFloat(imageview,"scaleX",0.4f,scale);
		ObjectAnimator scaleyanimator=ObjectAnimator.ofFloat(imageview,"scaleY",0.4f,scale);
		ObjectAnimator alphaanimator=ObjectAnimator.ofFloat(imageview,"aplpha",1.0f,aplpha);//"aplpha"

		AnimatorSet set=new AnimatorSet();
		if(isHeart) {
			Animator anim = AnimatorInflater.loadAnimator(context, R.animator.love);
			scaleyanimator=ObjectAnimator.ofFloat(imageview,"scaleY",0.4f,heartSizeScale);
			scalexanimator=ObjectAnimator.ofFloat(imageview,"scaleX",0.4f,heartSizeScale);
			set.playTogether(scalexanimator, scaleyanimator, alphaanimator, anim);
			set.setDuration(1000);
		}else{
			set.playTogether(scalexanimator, scaleyanimator, alphaanimator);
			set.setDuration(3000);
		}

		//贝塞尔曲线ValueAnimator
		ValueAnimator bezierAnimator=getBezierAnimator(imageview,point,isHeart);
		//将放大+透明度+贝塞尔曲线三种动画添加在一起
		AnimatorSet sets=new AnimatorSet();
		sets.playTogether(set,bezierAnimator);
		//设置动画的对象与时间
		sets.setTarget(imageview);
		if(isHeart) {
			sets.setDuration(1000);
		}else{
			sets.setDuration(4000);
		}
		return sets;
	}

	public ValueAnimator getBezierAnimator(final ImageView imageview,PointF point,boolean isHeart) {
		//贝塞尔曲线需要四个点
		//起点
		PointF point0=new PointF();
		point0.x=point.x-mDrawWidth/2;
		point0.y=point.y-mDrawHeight/2;
		//第一个中间点
		final PointF point1=getTooglePoint(1,point);
		//d第二个中间点
		PointF point2=getTooglePoint(2,point);
		//最后一个点
		//PointF point3=new PointF(mRandom.nextInt(mWidth-mDrawWidth),mRandom.nextInt(mHeight- mDrawHeight)+mDrawHeight/2);
		//if(n <= 60){

		PointF 	point3 = finish?new PointF(mRandom.nextInt(mWidth),remove?mHeight+mDrawHeight:mHeight-mRandom.nextInt(mDrawHeight*3)):getHeartPoint(n);
		//}
		if(cloume < initFlower.length && n == initFlower[cloume]){
			heartScale+=heartSizeScaleSize;
		}
		if(isHeart){
			point3 = new PointF(mWidth/2-beateLoveDrawable.getIntrinsicWidth()*heartSizeScale/2,mHeight/2-
					beateLoveDrawable.getIntrinsicHeight()*heartSizeScale/2);
		}
		BerzierValutor valutor=new BerzierValutor(point1,point2);
		ValueAnimator valueAnimator=ValueAnimator.ofObject(valutor,point0,point3);
		valueAnimator.setDuration(5000);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF point= (PointF) animation.getAnimatedValue();
				imageview.setX(point.x);
				imageview.setY(point.y);
				//imageview.setAlpha(1-animation.getAnimatedFraction()+0.5f);
			}
		});
		return valueAnimator;
	}

	public PointF getHeartPoint(float angle) {
		float t = (float) (angle / Math.PI);
		float x = (float) (19.5 * (16 * Math.pow(Math.sin(t), 3)))*heartScale - mDrawWidth/2;
		float y = (float) (-20 * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)))*heartScale;
		return new PointF(offsetX + (int) x, offsetY + (int) y);
	}

	private PointF getTooglePoint(int i,PointF point) {
		PointF point1=new PointF();
		point1.x=mRandom.nextInt(Math.max(mDrawWidth,mWidth));
		int reactHeight=(int)(point.y/2);

		if(i==1){
			//第一个中间点，一半以下的随机位置
			point1.y=reactHeight+mRandom.nextInt(reactHeight);
		}else {
			//第二个中间点，一半以上的随机位置
			point1.y=mRandom.nextInt(reactHeight);
		}
		return point1;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth=getMeasuredWidth();
		mHeight=getMeasuredHeight();
		if(timer == null) {
			timer = new Timer();
			timer.schedule(task, 5000, 100);
			offsetX = mWidth/2;
			offsetY = mHeight/2;
			if(textCallBackListener != null)
			 textCallBackListener.play520();
		}
	}
	class  BerzierValutor implements TypeEvaluator<PointF> {//TypeEvaluator
		private PointF mPoint1;
		private PointF mpoint2;

		public BerzierValutor(PointF mPoint1, PointF mpoint2) {
			this.mPoint1 = mPoint1;
			this.mpoint2 = mpoint2;
		}

		@Override
		public PointF evaluate(float t, PointF startValue, PointF endValue) {
			PointF pointf=new PointF();
			//根据贝塞尔公式设置三次方贝塞尔曲线
			pointf.x=startValue.x*(1-t)*(1-t)*(1-t)+3*mPoint1.x*t*(1-t)*(1-t)+3*mpoint2.x*t*t*(1-t)+endValue.x*t*t*t;
			pointf.y=startValue.y*(1-t)*(1-t)*(1-t)+3*mPoint1.y*t*(1-t)*(1-t)+3*mpoint2.y*t*t*(1-t)+endValue.y*t*t*t;
			return pointf;
		}
	}

	public void stop(){
		if(timer != null){
			timer.cancel();
		}
		if(timer1 != null){
			timer1.cancel();
		}
		isStop = true;
	}
}