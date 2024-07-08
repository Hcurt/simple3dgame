package net.crimson.a3dgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;
    private float touchX;
    private float touchY;
    private boolean isTouching;

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
    }

    public JoystickView(Context context) {
        super(context);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.DKGRAY);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2.0f;
        centerY = getHeight() / 2.0f;
        baseRadius = Math.min(getWidth(), getHeight()) / 3.0f;
        hatRadius = Math.min(getWidth(), getHeight()) / 6.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, baseRadius, outerCirclePaint);
        if (isTouching) {
            canvas.drawCircle(touchX, touchY, hatRadius, innerCirclePaint);
        } else {
            canvas.drawCircle(centerX, centerY, hatRadius, innerCirclePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        float displacement = (float) Math.sqrt(Math.pow(touchX - centerX, 2) + Math.pow(touchY - centerY, 2));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                if (displacement < baseRadius) {
                    touchX = event.getX();
                    touchY = event.getY();
                } else {
                    float ratio = baseRadius / displacement;
                    touchX = centerX + (touchX - centerX) * ratio;
                    touchY = centerY + (touchY - centerY) * ratio;
                }
                if (joystickCallback != null) {
                    joystickCallback.onJoystickMoved((touchX - centerX) / baseRadius, (touchY - centerY) / baseRadius);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (displacement < baseRadius) {
                    touchX = event.getX();
                    touchY = event.getY();
                } else {
                    float ratio = baseRadius / displacement;
                    touchX = centerX + (touchX - centerX) * ratio;
                    touchY = centerY + (touchY - centerY) * ratio;
                }
                if (joystickCallback != null) {
                    joystickCallback.onJoystickMoved((touchX - centerX) / baseRadius, (touchY - centerY) / baseRadius);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                if (joystickCallback != null) {
                    joystickCallback.onJoystickMoved(0, 0);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setJoystickListener(JoystickListener listener) {
        joystickCallback = listener;
    }
}
