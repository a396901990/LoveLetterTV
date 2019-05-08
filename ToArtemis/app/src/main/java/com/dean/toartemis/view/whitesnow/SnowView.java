package com.dean.toartemis.view.whitesnow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class SnowView extends View {
    private Context context;
    private static final long DELAY = 1;
    private static final int NUM_SNOWFLAKES = 80;
    private Runnable runnable = new Runnable() {
        public void run() {
            SnowView.this.invalidate();
        }
    };
    private SnowFlake[] snowflakes;
    private static final float CENTER_TOP_Y_RATE = 0.3f;    // 中间顶部比例
    private static final float MOST_WIDTH_RATE = 0.49f;     // 心形一半most宽度
    private static final float LINE_WIDTH_RATE = 0.35f;     // 左右边线宽度比例
    private static final float K_1 = 1.14f;                 // 左右边线斜率
    private static final float K_2 = 0.80f;                 // 顶部圆球曲率


    public SnowView(Context context) {
        super(context);
        this.context = context;
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void resize(int width, int height) {
        Paint paint = new Paint(1);
        paint.setColor(getRandomColor());
        paint.setAlpha(100);
        paint.setStyle(Style.FILL);
        this.snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
//            Log.e("c", getRandomColor()+"");

            this.snowflakes[i] = SnowFlake.create(width, height, paint);
        }
    }

    public  static int getRandomColor(){
        Random random=new Random();
        int r=0;
        int g=0;
        int b=0;
        for(int i=0;i<2;i++){
            //       result=result*10+random.nextInt(10);
            int temp=random.nextInt(16);
            r=r*16+temp;
            temp=random.nextInt(16);
            g=g*16+temp;
            temp=random.nextInt(16);
            b=b*16+temp;
        }
        return Color.rgb(r,g,b);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : this.snowflakes) {
            snowFlake.draw(canvas);
        }
        getHandler().postDelayed(this.runnable, DELAY);
    }
}
