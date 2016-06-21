/*
 * Copyright (c) 2016/5/28.
 * HuaYing Network Technology Co., Ltd.
 * Create by 宋小雄.
 */

package org.itsk.slideimagedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Jour on 2016/5/27.
 */

public class SlideImageView extends ImageView implements View.OnTouchListener {
    private boolean isInit=false;
    private int initLeft;
    private int endX;
    private OnMoveDoneListener onMoveDoneListener;
    private boolean isDone=false;
    private boolean isDoneAnimation;

    public SlideImageView(Context context) {
        this(context,null);
    }

    public SlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getDisplayMetrics(context);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下,获取初始坐标
                synchronized (SlideImageView.this) {
                    if (!isInit) {
                        int[] locations = new int[2];
                        v.getLocationInWindow(locations);
                        isInit=true;
                        initLeft = (int) v.getX();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动
                moveWithView(v,X);
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起
                upWithView(v,endX);
                break;
        }
        return true;
    }
    private void moveWithView(View view,int x) {
        int left = x - view.getWidth() / 2;
        if (left>initLeft && left+view.getWidth()< screenWidth) {
            view.setX(left);
            endX=x;
        }
        else if (left+view.getWidth() >= screenWidth){
            if (onMoveDoneListener!=null) {
                if (!isDone) {
                    isDone=true;
                    onMoveDoneListener.moveDone();
                }
            }
        }
    }

    private void upWithView(View view,int x) {
        int left = x - view.getWidth() / 2;
        if (left>initLeft && left+view.getWidth()< screenWidth) {
            slideview(0,initLeft-left);
        }
        else if (left+view.getWidth() >= screenWidth){
            if (onMoveDoneListener!=null) {
                if (!isDone) {
                    isDone=true;
                    onMoveDoneListener.moveDone();
                }
                slideview(0,initLeft-left);
            }
        }
    }

    public void slideview(final float p1, final float p2) {
        setOnTouchListener(null);
        if (isDone && !isDoneAnimation){
            return ;
        }
        TranslateAnimation animation = new TranslateAnimation(p1, p2, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int left = SlideImageView.this.getLeft()+(int)(p2-p1);
                int top = SlideImageView.this.getTop();
                int width = SlideImageView.this.getWidth();
                int height = SlideImageView.this.getHeight();
                SlideImageView.this.clearAnimation();
                SlideImageView.this.layout(left, top, left+width, top+height);
                setDone(false);
                setOnTouchListener(SlideImageView.this);
            }
        });
        startAnimation(animation);
    }


    public void setOnMoveDoneListener(OnMoveDoneListener onMoveDoneListener) {
        this.onMoveDoneListener = onMoveDoneListener;
    }

    private int screenWidth;
    private int screenHeight;
    private float density;
    public void getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
        System.out.println("screenWidth:"+screenWidth+"\nscreenHeight:"+screenHeight+"\ndensity:"+density);
    }

    public void setDoneAnimation(boolean doneAnimation) {
        isDoneAnimation = doneAnimation;
    }

    public interface OnMoveDoneListener{
        void moveDone();
    }

    private void setDone(boolean done) {
        isDone = done;
    }
}
