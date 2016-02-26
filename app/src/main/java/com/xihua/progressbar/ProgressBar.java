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
    //弧形颜色
    private int arcColor;
    //大圆颜色
    private int circleColor;
    //大圆宽度
    private int circleWidth;
    //弧形画笔，大圆画笔，文本画笔，文本圆画笔
    private Paint arcPaint,circlePaint,textPaint,textCirclePaint;
    private float progress;
    private RectF rect;
    //大圆圆心横坐标，纵坐标，文本圆纵坐标
    private float cX,cY,scY;
    //大圆半径，文本圆半径
    private float radius,sRadius;
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
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint(arcPaint);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textOffsetY = (textPaint.descent() + textPaint.ascent()) / 2;
        textCirclePaint = new Paint(arcPaint);
        textCirclePaint.setStrokeWidth(10);
        textCirclePaint.setColor(0xFF16A3FA);
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
        arcPaint.setColor(arcColor);
        circlePaint.setColor(circleColor);
        arcPaint.setStrokeWidth(circleWidth);
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
                widthSize = (int) (radius * 2 + circleWidth);
                break;
            case MeasureSpec.EXACTLY:
                radius = (widthSize - circleWidth) / 2;
                break;
        }
//        switch (heightMode) {
//            case MeasureSpec.AT_MOST:
//                height = (int) (radius * 2 + circleWidth);
//                break;
//            case MeasureSpec.EXACTLY:
//                height = heightSize;
//                radius = Math.min(width / 2,height / 2);
//                break;
//        }
        setMeasuredDimension(widthSize, widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = progress * 360 / 100;
        canvas.drawCircle(cX, cY, radius, circlePaint);
        canvas.drawArc(rect, -90, angle, false, arcPaint);
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

    public void setRadius(float radius) {
        this.radius = radius;
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
