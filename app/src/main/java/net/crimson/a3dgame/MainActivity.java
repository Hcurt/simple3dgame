package net.crimson.a3dgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import org.rajawali3d.view.SurfaceView;


public class MainActivity extends Activity implements View.OnTouchListener {
    private MyRenderer mRenderer;
    private float mPreviousX;
    private JoystickView joystickView;
    private float mPreviousY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.setFrameRate(60.0);
        surfaceView.setRenderMode(1);

        mRenderer = new MyRenderer(this);
        surfaceView.setSurfaceRenderer(mRenderer);
        surfaceView.setOnTouchListener(this);
        setContentView(surfaceView);

        joystickView = findViewById(R.id.joystickView);
        if(joystickView != null) {
            joystickView.setJoystickListener(new JoystickView.JoystickListener() {
                @Override
                public void onJoystickMoved(float xPercent, float yPercent) {
                    mRenderer.moveCamera(xPercent, yPercent);
                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mPreviousX;
                float deltaY = y - mPreviousY;
                mPreviousX = x;
                mPreviousY = y;

                // Adjust the sensitivity as needed
                float sensitivity = 0.5f;
                mRenderer.rotateCamera(deltaX * sensitivity, deltaY * sensitivity);
                return true;
        }
        return false;
    }
}