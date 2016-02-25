package com.xihua.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * @author cyq7on
 * @version V1.0
 * @Package com.xihua.progressbar
 * @Description: 带百分数的圆形ProgressBar
 * @date 2016/2/259:42
 */
public class ProgressBar extends View {
    private int arcColor;
    private int circleColor;
    private int circleWidth;
    private Paint paint,circlePaint,textPaint,textCirclePaint;
    private float progress;
    private RectF rect;
    private float cX,cY,scY;
    private float radius,sRadius;
    private int width,height;
    private float textOffsetY;

    public ProgressBar(Context context) {
        this(context, null, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ProgressBar, defStyleAttr, 0);
        arcColor = a.getColor(R.styleable.ProgressBar_arcColor, Color.RED);
        circleColor = a.getColor(R.styleable.ProgressBar_circleColor, Color.GREEN);
        circleWidth = a.getDimensionPixelSize(R.styleable.ProgressBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        20, getResources().getDisplayMetrics()));
        radius = a.getDimensionPixelSize(R.styleable.ProgressBar_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        100, getResources().getDisplayMetrics()));
        a.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint(paint);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textOffsetY = (textPaint.descent() + textPaint.ascent()) / 2;
        textCirclePaint = new Paint(paint);
        textCirclePaint.setStrokeWidth(10);
        textCirclePaint.setColor(0xFF16A3FA);

//        new Thread() {
//            private int progress;
//            @Override
//            public void run() {
//                while (progress < 100) {
//                    progress += 2;
//                    setProgress(progress);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cX = w / 2;
        cY = h / 2;
        sRadius = 1F / 5 * radius;
        //这里还有疑问
        scY = - radius + sRadius + 10;
        rect = new RectF(cX - radius, cX - radius, cX + radius, cX + radius);
        paint.setColor(arcColor);
        circlePaint.setColor(circleColor);
        paint.setStrokeWidth(circleWidth);
        circlePaint.setStrokeWidth(circleWidth);
        Log.e("cY", cY + "");
        Log.e("scY", scY + "");
        Log.e("radius", radius + "");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = (int) (radius * 2 + circleWidth);
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                radius = (width - circleWidth) / 2;
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = (int) (radius * 2 + circleWidth);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
//                radius = Math.min(width / 2,height / 2);
                break;
        }
        setMeasuredDimension(width, height);
        Log.i("onMeasure",radius + "");
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        float angle  = progress * 360 / 100;
//        float scX = (float) (cX + (radius * (1 - CIRCLE_RATIO) - circleWidth ) * Math.sin(angle));
//        float scY = (float) (cY - (radius * (1 - CIRCLE_RATIO) - circleWidth ) * Math.cos(angle));
//        canvas.drawCircle(cX, cY, radius, circlePaint);
//        canvas.drawArc(rect, -90, angle, false, paint);
//        canvas.drawCircle(scX,scY,CIRCLE_RATIO * radius,textCirclePaint);
//        Log.i("onDraw",angle + "");
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = progress * 360 / 100;
        canvas.drawCircle(cX, cY, radius, circlePaint);
        canvas.drawArc(rect, -90, angle, false, paint);
        canvas.save();
        canvas.translate(cX, cY);
        canvas.rotate(angle);
        canvas.drawCircle(0, scY, sRadius, textCirclePaint);

        canvas.translate(0,scY);
        canvas.rotate(-angle);
        canvas.drawText(String.format("%d%%",(int)progress),0,0 - textOffsetY,textPaint);
        
        canvas.restore();
        Log.i("onDraw", angle + "");
    }

    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
    }


    public void setProgress(int progress) {
        if (progress > 100) {
            return;
        }

        this.progress = progress;
        invalidate();

//        int i = 0;
//        float add = (progress - this.progress) / 4;
//        Log.d("add", add + "");
//        //这里使每次增加的弧度接近1，“画”弧线时更加顺畅
//        while (i++ < 4) {
//            this.progress += add;
//            invalidate();
//            Log.d("progress", this.progress + "");
//        }
    }
}
