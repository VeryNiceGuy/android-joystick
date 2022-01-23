package com.ferzflun.androidjoystick;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import java.util.*;

import com.ferzflun.androidjoystick.R;

public class FerzGLSurfaceView extends GLSurfaceView{
	private final FerzGLRenderer mRenderer;
	private Timer mTimer;
	 
	public FerzGLSurfaceView(Context context) {
		super(context);
		
		setEGLContextClientVersion(2);
		setPreserveEGLContextOnPause(true);
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
													R.drawable.atlas,
													options);
        mRenderer = new FerzGLRenderer(bitmap);
        setRenderer(mRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
	    float x = e.getX();
	    float y = e.getY();
	    
	    Joystick joystick = mRenderer.getJoystick();

	    if(e.getAction() == MotionEvent.ACTION_DOWN) {
	    	joystick.isButtonTapped(x, y);
	    	
	    	mTimer = new Timer();
	    	mTimer.scheduleAtFixedRate(new TimerTask() {
	    		public void run() {
	    			mRenderer.getJoystick().tapInterval();
	    		}
	    	}, 0, 1000);
	    }
	    
	    if(e.getAction() == MotionEvent.ACTION_UP){
	    	mTimer.cancel();
	    	joystick.tappingEnded();
	    }

	    return true;
	}
}
