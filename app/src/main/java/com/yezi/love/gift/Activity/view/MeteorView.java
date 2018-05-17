package com.yezi.love.gift.Activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.yezi.love520.gift.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 绘画View
 * author True Lies
 * Created by Administrator on 2016/6/11.
 */
public class MeteorView extends SurfaceView
		implements
		SurfaceHolder.Callback {

	private SurfaceHolder mSurfaceHolder = null;
	private boolean isThreadOpen = false;
	private Bitmap mMeteorBmp = null;
	private List<PointF> mMeteorAddressModuleArray = new ArrayList<PointF>();

	private Random mRandom = new Random();
	private Paint mPaint = new Paint();
	private int screen_width;
	private int screen_height;

	public MeteorView(Context context) {
		this(context, null);
	}

	public MeteorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MeteorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context mContext) {

		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		screen_width = displayMetrics.widthPixels;
		screen_height = displayMetrics.heightPixels;


		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);//事件添加


	}


	private class DrawThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		public boolean isRun;

		public DrawThread(SurfaceHolder holder) {
			this.mSurfaceHolder = holder;
			isRun = true;
			for (int i = 0; i < 10; i++) {
				float mX = mRandom.nextInt(screen_width);
				float mY = mRandom.nextInt(500);
				PointF meteorAddressModule = new PointF();
				meteorAddressModule.x = mX;
				meteorAddressModule.y = mY;
				mMeteorAddressModuleArray.add(meteorAddressModule);
			}
		}

		@Override
		public void run() {
			Canvas mCanvas = null;
			while (isRun) {

				try {
					synchronized (mSurfaceHolder) {
						mCanvas = mSurfaceHolder.lockCanvas();
						//下面三句段代码  重置画板的作用 先清除xfermode对象再设置
						mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
						mCanvas.drawPaint(mPaint);
						mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
						for (int i = 0; i < mMeteorAddressModuleArray.size(); i++) {
							Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.meteor);
							PointF meteorAddressModule = mMeteorAddressModuleArray.get(i);
							meteorAddressModule.x = meteorAddressModule.x - 5;
							meteorAddressModule.y = meteorAddressModule.y + 5;
							System.out.println(meteorAddressModule.x + "x");
							System.out.println(meteorAddressModule.y + "y");
							mCanvas.drawBitmap(bitmap, meteorAddressModule.x, meteorAddressModule.y, mPaint);
							if (meteorAddressModule.x < 0 || meteorAddressModule.y > screen_width) {
								if (!bitmap.isRecycled()) {
									bitmap.recycle();
								}
								mMeteorAddressModuleArray.remove(meteorAddressModule);
							}
						}
						if (mMeteorAddressModuleArray.size() <= 0) {
							isRun = false;
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (mCanvas != null) {
						mSurfaceHolder.unlockCanvasAndPost(mCanvas);
					}
				}

			}
		}
	}

	//绘画事件
	@Override
	public void surfaceCreated(SurfaceHolder holder) {//surface创建的时候
		DrawThread mDrawThread = new DrawThread(mSurfaceHolder);
		mDrawThread.start();


		isThreadOpen = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {//surfaceView改变的时候

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {//surfaceView销毁的时候
		isThreadOpen = false;
	}
}