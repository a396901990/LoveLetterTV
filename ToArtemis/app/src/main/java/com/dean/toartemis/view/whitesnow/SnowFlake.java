package com.dean.toartemis.view.whitesnow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

class SnowFlake {
    private static final float ANGE_RANGE = 0.1f;
    private static final float ANGLE_DIVISOR = 10000.0f;
    private static final float ANGLE_SEED = 100.0f;
    private static final float FLAKE_SIZE_LOWER = 7.0f;
    private static final float FLAKE_SIZE_UPPER = 20.0f;
    private static final float HALF_ANGLE_RANGE = 0.05f;
    private static final float HALF_PI = 1.5707964f;
    private static final float INCREMENT_LOWER = 2.0f;
    private static final float INCREMENT_UPPER = 4.0f;
    private float angle;
    private final float flakeSize;
    private final float increment;
    private final Paint paint;
    private final Point position;
    private final MRandom random;

    public static SnowFlake create(int width, int height, Paint paint1) {
        MRandom random = new MRandom();
        Paint paint = new Paint(1);
        paint.setColor(getRandomColor());
        paint.setAlpha(140);
        paint.setStyle(Paint.Style.FILL);
        return new SnowFlake(random, new Point(random.getRandom(width), random.getRandom(height)), (((random.getRandom((float) ANGLE_SEED) / ANGLE_SEED) * ANGE_RANGE) + HALF_PI) - HALF_ANGLE_RANGE, random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER), random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER), paint);
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

    SnowFlake(MRandom random, Point position, float angle, float increment, float flakeSize, Paint paint) {
        this.random = random;
        this.position = position;
        this.angle = angle;
        this.increment = increment;
        this.flakeSize = flakeSize;
        this.paint = paint;
    }

    private void move(int width, int height) {
        double x = ((double) this.position.x) + (((double) this.increment) * Math.cos((double) this.angle));
        double y = ((double) this.position.y) + (((double) this.increment) * Math.sin((double) this.angle));
        this.angle += this.random.getRandom(-100.0f, ANGLE_SEED) / ANGLE_DIVISOR;
        this.position.set((int) x, (int) y);
        if (!isInside(width, height)) {
            reset(width);
        }
    }

    private boolean isInside(int width, int height) {
        int x = this.position.x;
        int y = this.position.y;
        return ((float) x) >= (-this.flakeSize) - 1.0f && ((float) x) + this.flakeSize <= ((float) width) && ((float) y) >= (-this.flakeSize) - 1.0f && ((float) y) - this.flakeSize < ((float) height);
    }

    private void reset(int width) {
        this.position.x = this.random.getRandom(width);
        this.position.y = (int) ((-this.flakeSize) - 1.0f);
        this.angle = (((this.random.getRandom((float) ANGLE_SEED) / ANGLE_SEED) * ANGE_RANGE) + HALF_PI) - HALF_ANGLE_RANGE;
    }

    public void draw(Canvas canvas) {
        move(canvas.getWidth(), canvas.getHeight());
        canvas.drawCircle((float) this.position.x, (float) this.position.y, this.flakeSize, this.paint);
    }
}
