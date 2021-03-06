package com.example.tenprint;

import android.app.Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO
        // switch arraylists to non-allocated in draw
        // speed adjustment option
        // slow color morph?
    }

    public static class Drawer extends View {
        private static final int frameRate = 15;
        private static final int speed = 1000 / frameRate;
        private static Random rnd;
        private static Paint paint;
        private static int viewWidth;
        private static int viewHeight;
        private static ArrayList<ArrayList<Boolean>> strikes;
        private static int size = 30;
        private static int strokeScale = 5;
        private static boolean running;

        public Drawer(Context con, AttributeSet attr) {
            super(con, attr);
            rnd = new Random();
            strikes = new ArrayList<>();
            strikes.add(new ArrayList<>());
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size / strokeScale);
            paint.setStrokeCap(Paint.Cap.ROUND);
            running = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getPointerCount() == 1 && (running = !running)) {
                    invalidate();
                }

                return true;
            }
            else if (event.getAction() == MotionEvent.ACTION_MOVE &&
                        event.getPointerCount() == 2) {
                int x = (int)(event.getX(0) - event.getX(1));
                int y = (int)(event.getY(0) - event.getY(1));
                size = (int)Math.max(strokeScale, Math.sqrt(x * x + y * y) / 4);
                strikes.clear();
                strikes.add(new ArrayList<>());
                paint.setStrokeWidth(size / strokeScale);
                running = true;
                invalidate();
                return true;
            }

            return false;
        }

        @Override
        protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            viewWidth = xNew;
            viewHeight = yNew;
        }

        @Override
        protected void onDraw(final Canvas c) {
            super.onDraw(c);

            if (running) {
                postInvalidateDelayed(speed);
            }

            int direction = rnd.nextInt(2);
            ArrayList<Boolean> lastRow = strikes.get(strikes.size() - 1);
            lastRow.add(direction == 1);

            if (lastRow.size() > viewWidth / size) {
                strikes.add(new ArrayList<>()); // TODO don't alloc

                if (strikes.size() > viewHeight / size) {
                    strikes.remove(0);
                }
            }

            for (int i = 0; i < strikes.size(); i++) {
                for (int j = 0; j < strikes.get(i).size(); j++) {
                    if (strikes.get(i).get(j)) {
                        c.drawLine(
                            j * size,
                            i * size,
                            j * size + size,
                            i * size + size,
                            paint
                        );
                    }
                    else {
                        c.drawLine(
                            j * size + size,
                            i * size,
                            j * size,
                            i * size + size,
                            paint
                        );
                    }
                }
            }
        }
    }
}
