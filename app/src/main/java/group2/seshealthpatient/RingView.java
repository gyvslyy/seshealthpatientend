package com.patient_doctor.heartbeat_analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RingView extends View {
    private static final String TAG = "RingView";
    private int mTotalHeight,mTotalWidth;
    private int mHeartBeatWidth;
    private int mRadius=260;
    private int x,y;
    private Paint mRingPaint;
    private Paint mRingAnimPaint;
    private RectF mRectf;
    private Context mContext;
    private final int mHeartPaintWidth=60;
    private int mAnimAngle=-1;
    private DrawFilter mDrawFilter;

    Path path=new Path();

    private void init(){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        mRingPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        if(!isInEditMode()){
            mRingPaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        mRingPaint.setStrokeWidth(mHeartPaintWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingAnimPaint=new Paint(mRingPaint);
        mRingAnimPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));

        startAnim();
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mTotalHeight=h;
        mTotalWidth=w;
        mHeartBeatWidth=w-mHeartPaintWidth*2-40;
        x=w/2;
        y=h/2;
        mRadius=w/2-mHeartPaintWidth/2;
        mRectf=new RectF(x-mRadius, y-mRadius, x+mRadius, y+mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);

        for (int i = 0; i < 360; i += 3) {
            canvas.drawArc(mRectf, i, 1, false, mRingPaint);
        }
        if (mAnimAngle != -1) {
            for (int i = -90; i < mAnimAngle - 90; i += 3) {
                canvas.drawArc(mRectf, i, 1, false, mRingAnimPaint);
            }
        }
    }

    private volatile boolean StartHeartBeatAnmiFlag=false;
    private volatile boolean StopHeartBeatAnmiFlag=false;

    private void startRingAnim(){
        mAnimAngle=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mAnimAngle<360){
                    mAnimAngle++;
                    try {
                        Thread.sleep(28);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "run: Test1:"+mAnimAngle);
                    postInvalidate();
                    //Log.d(TAG, "run: Test2:"+mAnimAngle);
                }
                mAnimAngle=-1;
                stopAnim();
            }
        }).start();
    }

    public void stopAnim(){
        StopHeartBeatAnmiFlag = true;
        StartHeartBeatAnmiFlag = false;
    }
    public void startAnim(){
        startRingAnim();
    }

    public RingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }
}
